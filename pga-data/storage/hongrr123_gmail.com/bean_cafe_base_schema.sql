/*
bean_cafe_base_schema
- implements building background works behind of schema
- execute with "postgres" role
*/

-- 0.1 Create Tablespace directory in psql
/*
# docker exec -it bean-cafe-project_psql_1 /bin/sh
# mkdir -p /var/lib/postgresql/data/bean_cafe
# chown postgres /var/lib/postgresql/data/bean_cafe
# chmod 700 /var/lib/postgresql/data/bean_cafe
*/

-- 0.2 Execute script in pgadmin4
/*
# docker exec -it bean-cafe-project_psql_1 /bin/sh

build base script:
# psql --host=172.18.0.12 --port=5432 --username=postgres --dbname=postgres --password --file=/var/lib/pgadmin/storage/hongrr123_gmail.com/bean_cafe_base_schema.sql

build other script:
# psql --host=172.18.0.12 --port=5432 --username=postgres --dbname=postgres --password --file=/var/lib/pgadmin/storage/hongrr123_gmail.com/bean_cafe_dump.sql

*/

-- 1. Tablespace
CREATE TABLESPACE bean_cafe_ts
  OWNER postgres
  LOCATION '/var/lib/postgresql/data/bean_cafe';

ALTER TABLESPACE bean_cafe_ts
  OWNER TO postgres;

-- 2. Role
CREATE ROLE bean_cafe_dev WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION;

-- 3. Database
CREATE DATABASE bean_cafe_db
    WITH 
    OWNER = bean_cafe_dev
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TABLESPACE = bean_cafe_ts
    CONNECTION LIMIT = -1;

-- 3.1 Database configuration
ALTER ROLE bean_cafe_dev IN DATABASE bean_cafe_db
    SET search_path TO bean_cafe;
ALTER ROLE bean_cafe_dev IN DATABASE bean_cafe_db
    SET default_tablespace TO 'bean_cafe_ts';

-- 3.2 Role configuration after create database
ALTER ROLE bean_cafe_dev PASSWORD 'ghdfhr';
ALTER ROLE bean_cafe_dev IN DATABASE bean_cafe_db SET search_path TO bean_cafe;
ALTER ROLE bean_cafe_dev IN DATABASE bean_cafe_db SET default_tablespace TO 'bean_cafe_ts';