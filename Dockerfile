FROM maven:3.5.4-jdk-8 AS builder
COPY . /usr/src/mymaven

RUN cd /usr/src/mymaven && mvn -Dmaven.test.skip=true clean install

FROM debian:stable-slim

LABEL Description="STIF BOIV IEV" \
      Version="1.0"

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get -y update ; apt-get -y install wget vim net-tools netcat-traditional curl && mkdir /install && rm -rf /var/lib/apt/lists/*


#--- Java
RUN echo "deb http://http.debian.net/debian stretch-backports main" > /etc/apt/sources.list.d/stretch-backports.list && \
    mkdir -p /usr/share/man/man1/ && \
    apt-get update && \
    apt-get install -y -t stretch-backports openjdk-8-jre-headless && rm -rf /var/lib/apt/lists/*


#--- Wildfly

ENV WILDFLY_HOME=/opt/wildfly WILDFLY_VERSION=9.0.2.Final LANG=fr_FR.UTF-8
COPY config/*.xml /install/

RUN cd /install && wget -q http://download.jboss.org/wildfly/${WILDFLY_VERSION}/wildfly-${WILDFLY_VERSION}.tar.gz && \
    cd /opt ; tar xzf /install/wildfly-${WILDFLY_VERSION}.tar.gz ; ln -s wildfly-${WILDFLY_VERSION} wildfly && rm /install/wildfly-${WILDFLY_VERSION}.tar.gz
RUN cd /install && wget -q http://central.maven.org/maven2/org/postgresql/postgresql/9.4-1206-jdbc41/postgresql-9.4-1206-jdbc41.jar && \
	wget -q http://central.maven.org/maven2/net/postgis/postgis-jdbc/2.2.1/postgis-jdbc-2.2.1.jar && \
	wget -q http://www.hibernatespatial.org/repository/org/hibernate/hibernate-spatial/4.3/hibernate-spatial-4.3.jar && \
	wget -q http://central.maven.org/maven2/com/vividsolutions/jts/1.13/jts-1.13.jar && \
	wget -q http://central.maven.org/maven2/org/hibernate/hibernate-core/4.3.11.Final/hibernate-core-4.3.11.Final.jar && \
	wget -q http://central.maven.org/maven2/org/hibernate/hibernate-envers/4.3.11.Final/hibernate-envers-4.3.11.Final.jar && \
	wget -q http://central.maven.org/maven2/org/hibernate/javax/persistence/hibernate-jpa-2.1-api/1.0.0.Final/hibernate-jpa-2.1-api-1.0.0.Final.jar && \
	wget -q http://central.maven.org/maven2/org/hibernate/hibernate-entitymanager/4.3.11.Final/hibernate-entitymanager-4.3.11.Final.jar && \
	wget -q http://central.maven.org/maven2/org/hibernate/hibernate-infinispan/4.3.11.Final/hibernate-infinispan-4.3.11.Final.jar && \
    \
#    mkdir -p $WILDFLY_HOME/modules/org/postgres/main && \
#    cd /install && mv post*.jar $WILDFLY_HOME/modules/org/postgres/main && \
#    mv module_postgres.xml $WILDFLY_HOME/modules/org/postgres/main/module.xml && \
#    rm $WILDFLY_HOME/modules/system/layers/base/org/hibernate/main/hibernate*4.3.10.Final.jar && \
#    mv hibernate-core*.jar hibernate-envers*.jar hibernate-entitymanager*.jar hibernate-jpa-2.1-api-1.0.0.Final.jar $WILDFLY_HOME/modules/system/layers/base/org/hibernate/main && \
#    mv hibernate-spatial-4.3.jar jts-1.13.jar $WILDFLY_HOME/modules/system/layers/base/org/hibernate/main && \
#    mv -f module_hibernate.xml $WILDFLY_HOME/modules/system/layers/base/org/hibernate/main/module.xml && \
#    rm $WILDFLY_HOME/modules/system/layers/base/org/hibernate/infinispan/main/hibernate*4.3.10.Final.jar && \
#    mv hibernate-infinispan-4.3.11.Final.jar $WILDFLY_HOME/modules/system/layers/base/org/hibernate/infinispan/main/ && \
#    mv module_infinispan.xml $WILDFLY_HOME/modules/system/layers/base/org/hibernate/infinispan/main/module.xml
#RUN mkdir -p $WILDFLY_HOME/modules/org/postgres/main && \
    mkdir -p $WILDFLY_HOME/modules/org/postgres/main && \
    cd /install && cp post*.jar $WILDFLY_HOME/modules/org/postgres/main && \
    cp module_postgres.xml $WILDFLY_HOME/modules/org/postgres/main/module.xml && \
    rm $WILDFLY_HOME/modules/system/layers/base/org/hibernate/main/hibernate*4.3.10.Final.jar && \
    cp hibernate-core*.jar hibernate-envers*.jar hibernate-entitymanager*.jar hibernate-jpa-2.1-api-1.0.0.Final.jar $WILDFLY_HOME/modules/system/layers/base/org/hibernate/main && \
    cp hibernate-spatial-4.3.jar jts-1.13.jar $WILDFLY_HOME/modules/system/layers/base/org/hibernate/main && \
    cp -f module_hibernate.xml $WILDFLY_HOME/modules/system/layers/base/org/hibernate/main/module.xml && \
    rm $WILDFLY_HOME/modules/system/layers/base/org/hibernate/infinispan/main/hibernate*4.3.10.Final.jar && \
    cp hibernate-infinispan-4.3.11.Final.jar $WILDFLY_HOME/modules/system/layers/base/org/hibernate/infinispan/main/ && \
    cp -f module_infinispan.xml $WILDFLY_HOME/modules/system/layers/base/org/hibernate/infinispan/main/module.xml
#RUN apt-get update && apt-get install -y locales && apt-get -y clean && rm -rf /var/lib/apt/lists/* && \
#    localedef -i fr_FR -c -f UTF-8 -A /usr/share/locale/locale.alias fr_FR.UTF-8 && \
#    mkdir /update
RUN apt-get update && apt-get install -y locales procps && apt-get -y clean && rm -rf /var/lib/apt/lists/* && \
    localedef -i fr_FR -c -f UTF-8 fr_FR.UTF-8 && \
    mkdir /update


COPY --from=builder /usr/src/mymaven/boiv_iev/target/chouette_iev.ear /chouette_iev.ear
VOLUME /update

COPY boiv.properties /etc/chouette/boiv/

EXPOSE 8080 9990
COPY docker-entrypoint.sh /
ENTRYPOINT ["/docker-entrypoint.sh"]
