#!/bin/bash

## Colors
RED="\033[0;31m"
GRN="\033[0;32m"
BLU="\033[0;34m"
WHT="\033[0;37m"

# Parameters for fork/fg
set -m

# Global variables
JAR_PATH="jar"
CLASS_PATH="`pwd`/jar/annuaire.jar:lille1/car3/durieux_gouzer/rmi"
REGISTRY_PATH="/usr/lib/jvm/java-1.7.0/jre/bin/rmiregistry 58432"

# if mac is passed on argument, we change the registry_path
if [ $# -eq 1 ] && [ $1 ==  "mac" ]; then
    REGISTRY_PATH="/Library/Java/JavaVirtualMachines/jdk1.7.0_45.jdk/Contents/Home/jre/bin/rmiregistry 58432"
fi

function killJava {
    # maybe check if it is already running before killing ?
    echo -e "$GRN Killing Java... $WHT"
    kill `ps -ef | grep "annuaire.jar" | awk '{print $2}'` > /dev/null 2&>1
    kill `ps -ef | grep "noeud.jar" | awk '{print $2}'` > /dev/null 2&>1
    kill `ps -ef | grep "sendMessage.jar" | awk '{print $2}'` > /dev/null 2&>1
    kill `ps -ef | grep "rmiregistry" | awk '{print $2}'` > /dev/null 2&>1
}
killJava

# start the rmi registry
export CLASSPATH=$CLASS_PATH
$REGISTRY_PATH &
rmi_registry_pid=$!
sleep 0.5

rm -f out.txt

# Spawn a fixed number of nodes
for i in {1..6}
do
    java -jar $JAR_PATH/noeud.jar $i >> out.txt 2> /dev/null &
done
sleep 0.6

# Launch registy and store its pid
java -jar $JAR_PATH/connectSite.jar "1->2"
java -jar $JAR_PATH/connectSite.jar "1->5"
java -jar $JAR_PATH/connectSite.jar "5->6"
java -jar $JAR_PATH/connectSite.jar "2->3"
java -jar $JAR_PATH/connectSite.jar "2->4"
sleep 0.5

# sends test message
java -jar $JAR_PATH/sendMessage.jar 1 testMessage
sleep 0.3

echo
echo -e $GRN"Test connections:"$WHT
echo -e "==========================="
grep "\[1\] connected with 2" out.txt | wc -l | awk \
    'BEGIN { printf "[1] is connected with [2] ? ";} $1 == "1" { print "Yes" } $1 != "1" { print "No " $1 }'
grep "\[1\] connected with 5" out.txt | wc -l | awk \
    'BEGIN { printf "[1] is connected with [5] ? ";} $1 == "1" { print "Yes" } $1 != "1" { print "No " $1 }'
grep "\[5\] connected with 6" out.txt | wc -l | awk \
    'BEGIN { printf "[5] is connected with [6] ? ";} $1 == "1" { print "Yes" } $1 != "1" { print "No " $1 }'
grep "\[2\] connected with 3" out.txt | wc -l | awk \
    'BEGIN { printf "[2] is connected with [3] ? ";} $1 == "1" { print "Yes" } $1 != "1" { print "No " $1 }'
grep "\[2\] connected with 4" out.txt | wc -l | awk \
    'BEGIN { printf "[2] is connected with [4] ? ";} $1 == "1" { print "Yes" } $1 != "1" { print "No " $1 }'
echo

echo -e $GRN"Test message communication:"
echo -e "==========================="$WHT
grep "\[2\] \"testMessage\" receives message from 1" out.txt | wc -l | awk \
    'BEGIN { printf "[2] did receive message \"testMessage\" from [1] ? ";} $1 == "1" { print "Yes" } $1 != "1" { print "No " $1 }'
grep "\[3\] \"testMessage\" receives message from 1" out.txt | wc -l | awk \
    'BEGIN { printf "[3] did receive message \"testMessage\" from [1] ? ";} $1 == "1" { print "Yes" } $1 != "1" { print "No " $1 }'
grep "\[4\] \"testMessage\" receives message from 1" out.txt | wc -l | awk \
    'BEGIN { printf "[4] did receive message \"testMessage\" from [1] ? ";} $1 == "1" { print "Yes" } $1 != "1" { print "No " $1 }'
grep "\[5\] \"testMessage\" receives message from 1" out.txt | wc -l | awk \
    'BEGIN { printf "[5] did receive message \"testMessage\" from [1] ? ";} $1 == "1" { print "Yes" } $1 != "1" { print "No " $1 }'
grep "\[6\] \"testMessage\" receives message from 1" out.txt | wc -l | awk \
    'BEGIN { printf "[6] did receive message \"testMessage\" from [1] ? ";} $1 == "1" { print "Yes" } $1 != "1" { print "No " $1 }'
echo

echo -e $GRN"Cleaning up and leaving..."
echo -e "==========================="$WHT
kill `ps -ef | grep "java" | awk '{print $2}'` > /dev/null 2>/dev/null # kill everything related to java
kill $rmi_registry_pid # kill registry
rm -f out.txt
killJava
echo
echo
