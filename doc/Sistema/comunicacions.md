# Gestio de comunicacions

App client i App admin es comuniquen amb els backends mitjançant peticions HTTP  

- App client utilitza la llibreria Dio per a fer les peticions HTTP
- App admin utiliza la llibreria Http de Java per a fer les peticions HTTP

Els backends exposen API REST per a que les apps puguin consumir-les

- Backend Spring Boot utilitza controllers REST per a exposar les API
- Backend Express utilitza rutes per a exposar les API
- Odoo exposa API REST mitjançant mòduls específics

## Notes

Per a iniciar sessio, les apps envien les credencials al backend odoo, i aquest retorna un token que s'utilitza per a autenticar les peticions posteriors.  
