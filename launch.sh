#!/bin/bash

# Parameters for fork/fg
set -m

# Global variables
TERMINAL="gnome-terminal" # xterm
JAR_PATH="jar"
PWD=`pwd`
CLASS_PATH="$PWD/jar/annuaire.jar:lille1/car3/durieux_gouzer/rmi"
REGISTRY_PATH="/usr/lib/jvm/java-1.7.0-openjdk-1.7.0.60-2.4.5.1.fc20.x86_64/jre/bin/rmiregistry 58432"

# start the rmi registry
export CLASSPATH=$CLASS_PATH
$REGISTRY_PATH &
rmi_registry_pid=$!
sleep 1

# start the message jar in another terminal
$TERMINAL -e "java -jar $JAR_PATH/sendMessage.jar" . &
sleep 0.3

# Spawn a fixed number of nodes
for i in {1..6}
do
    java -jar $JAR_PATH/noeud.jar $i &
done
sleep 0.3

# Launch registy and store its pid
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

# kill everything related to java
kill `ps -ef | grep "java" | awk '{print $2}'` > /dev/null 2&>1
