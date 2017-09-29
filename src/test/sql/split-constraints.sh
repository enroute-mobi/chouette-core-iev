#!/bin/bash

input="$1"

if [ "$input" == "" ] || [ ! -e "${input}" ]; then
	echo "Usage $(basename $0) <input sql file>"
	exit 1
fi
file_with_constraints_only=${input%.sql}_constraints.sql
file_wihout_constraints=${input%.sql}_without-constraints.sql

newfile=${input}.zzz
cat ${input} > ${newfile}

input=${newfile}


sed -i "s/^--.*//g"  ${input}


sed -i '/^ALTER TABLE ONLY/{N;s/\n//;}' ${input}
sed -i '/;/{s/;/;\n/;}' ${input}
sed -i '/^$/d'  ${input}

cat ${input} | grep  "ADD CONSTRAINT.*FOREIGN KEY" > ${file_with_constraints_only}


grep -Fvxf ${file_with_constraints_only} ${input} > ${file_wihout_constraints}

rm ${newfile}
