#!/bin/bash

for FILE in *.zip; do
  echo $FILE
  unzip -j $FILE -d tempdir
  mv $FILE "$FILE.done"
  cd tempdir
  for XML in *.xml; do
    sed -i -E 's/STIF:CODIFLIGNE:Line:(\w*?)/FR1:Line:\1:/' $XML
    sed -i -E 's/STIF:CODIFLIGNE:Network:(\w*?)/FR1:Network:\1:LOC/' $XML
    sed -i -E 's/STIF:CODIFLIGNE:Notice:(\w*?)/FR1:Notice:\1:/' $XML
    sed -i -E 's/STIF:CODIFLIGNE:Operator:(\w*?)/FR1:Operator:\1:LOC/' $XML
  done
  zip ../$FILE *
  cd ..
  rm -rf tempdir
done
