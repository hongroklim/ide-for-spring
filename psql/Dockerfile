FROM postgres:13.5-alpine

# Environment Variable
# ENV POSTGRES_SCHEMA=bean_cafe

# Initialize Script
COPY ./psql/init.sh /docker-entrypoint-initdb.d/01-init.sh
