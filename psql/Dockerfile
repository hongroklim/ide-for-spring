FROM postgres:13.5-alpine

# Initialize Script
COPY ./psql/init.sql /docker-entrypoint-initdb.d/01-init.sql
