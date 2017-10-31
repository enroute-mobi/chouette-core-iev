#!/bin/bash

dir=$1
if [ "$dir" == "" ]; then
	dir="."
fi

for i in ${dir}/*.sql; do 
	sed -i "s/^SET row_security/-- SET row_security/g" $i;
	sed -i "s/^SET idle_in_transaction_session_timeout/-- SET idle_in_transaction_session_timeout/g" $i;
done
