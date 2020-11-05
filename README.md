# Bean Cafe Project

> Demo Project by Hongrok Lim As airman
from ROKAF CKIS *(Cyber Knowledge Information Space)*

## Envorinment Configuration

### git

```bash
# install git (Ubuntu)
$ sudo apt-get update
$ sudo apt-get install git

# initial configuration (name, email)
$ git config --global --edit
```

### Docker

https://docs.docker.com/get-docker/

### Postgresql

```bash
# in host
$ sudo docker exec -it bean-cafe-project_pgadmin4_1 /bin/sh

# in pgadmin4 container
$ psql --host=172.18.0.12 --port=5432 --dbname=bean_cafe_db \
  --username=bean_cafe_dev --password \
  --file=/var/lib/pgadmin/storage/hongrr123_gmail.com/bean_cafe_base_schema.sql
# and execute more script

```

### NGINX

#### 1. install nginx

```bash
# install nginx (Ubuntu)
$ sudo apt-get update
$ sudo apt-get install nginx

# start nginx (Ubuntu)
$ sudo service nginx start
```

#### 2. configuration nginx and restart

```bash
$ sudo vim /etc/nginx/conf.d/default.d
```

```
server {
    listen 172.25.10.99:80;
    server_name 172.25.10.99;

    # pgadmin4
    location /pgadmin4/ {
        proxy_set_header X-Script-Name /pgadmin4;
        proxy_set_header Host          $host;

        proxy_pass http://127.0.0.1:5433/;
        proxy_redirect off;
    }

    # code-server
    location /code-server/ {
        proxy_set_header X-Script-Name /code-server;
        proxy_set_header Host          $host;

        # web socket conf
        proxy_set_header Upgrade       $http_upgrade;
        proxy_set_header Connection    "Upgrade";

        proxy_pass http://127.0.0.1:8081/;
        proxy_redirect off;
        proxy_http_version 1.1;
    }

    # wildfly
    location /wildfly/ {
        proxy_set_header X-Script-Name /wildfly;
        proxy_set_header Host          $host;

        proxy_pass http://127.0.0.1:8082/;
        proxy_redirect off;
    }
}
```

```bash
$ sudo service nginx restart
```

### privileges

#### 1. mount privilege

``` bash
# pgadmin4
$ sudo chown -R hongrr123:5050 ~/bean-cafe-project/pga-data
$ sudo chmod -R 775 ~/bean-cafe-project/pga-data

# maven repository
$ sudo chown -R hongrr123 /root/.m2/repository
```

#### 2. volume privilege

```bash
# wildfly deployment (in code-server)
$ sudo chown coder ~/wildfly/standalone/deployments/*
```

### Extensions and Shortcuts

#### extensions

- Java Extension Pack (vscjava)
- XML (redhat)
- Dracula Official (dracula-theme)
- lombok (Gabriel Basilio Brito)

#### extensions for window

- Remote-containers
- Remote-WSL
- Docker

#### Kebyoard Shortcuts

- Trigger Suggest : "shift + space"
