#!/bin/bash

## Parameters for fork/fg
set -m

## Global variables
TERMINAL="gnome-terminal" # xterm

## Launch registy and store its pid
java -jar annuaire.jar &
pid=$!
# sleep 0.3

## Spawn a fixed number of nodes
for i in {1..6}
do
    java -jar noeud.jar $1 &
done

## Open a new terminal & launch the sendMessage program in it
## this line will allow sendMessage & registry on stdout without modifying our code
$TERMINAL -e "java -jar sendMessage.jar" . &

## Retrieve jobs from the pid of the registry and move it forward on the terminal
jobs $pid
fg %1


#  <<EOF
# connect 1->2
# EOF


kill `ps -ef | grep "java -jar *noeud.jar" | awk '{print $2}'`
kill `ps -ef | grep "java -jar *annuaire.jar" | awk '{print $2}'`
kill `ps -ef | grep "java -jar *sendMessage.jar" | awk '{print $2}'`
# killall java
