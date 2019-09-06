#!/bin/bash

for FILE in *.zip; do
  echo $FILE
  unzip -j $FILE -d tempdir
  mv $FILE "$FILE.done"
  cd tempdir
  for XML in *.xml; do
    sed -i -E 's/"NETEX_OFFRE_LIGNE"/"FR1:TypeOfFrame:NETEX_OFFRE_LIGNE:"/' $XML
    sed -i -E 's/"NETEX_STRUCTURE"/"FR1:TypeOfFrame:NETEX_STRUCTURE:"/' $XML
    sed -i -E 's/"NETEX_CALENDRIER"/"FR1:TypeOfFrame:NETEX_CALENDRIER:"/' $XML
    sed -i -E 's/"NETEX_COMMUN"/"FR1:TypeOfFrame:NETEX_COMMUN:"/' $XML
  done
  zip ../$FILE *
  cd ..
  rm -rf tempdir
done
