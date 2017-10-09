#!/bin/bash

dir=$1
if [ "$dir" == "" ]; then
	dir="."
fi

for i in ${dir}/*.sql; do 
	sed -i "s/^SET row_security/-- SET row_security/g" $i;
done
