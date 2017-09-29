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

flist=""
outdir="generated"
[ ! -e ${outdir} ] && mkdir ${outdir}
while read line; do
	echo "$line" | grep "^#" > /dev/null
	if [ "$?" != "0" ] && [ "$line" != "" ];then
		file=${line%% *}
		flist="${flist} ${file}"
		tables=${line#* }
		${WORK_DIR}/extract-sql-from-db.sh -t "${tables}" -o ${outdir}/${file} -p ${port} -d ${database} -u ${user} -h ${host}
	fi 
done < tables-file.txt

	
for i in ${flist}; do
	cat ${outdir}/$i |grep "CREATE TABLE"| sed "s/CREATE TABLE /DROP TABLE IF EXISTS /g" | sed "s/ (/ CASCADE;/g"
done > ${outdir}/00_drop_all_tables.sql


current=$(pwd)
cd $outdir
for sqlfile in ${flist}; do
	${WORK_DIR}/split-constraints.sh "$sqlfile"
done 

cd $current

${WORK_DIR}/remove-row-security.sh ${outdir}









