#!/bin/bash

## Colors
RED="\033[0;31m"
GRN="\033[0;32m"
BLU="\033[0;34m"
WHT="\033[0;37m"

## Backgrounds
B_RED=""
B_GRN=""
B_BLU=""
B_WHT=""

# Parameters for fork/fg
set -m

# Global variables
TERMINAL="gnome-terminal" # xterm
JAR_PATH="jar"
CLASS_PATH="`pwd`/jar/annuaire.jar:lille1/car3/durieux_gouzer/rmi"
REGISTRY_PATH="/usr/lib/jvm/java-1.7.0/jre/bin/rmiregistry 58432"

# if mac is passed on argument, we change the registry_path
if [ $# -eq 1 ] && [ $1 ==  "mac" ]; then
    REGISTRY_PATH="/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/bin/rmiregistry 58432"
fi

export CLASSPATH=$CLASS_PATH

function killJava {
    # maybe check if it is already running before killing ?
    echo -e "$GRN Killing Java... $WHT"
    kill `ps -ef | grep "annuaire.jar" | awk '{print $2}'` > /dev/null 2&>1
    kill `ps -ef | grep "noeud.jar" | awk '{print $2}'` > /dev/null 2&>1
    kill `ps -ef | grep "sendMessage.jar" | awk '{print $2}'` > /dev/null 2&>1
    kill `ps -ef | grep "rmiregistry" | awk '{print $2}'` > /dev/null 2&>1
}

function startRmi {
    echo -e "$GRN Starting rmi-registry... $WHT"
    $REGISTRY_PATH &
    rmi_registry_pid=$!
    sleep 1
}

function startNode {
    echo -e "$GRN Creating sites (nodes)... $WHT"
    for i in {1..20}
    do
        java -jar $JAR_PATH/noeud.jar $i &
        sleep 0.2
    done
}

function createConnections {
    echo -e "$GRN Starting registry and create connections between sites... $WHT"
    java -jar $JAR_PATH/connectSite.jar "1->2"
    java -jar $JAR_PATH/connectSite.jar "2->3"
    java -jar $JAR_PATH/connectSite.jar "3->4"
    java -jar $JAR_PATH/connectSite.jar "4->5"
    java -jar $JAR_PATH/connectSite.jar "5->6"
    java -jar $JAR_PATH/connectSite.jar "6->7"
    java -jar $JAR_PATH/connectSite.jar "7->8"
    java -jar $JAR_PATH/connectSite.jar "8->9"
    java -jar $JAR_PATH/connectSite.jar "9->10"
    java -jar $JAR_PATH/connectSite.jar "1->11"
    java -jar $JAR_PATH/connectSite.jar "11->12"
    java -jar $JAR_PATH/connectSite.jar "12->13"
    java -jar $JAR_PATH/connectSite.jar "13->14"
    java -jar $JAR_PATH/connectSite.jar "14->15"
    java -jar $JAR_PATH/connectSite.jar "15->16"
    java -jar $JAR_PATH/connectSite.jar "16->17"
    java -jar $JAR_PATH/connectSite.jar "17->18"
    java -jar $JAR_PATH/connectSite.jar "18->19"
    java -jar $JAR_PATH/connectSite.jar "19->20"
    sleep 0.3
}


# Make sure the program is not already running
killJava

# # mac path
# REGISTRY_PATH="/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/bin/rmiregistry 58432"

# start the rmi registry
startRmi

# Spawn a fixed number of nodes
startNode

# Connects the nodes
createConnections

# Send a message to root
echo -e "$GRN Send message from root... $WHT"
java -jar $JAR_PATH/sendMessage.jar 1 testMessage1 &
echo -e "$GRN Send message from root... $WHT"
java -jar $JAR_PATH/sendMessage.jar 1 testMessage2 &
echo -e "$GRN Send new message with the program jar/sendMessage.jar nodeName message $WHT"

echo -e "$RED Type quit to leave the program... $WHT"
java -jar $JAR_PATH/annuaire.jar

# kill everything related to java when leaving
killJava
