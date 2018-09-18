#!/bin/bash
cmd=$1

SETUP_FILE=/setup.done

POSTGRES_HOST=${POSTGRES_HOST:-stif-boiv-postgres}
POSTGRES_PORT=${POSTGRES_PORT:-5432}
POSTGRES_NAME=${POSTGRES_NAME:-chouette2}
POSTGRES_USER=${POSTGRES_USER:-chouette}
POSTGRES_PASS=${POSTGRES_PASS:-chouette}


WEBGUI_HOST=${WEBGUI_HOST:-stif-boiv-web}
WEBGUI_PORT=${WEBGUI_PORT:-3000}

WILDFLY_HOST=localhost
WILDFLY_PORT=9990

WILDFLY_HOME=/opt/wildfly

IEV_MAX_PARALLEL_JOBS=${IEV_MAX_PARALLEL_JOBS:-5}
IEV_MAX_COPY_BY_IMPORT=${IEV_MAX_COPY_BY_IMPORT:-2}

function waitPostgres {
  while ! nc -z $POSTGRES_HOST $POSTGRES_PORT;do
    echo 'Waiting for Postgres...'
    sleep 2
  done
}

function waitWildfly {
  while ! nc -z $WILDFLY_HOST $WILDFLY_PORT;do
    echo 'Waiting for Wildfly...'
    sleep 2
  done
}

function waitWebGui {
  status="closed"
  while ! nc -z $WEBGUI_HOST $WEBGUI_PORT;do
    echo 'Waiting for WebGUI (Ruby webapp)...'
    sleep 2
  done
}





function setup() {
	sed -i "s#BOIV_GUI_URL_BASE#$BOIV_GUI_URL_BASE#g" /etc/chouette/boiv/boiv.properties
	sed -i "s#BOIV_GUI_URL_TOKEN#$BOIV_GUI_URL_TOKEN#g" /etc/chouette/boiv/boiv.properties
	sed -i "s#IEV_MAX_PARALLEL_JOBS#$IEV_MAX_PARALLEL_JOBS#g" /etc/chouette/boiv/boiv.properties
	sed -i "s#IEV_MAX_COPY_BY_IMPORT#$IEV_MAX_COPY_BY_IMPORT#g" /etc/chouette/boiv/boiv.properties

	waitPostgres
	waitWebGui
	cd $WILDFLY_HOME
	sh ./bin/standalone.sh &

	waitWildfly

	# create admin user
	$WILDFLY_HOME/bin/add-user.sh  -u admin -p admin -s

	# add postgres driver and chouette database
	sh bin/jboss-cli.sh <<EOS
	connect
    /system-property=jboss.as.management.blocking.timeout:add(value=900)
	/subsystem=datasources/jdbc-driver=postgresql:add(driver-name="postgresql",driver-module-name="org.postgres",driver-xa-datasource-class-name=org.postgresql.xa.PGXADataSource)
	data-source add --jndi-name=java:jboss/datasources/chouette --name=chouette --connection-url=jdbc:postgresql_postGIS://${POSTGRES_HOST}:${POSTGRES_PORT}/${POSTGRES_NAME} --driver-class=org.postgis.DriverWrapper --driver-name=postgresql --user-name=${POSTGRES_USER} --password=${POSTGRES_PASS} --max-pool-size=30
	/subsystem=datasources/data-source=chouette/connection-properties=stringtype:add(value=unspecified)
	/subsystem=ee/managed-executor-service=default/ :write-attribute(name=max-threads,value=20)
	/subsystem=ee/managed-executor-service=default/ :write-attribute(name=queue-length,value=20)
EOS


	pkill -15 java

}


waitPostgres
case $cmd in
bash)
  bash
  ;;
*)
  if [ ! -e $SETUP_FILE ]; then
  	setup && echo $(date) > $SETUP_FILE
  fi
  mv /chouette_iev.ear $WILDFLY_HOME/standalone/deployments/
  if [ -e  /update/chouette_iev.ear ]; then
  	diff $WILDFLY_HOME/standalone/deployments/chouette_iev.ear /update/chouette_iev.ear || cp /update/chouette_iev.ear $WILDFLY_HOME/standalone/deployments/
  else
	if [ ! -e $WILDFLY_HOME/standalone/deployments/chouette_iev.ear ]; then
		cd /tmp
		wget -q $chouette_iev.ear_BUILD_URL
		mv /tmp/chouette_iev.ear $WILDFLY_HOME/standalone/deployments/
	fi

  fi
  exec $WILDFLY_HOME/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0
  ;;
esac
