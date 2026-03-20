#!/bin/bash
set -e

echo "----- ENTRYPOINT PERSONALIZADO EJECUTÁNDOSE -----"
echo "Usuario actual: $(whoami)"
echo "- Preparando Odoo..."

mkdir -p /mnt/extra-addons

if [ -d /mnt/modulos-zip ]; then
  echo "- Contenido de /mnt/modulos-zip:"
  ls -la /mnt/modulos-zip/
  
  if ls /mnt/modulos-zip/*.zip 1> /dev/null 2>&1; then
    echo "- Descomprimiendo módulos (sin sobrescribir existing files)..."
    for zipfile in /mnt/modulos-zip/*.zip; do
      echo "  Procesando: $(basename $zipfile)"
      # -n asegura que NO sobrescriba archivos que ya existen en el host
      unzip -n "$zipfile" -d /mnt/extra-addons
    done
  else
    echo "- No se encontraron archivos .zip"
  fi
else
  echo "- No existe /mnt/modulos-zip"
fi

chown -R odoo:odoo /mnt/extra-addons

echo "- Contenido de /mnt/extra-addons:"
ls -la /mnt/extra-addons/

cat > /etc/odoo/odoo.conf <<EOF
[options]
addons_path = /usr/lib/python3/dist-packages/odoo/addons,/mnt/extra-addons
data_dir = /var/lib/odoo
db_host = db
db_port = 5432
db_user = odoo
db_password = odoo
admin_passwd = padalustro
EOF

echo "> Arrancando Odoo..."

exec odoo -c /etc/odoo/odoo.conf