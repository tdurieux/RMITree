TP 3 RMI
========

# Auteurs

Thomas Durieux

29 mars 2014

# Exemples de code

# Commer lancer le projet

1. Lancer annuaire.jar

``` bash
java -jar annuaire.jar [registryHost registryPort] 
```

1. Lancer les différents noeuds

``` bash
java -jar noeuds.jar nomNoeud [registryHost registryPort] 
```

1. Lancer l'envoyeur de message

``` bash
java -jar sendMessage.jar [registryHost registryPort] 
```

1. Lier les diffrénts noeuds

Aller dans le terminal ayant lancé annuaire.jar.
entrer la commande:

``` bash
connect nomNoeudParent->nomNoeudFils
```

1. Envoyer des messages

Aller dans le terminal ayant lancé sendMessage.jar
entrer la commande:

```bash
send nomNoeud message
```

