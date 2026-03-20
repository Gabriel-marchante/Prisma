# Relaciones generales entre servicios

````
Catàleg
````
-**Relacions**:

- Rep consultes de l'App client
- Rep consultes de l'App web d'administrador
- Es comunica amb la base de dades "Catàleg"
````
Server media (Express)
````
-**Relacions**:

- Rep sol·licituds de reproducció de l'App client
- Rep sol·licituds per afegir contingut de l'Aplicació d'administrador
- Gestiona la base de dades "Continguts"
- S'autentica amb Odoo per verificar subscripcions
````
Odoo
````
-**Relacions**:

- Rep operacions CRUD des del Portal web de subscripcions
- Gestiona la base de dades "Subscripcions"
- Autentica l'accés al Servei de continguts segons l'estat de subscripció