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
REGISTRY_PATH="/usr/lib/jvm/java-1.7.0-openjdk-1.7.0.60-2.4.5.1.fc20.x86_64/jre/bin/rmiregistry 58432"
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

function startMessage {
    echo -e "$GRN Starting message app in another terminal... $WHT"
    $TERMINAL -e "java -jar $JAR_PATH/sendMessage.jar" . &
    sleep 0.3
}

function startNode {
    echo -e "$GRN Creating sites (nodes)... $WHT"
    for i in {1..6}
    do
        java -jar $JAR_PATH/noeud.jar $i &
    done
    sleep 0.3
}

function startRegistry {
    echo -e "$GRN Starting registry and create connections between sites... $WHT"
    echo -e "$RED Type quit to leave the program... $WHT"
    java -jar $JAR_PATH/annuaire.jar false
    <<EOF
    connect 1->2
    connect 1->5
    connect 5->6
    connect 2->3
    connect 2->4
EOF
    sleep 0.3
}


# Make sure the program is not already running
killJava

# start the rmi registry
startRmi

# start the message jar in another terminal
startMessage

# Spawn a fixed number of nodes
startNode

# Launch registy and connects the nodes
startRegistry

# kill everything related to java when leaving
killJava


# Miscs for resume background task
# pid=$!
# jobs $pid
# fg %1
