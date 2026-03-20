# Server Express

```plantuml
@startuml

left to right direction

actor Admin

rectangle "Server" {
  usecase "post .mp4" as post
  usecase "EDITAR METADATA" as editar
  usecase "Delete .mp4" as delete
  usecase "COMPROBAR\nERROR" as error
  usecase "Comprovar token" as token
  usecase "Trovar .mp4" as vid

}

Admin --> post
Admin --> editar
Admin --> delete
Admin --> error
Admin --> token
Usuari --> vid
Admin --> vid

@enduml

```
