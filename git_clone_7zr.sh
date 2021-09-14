#!/bin/bash

cd ..
echo `pwd`
while ( ! git clone https://github.com/jinfeihan57/p7zip.git)
do
  echo "再来一次试试"
done
