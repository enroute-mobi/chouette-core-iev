#!/bin/bash

WORK_DIR=`dirname $(readlink -f $0)`
VERBOSE=""

database=chouette2
user=chouette
host=localhost
port=5432
datatype="--column-inserts"
schema_name=""



function usage(){
    echo "Usage `basename $0`  [-p port] [-d database] [-u user] [-t tables separated with space] [-o outputfile]  [-n schema-name]  [-r new-name] [-h host] -s [schema | data]"
    echo "  example: `basename $0`  -p 5433 -d chouette -u chouette -t 'compliances* referentiel' -o toto.sql -h localhost -s "
}

while getopts p:d:u:t:o:h:s:n:r: option
do
 case $option in
  n)
   schema_name="$OPTARG"
   opt_schema="--schema=$schema_name"
   ;;
  p)
   port=$OPTARG
   ;;
  d)
   database=$OPTARG
   ;;
  u)
   user=$OPTARG
   ;; 
  t)
   tables=$OPTARG
   ;;
  o)
   output=$OPTARG
   ;;
  h)
   host=$OPTARG
   ;; 
  r)
   new_name=$OPTARG
   ;; 
  s)
   if [ "$OPTARG" == "data" ]; then
	datatype="--data-only --column-inserts"
   elif [ "$OPTARG" == "schema" ]; then
	datatype="--schema-only --column-inserts"
   fi
   ;; 
 esac
done

if [ "${output}" == "" ] || [ "${new_name}" == "" ]; then
	usage
	
else
	opt_tables="";
	for i in ${tables}; do
		opt_tables="${opt_tables} --table=${i}"
	done
	CMD="pg_dump ${datatype} ${opt_schema} --format=plain --file=${output} ${opt_tables} --username=${user} --host=${host} --port=${port} ${database}"
	$CMD
	sed -i "s/${schema_name}/${new_name}/g" ${output}
	sed -i "s/^SET row_security/-- SET row_security/g" ${output};


	sed -i "1s/^/DROP SCHEMA IF EXISTS ${new_name} CASCADE;\n/" ${output}	


	echo -e "
-- Ajout de l'offre dans les tables REFERENTIALS & REFERENTIAL_METADATA

SET search_path = public, pg_catalog;
DELETE FROM referential_metadata WHERE referential_id IN (SELECT id FROM referentials as r WHERE r.name='${new_name}');
DELETE FROM referentials AS ref WHERE ref.name='${new_name}'; 
INSERT INTO referentials (name, slug, created_at, updated_at, time_zone, user_name, organisation_id, workbench_id, line_referential_id, stop_area_referential_id) VALUES ('${new_name}', '${new_name}', '2017-10-06', '2017-10-06', 'Paris' , '!AutoTest!', 1, 1,1,1); \n \

INSERT INTO referential_metadata (referential_id, line_ids, created_at, updated_at, periodes) VALUES ((SELECT id FROM referentials where name='${new_name}'), '{1,2,3,4,5,6,7,8}', '2017-10-06', '2017-10-06', '{\"[2017-12-03,2017-12-08)\",\"[2017-12-10,2017-12-15)\"}'); \n \
	" >> ${output}
fi

	










