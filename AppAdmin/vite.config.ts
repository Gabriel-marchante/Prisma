// import { defineConfig } from "vite";
// import vue from "@vitejs/plugin-vue";

// // @ts-expect-error process is a nodejs global
// const host = process.env.TAURI_DEV_HOST;

// // https://vitejs.dev/config/
// export default defineConfig(async () => ({
//   plugins: [vue()],

//   // Vite options tailored for Tauri development and only applied in `tauri dev` or `tauri build`
//   //
//   // 1. prevent vite from obscuring rust errors
//   clearScreen: false,
//   // 2. tauri expects a fixed port, fail if that port is not available
//   server: {
//     port: 1420,
//     strictPort: true,
//     host: host || false,
//     hmr: host
//       ? {
//           protocol: "ws",
//           host,
//           port: 1421,
//         }
//       : undefined,
//     watch: {
//       // 3. tell vite to ignore watching `src-tauri`
//       ignored: ["**/src-tauri/**"],
//     },
//   },
// }));

// import { defineConfig } from "vite";
// import vue from "@vitejs/plugin-vue";

// // @ts-expect-error process is a nodejs global
// const host = process.env.TAURI_DEV_HOST;

// export default defineConfig(async () => ({
//   plugins: [vue()],

//   clearScreen: false,

//   server: {
//     port: 1420,
//     strictPort: true,
//     host: host || false,
//     hmr: host
//       ? {
//           protocol: "ws",
//           host,
//           port: 1421,
//         }
//       : undefined,
//     watch: {
//       ignored: ["**/src-tauri/**"],
//     },
//     // ← Afegim proxy per a Odoo
//     proxy: {
//       "/api": {
//         target: "http://localhost:8069",
//         changeOrigin: true,
//         secure: false,
//       },
//     },
//   },
// }));


import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

// @ts-expect-error process is a nodejs global
const host = process.env.TAURI_DEV_HOST;

export default defineConfig(async () => ({
  plugins: [vue()],

  base: "./", // 👈 CLAVE para NW.js

  clearScreen: false,

  server: {
    port: 1420,
    strictPort: true,
    host: host || false,
    hmr: host
      ? {
          protocol: "ws",
          host,
          port: 1421,
        }
      : undefined,
    watch: {
      ignored: ["**/src-tauri/**"],
    },
    proxy: {
      "/api": {
        target: "http://localhost:8069",
        changeOrigin: true,
        secure: false,
      },
    },
  },
}));
