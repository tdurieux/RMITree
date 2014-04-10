#!/bin/bash

## Parameters for fork/fg
set -m

## Global variables
TERMINAL="terminator" # xterm
JAR_PATH="jar"
JDK_PATH="/usr/lib/jvm/java-1.7.0-openjdk-1.7.0.60-2.4.5.1.fc20.x86_64/jre"

# start the rmi registry
$JDK_PATH/bin/rmiregistry &
rmi_registry_pid=$!
sleep 0.3

# start the message jar in another terminal
$TERMINAL -e "java -jar $JAR_PATH/sendMessage.jar" . &
sleep 0.3

## Spawn a fixed number of nodes
for i in {1..6}
do
    java -jar $JAR_PATH/noeud.jar $i &
done
sleep 0.3

## Launch registy and store its pid
java -jar $JAR_PATH/annuaire.jar false <<EOF
connect 1->2
connect 1->5
connect 5->6
connect 2->3
connect 2->4
EOF
sleep 0.3


# pid=$!
# jobs $pid
# fg %1

# kill `ps -ef | grep "java -jar *noeud.jar" | awk '{print $2}'`
# kill `ps -ef | grep "java -jar *annuaire.jar" | awk '{print $2}'`
# kill `ps -ef | grep "java -jar *sendMessage.jar" | awk '{print $2}'`
killall java
