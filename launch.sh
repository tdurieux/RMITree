#!/bin/bash

function startNoeud {
	java -jar noeud.jar $1 &
	sleep 0.3
}

java -jar annuaire.jar &
pid=$!
sleep 0.3

for i in {1..6}
do
	startNoeud $i
done

echo "connect 1->2" &> 0
sleep 0.3

#java -jar sendMessage.jar
<<EOF
	
EOF
kill `ps -ef | grep "java -jar *noeud.jar" | awk '{print $2}'`
kill `ps -ef | grep "java -jar *annuaire.jar" | awk '{print $2}'`


