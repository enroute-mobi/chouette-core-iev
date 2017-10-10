#!/bin/bash

WORK_DIR=`dirname $(readlink -f $0)`


database=boiv_test
user=chouette
host=localhost
port=5432

function usage(){
    echo "Usage `basename $0`  [-p port] [-d database] [-u user]  [-h host] [-f schema_filter] "
    echo "  example: `basename $0`  -p 5433 -d chouette -u chouette -h localhost"
}


while getopts p:d:u:t:o:h:?f: option
do
 case $option in
  p)
   port=$OPTARG
   ;;
  d)
   database=$OPTARG
   ;;
  u)
   user=$OPTARG
   ;; 
  h)
   host=$OPTARG
   ;; 
  f)
   schema_filter=$OPTARG
   ;;
  ?)
  usage
   ;; 
 esac
done

function init (){
	echo "SET search_path = public, pg_catalog;"

	echo "update compliance_check_blocks set compliance_check_set_id=NULL  where id>=100;"
	echo "update compliance_checks set compliance_check_block_id=NULL where id>=100;"
	echo "delete from compliance_check_blocks where id>=100;"
	echo "delete from compliance_checks where id>=100;"
	echo "delete from compliance_check_sets where id>=100;"
	#

	echo "update compliance_control_blocks set compliance_control_set_id=NULL  where id>=100;"
	echo "update compliance_controls set compliance_control_block_id=NULL  where id>=100;"
	echo "delete from compliance_control_blocks where id>=100;"
	echo "delete from compliance_controls where id>=100;"
	echo "delete from compliance_control_sets where id>=100;"
}


psql="psql -U ${user} -d ${database} -h ${host} -p ${port}"


rm -f compliance*tmp.sql

tmpsql=$(mktemp complianceXXXXXtmp.sql)
echo "Delete previous data...(${tmpsql})"
init | tee ${tmpsql} | ${psql}



tmpsql=$(mktemp complianceXXXXXtmp.sql)
echo "Insert blocks....(${tmpsql})"
cat ${WORK_DIR}/block.csv | awk -f ${WORK_DIR}/create-compliance-check-block.awk | tee ${tmpsql} | ${psql}

tmpsql=$(mktemp complianceXXXXXtmp.sql)
echo "Insert controls....(${tmpsql})"
cat ${WORK_DIR}/jdc.csv | awk -f ${WORK_DIR}/create-compliance-check.awk | tee ${tmpsql} | ${psql}



