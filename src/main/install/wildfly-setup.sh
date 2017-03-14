#!/bin/bash

echo postgres database supposed to be on localhost port 5432 and named chouette2.  
echo If not : stop this script and change connection-url on line 53
echo -n press ENTER to continue : 
input a

# install wildfly as service
sudo ./wildfly-install.sh

# update hibernate and add postgres and hibernate-spatial
sudo service wildfly stop

wget http://central.maven.org/maven2/org/postgresql/postgresql/9.4-1206-jdbc41/postgresql-9.4-1206-jdbc41.jar
wget http://central.maven.org/maven2/net/postgis/postgis-jdbc/2.2.1/postgis-jdbc-2.2.1.jar
wget http://www.hibernatespatial.org/repository/org/hibernate/hibernate-spatial/4.3/hibernate-spatial-4.3.jar
wget http://central.maven.org/maven2/com/vividsolutions/jts/1.13/jts-1.13.jar
wget http://central.maven.org/maven2/org/hibernate/hibernate-core/4.3.11.Final/hibernate-core-4.3.11.Final.jar
wget http://central.maven.org/maven2/org/hibernate/hibernate-envers/4.3.11.Final/hibernate-envers-4.3.11.Final.jar
wget http://central.maven.org/maven2/org/hibernate/javax/persistence/hibernate-jpa-2.1-api/1.0.0.Final/hibernate-jpa-2.1-api-1.0.0.Final.jar
wget http://central.maven.org/maven2/org/hibernate/hibernate-entitymanager/4.3.11.Final/hibernate-entitymanager-4.3.11.Final.jar
wget http://central.maven.org/maven2/org/hibernate/hibernate-infinispan/4.3.11.Final/hibernate-infinispan-4.3.11.Final.jar

WILDFLY_HOME=/opt/wildfly

sudo mkdir -p $WILDFLY_HOME/modules/org/postgres/main
sudo cp post*.jar $WILDFLY_HOME/modules/org/postgres/main
sudo cp module_postgres.xml $WILDFLY_HOME/modules/org/postgres/main/module.xml

sudo rm $WILDFLY_HOME/modules/system/layers/base/org/hibernate/main/hibernate*4.3.10.Final.jar
sudo cp hibernate-core*.jar hibernate-envers*.jar hibernate-entitymanager*.jar hibernate-jpa-2.1-api-1.0.0.Final.jar $WILDFLY_HOME/modules/system/layers/base/org/hibernate/main
sudo cp hibernate-spatial-4.3.jar jts-1.13.jar $WILDFLY_HOME/modules/system/layers/base/org/hibernate/main
sudo cp -f module_hibernate.xml $WILDFLY_HOME/modules/system/layers/base/org/hibernate/main/module.xml

sudo rm $WILDFLY_HOME/modules/system/layers/base/org/hibernate/infinispan/main/hibernate*4.3.10.Final.jar
sudo cp hibernate-infinispan-4.3.11.Final.jar $WILDFLY_HOME/modules/system/layers/base/org/hibernate/infinispan/main/
sudo cp -f module_infinspan.xml $WILDFLY_HOME/modules/system/layers/base/org/hibernate/infinispan/main/module.xml

sudo service wildfly start

# create admin user
cd $WILDFLY_HOME
sudo bin/add-user.sh << EOS
a
admin
admin
EOS

# add postgres driver and chouette database
sudo bin/jboss-cli.sh <<EOS
connect
/subsystem=datasources/jdbc-driver=postgresql:add(driver-name="postgresql",driver-module-name="org.postgres",driver-xa-datasource-class-name=org.postgresql.xa.PGXADataSource)
data-source add --jndi-name=java:jboss/datasources/chouette --name=chouette --connection-url=jdbc:postgresql_postGIS://localhost:5432/chouette2 --driver-class=org.postgis.DriverWrapper --driver-name=postgresql --user-name=chouette --password=chouette --max-pool-size=30
/subsystem=ee/managed-executor-service=default/ :write-attribute(name=max-threads,value=20)
/subsystem=ee/managed-executor-service=default/ :write-attribute(name=queue-length,value=20)
EOS




