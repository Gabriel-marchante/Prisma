# Requerimetns funcionals y no funcionals del sistema

## Backend

### Cataleg

- **Funcionals**

    ```text
    RF01 - L'usuari pot consultar els videos del cataleg  
    RF02 - L'usuari pot filtrar la busqueda del cataleg  
    RF03 - L'usuari podra puntuar i comentar els videos  
    RF04 - El admin pot afegir videos al cataleg  
    RF05 - El admin pot actualitzar videos del cataleg  
    RF06 - El admin pot eliminar videos del cataleg  
    RF07 - El sistema mostrara un video amb la seua informacio detallada  
    RF08 - El sistema permitira crear, editar i eliminar llistes de reproduccio personalitzades  
    RF10 - El cataleg mostrara seccions com : "comedia, drama, terror..."
    RF11 - El sistema ha de poder validar el token creat per odoo
    ```

- **No Funcionals**  

    ```text
    RNF01 - El servidor mostrara el cataleg en format JSON  
    RNF02 - El servidor mostrara 10 videos per pagina  
    ```

### Servei de continguts

- **Funcionals**  

    ```text
    RF01 - L'usuari pot reproduir videos  
    RF02 - El admin pot afegir nous videos  
    RF03 - EL admin pot eliminar videos  
    RF04 - El sistema ha de poder verificar les credencials de l'usuari  
    RF05 - El admin ha de ser capac de comprovar que totes les dades estiguen correctes
    RF06 - El sistema permitirá descarregar videos (depende de suscripcio).
    RF07 - El sistema ha de poder validar el token creat per odoo

    ```

- **No Funcionals**  

    ```text
    RNF01 - El sistema retorna un fitxer index.u3m8s  
    RNF02 - La resposta al retornar un video ha de ser instantanea
    ```

### Odoo

- **Funcionals**  

    ```text  
    RF01 - L'usuari ha de ser capac de crear-se un conter  
    RF02 - L'usuari ha de insertar un metode de pagament  
    RF03 - El sistema comprovara les credencials dels usuaris  
    RF04 - El sistema controlara tots els perfils de cada usuari  
    RF05 - L'usuari ha de poder modificar els parametres de la suscripcio  
    RF06 - L'usuari ha de poder modificar els parametres del seu conter i perfils
    ```

- **No Funcionals**  

    ```text
    RNF01 - ?
    ```

## Frontend

### App mobil

- **Funcionals**  

    ```text
    RF01 - L'usuari ha d'iniciar sessio amb usuari i contrasenya  
    RF02 - El sistema ha de guardar l'estat dels videos de l'usuari  
    RF02 - L'usuari ha de poder reproduir el video  
    RF04 - L'usuari ha de poder filtrar la busqueda de videos  
    RF05 - L'usari ha de poder veure informacio detallada dels videos  
    RF06 - L'usuai ha de poder pausar i continuar el video en reproduccio
    ```

- **No Funcionals**  

### Aplicacio d'escritori

- **Funcionals**  

    ```text
    RF01 - El admin ha de poder afegir nous videos
    RF02 - El admin ha de poder insertar videos al cataleg usant les metadates dels videos afegits
    RF03 - El admin ha de poder editar metadates dels videos
    ```

**No Funcionals**  

### Web Odoo

- **Funcionals**  

    ```text
    RF01 - L'usuari ha de poder gestionar-se la seua suscripcio
    RF02 - L'usuari ha de poder crear-se un conter
    ```

- **No Funcional**  

    ```text
    RNF01 - El sistema retorna un token amb la verificacio de l'usuari

    ```
