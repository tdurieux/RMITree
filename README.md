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

Le guide d'utilisation des programmes et des scripts de tests de ces programmes se trouvent à la fin de
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


# Exécuter le projet

1. Lancer annuaire.jar

```bash
    java -jar annuaire.jar [registryHost registryPort]
```

1. Lancer les différents noeuds

```bash
    java -jar noeuds.jar nomNoeud [registryHost registryPort]
```

1. Lancer l'envoyeur de message

```bash
    java -jar sendMessage.jar nomNoeud message [registryHost registryPort]
```

1. Lier les différents noeuds

Aller dans le terminal ayant lancé annuaire.jar.
entrer la commande:

``` bash
connect nomNoeudParent->nomNoeudFils
```
