FROM odoo:16

USER root

COPY Odoo/requirements.txt /tmp/
RUN pip3 install --no-cache-dir -r /tmp/requirements.txt

RUN apt-get update && apt-get install -y unzip && rm -rf /var/lib/apt/lists/*

COPY Odoo/entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]
CMD []  # ← CAMBIO AQUÍ: Vacío en lugar de ["odoo"]