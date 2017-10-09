#!/bin/bash

WORK_DIR=`dirname $(readlink -f $0)`
VERBOSE=""

database=chouette2
user=chouette
host=localhost
port=5432
schema_filter=""

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

flist=""
outdir="generated"
[ ! -e ${outdir} ] && mkdir ${outdir}
list=$(echo "\\dn" | psql -U ${user} -d ${database} -h ${host} -p ${port}  -A |tail -n "+3"| head -n -1|grep "${schema_filter}" |cut -d"|" -f1)

echo $list

for i in $list; do 
	newname=${i%_*}
	${WORK_DIR}/extract-and-rename-schema.sh -o ${outdir}/${newname}.sql -n ${i} -p ${port} -d ${database} -u ${user} -h ${host} -r ${newname}
done













