# Modelos de alojamiento

La plataforma se implementa mediante un enfoque de **alojamiento distribuido**, en el que los distintos componentes del sistema se despliegan como servicios independientes. Cada uno asume una función concreta dentro de la arquitectura, lo que facilita la escalabilidad, el mantenimiento y la evolución del sistema sin afectar al resto de módulos.

---

```
        ┌──────────────────────────┐
        │                          │     
        │       Portal Web         │  
        │       (Aplicación)       │     
        │                          │     
        └────────────┬─────────────┘
                     │
                     │ 
                     │
    ┌────────────────┴┌─────────┐
    │                 |         │
┌───▼──────────┐  ┌───▼─────┐  ┌▼─────────────┐
│ Server video │  │Catálogo │  │    ODOO      │
│              │  │         │  │              │
│ Videos       │  │ Info de │  │ Usuarios y   │
│              │  │video    │  │suscripciones │
└──────────────┘  └─────────┘  └──────────────┘
     ↑                 ↑              ↑
     │                 │              │
     └─────────────────┴──────────────┘
```
---
# Elección de Alojamiento para el Portal de Streaming en OVHcloud


## **Cloud VPS en OVHcloud**

---

### Razones de la elección

- **Balance ideal entre rendimiento y precio:**  
  Proporciona recursos dedicados (CPU, RAM, disco NVMe) y tráfico ilimitado, permitiendo múltiples conexiones simultáneas y consultas al catálogo.

- **Escalabilidad sencilla:**  
  Se pueden aumentar CPU, RAM o almacenamiento según crezca tu proyecto, sin interrupciones significativas.

- **Control total:**  
  Acceso root para instalar y configurar servicios como Nginx, base de datos, media server, portal web y Odoo según tus necesidades.

- **Rentable frente a un servidor dedicado:**  
  Un dedicado es más caro y solo se justifica con mucho tráfico o requisitos de hardware especiales.

---

### Conclusión

**OVHcloud Cloud VPS** es la opción más equilibrada, escalable, flexible y rentable para alojar el portal de streaming y todos sus componentes hasta que el tráfico justifique un servidor dedicado.
