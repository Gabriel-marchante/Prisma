<div align="center">

# 🎬 PRISMA

### Plataforma de Distribución y Gestión de Contenido de Vídeo

*Una plataforma de streaming completa inspirada en Netflix, construida con arquitectura de microservicios.*

![Version](https://img.shields.io/badge/versión-1.0-blue?style=flat-square)
![Architecture](https://img.shields.io/badge/arquitectura-microservicios-purple?style=flat-square)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?style=flat-square&logo=docker&logoColor=white)

</div>

---

## 📖 Descripción

**Prisma** es una plataforma completa de streaming de vídeo que permite a los usuarios explorar un catálogo, reproducir contenido en alta calidad mediante **streaming adaptativo HLS** y gestionar sus suscripciones. Los administradores disponen de un panel dedicado para gestionar todo el contenido y los metadatos.

El sistema está compuesto por **5 servicios independientes** que se comunican entre sí y se despliegan mediante Docker.

---

## 🏗️ Arquitectura

```
┌─────────────────┐     ┌─────────────────┐
│   App Móvil     │     │   App Admin     │
│  Flutter/Dart   │     │    Vue.js       │
└────────┬────────┘     └────────┬────────┘
         │                       │
         ▼                       ▼
┌─────────────────┐     ┌─────────────────┐
│ Servidor        │     │ Servidor        │
│ Catálogo        │     │ de Medios       │
│ SpringBoot/Java │     │ Express/Node.js │
└────────┬────────┘     └────────┬────────┘
         │                       │
         ▼                       ▼
┌─────────────────────────────────────────┐
│         Sistema de Gestión (Odoo)       │
│    Autenticación · Suscripciones · JWT  │
└────────┬──────────────┬────────────────-┘
         │              │
    ┌────▼────┐    ┌─────▼──────┐   ┌────────────┐
    │ MongoDB │    │   MySQL    │   │ PostgreSQL │
    │Catálogo │    │   Odoo     │   │   Medios   │
    └─────────┘    └────────────┘   └────────────┘
```

---

## 🧩 Servicios

| Servicio | Tecnología | Responsabilidad |
|----------|-----------|-----------------|
| 📱 App Móvil | Flutter / Dart | Cliente multiplataforma iOS & Android |
| 🖥️ App Admin | Vue.js | Panel de administración de contenido |
| 📚 Catálogo | SpringBoot (Java) | API REST de metadatos, categorías y series |
| 🎞️ Medios | Express (Node.js) | Streaming HLS adaptativo (720p / 1080p / 4K) |
| 👤 Gestión | Odoo (Python) | Autenticación JWT, suscripciones y pagos |

---

## ✨ Funcionalidades

### Usuario final
- 🔐 Autenticación con tokens JWT
- 🎬 Reproducción en streaming adaptativo (HLS) según ancho de banda
- 📋 Catálogo con filtros por categoría, género y serie
- 📊 Historial de reproducciones con progreso guardado
- 💳 Gestión de suscripción y métodos de pago (Visa, MasterCard, PayPal)
- 👥 Múltiples perfiles por cuenta

### Administrador
- ⬆️ Subida y eliminación de vídeos
- ✏️ Gestión de metadatos del catálogo
- 🔄 Pipeline automático de procesamiento de vídeo con FFmpeg

---

## 🛠️ Tecnologías

### Backend
![Java](https://img.shields.io/badge/Java-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=springboot&logoColor=white)
![Node.js](https://img.shields.io/badge/Node.js-339933?style=flat-square&logo=node.js&logoColor=white)
![Express](https://img.shields.io/badge/Express-000000?style=flat-square&logo=express&logoColor=white)
![Python](https://img.shields.io/badge/Odoo-714B67?style=flat-square&logo=odoo&logoColor=white)

### Frontend
![Flutter](https://img.shields.io/badge/Flutter-02569B?style=flat-square&logo=flutter&logoColor=white)
![Vue.js](https://img.shields.io/badge/Vue.js-4FC08D?style=flat-square&logo=vue.js&logoColor=white)

### Bases de datos
![MongoDB](https://img.shields.io/badge/MongoDB-47A248?style=flat-square&logo=mongodb&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=mysql&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-336791?style=flat-square&logo=postgresql&logoColor=white)

### Infraestructura
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat-square&logo=docker&logoColor=white)
![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=flat-square&logo=githubactions&logoColor=white)
![FFmpeg](https://img.shields.io/badge/FFmpeg-007808?style=flat-square&logo=ffmpeg&logoColor=white)

---

## 🎞️ Pipeline de Procesamiento de Vídeo

Cuando un administrador sube un vídeo, el sistema ejecuta automáticamente:

```
1. 📤 Subida del .mp4 original
        ↓
2. 🔍 Extracción de metadatos (FFmpeg)
        ↓
3. ✂️ Segmentación en 720p / 1080p / 4K (FFmpeg)
        ↓
4. 💾 Almacenamiento en /Public/video:id/:resolution
        ↓
5. 📄 Generación del index.m3u8 (HLS playlist)
```

---

## 🚀 Instalación

### Requisitos previos
- [Docker](https://www.docker.com/) y Docker Compose
- [Node.js](https://nodejs.org/) v18+
- [Flutter SDK](https://flutter.dev/)

### Levantar el sistema

```bash
# 1. Clona el repositorio
git clone https://github.com/Gabriel-marchante/Prisma.git
cd Prisma

# 2. Levanta todos los servicios
docker compose up -d

# 3. Servicios disponibles
# Catálogo:  http://localhost:8080
# Medios:    http://localhost:3001
# Odoo:      http://localhost:8069
```

---

## 📡 API — Endpoints principales

### Catálogo
| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/cataleg` | Lista el catálogo paginado (10 por página) |
| `GET` | `/cataleg/:id` | Detalle completo de un vídeo |
| `POST` | `/cataleg` | Añade un vídeo (admin) |
| `PATCH` | `/cataleg/:id` | Actualiza metadatos (admin) |
| `DELETE` | `/cataleg/:id` | Elimina un vídeo (admin) |

### Medios
| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/Public/video:id/:resolution` | Streaming HLS por resolución |
| `POST` | `/vid` | Sube un fichero de vídeo (admin) |
| `DELETE` | `/vid/:id` | Elimina un vídeo (admin) |

### Gestión (Odoo)
| Método | Ruta | Descripción |
|--------|------|-------------|
| `POST` | `/login` | Inicio de sesión → JWT |
| `POST` | `/user` | Registro de usuario |
| `POST` | `/sub/cancel/:user` | Cancela suscripción |
| `PUT` | `/sub/payment/:id` | Modifica método de pago |



---

## ✍️ Créditos

Desarrollado por **Macloud Team (Gabriel Marchante)**. Inspirado en herramientas de seguridad clásicas y estética retro-futurista.

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=flat-square&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/gabriel-m-833856242/)
[![GitHub](https://img.shields.io/badge/GitHub-181717?style=flat-square&logo=github&logoColor=white)](https://github.com/Gabriel-marchante)

---
