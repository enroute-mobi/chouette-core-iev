#!/bin/bash

for i in *_*.sql; do 
	sed -i "s/^SET row_security/-- SET row_security/g" $i;
done
