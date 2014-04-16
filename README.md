TP 3 RMI
========

# Auteurs

Thomas Durieux & William Gouzer

29 mars 2014


# Introduction

Ce projet est implémenté en Java et utilise son API Remote Method Invocation (Java RMI) pour
communiquer entre plusieurs sites de manière distribuée et
asynchrone. Pour permettre le lancement sur plusieurs machines virtuelles,
les fonctionnalités du programme ont été divisés en trois parties :
  * noeud représente les différents sites à créer
  * connectSite permet de connecter les sites entre eux
  * sendMessage pour envoyer un message à destination d'un site

Le guide d'utilisation et des scripts de tests de ces programmes se trouvent à la fin de
ce README


# Exceptions

Les exceptions prises en compte par ce programme sont les suivantes :

*  **Exception**, lancée par un argument manquant ou de mauvais type pour les différents
   programmes (un Main par programme) :

```java
try {
	host = args[1];
} catch (final Exception e) {
	host = RMIConfiguration.INSTANCE.getProperty("registryHost");
}
```

* **RemoteException**, lancée lorsqu'une tentative d'accès au registry
  échoue :

```java
try {
	registry = LocateRegistry.getRegistry(host, port);
} catch (final RemoteException e1) {
	throw new RuntimeException("Impossible de trouver le registry", e1);
}
```

* **InterruptedException**, lancée lorsque l'on termine le thread avant sa fin
 d'exécution "naturelle". Le thread est ici utilisé pour éviter une
 boucle while(true) bloquante :
```java
@Override
public void run() {
	try {
		Thread.currentThread().sleep(99999999);
	} catch (final InterruptedException e) {
		throw new RuntimeException(e);
	}
}
```

* **IOException**, lancée lorsque l'on tente d'accéder à un fichier
(ici de configuration) sur le disque et que la lecture échoue :
```java
try {
	this.properties.load(openStream);
} catch (final IOException e) {
	throw new RuntimeException(
			"Impossible de charger les fichiers de configurations", e);
}

```

* **NotBoundException**, lancée lorsque l'on tente de communiquer avec
le registry mais que la connexion n'est pas ou plus active :
```java
try {
	registry.lookup(siteName);
	System.out.println("[" + siteName + "] already exists");
	return;
} catch (final NotBoundException e) {
	// the site doesn't already exist
} catch (final RemoteException e) {
	throw new RuntimeException("Unable to connect to RMI server", e);
}
```


# Exemples de code

## Connecter deux sites à partir de leur nom
Fichier lille1.car3.durieux_gouzer.rmi.mains.ConnectSite Méthode: main
```Java
// récupère les deux site stockés dans le registres
Site site1 = (Site) registry.lookup(siteName1);
Site site2 = (Site) registry.lookup(siteName2);

// ajoute le site2 comme fils du site1
site1.addConnection(site2);

```

## envoyer un message à partir d'un site
Fichier lille1.car3.durieux_gouzer.rmi.mains.SendMessage Méthode: sendMessage
```Java
// construit un objet message et l'envoie
Message m = new MessageImpl(message, sender);
sender.transferMessage(m);
```

## ajouter un site à l'annuaire
Fichier lille1.car3.durieux_gouzer.rmi.mains.Noeud Méthode: main
```Java
Site site = new SiteImpl(siteName);
// connect le site au registre avec comme clé, le nom du site.
registry.rebind(site.getName(), site);
```

## empêcher l'envoie de message dupliqué
Fichier lille1.car3.durieux_gouzer.rmi.SiteImpl Méthode: transferMessage
```Java
// la section critique permet d'assurer
// qu'aucune interruption aura lieux
// entre la vérification et de l'ajout dans la liste.
synchronized (this.receivedMessages) {
		if (this.receivedMessages.contains(message)) {
			// le message a déjà été transféré
			return;
		}
		this.receivedMessages.add(message);
}
```

## envoyer les messages aux fils
Fichier lille1.car3.durieux_gouzer.rmi.SiteImpl Méthode: transferMessage
```Java
for (final Site connection : SiteImpl.this.connections) {
	// ne pas envoyer le message à l'émetteur
	if (connection.equals(message.getSender())) {
		continue;
	}
	// créer un nouveau thread permettant l'envoie concurrent
	new Thread(new Runnable() {
		final Site c = connection;
		@Override
		public void run() {
			// envoyer le message au fils
			this.c.transferMessage(message);
		}
	}).run();
}
```

# Exécuter le projet

### Lancer annuaire.jar
L'annuaire permet de créer un serveur RMI, si il n'est pas encore lancé sur un port donnée.
L'annuaire permet également de connecter deux sites entre eux avec la syntaxe suivante: ```connect siteName1->siteName2```.

```bash
    java -jar annuaire.jar [registryHost registryPort]
```

### Lancer les différents noeuds
L'exécutable noeud.jar permet d'ajouter un nouveau noeud au registre,

```bash
    java -jar noeud.jar siteName [registryHost registryPort]
```

### Lancer l'envoyeur de message
L'exécutable sendMessage permet d'envoyer un message à partir d'un site donné.
```bash
    java -jar sendMessage.jar siteName message [registryHost registryPort]
```

# Scripts démo

### RMIBasedTreeCreation.sh
Ce script montre la création et la connexion de plusieurs site entre eux.

### MessageTransferFromRoot.sh
Ce script montre l'envoie de message à partir de l'élément root de l'arbre.

### MessageTransferFromNode.sh
Ce script montre l'envoie de message à partir d'un élément quelconque de l'arbre.

### ConcurentMessageTransfer.sh
Ce script montre l'envoie de messages concurrents.

### GraphMessageTransfer.sh
Ce script montre le comportement de l'implémentation dans un environnement de graphe (un site peut posséder plusieurs parents).

# Scripts de tests

### testConnectAndSendMessage.sh
Ce script test la connexion et l'envoie de message simple.

### testConcurentMessageTransfer.sh
Ce script test la bonne réception de message envoyé de façon concurrente.