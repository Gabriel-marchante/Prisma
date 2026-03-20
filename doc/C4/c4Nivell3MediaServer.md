# Diagrama C4  Cátaleg de continguts

```plantuml

@startuml

!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml

'LAYOUT_LEFT_RIGHT()

title Diagrama C4 Lvl 3 Servei< de continguts

Container(AppClient, "App Client")

Container(AppAdmin, "App d'escriptori/D'administració de continguts")

ContainerDb(contiguts, "Base de Datos de continguts", "MongoDB")


System_Boundary(ServeiContinguts, "Servei de continguts") {
  System_Boundary(partClient, " Client ") {

    Container(RetornarLlistaCli, "Retorna una llista amb les metadades del videos")

    Container(RetornarReproduible, "Retorna un video partit segons el seu id i resolució")
  }
  
  Container(Almacen, "Almacen de videos, estan guardats en /Public seguint aquesta estructura /Public/video:id/:resolution")

  System_Boundary(partAdmin, " Administrador ") {
    System_Boundary(partAdminEnvio, " Envia ") {

    Container(PujarMP4, "Recibeix video.mp4")

    Container(ExtrauMeta, "Extrau Metadades/Envia Metadades")

    Container(PartixVideo, "Partix un video")
    }

    System_Boundary(partAdminElimina, " Elimina ") {

    Container(delete, "Elimina el video de /Public/video:id/ i suprimix la fila amb el mateix id de la base de dades")

    }

    Container(error, "Retorna la llista de videos per comprobar posibles errors")

  }

    Container(credencial, "Comprobar credencials de l'usuari")


}


Rel(AppAdmin, credencial, "Comproba credencials del usuaris per a permitir el seu acces")

Rel(AppClient, credencial, "Comproba credencials del usuaris per a permitir el seu acces")


Rel(PujarMP4, PartixVideo, "")
Rel(PujarMP4, ExtrauMeta, "")
Rel(PartixVideo, Almacen, "Guarda")
Rel(delete, BorraLinea, "")
Rel(Almacen,RetornarReproduible,"")

Rel(AppAdmin, PujarMP4, "")
Rel(AppAdmin, delete, "")
Rel(error, AppAdmin, "")

Rel(contiguts,RetornarLlistaCli,"Retorna")
Rel(contiguts, error, "")
Rel(BorraLinea, contiguts, "")
Rel(ExtrauMeta,Guarda,"")
Rel(Guarda,contiguts,"")


Rel(RetornarReproduible,AppClient,"")
Rel(RetornarLlistaCli,AppClient,"")



@enduml
```
