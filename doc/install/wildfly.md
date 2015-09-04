# Install and configure Wildfly


Download
--------
download Wildfly 8.2.0.Final from [http://wildfly.org/downloads/](http://wildfly.org/downloads/)

uncompress wildfly in installation directory (/opt for exemple)

download Postgresql jdbc driver postgresql-9.3-1103.jdbc41.jar from [https://jdbc.postgresql.org/download.html](https://jdbc.postgresql.org/download.html)

Setup
-----

in installation directory (/opt/wildfly-8.2.0.Final)
start server : 
on default ports (8080 and 9990 for administration)
```sh
bin/standalone.sh -c standalone-full.xml
```
if port 8080 is used (8180 and 10090 for adminstration)
```sh
bin/standalone.sh -c standalone-full.xml -Djboss.socket.binding.port-offset=100
```
add a managment user for web administration console
```sh
bin/add-user.sh
type: management user (a)
login : admin
password : admin
```

create database access :
```sh
bin/jboss-cli.sh
connect

module add --name=org.postgres --resources=(path to driver)/postgresql-9.3-1103-jdbc41.jar --dependencies=javax.api,javax.transaction.api

/subsystem=datasources/jdbc-driver=postgres:add(driver-name="postgres",driver-module-name="org.postgres",driver-class-name=org.postgresql.Driver)

data-source add --jndi-name=java:jboss/datasources/chouette --name=chouette --connection-url=jdbc:postgresql://localhost:5432/chouette2 --driver-name=postgres --user-name=chouette --password=chouette --max-pool-size=30

data-source add --jndi-name=java:jboss/datasources/iev --name=iev --connection-url=jdbc:postgresql://localhost:5432/iev --driver-name=postgres --user-name=chouette --password=chouette

/subsystem=ee/managed-executor-service=default/ :write-attribute(name=max-threads,value=15)
/subsystem=ee/managed-executor-service=default/ :write-attribute(name=queue-length,value=15)

exit
```
Notes : 
(path to driver) must be replaced by absolute paths without parenthesis
max-threads and queue-length should be sized according to iev.started.jobs.max and iev.copy.by.import.max defined in iev.properties (default are 5)
```sh
max-threads >= 2 * iev.started.jobs.max
max-threads + queue-length >= iev.started.jobs.max * (iev.copy.by.import.max + 1)
```

change uploaded file size: 

```sh
bin/jboss-cli.sh
connect
/subsystem=undertow/server=default-server/http-listener=default/ :write-attribute(name=max-post-size, value=80000000)
exit
```
where 80000000 is the file size in bytes (defauls is 10 Mb)

change JVM heap size :
for huge data, JVM heap size should be increased to 1024kb (defaults is 512kb)

* stop wildfly
* edit bin/standalone.configure
* in JAVA_OPTS : change Xmx value
* save and restart wildfly

Install as service
------------------

On github sukharevd gives a shell to download and install as a linux service :

[wildfly-install.sh](https://gist.github.com/sukharevd/6087988)

after using it, just process steps from "add a managment user for web administration console"