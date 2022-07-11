FROM jboss/wildfly:20.0.1.Final

# Prepare to deploy and log
# Standalone debugging settings
USER root

RUN mkdir -p $JBOSS_HOME/standalone/log/ \
    && touch $JBOSS_HOME/standalone/log/server.log $JBOSS_HOME/standalone/log/audit.log \
    && chown -R jboss:0 $JBOSS_HOME/standalone/log/ \
    && echo "JAVA_OPTS=\"$JAVA_OPTS -agentlib:jdwp=transport=dt_socket,address=*:8787,server=y,suspend=n\"" \
       >> $JBOSS_HOME/bin/standalone.conf

# Management console
ARG WILDFLY_ADMIN_NAME
ARG WILDFLY_ADMIN_PASSWORD

RUN $JBOSS_HOME/bin/add-user.sh ${WILDFLY_ADMIN_NAME} ${WILDFLY_ADMIN_PASSWORD} --silent

USER jboss

# Put standalone configuration file
COPY ./wildfly/standalone.xml $JBOSS_HOME/standalone/configuration/standalone.xml

# Management console ip
CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
