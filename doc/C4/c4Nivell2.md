# Diagrama C4 Nivell 2

```plantuml
@startuml
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml

LAYOUT_LEFT_RIGHT()

title Diagrama C4 Nivell 2

Person(User, "Usuario", "Usuario que accede vía web o móvil")

System_Boundary(app, "Padalustro") {
    System_Boundary(front, "Frontend") {
    
    ' Client Movil
    Container(Dart, "App Client", "Dart", "App movil")

    ' Client admin
    Container(appEscritori, "Aplicacio d'escritori", "?", "App escritori")

    ' Portal Odoo
    Container(webOdoo, "Portal Web" , "HTML, CSS, JS", "Web on gestionar suscripcions")
    }
    System_Boundary(back, "Backend") {
    ' Server Cataleg
    Container(cataleg, "Cataleg de videos", "Java, Spring Boot, hibernate")
    
    ' Server Express
    Container(express, "Servei de continguts", "express","Servei on guardar els videos")

    ' Server Odoo
    Container(serverOdoo, "Servei Odoo", "Odoo", "Servidor on es gestionaran tots els usuaris i suscripcions")

    ContainerDb( catalegDB, "DB del cataleg", "MongoDB", "Almacena tota la informacio del cataleg")
    
    ContainerDb( expressDB, "DB del servidor express", "MySql", "Almacena tota la informacio dels videos")

    ContainerDb( odooDB, "DB del servidor Odoo", "PostgresSql", "Almacena tota la informacio del servidor Odoo")
    }
}

Rel(User, Dart, "Accede a")
Rel(User,appEscritori, "Utilitza")
Rel(User,webOdoo,"Gestiona")

Rel(Dart,cataleg,"Consulta","HTTP")
Rel(Dart, express, "Reproduir Video")
Rel(Dart,serverOdoo,"Inicia sessio")

Rel(cataleg, catalegDB, "Consulta")

Rel(appEscritori, cataleg, "Consulta", "HTTP")
Rel(appEscritori, express, "Pujar Video")
Rel(appEscritori, serverOdoo, "Inicia sessio")

Rel(express, expressDB, "Consulta")

Rel(webOdoo, serverOdoo, "Gestiona")

Rel(serverOdoo, odooDB, "Gestiona")

@enduml
```
