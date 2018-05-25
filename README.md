
# Chouette Core IEV &nbsp;


**Note: Chouette Core IEV  is the Java side application of Chouette Core  ([here](https://github.com/af83/chouette-core)) .  
This application has been engineered to manage the dataset import, export and validation processes inside the Chouette workflow.**

---



## Installation
###   Prerequisites
* **[Chouette Core](https://github.com/af83/chouette-core)** application has been installed

* You configured your postgres database chouette2,  postgres user chouette2 (pw: 'chouette2').

####   EAR packaging
Once the sources have been dowloaded, move to your installation folder and generate the ear file :

	mvn install -DskipTests 
Your application generated **.ear** file is located under *./boiv_iev/target/* folder.

Alternately, you can find the already packaged **ear** corresponding to the latest stable version of **Chouette IEV** **[here](https://github.com/af83/chouette-core)**.

####   Chouette IEV config file installation

Create the **/etc/chouette/boiv/** folder :

	mkdir -p /etc/chouette/boiv

Create the config file boiv.properties and initialize the required parameters : 

cat > /etc/chouette/boiv/boiv.properties <<EOF
	# properties for iev server

	# base directory for referential storage
	boiv.directory=/tmp

	# base url for GUI file exchange
	boiv.gui.url.base=http://localhost:3000
	boiv.gui.url.token=VFHir2GWWjuRNZnHHnQD5Hn+ubRMQ1kNLnu7oCLf+4KR8+PmYqb1EzKZmmuRfVP/yxS0aQ3NklfNbbgUatTtly5540oo4L6ePdbYkwDzrBXF9xgYekOlTCwIGSl430mluv3wcXNEbrRLu2CevIBULtiRZriAEYVOpp9G+lQI+t8=

	# maximun jobs working at same time 
	boiv.started.jobs.max=5

	# maximun copy by import (save passing times for a line)
	boiv.copy.by.import.max=2

	# admin key
	boiv.admin.key=

	# validation 
	## add waiting time in milliseconds on progression steps (comment for production mode)
	#boiv.progression.slow=30000
	EOF
####   Wildfly installation
	wget -q -O - http://download.jboss.org/wildfly/9.0.2.Final/wildfly-9.0.2.Final.tar.gz | sudo tar -xzf - -C /opt

Create a link  :

	sudo ln -s /opt/wildfly-9.0.2.Final /opt/wildfly

Grant ownership over that folder to the local user :

	sudo chown -R $USER /opt/wildfly-9.0.2.Final

####   Wildfly configuration
**Note:** You will have to download the drivers provided in the  **[**Chouette STIF IEV Install**](https://github.com/af83/stif-iev-install)** repository.

Move to the **Chouette STIF IEV Install** folder.  

Postgres driver installation :
	
	mkdir -p /opt/wildfly/modules/org/postgres/main
	cp postgres/* /opt/wildfly/modules/org/postgres/main

Hibernate driver installation :


	rm /opt/wildfly/modules/system/layers/base/org/hibernate/main/hibernate*4.3.10.Final.jar
	cp hibernate/* /opt/wildfly/modules/system/layers/base/org/hibernate/main

Start Wildfly :
	```/opt/wildfly/bin/standalone.sh -c standalone-full.xml```

Start JBoss CLI : 
```
/opt/wildfly/bin/jboss-cli.sh --connect
```
Execute these commands :
**Note :** those parameters display Chouette default configuration, considering the database name, user and password are set to **'chouette2'**. 
```
/subsystem=datasources/jdbc-driver=postgresql:add(driver-name="postgresql",driver-module-name="org.postgres",driver-xa-datasource-class-name=org.postgresql.xa.PGXADataSource)
data-source add --jndi-name=java:jboss/datasources/chouette --name=chouette --connection-url=jdbc:postgresql_postGIS://localhost:5432/chouette2 --driver-class=org.postgis.DriverWrapper --driver-name=postgresql --user-name=chouette --password=chouette --max-pool-size=30
/subsystem=ee/managed-executor-service=default/ :write-attribute(name=max-threads,value=20)
/subsystem=ee/managed-executor-service=default/ :write-attribute(name=queue-length,value=20)
/subsystem=datasources/data-source=chouette/connection-properties=stringtype:add(value=unspecified)
exit
```


####  Deployment
Copy your ear file in Wildfly tmp folder.

**Note :** if you packaged your **ear** file with maven, change the file name parameter in the following commands accordingly. 
	
	mkdir /opt/wildfly/tmp/
	cp chouette_iev.ear /opt/wildfly/tmp/

Deploy the ear :
	
	/opt/wildfly/bin/jboss-cli.sh --connect --command="deploy /opt/wildfly/tmp/chouette_iev.ear"

Test if the application is correctly running : 
```
curl "http://localhost:8080/boiv_iev/referentials/importer/new?id=1"
{"error_code":"UNKNOWN_JOB"}
```
## Configuration
###   Database swap
To change the database used by the application, start JBoss CLI :

	/opt/wildfly/bin/jboss-cli.sh --connect
Execute those commands to undeploy, remove the old datasource, add the new one and redeploy the ear : 

undeploy chouette_iev.ear

	data-source remove --name=chouette

	data-source add --jndi-name=java:jboss/datasources/chouette --name=chouette --connection-url=jdbc:postgresql_postGIS://{DATABASE_URL} --driver-class=org.postgis.DriverWrapper --driver-name=postgresql --user-name={DATABASE_USER} --password={PASSWORD} --max-pool-size=30

	deploy chouette_iev.ear

	exit


## Chouette - IEV workers

Here is the Chouette Core part responsible of import, export and validation workers.

Visit [chouette-core main repository](https://github.com/af83/chouette-core) for more information.

## License

BOIV is licensed under the CeCILL-B license, a copy of which can be found in the [LICENSE](./LICENSE.md) file.
