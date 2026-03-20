# UC App Client

```plantuml

@startuml
left to right direction

actor Usuari
actor Admin

rectangle "Server" {
  usecase "Comprovar token i suscricio" as RV
  usecase "Llistar videos" as IS
  usecase "Post .mp4" as Post
}

Usuari --> IS
Usuari --> RV
Admin --> Post


@enduml

```
