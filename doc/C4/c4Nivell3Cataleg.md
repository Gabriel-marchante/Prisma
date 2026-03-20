# Diagrama C4  Cátaleg de continguts

```plantuml

@startuml

!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml

LAYOUT_LEFT_RIGHT()

title Diagrama C4 Lvl 3 Cátaleg de continguts

Container(AppClient, "App Client")

Container(AppAdmin, "App d'escriptori/D'administració de continguts")

ContainerDb(cataleg, "Base de Datos del cataleg", "MongoDB")

System_Boundary(appCataleg, "Cataleg de continguts") {
  System_Boundary(partClient, " Client ") {

    Container(RetornarLlistaCli, "Retorna una llista filtrable")

    Container(RetornarFilaCli, "Retorna una fila segons el seu id")
  }

  System_Boundary(partAdmin, " Administrador ") {
    System_Boundary(partAdminEnvio, " Envia ") {

    Container(CrearFila, "Crea un fila")

    Container(EliminarFila, "Elimina una fila")

    Container(ModificaFila, "Modifica una fila")
    }

    System_Boundary(partAdminRecib, " Recibeix ") {

    Container(error, "Retorna la llista de videos per comprobar posibles errors")
    }

  }

    Container(credencial, "Comprobar credencials de l'usuari")


}



Rel(AppAdmin, credencial, "Comproba credencials del usuaris per a permitir el seu acces")

Rel(AppClient, credencial, "Comproba credencials del usuaris per a permitir el seu acces")

Rel(partClient, AppClient, "")
Rel(cataleg, partClient, "")


Rel(partAdminRecib, AppAdmin, "Recibeix")
Rel(AppAdmin, partAdminEnvio, "Modifica taula", "Hibernate")

Rel(cataleg, partAdminRecib, "Recibeix")
Rel(partAdminEnvio, cataleg, "")


@enduml
```
