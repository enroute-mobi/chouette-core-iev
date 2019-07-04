#!/bin/bash

for FILE in *.zip; do
  echo $FILE
  unzip -j $FILE -d tempdir
  mv $FILE "$FILE.done"
  cd tempdir
  for XML in *.xml; do
    sed -i -re 's/FR:[0-9]+:ZDE:(\w*?):STIF/FR::Quay:\1:FR1/g' $XML
  done
  zip ../$FILE *
  cd ..
  rm -rf tempdir
done
