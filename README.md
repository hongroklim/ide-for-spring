# IDE for Spring

> Fully-Dockernized Spring WebMVC Development Environment  

**IDE for Spring** provides all services and tools, based on Docker, to
develop Spring WebMVC.

|            |             |                                                       |
|:----------:|:-----------:|:-----------------------------------------------------:|
| VSCode     | Code Editor | [127.0.0.1:8081](http://127.0.0.1:8081)               |
| WildFly    | WAS         | [127.0.0.1:8082](http://127.0.0.1:8082)               |
| WildFly    | Console     | [127.0.0.1:8083](http://127.0.0.1:8083/console)       |
| SpringWeb  | WebApp      | [127.0.0.1:8082/my-app](http://127.0.0.1:8082/my-app) |
| PostgreSQL | DBMS        | 127.0.0.1:5432                                        |
| PGAdmin4   | Query Tool  | [127.0.0.1:5433](http://127.0.0.1:5433)               |

Enjoy your programming!

## How To Use

### Installation
1. docker and docker-compose are required.
1. `git clone --depth=1 https://github.com/hongroklim/ide-for-spring.git`
1. `cd ide-for-spring`
1. `docker-compose up -d`

### Code Edit (on VSCode)
1. Access [127.0.0.1:8081](http://127.0.0.1:8081)
1. Enter password (`mycdrpassword` default)
1. `File` > `Open Workspace` and choose `~/my-app`
1. (Optional) `Extensions` > `Search` and install `Java Extension Pack`

### Deploy (on VSCode)
1. `File` > `Maven Projects` > `my-app` right-click then `deploy`
1. (See) [WildFly Maven Plugin](https://docs.jboss.org/wildfly/plugins/maven/latest/)

### Remote Debugging (on VSCode)
1. `Debug` > `Debug WildFly` and start

### Monitoring (on WildFly Console)
1. Access [127.0.0.1:8083](http://127.0.0.1:8083/console)
1. Login (`myname` and `mywildflypassword` default)
1. (See) [WildFly Admin Guide](https://docs.wildfly.org/17/Admin_Guide.html#web-management-interface)

### Query Tool (on PGAdmin4)
1. Access [127.0.0.1:5433](http://127.0.0.1:5433)
1. Login (`myname` and `mypgapassword` default)
1. `Server` > `Create` and create the connection
  * Address: `172.18.0.12`
  * Port: `5432`
  * DB: `db_psql`
  * USER: `usr_psql`
  * PASSWORD: `mypsqlpassword`

## Features

### VSCode

It contains `Openjdk 8`, `Openjdk 11` and `Maven`. Install VSCode extensions
in your preferences. (e.g. Java Extension Pack, XML and lombok)

## Configurations

All configurations are assigned with default values. They can be modifed as
well as added.

[./.env](./.env)\
Usernames and passwords of the services.

[./docker-compose.yml](./docker-compose.yml)\
Mount directory, external ports and IP address of intranet.

[./wildfly/standalone.xml](./wildfly/standalone.xml)\
WildFly configurations.

[./psql/init.sh](./psql/init.sh)\
Initialize PostgreSQL with a script (e.g. dump or schema).

## Using with NGINX

If you have very limited ports (e.g. `80` only in cloud server), NGINX can
solve this problem.

```bash
# Install NGINX

$ sudo apt-get update
$ sudo apt-get install nginx

# Start Up
$ sudo service nginx start
```

```
# /etc/nginx/conf.d/default.d

server {
	listen [::]:80;
  server_name localhost

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

  # WildFly Console
  location /wildfly-admin/ {
    proxy_set_header X-Script-Name /wildfly-admin;
    proxy_set_header Host          $host;

    proxy_pass http://127.0.0.1:8083/;
    proxy_redirect off;
  }

  # pgadmin4
  location /pgadmin4/ {
    proxy_set_header X-Script-Name /pgadmin4;
    proxy_set_header Host          $host;

    proxy_pass http://127.0.0.1:5433/;
    proxy_redirect off;
  }
}
```

```bash
# Restart

$ sudo service nginx restart
```

Now you can access all services in 80 port distinguished by subpath
(e.g. `/code-server`, `/wildfly` and `/pgadmin4`)
