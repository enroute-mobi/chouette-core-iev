#!/bin/bash

WORK_DIR=`dirname $(readlink -f $0)`
VERBOSE=""

database=chouette2
user=chouette
host=localhost
port=5432


function usage(){
    echo "Usage `basename $0`  [-p port] [-d database] [-u user]  [-h host]"
    echo "  example: `basename $0`  -p 5433 -d chouette -u chouette -h localhost"
}


while getopts p:d:u:t:o:h:? option
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
  ?)
  usage
   ;; 
 esac
done

while read line; do
	echo "$line" | grep "^#" > /dev/null
	if [ "$?" != "0" ] && [ "$line" != "" ];then
		file=${line%% *}
		tables=${line#* }
		./extract-sql-from-db.sh -t "${tables}" -o ${file} -p ${port} -d ${database} -u ${user} -h ${host}
	fi 
done < tables-file.txt

	










