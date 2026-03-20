# Diagrama Secuencia

```plantuml
@startuml DiagramaSecuenciaUCPrincipales
title Diagrama de Secuencia - Casos de Uso Principales

actor Cliente
participant "APP" as App
participant "Catálogo" as Catalogo
participant "Servidor\nContenido" as Contenido
participant "Odoo" as Odoo

Cliente -> App: Abrir APP

alt Tiene token
    App -> Catalogo: Verificar token
    Catalogo-> App: Validación de token
    App -> Catalogo: Ver catalogo

else No tiene token
    App -> Odoo: Crear cuenta
    
    opt Cliente decide pagar
        App --> Odoo: Suscribirse
        App --> Odoo: Procesa suscripción
        Odoo --> App: Suscripción realizada
    end
end

Cliente --> App: Seleccionar perfil
App --> App: Cargar perfil

alt Ver catálogo
    Cliente --> App: Buscar contenido
    App --> Catalogo: Solicitar catálogo
    Catalogo --> App: Lista catálogo
    App --> Cliente: Mostrar catálogo
    
    alt Ver información del video
        Cliente --> App: Ver info video cataleg
        App --> Catalogo: Obtener detalles
        Catalogo --> App: Información 
        App --> Cliente: Mostrar información
        
        alt Reproducción video
            Cliente --> App: Solicitud reproducir
            App --> Odoo: Verificar suscripción
            Odoo --> App: Estado suscripción
            
            alt Cliente suscrito
                App --> Contenido: Solicitar video
                Contenido --> App: Video
                App --> Cliente: Reproducir video
            else No suscrito
                Odoo --> App: Solicitar suscripción
                opt Cliente decide pagar
                    Cliente -> Odoo: Suscribirse
                    Odoo --> App: Suscripción exitosa
                    App --> Contenido: Solicitar video
                    Contenido --> App: Video
                    App --> Cliente: Reproducir video
                end
            end
        end
        
        
    else Gestionar listas
        Cliente --> App: Añadir a lista
        App --> Catalogo: Añadir a lista

        Cliente -> App: Buscar lista
        App -> Catalogo: Buscar lista
        Catalogo --> App: Mostrar listas
        
            Cliente -> App: Crear lista
            App -> Catalogo: Crear lista
        
            Cliente -> App: Borrar lista
            App -> Catalogo: Borrar lista
    
else Configuración
    Cliente -> App: Ir a configuración
    
    opt Gestión de perfiles
            Cliente -> App: Crear perfil
        
            Cliente -> App: Borrar perfil            
        
            Cliente -> App: Modificar perfil
    end
    
    par Gestión de pagos
        Cliente -> App: Pagamento (Odoo)
        App -> Odoo: Gestionar suscripción
        Odoo --> App: Pago actualizado
        App --> Cliente: Confirmación
    end
    
    par Cerrar sesión
        Cliente -> App: Cerrar sesión
        App --> Cliente: Sesión cerrada
    end
end

@enduml
```
