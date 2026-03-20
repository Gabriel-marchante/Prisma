# PRISMA
## Plataforma de Distribución y Gestión de Contenido de Vídeo
 
> **Documentación Técnica del Sistema — Versión 1.0 · 2025**
 
---
 
## Índice de Contenidos
 
1. [Descripción General del Sistema](#1-descripción-general-del-sistema)
2. [Arquitectura de Alto Nivel](#2-arquitectura-de-alto-nivel)
3. [Componentes del Sistema](#3-componentes-del-sistema)
4. [Modelo de Datos y Entidades](#4-modelo-de-datos-y-entidades)
5. [API y Endpoints](#5-api-y-endpoints)
6. [Requisitos Funcionales](#6-requisitos-funcionales)
7. [Tecnologías Utilizadas](#7-tecnologías-utilizadas)
8. [Pipeline de Ingestión de Vídeo](#8-pipeline-de-ingestión-de-vídeo)
9. [Flujos de Interacción](#9-flujos-de-interacción)
 
---
 
## 1. Descripción General del Sistema
 
Prisma es una plataforma completa de distribución y gestión de contenido de vídeo, diseñada para ofrecer una experiencia de streaming similar a servicios como Netflix. El sistema integra varios servicios especializados que trabajan de forma coordinada para ofrecer catálogo, reproducción, autenticación y gestión de suscripciones.
 
> **Objetivo Principal:** Prisma permite a los usuarios acceder a un catálogo de vídeos, reproducirlos en streaming y gestionar sus suscripciones, mientras que los administradores pueden gestionar el contenido, los metadatos y los ficheros de vídeo.
 
### Servicios Principales
 
- **Servicio de Catálogo (SpringBoot):** Gestiona los metadatos de los vídeos, categorías, series y estudios.
- **Servidor de Medios (Express/Node.js):** Entrega los ficheros `.mp4` fragmentados por resolución mediante streaming HLS.
- **Sistema de Gestión (Odoo):** Controla las suscripciones de usuarios, los pagos y la autenticación.
- **App Móvil (Flutter/Dart):** Aplicación cliente multiplataforma para el usuario final.
- **App de Administración (Vue.js):** Panel de administración para la gestión de contenido.
 
---
 
## 2. Arquitectura de Alto Nivel
 
La arquitectura de Prisma sigue un enfoque de microservicios distribuidos, donde cada componente tiene una responsabilidad bien definida. El sistema se despliega mediante Docker y está diseñado para escalar horizontalmente.
 
### Visión General (C4 Nivel 1)
 
El sistema Prisma interactúa con los siguientes actores y sistemas externos:
 
- **Usuario final:** accede a través de la app móvil (Flutter).
- **Administrador:** accede a través de la app de administración (Vue.js).
- **Odoo:** sistema ERP usado para autenticación y suscripciones.
- **Bases de datos:** MongoDB (catálogo), MySQL (Odoo), PostgreSQL (servicio de medios).
 
### Contenedores del Sistema (C4 Nivel 2)
 
| Contenedor | Tecnología | Responsabilidad |
|---|---|---|
| App Cliente (Móvil) | Flutter / Dart | Interfaz de usuario para reproducción, catálogo y perfiles |
| App Admin (Escritorio) | Vue.js | Panel de administración para gestión de vídeos y metadatos |
| Servidor Catálogo | SpringBoot (Java) | API REST para metadatos, categorías, series y valoraciones |
| Servidor de Medios | Express (Node.js) | Streaming de vídeos `.mp4` fragmentados por resolución |
| Sistema de Suscripciones | Odoo | Autenticación, suscripciones, pagos y perfiles de usuario |
| Base de datos Catálogo | MongoDB | Almacenamiento de metadatos de vídeos |
| Base de datos Odoo | MySQL | Datos de usuarios, suscripciones y pagos |
| Base de datos Medios | PostgreSQL | Relación entre ficheros de vídeo y metadatos |
 
---
 
## 3. Componentes del Sistema
 
### 3.1 Servidor de Catálogo (SpringBoot)
 
El servidor de catálogo es el núcleo de los metadatos del sistema. Está desarrollado en Java con el framework SpringBoot y sigue una arquitectura en capas con patrón Clean Architecture.
 
- Gestiona la lista de vídeos disponibles con paginación (10 por página).
- Permite filtrar por categoría, género, serie o estudio.
- Expone una API REST que devuelve datos en formato JSON.
- Valida los tokens JWT generados por Odoo para controlar el acceso.
- Permite a los administradores añadir, actualizar y eliminar vídeos del catálogo.
 
### 3.2 Servidor de Medios (Express/Node.js)
 
El servidor de medios es responsable de la entrega efectiva de los ficheros de vídeo. Está desarrollado con Node.js y el framework Express.
 
- Sirve ficheros de vídeo `.mp4` segmentados por resolución (720p, 1080p, 4K).
- Almacena los vídeos en la ruta pública `/Public/video:id/:resolution`.
- Retorna un fichero `index.m3u8` para el streaming adaptativo (HLS).
- Verifica las credenciales del usuario antes de servir contenido.
- Permite a los administradores subir y eliminar vídeos.
 
### 3.3 Sistema de Gestión de Suscripciones (Odoo)
 
Odoo actúa como el sistema central de autenticación, gestión de usuarios y control de suscripciones.
 
- Gestiona el registro e inicio de sesión de usuarios.
- Emite tokens JWT para la autenticación entre servicios.
- Controla los tipos de suscripción y los métodos de pago (Visa, MasterCard, PayPal).
- Gestiona múltiples perfiles por cuenta de usuario.
- Permite cancelar o modificar suscripciones activas.
 
### 3.4 App Móvil (Flutter/Dart)
 
La aplicación móvil es el cliente principal para usuarios finales. Desarrollada con Flutter, es compatible con iOS y Android. Sigue la arquitectura **Clean + MVVM** con las siguientes capas:
 
- **Capa de Presentación:** vistas y componentes de UI.
- **Capa de Dominio:** lógica de negocio e interfaces.
- **Capa de Datos:** repositorios y fuentes de datos remotas (usando `Dio` para HTTP).
 
### 3.5 App de Administración (Vue.js)
 
Panel web de administración desarrollado con Vue.js que permite a los administradores gestionar todo el contenido de la plataforma. Desde este panel se pueden subir vídeos, gestionar metadatos y supervisar el estado del sistema.
 
---
 
## 4. Modelo de Datos y Entidades
 
El modelo de datos de Prisma está distribuido entre varias bases de datos según el servicio. A continuación se describen las entidades principales del dominio:
 
| Entidad | Descripción |
|---|---|
| `Usuari` | Representa a un usuario registrado. Contiene credenciales, perfil y relación con su suscripción. |
| `Suscripcio` | Suscripción activa de un usuario. Relacionada con el tipo de plan y el método de pago. |
| `Perfil` | Perfil de visualización dentro de una cuenta. Una cuenta puede tener múltiples perfiles. |
| `MetodePago` | Método de pago registrado. Subclases: `Visa`, `MasterCard`, `PayPal`. |
| `Video` | Fichero de vídeo físico almacenado en el servidor de medios. Referenciado por ID. |
| `VideoCataleg` | Metadatos de un vídeo: título, descripción, duración, categoría, serie, estudio. |
| `Categoria` | Categoría temática del vídeo (comedia, drama, terror, etc.). |
| `Serie` | Serie a la que pertenece un vídeo, si aplica. |
| `Estudi` | Estudio o productora del contenido. |
| `Historial` | Registro del historial de visualizaciones de un perfil. Guarda el progreso de cada vídeo. |
 
### Relaciones Clave
 
- Un `Usuari` tiene una o más `Suscripcio` y uno o más `Perfil`.
- Una `Suscripcio` está asociada a un `MetodePago` (`Visa`, `MasterCard` o `PayPal`).
- Un `VideoCataleg` referencia un `Video` físico y está categorizado por `Categoria`, `Serie` y `Estudi`.
- Un `Perfil` tiene un `Historial` de reproducciones con el progreso de cada `VideoCataleg`.
 
---
 
## 5. API y Endpoints
 
Prisma expone tres APIs REST bien diferenciadas.
 
### 5.1 Servidor de Catálogo
 
| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/cataleg` | Retorna JSON con todos los vídeos del catálogo (paginado, 10 por página) |
| `GET` | `/cataleg/:id` | Retorna JSON con los datos completos de un vídeo específico |
| `GET` | `/error` | Retorna JSON con IDs de todos los vídeos del catálogo |
| `POST` | `/login` | Verifica token de usuario (body: JSON con credenciales) |
| `POST` | `/logout` | Elimina el token activo del usuario |
| `POST` | `/cataleg` | Añade un nuevo vídeo al catálogo (body: JSON con datos del vídeo) |
| `PATCH` | `/cataleg/:id` | Actualiza los datos de un vídeo (body: JSON con campos a cambiar) |
| `DELETE` | `/cataleg/:id` | Elimina un vídeo del catálogo por ID |
 
### 5.2 Servidor de Medios (Express)
 
| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/vid` | Lista todos los vídeos disponibles (sólo nombres, sin contenido) |
| `GET` | `/vid/:id` | Retorna el fichero `.mp4` de un vídeo por ID |
| `GET` | `/Public/video:id/:resolution` | Sirve el segmento de vídeo por ID y resolución (streaming HLS) |
| `GET` | `/error` | Verifica concordancia entre ID y catálogo |
| `POST` | `/vid` | Sube un nuevo fichero de vídeo `.mp4` al servidor |
| `POST` | `/login` | Verifica token del usuario |
| `POST` | `/logout` | Elimina el token activo |
| `DELETE` | `/vid/:id` | Elimina un fichero de vídeo del servidor por ID |
 
### 5.3 Sistema de Gestión (Odoo)
 
| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/user/:id` | Retorna JSON con la información del usuario |
| `GET` | `/sub/type` | Retorna JSON con todos los tipos de suscripción disponibles |
| `POST` | `/user` | Crea un nuevo usuario (body: JSON con datos del usuario) |
| `POST` | `/sub/payment` | Registra un nuevo método de pago |
| `POST` | `/sub/cancel/:user` | Cancela la suscripción de un usuario |
| `POST` | `/login` | Inicio de sesión, retorna token de verificación |
| `POST` | `/logout` | Cierra la sesión del usuario |
| `PUT` | `/sub/payment/:id` | Modifica el método de pago (body: JSON con datos del método) |
| `PATCH` | `/user/:id` | Actualiza los datos del usuario |
| `DELETE` | `/user/:id` | Elimina la cuenta de usuario |
 
---
 
## 6. Requisitos Funcionales
 
### Backend — Servidor de Catálogo
 
| ID | Requisito |
|---|---|
| RF01 | El usuario puede consultar los vídeos del catálogo. |
| RF02 | El usuario puede filtrar la búsqueda del catálogo. |
| RF03 | El usuario puede puntuar y comentar los vídeos. |
| RF04 | El administrador puede añadir vídeos al catálogo. |
| RF05 | El administrador puede actualizar vídeos del catálogo. |
| RF06 | El administrador puede eliminar vídeos del catálogo. |
| RF07 | El sistema muestra un vídeo con su información detallada. |
| RF08 | El sistema permite crear, editar y eliminar listas de reproducción personalizadas. |
| RF10 | El catálogo muestra secciones temáticas (comedia, drama, terror...). |
| RF11 | El sistema puede validar el token creado por Odoo. |
 
**Requisitos No Funcionales:**
 
- RNF01 — El servidor muestra el catálogo en formato JSON.
- RNF02 — El servidor muestra 10 vídeos por página.
 
### Backend — Servidor de Medios
 
| ID | Requisito |
|---|---|
| RF01 | El usuario puede reproducir vídeos. |
| RF02 | El administrador puede añadir nuevos vídeos. |
| RF03 | El administrador puede eliminar vídeos. |
| RF04 | El sistema puede verificar las credenciales del usuario. |
| RF05 | El administrador puede comprobar que todos los datos sean correctos. |
| RF06 | El sistema permite descargar vídeos (dependiendo de la suscripción). |
| RF07 | El sistema puede validar el token creado por Odoo. |
 
**Requisitos No Funcionales:**
 
- RNF01 — El sistema retorna un fichero `index.m3u8`.
- RNF02 — La respuesta al retornar un vídeo ha de ser instantánea.
 
### Backend — Sistema de Suscripciones (Odoo)
 
| ID | Requisito |
|---|---|
| RF01 | El usuario puede crear una cuenta. |
| RF02 | El usuario debe insertar un método de pago. |
| RF03 | El sistema comprueba las credenciales de los usuarios. |
| RF04 | El sistema controla todos los perfiles de cada usuario. |
| RF05 | El usuario puede modificar los parámetros de su suscripción. |
| RF06 | El usuario puede modificar los parámetros de su cuenta y perfiles. |
 
### Frontend — App Móvil
 
| ID | Requisito |
|---|---|
| RF01 | El usuario debe iniciar sesión con usuario y contraseña. |
| RF02 | El sistema guarda el estado de visualización de los vídeos del usuario. |
| RF03 | El usuario puede reproducir el vídeo. |
| RF04 | El usuario puede filtrar la búsqueda de vídeos. |
| RF05 | El usuario puede ver información detallada de los vídeos. |
| RF06 | El usuario puede pausar y continuar el vídeo en reproducción. |
 
### Frontend — App de Administración
 
| ID | Requisito |
|---|---|
| RF01 | El administrador puede añadir nuevos vídeos. |
| RF02 | El administrador puede insertar vídeos al catálogo usando los metadatos de los vídeos añadidos. |
| RF03 | El administrador puede editar metadatos de los vídeos. |
 
### Frontend — Web Odoo
 
| ID | Requisito |
|---|---|
| RF01 | El usuario puede gestionar su suscripción. |
| RF02 | El usuario puede crear una cuenta. |
 
---
 
## 7. Tecnologías Utilizadas
 
| Capa | Tecnología | Uso |
|---|---|---|
| Backend | SpringBoot (Java) | Servidor de catálogo, API REST de metadatos |
| Backend | Express (Node.js) | Servidor de medios, streaming de vídeo HLS |
| Backend | Odoo (Python) | ERP para autenticación, suscripciones y pagos |
| Frontend | Flutter / Dart | App móvil multiplataforma (iOS y Android) |
| Frontend | Vue.js | App de administración web |
| Base de datos | MongoDB | Almacenamiento de metadatos del catálogo |
| Base de datos | MySQL | Datos de usuarios y suscripciones en Odoo |
| Base de datos | PostgreSQL | Relación entre vídeos físicos y metadatos |
| Infraestructura | Docker | Contenerización y despliegue de los servicios |
| Infraestructura | GitHub Actions | CI/CD y pipeline de integración continua |
| Documentación | MkDocs + Material | Documentación técnica publicada en GitHub Pages |
| Diagramas | PlantUML | Diagramas C4, ER, secuencia y casos de uso |
| HTTP Client | Dio (Dart) | Peticiones HTTP desde la app Flutter |
| Procesamiento | FFmpeg | Extracción de metadatos y segmentación de vídeo |
 
---
 
## 8. Pipeline de Ingestión de Vídeo
 
Cuando un administrador sube un nuevo vídeo a Prisma, el sistema ejecuta automáticamente un pipeline de procesamiento para preparar el contenido para su distribución en streaming.
 
### Paso 1 — Subida del fichero
 
El administrador sube el fichero `.mp4` original desde la App de Administración (Vue.js) a través del endpoint `POST /vid` del servidor de medios.
 
### Paso 2 — Extracción de metadatos
 
FFmpeg extrae automáticamente los metadatos del vídeo: duración, resolución original, codec de vídeo y audio, bitrate, etc. Estos metadatos se almacenan en la base de datos del catálogo.
 
### Paso 3 — Segmentación del vídeo
 
El vídeo original se procesa con FFmpeg para generar múltiples versiones según la resolución (720p, 1080p, 4K). Cada versión se divide en segmentos de tiempo para facilitar el streaming adaptativo (HLS).
 
### Paso 4 — Almacenamiento público
 
Los segmentos generados se almacenan en la carpeta pública del servidor en la ruta `/Public/video:id/:resolution`, donde `:id` es el identificador único del vídeo y `:resolution` es la calidad.
 
### Paso 5 — Generación del índice
 
Se genera un fichero `index.m3u8` (playlist HLS) que permite al cliente seleccionar automáticamente la mejor calidad según el ancho de banda disponible.
 
> **Nota:** Los ficheros `.mp4` originales no se almacenan de forma permanente después del procesamiento. Solo se conservan los segmentos HLS procesados para optimizar el almacenamiento del servidor.
 
---
 
## 9. Flujos de Interacción
 
### Flujo de Inicio de Sesión
 
1. El usuario abre la app e introduce sus credenciales (usuario y contraseña).
2. La app envía las credenciales a Odoo vía `POST /login`.
3. Odoo verifica las credenciales y retorna un token JWT.
4. La app almacena el token y lo usa en todas las peticiones posteriores.
5. El catálogo y el servidor de medios validan el token en cada petición.
 
### Flujo de Acceso al Catálogo
 
1. El usuario accede a la sección de catálogo en la app.
2. La app envía una petición `GET /cataleg` al servidor de catálogo con el token en la cabecera.
3. El servidor valida el token con Odoo y retorna el catálogo en formato JSON.
4. La app muestra los vídeos paginados (10 por página) con su información básica.
5. El usuario puede filtrar por categoría, género o búsqueda textual.
 
### Flujo de Reproducción de Vídeo
 
1. El usuario selecciona un vídeo del catálogo.
2. La app solicita información detallada al catálogo vía `GET /cataleg/:id`.
3. La app verifica la suscripción del usuario con Odoo para comprobar si tiene acceso.
4. Si el usuario tiene suscripción activa, la app solicita el vídeo al servidor de medios.
5. El servidor de medios retorna el fichero `index.m3u8` con los segmentos disponibles.
6. La app reproduce el vídeo de forma adaptativa según el ancho de banda del usuario.
7. El historial de reproducción se actualiza con el progreso del vídeo.
 
### Flujo de Gestión de Suscripción
 
1. El usuario accede a la sección de cuenta en la web de Odoo o en la app.
2. Puede ver su suscripción activa, cambiar el plan o el método de pago.
3. Los cambios de plan se realizan vía `PUT /sub/payment/:id`.
4. La cancelación de la suscripción se realiza vía `POST /sub/cancel/:user`.
5. Odoo gestiona la lógica de facturación y actualiza el estado del usuario.
 
---
 
> *Documentación generada para el proyecto Prisma · Plataforma de Distribución de Contenido de Vídeo*
