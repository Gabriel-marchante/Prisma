# Diagrama de flujo 1

```plantuml
@startuml

(*) --> Obrir_app

if "Te conter ?" then

    -left->[true] Token

    if "" then

        -->[true] "Sel_Perfil"
        --> Cataleg
        --> Buscar
        --> Info_Video

        if "Reproducir ?" then
            if "Suscrito ?" then
                if "Reproduccion" then
                    -right->[true] Reproducir_Video
                    -->[fin video] Info_Video
                else
                    -> Cataleg
                endif
            else
                -up->[false] payment
            endif
        else
            -->[false] "Insertar en lista"
            -right-> Buscar_Lista
        endif
            
        Buscar --> Buscar_Lista
        --> Info_Video
        Buscar --> Crear
        Buscar --> Borrar
    else
        -right->[false] "Iniciar_Sessio"
        -down-> Sel_Perfil
        Iniciar_Sessio --> Config
        --> Perfiles
        --> Crear_Perfil
        Perfiles --> Eliminar_Perfil
        Perfiles --> Modificar_Perfil
        Config --> "Modificar Pagament"
        Config --> "Tancar Sessio"
    endif

else

    -right->[false] "crear_conter"

    if "suscribirse" then
        -right->[true] payment
        --> Iniciar_Sessio
    else
        -left->[false] Iniciar_Sessio
    endif

endif

@enduml
```

# Diagrama de flujo 2

```plantuml
@startuml
start
:Abre APP;

if (¿Tiene cuenta?) then (NO)

  :Crear Cuenta
  (Work);

  if(Pagar) then (SI)
    :Suscribirse;
  else (NO)
  endif
else (SI)
endif

if (¿Tiene token?) then (NO)
  :Iniciar Sesión;
else (SI)
endif

:Sel. Perfil;

if (¿Ver cataleg?) then (SÍ)
  :Buscar Contenido;
  
  if (¿Ver Info MP?) then (SÍ)
    if (¿Repro directa?) then (SÍ)
        if (¿Estas suscrito?) then (Sí)
            :Repro .mp4;
        else (NO)
            if(Pagar) then (SI)
                :Suscribirse;
            else (NO)
  endif   
        endif
    else (NO)
      if (+ lista) then (SI)
      :Añadir lista;
    else (NO)
    endif
    endif
  else (NO)
    :Buscar Lista;
    split
      :Crear Lista;
    split again
      :Borrar;
    end split
  endif
  
else (NO)
  :Config;
  split
    :Perfiles;
    split
      :Crear;
    split again
      :Borrar;
    split again
      :Mod;
    end split
  split again
    :Pagamento (odoo);
  split again
    :Cerrar Sesión;
  end split
endif

stop
@enduml
```
