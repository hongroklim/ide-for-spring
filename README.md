# Bean Cafe Project

> Demo Project by Hongrok Lim As airman
from ROKAF CKIS *(Cyber Knowledge Information Space)*

Development in IntelliJ idea Ultimate in windows

## Envorinment Configuration

### Installation in Windows

* **JDK8** https://adoptopenjdk.net/
* **MAVEN** https://maven.apache.org/download.cgi
* **User Variable** <code>JAVA_HOME</code> and <code>MAVEN_HOME</code>

### Intellij idea Ultimate



### Git (on WSL2)

```bash
# install git (Ubuntu)
$ sudo apt-get update
$ sudo apt-get install git

# initial configuration (name, email)
$ git config --global --edit
```

### Docker (with WSL2)

#### install Docker and Docker Desktop

https://docs.docker.com/get-docker/

#### docker compose
```bash
$ sudo curl -L "https://github.com/docker/compose/releases/download/1.27.4/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
$ sudo chmod +x /usr/local/bin/docker-compose
$ sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
```

