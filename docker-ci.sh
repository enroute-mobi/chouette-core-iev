#!/bin/sh -e

# Requires these variables to work :

# PGDATABASE
# PGHOST
# PGUSER
# PGPASSWORD

if type psql; then
   if ! psql -l | grep $PGDATABASE; then
       echo "Create build database $PGDATABASE"
       createdb $PGDATABASE
   else
       echo "Build database $PGDATABASE already exists"
   fi
else
    echo "Can't find postgresql client. Skip database creation"
fi

mvn --batch-mode -Dboiv.testdb.hostname=$PGHOST -Dboiv.testdb.name=$PGDATABASE -Dboiv.testdb.username=$PGUSER -Dboiv.testdb.password=$PGPASSWORD -DskipWildfly install
