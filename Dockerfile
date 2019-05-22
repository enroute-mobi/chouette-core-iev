# Usage sample :
#
# docker build --build-arg WEEK=`date +%Y%U` -t chouette-core-iev .
# docker run -p 8080:8080 --add-host "db:172.17.0.1" --add-host "chouette-core.test:172.17.0.1" -e POSTGRES_NAME="chouette2" -e BOIV_GUI_URL_BASE=http://chouette-core.test -e BOIV_GUI_URL_TOKEN="secret" -it --rm chouette-core-iev

FROM maven:3.6.0-jdk-8 AS builder

ARG WEEK

WORKDIR /usr/src/mymaven

# Generated with : ls **/pom.xml | awk '{ printf "COPY %s ./%s\n", $0, $0 }'
COPY pom.xml ./pom.xml
COPY boiv_iev/pom.xml ./boiv_iev/pom.xml
COPY chouette_iev/pom.xml ./chouette_iev/pom.xml
COPY mobi.chouette.boiv.service/pom.xml ./mobi.chouette.boiv.service/pom.xml
COPY mobi.chouette.boiv.ws/pom.xml ./mobi.chouette.boiv.ws/pom.xml
COPY mobi.chouette.command/pom.xml ./mobi.chouette.command/pom.xml
COPY mobi.chouette.common/pom.xml ./mobi.chouette.common/pom.xml
COPY mobi.chouette.dao.iev/pom.xml ./mobi.chouette.dao.iev/pom.xml
COPY mobi.chouette.dao/pom.xml ./mobi.chouette.dao/pom.xml
COPY mobi.chouette.exchange.converter/pom.xml ./mobi.chouette.exchange.converter/pom.xml
COPY mobi.chouette.exchange.netex/pom.xml ./mobi.chouette.exchange.netex/pom.xml
COPY mobi.chouette.exchange.netex_stif/pom.xml ./mobi.chouette.exchange.netex_stif/pom.xml
COPY mobi.chouette.exchange/pom.xml ./mobi.chouette.exchange/pom.xml
COPY mobi.chouette.exchange.validator/pom.xml ./mobi.chouette.exchange.validator/pom.xml
COPY mobi.chouette.model.iev/pom.xml ./mobi.chouette.model.iev/pom.xml
COPY mobi.chouette.model/pom.xml ./mobi.chouette.model/pom.xml
COPY mobi.chouette.persistence.hibernate/pom.xml ./mobi.chouette.persistence.hibernate/pom.xml
COPY mobi.chouette.schema.checker/pom.xml ./mobi.chouette.schema.checker/pom.xml
COPY mobi.chouette.service/pom.xml ./mobi.chouette.service/pom.xml
COPY mobi.chouette.ws/pom.xml ./mobi.chouette.ws/pom.xml

# See https://github.com/apache/maven-dependency-plugin/pull/2
RUN mvn -T 2C --batch-mode com.offbytwo.maven.plugins:maven-dependency-plugin:3.1.1.MDEP568:go-offline -DexcludeGroupIds=mobi.chouette

COPY . /usr/src/mymaven
RUN mvn -Dmaven.test.skip=true -DskipTests=true --batch-mode install

FROM debian:stable-slim

LABEL Description="Chouette IEV"

#--- Java, wget & locales
ENV LANG=en_US.UTF-8 LANGUAGE=en_US:en LC_ALL=en_US.UTF-8
RUN echo "deb http://http.debian.net/debian stretch-backports main" > /etc/apt/sources.list.d/stretch-backports.list && \
    mkdir -p /usr/share/man/man1/ && \
    DEBIAN_FRONTEND=noninteractive && \
    apt-get update && \
    apt-get install -y --no-install-recommends wget netcat-traditional locales && \
    localedef -i en_US -c -f UTF-8 en_US.UTF-8 && \
    apt-get install -y -t stretch-backports --no-install-recommends openjdk-8-jre-headless && rm -rf /var/lib/apt/lists/*

#--- Wildfly

ENV WILDFLY_HOME=/opt/wildfly WILDFLY_VERSION=9.0.2.Final
COPY config/*.xml /install/

RUN cd /install && wget -q http://download.jboss.org/wildfly/${WILDFLY_VERSION}/wildfly-${WILDFLY_VERSION}.tar.gz && \
    cd /opt ; tar xzf /install/wildfly-${WILDFLY_VERSION}.tar.gz ; ln -s wildfly-${WILDFLY_VERSION} wildfly && \
    cd /install && wget -q http://central.maven.org/maven2/org/postgresql/postgresql/9.4-1206-jdbc41/postgresql-9.4-1206-jdbc41.jar && \
    wget -q http://central.maven.org/maven2/net/postgis/postgis-jdbc/2.2.1/postgis-jdbc-2.2.1.jar && \
	  wget -q http://www.hibernatespatial.org/repository/org/hibernate/hibernate-spatial/4.3/hibernate-spatial-4.3.jar && \
  	wget -q http://central.maven.org/maven2/com/vividsolutions/jts/1.13/jts-1.13.jar && \
	  wget -q http://central.maven.org/maven2/org/hibernate/hibernate-core/4.3.11.Final/hibernate-core-4.3.11.Final.jar && \
    wget -q http://central.maven.org/maven2/org/hibernate/hibernate-envers/4.3.11.Final/hibernate-envers-4.3.11.Final.jar && \
	  wget -q http://central.maven.org/maven2/org/hibernate/javax/persistence/hibernate-jpa-2.1-api/1.0.0.Final/hibernate-jpa-2.1-api-1.0.0.Final.jar && \
	  wget -q http://central.maven.org/maven2/org/hibernate/hibernate-entitymanager/4.3.11.Final/hibernate-entitymanager-4.3.11.Final.jar && \
	  wget -q http://central.maven.org/maven2/org/hibernate/hibernate-infinispan/4.3.11.Final/hibernate-infinispan-4.3.11.Final.jar && \
    mkdir -p $WILDFLY_HOME/modules/org/postgres/main && \
    cd /install && cp post*.jar $WILDFLY_HOME/modules/org/postgres/main && \
    cp module_postgres.xml $WILDFLY_HOME/modules/org/postgres/main/module.xml && \
    rm $WILDFLY_HOME/modules/system/layers/base/org/hibernate/main/hibernate*4.3.10.Final.jar && \
    cp hibernate-core*.jar hibernate-envers*.jar hibernate-entitymanager*.jar hibernate-jpa-2.1-api-1.0.0.Final.jar $WILDFLY_HOME/modules/system/layers/base/org/hibernate/main && \
    cp hibernate-spatial-4.3.jar jts-1.13.jar $WILDFLY_HOME/modules/system/layers/base/org/hibernate/main && \
    cp -f module_hibernate.xml $WILDFLY_HOME/modules/system/layers/base/org/hibernate/main/module.xml && \
    rm $WILDFLY_HOME/modules/system/layers/base/org/hibernate/infinispan/main/hibernate*4.3.10.Final.jar && \
    cp hibernate-infinispan-4.3.11.Final.jar $WILDFLY_HOME/modules/system/layers/base/org/hibernate/infinispan/main/ && \
    cp -f module_infinispan.xml $WILDFLY_HOME/modules/system/layers/base/org/hibernate/infinispan/main/module.xml && \
    rm -rf /install/

COPY --from=builder /usr/src/mymaven/boiv_iev/target/chouette_iev.ear /chouette_iev.ear

COPY boiv.properties /etc/chouette/boiv/

ENV POSTGRES_HOST="db" POSTGRES_NAME="chouette" POSTGRES_USER="chouette" BOIV_GUI_URL_BASE="http://front:3000/"
ENV JAVA_OPTS='-Xms64m -Xmx768m -XX:MaxPermSize=256m -Djava.net.preferIPv4Stack=true -Djboss.modules.system.pkgs=org.jboss.byteman -Djava.awt.headless=true'

WORKDIR /

EXPOSE 8080 9990
COPY docker-entrypoint.sh /
ENTRYPOINT ["/docker-entrypoint.sh"]
