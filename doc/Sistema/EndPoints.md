# Endpoints del sistema

## Server Cataleg

| get | post | put | dispatch | delete |
| --- | --- | --- | --- | ---|
| /cataleg |/login|| /cataleg/:id | /cataleg/:id|
| /cataleg/:id | /logout ||||
|  /error | /cataleg  |   |   |   |

## Server Express

| get | post | put | dispatch | delete |
| --- | --- | --- | --- | --- |
| /vid | /vid | | | /vid/:id |
| /vid/:id  | /login | | | |
| /error    | /logout ||||
| /Public/video:id/:resolution|||||

## Server Odoo

|get | post |put |dispatch| delete |
|---|---|---|---|---|
|/user/:id|/user|/sub/payment/:id|/user/:id| /user/:id|
|/sub/type|/sub/payment||||
||/sub/cancel/:user||||
||/logout||||
||/login||||

## Explicacio

### Cataleg

#### /get

- /cataleg
  - Retrorna JSON amb tots els videos del cataleg
  - Es pot filtrar afegint els parametres al body
- /cataleg/:id
  - Retorna Json amb tots els valors del id especific
- /error
  - Retorna Json amb id de tots els videos del cataleg

#### /post

- /login
  - body --> JSON amb credencials
  - Retorna resultat de comprovacio token
- /logout
  - Retorna resultat de comprovacio si s'ha eliminat el token
- /cataleg
  - body --> JSON amb informacio d'un video
  - Retorna resultat de l'operacio

#### /dispatch

- /cataleg/:id
  - body --> JSON amb parametres a cambiar
  - Retorna resultat de l'operacio

#### /delete

- /cataleg/:id
  - Retorna resultat de l'operacio

### Express

#### /get

- /vid
  - Retorna tots els videos.mp4 amb sols el seu nom, no tot el video
- /vid/:id
  - Retorna l'arxiu mp4 d'un video amb un id en especific
- /error
  - Retorna el resultat, error si no coinxidis amb id cataleg, okey en cas contrari
- /user/logout
  - Retorna resultat de comprovacio si s'ha eliminat el token
- /Public/video:id/:resolution
  - Retorna un video partit segons el seu id i resolució

#### /post

- /vid
  - body --> video.mp4
- /login
  - body --> JSON amb credencials
  - Retorna resultat de comprovacio token
- /logout
  - Retorna resultat de comprovacio si s'ha eliminat el token

#### /delete

- /vid/:id
  - Retorna resultat operació, si se elimina correctament retorna "s'ha eliminat correctament", en cas contrari retorna error
  
### Odoo

#### /get

- /user/:id
  - Retorna JSON informacio usuari
- /sub/type
  - Retorna JSON amb totes les suscripcions i tipus

#### /post

- /user
  - body --> JSON amb parametres del usuari
  - Retorna resultat de les operacions
- /sub/contract

- /sub/cancel/:user
  - Retorna resultat de les operacions

- /user/logout
  - Retorna resultat de comprovacio si s'ha eliminat el token

- /login
  - body --> JSON amb credencials
  - Retorna resultat de comprovacio token
- /logout
  - Retorna resultat de comprovacio

#### /put

- /sub/payment/:id
  - body --> JSON amb parametres del metode de pagament generals i especific

#### /dispatch

- /user/:id
  - body --> JSON amb parametres a cambiar
  - Retorna resultat de l'operacio


#### /delete

- /user/:id
  - Retorna resultat de l'operacio
