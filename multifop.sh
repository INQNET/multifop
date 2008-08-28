#!/bin/bash

DIR=`dirname $0`
JCP="${DIR}/multifop/bin:${DIR}/build/fop.jar:${DIR}/lib/avalon-framework-4.2.0.jar:${DIR}/lib/batik-all-1.6.jar:${DIR}/lib/commons-io-1.3.1.jar:${DIR}/lib/commons-logging-1.0.4.jar:${DIR}/lib/serializer-2.7.0.jar:${DIR}/lib/xalan-2.7.0.jar:${DIR}/lib/xercesImpl-2.7.1.jar:${DIR}/lib/xml-apis-1.3.02.jar:${DIR}/lib/xmlgraphics-commons-1.2.jar"
CMD="java -cp $JCP at.inqnet.multifop.Main $@"

#echo $CMD
eval $CMD
