package org.padalustro.security;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.FileCopyUtils;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final ResourceLoader resourceLoader;

    public SecurityConfig(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtDecoder jwtDecoder,
            CustomAuthenticationEntryPoint authenticationEntryPoint,
            CustomAccessDeniedHandler accessDeniedHandler) throws Exception {

        http
                .cors(cors -> {})   
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // .requestMatchers(HttpMethod.GET, "/**").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder))
                        .authenticationEntryPoint(authenticationEntryPoint));

        return http.build();
    }


    @Bean
    public JwtDecoder jwtDecoder(
            @Value("${spring.security.oauth2.resourceserver.jwt.public-key-location}") String publicKeyLocation)
            throws Exception {

        Resource resource = resourceLoader.getResource(publicKeyLocation);

        if (!resource.exists()) {
            throw new RuntimeException("Public key not found at: " + publicKeyLocation);
        }

        try (InputStream inputStream = resource.getInputStream()) {
            String content = new String(FileCopyUtils.copyToByteArray(inputStream));
            String key = content
                    .replaceAll("-+[^-]+-+\\r?\\n?", "") // Quita headers y footers estilo PEM
                    .replaceAll("[^A-Za-z0-9+/=]", ""); // Quita TODO lo que no sea base64

            byte[] decoded = Base64.getDecoder().decode(key);

            // Si el archivo dice "RSA PUBLIC KEY", es PKCS#1 y Java necesita envolverlo en
            // X.509
            if (content.contains("RSA PUBLIC KEY")) {
                decoded = wrapPkcs1InX509(decoded);
            }

            X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(spec);

            return NimbusJwtDecoder.withPublicKey(publicKey).build();
        }
    }

    /**
     * Envuelve una clave PKCS#1 (RSA PUBLIC KEY) en una estructura X.509
     * (SubjectPublicKeyInfo).
     * Esto es necesario porque Java espera X.509, pero Odoo genera PKCS#1.
     */
    private byte[] wrapPkcs1InX509(byte[] pkcs1) {
        int len = pkcs1.length;
        int bitStringContentLen = len + 1; // +1 para el byte de pad bits (00)
        byte[] bitStringHeader = encodeDerLength(0x03, bitStringContentLen);

        // AlgId fijo: SEQUENCE { OID 1.2.840.113549.1.1.1, NULL } (15 bytes)
        byte[] algId = { 0x30, 0x0d, 0x06, 0x09, 0x2a, (byte) 0x86, 0x48, (byte) 0x86, (byte) 0xf7, 0x0d, 0x01, 0x01,
                0x01, 0x05, 0x00 };

        int topContentLen = algId.length + bitStringHeader.length + bitStringContentLen;
        byte[] topHeader = encodeDerLength(0x30, topContentLen);

        byte[] x509 = new byte[topHeader.length + algId.length + bitStringHeader.length + 1 + len];
        int pos = 0;

        System.arraycopy(topHeader, 0, x509, pos, topHeader.length);
        pos += topHeader.length;
        System.arraycopy(algId, 0, x509, pos, algId.length);
        pos += algId.length;
        System.arraycopy(bitStringHeader, 0, x509, pos, bitStringHeader.length);
        pos += bitStringHeader.length;
        x509[pos++] = 0x00; // Pad bits byte
        System.arraycopy(pkcs1, 0, x509, pos, len);

        return x509;
    }

    private byte[] encodeDerLength(int tag, int len) {
        if (len < 128) {
            return new byte[] { (byte) tag, (byte) len };
        } else if (len < 256) {
            return new byte[] { (byte) tag, (byte) 0x81, (byte) len };
        } else {
            return new byte[] { (byte) tag, (byte) 0x82, (byte) (len >> 8), (byte) (len & 0xFF) };
        }
    }
}
