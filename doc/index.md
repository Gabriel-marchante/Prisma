# Padalustro


**Contenido**:
- **Objetivo**: Resumen general del sistema.
- **Listado de ficheros**: Sección por sección con explicación de cada archivo y su diagrama.

---

**Objetivo del sistema**

Padalustro es una plataforma de distribución y gestión de contenido vídeo (catálogo, servidor de medios y gestión de suscripciones). La arquitectura propuesta separa:
- Un servicio de catálogo (metadatos de vídeos).
- Un servidor de medios (Express) que entrega ficheros .mp4 fragmentados por resolución.
- Un sistema de gestión de suscripciones (Odoo).
- Aplicaciones cliente: app móvil y app de administración.

---

**Resumen por fichero**

- `doc/index.md`: Índice/portada del conjunto de documentación (vacío o minimal).

- `doc/entities.md`: Lista de entidades del dominio: `Video`, `VideoCataleg`, `Usuari`, `Categoria`, `Serie`, `Estudi`, `Suscripcio`, `Metode Pago`, `Perfil`, `Historial de Reproduccions`.
  - Propósito: referencia rápida de las entidades que aparecen en ER/Diagrama de clases.

- `doc/DiagramaClases.md`:
  - Contiene un diagrama UML (PlantUML) de clases con atributos y relaciones.
  - Entidades clave: `Usuari`, `Suscripcio`, `MetodePago` (y subclases `Visa`, `MasterCard`, `PayPal`), `Video`, `Perfil`, `VideoCataleg`, `Serie`, `Estudi`, `Categoria`, `Historial`.
  - Explicación: muestra estructura de datos y asociaciones (p. ej. `VideoCataleg` relaciona con `Serie`, `Categoria`, `Estudi`, y enlaza a `Video` y `Historial`). Útil para modelado de la base de datos y DTOs.

- `doc/Pipeline.md`:
  - Diagrama PlantUML que describe el pipeline de ingestión de vídeo.
  - Componentes: actor `Administrador`, `ffmpeg` (procesamiento), extracción de metadatos (`Metadades Video`), `Partir Video` (segmentación), carpeta pública `Carpeta Public` y una zona de `No Guardar` para descartes.
  - Propósito: documentar cómo se procesa un .mp4 al subirlo (extracción de metadatos, particionado y almacenamiento público).

- `doc/DiagramaFlujo.md`:
  - Dos diagramas de flujo (PlantUML) que describen los flujos de uso principales de la app: inicio, ver catálogo, ver información de vídeo, reproducción (con verificación de suscripción), gestión de listas y configuración/perfiles.
  - Propósito: clarificar los caminos de interacción usuario → app → servicios (Odoo, Catálogo, Servidor de medios).

- `doc/DiagramaSecuencia.md`:
  - Diagrama de secuencia para casos de uso principales.
  - Actores/participantes: `Cliente`, `APP`, `Catálogo`, `Servidor Contenido`, `Odoo`.
  - Explica pasos: abrir app, verificar token, ver catálogo, ver info, reproducción (comprobación de suscripción en Odoo), gestión de perfiles y pagos.

- `doc/ER/ER_Conceptual.md`, `doc/ER/DiagramaModeloDatos.md`, `doc/ER/ER-Cataleg.md`:
  - Conjunto de diagramas ER (conceptual y modelos) en PlantUML.
  - Definen entidades, claves primarias/foráneas y relaciones (Usuari, Suscripcio, Perfil, Video_Cataleg, Video, Visualitzacio, Metodo_Pago y herencias de método de pago).
  - Propósito: servir como guía para la implementación de la base de datos (MongoDB/Postgres según componente).

- `doc/Sistema/EndPoints.md`:
  - Listado de endpoints por servicio: **Server Cataleg**, **Server Express**, **Server Odoo**.
  - Describe rutas `GET`, `POST`, `PUT`, `DELETE` y su comportamiento esperado (p. ej. `/cataleg`, `/vid`, `/user/:id`, `/sub/payment/:id`).
  - Propósito: contrato API básico y guía para implementar/controlar rutas en backend.

- `doc/Sistema/Arquitectures.md`:
  - Notas sobre la arquitectura de la app cliente: patrón Clean + MVVMM (capas Presentación, Dominio, Datos) y uso de `Dio` para peticiones HTTP en Dart.

- `doc/Sistema/relacionesGenerales.md`:
  - Resumen de relaciones entre servicios: Catálogo, Server media (Express) y Odoo, y sus responsabilidades (autenticación, persistencia y comunicación entre servicios).

- `doc/C4/DiagramaC4lvl1.md`, `doc/C4/c4Nivell2.md`, `doc/C4/c4Nivell3Cataleg.md`, `doc/C4/c4Nivell3MediaServer.md`:
  - Diagramas C4 (niveles 1, 2 y 3) en PlantUML que muestran fronteras del sistema, contenedores, bases de datos y relaciones entre actores y servicios.
  - Nivel 1: visión global del sistema Padalustro y dependencias (Odoo, Video, Cataleg, Apps).
  - Nivel 2: separación Frontend/Backend, contenedores (App Client, App Admin, Cataleg, Express, Odoo) y DBs (MongoDB, MySQL, Postgres).
  - Nivel 3 (Cataleg & MediaServer): detalles sobre endpoints internos (retornar listas, filas, almacenar/partir vídeos, guardar metadatos, almacenar en `/Public/video:id/:resolution`).

- `doc/UC/*.md` (carpeta UC): `Derivar_UC.md`, `odoo_diagrama.md`, `appCliente.md`, `serverExpress.md`:
  - Casos de uso (UC) para las aplicaciones: lista de UC funcionales para app cliente, admin y Odoo.
  - `Derivar_UC.md`: listas de funcionalidades esenciales derivadas de los UC (explorar catálogo, buscar, reproducir, gestionar listas, admin: subir/editar/eliminar vídeos, etc.).
  - `odoo_diagrama.md`: diagrama de casos de uso de Odoo (crear cuenta, autenticar, gestionar suscripciones y pagos).
  - `appCliente.md` y `serverExpress.md`: UC simplificados para cliente y servidor (comprobar token, listar videos, operaciones admin sobre .mp4 y metadatos).

---

**Guía rápida de uso de los diagramas (PlantUML)**

- La mayor parte de los ficheros usan PlantUML. Para generar imágenes desde los `.md` con bloques ```plantuml```, se puede usar cualquier renderer PlantUML o integrarlo con `mkdocs` y plugins o con la extensión de PlantUML en VSCode.
- Recomendación de orden de lectura para comprender el sistema:
  1. `C4/DiagramaC4lvl1.md` (visión global)
  2. `C4/c4Nivell2.md` (contendores)
  3. `C4/*lvl3*.md` (detalles de catálogo y media)
  4. `ER/*` y `DiagramaClases.md` (modelo de datos)
  5. `Pipeline.md` y `DiagramaFlujo.md` / `DiagramaSecuencia.md` (procesos e interacciones)

