package lille1.car3.durieux_gouzer.rmi.mains;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lille1.car3.durieux_gouzer.config.RMIConfiguration;
import lille1.car3.durieux_gouzer.rmi.Site;

/**
 * est la classe exécutable permettant de lancer l'annuaire de RMI
 * 
 * @author Thomas Durieux
 * 
 */
public class Annuaire {
	public static void main(final String[] args) {
		int port;
		Registry registry;
		try {
			port = Integer.parseInt(args[2]);
		} catch (final Exception e) {
			// utiliser le port par défaut
			port = RMIConfiguration.INSTANCE.getIntProperty("registryPort");
		}
		try {
			if (args[1].equals("true")) {
				registry = LocateRegistry.createRegistry(port);
			} else {
				registry = LocateRegistry.getRegistry(port);
			}
			System.out.println("L'annuaire a démarré sur le port " + port);
		} catch (final RemoteException e) {
			throw new RuntimeException("Impossible de démarrer l'annuaire", e);
		}

		String line = "";
		final Scanner s = new Scanner(System.in);
		while (s.hasNextLine() && (line = s.nextLine()) != null) {
			if (line.startsWith("help")) {
				System.out
				.println("Les commandes disponibles sont: help, quit et list");
			} else if (line.startsWith("quit")) {
				s.close();
				System.exit(0);
			} else if (line.startsWith("connect")) {
				final String[] splittedLine = line.split(" ");
				for (int i = 1; i < splittedLine.length; i++) {
					final Pattern pattern = Pattern.compile("(.*)(<-|->)(.*)");
					final Matcher matcher = pattern.matcher(splittedLine[i]);
					if (matcher.find()) {
						final String siteName1 = matcher.group(1);
						Site site1;
						Site site2;
						try {
							site1 = (Site) registry.lookup(siteName1);
						} catch (final Exception e) {
							System.out.println("Impossible de touver le site "
									+ siteName1);
							continue;
						}
						final String siteName2 = matcher.group(3);
						try {
							site2 = (Site) registry.lookup(siteName2);
						} catch (final Exception e) {
							System.out.println("Impossible de touver le site "
									+ siteName2);
							continue;
						}
						try {
							if (matcher.group(2).equals("->")) {
								site1.addConnection(site2);
							} else {
								site2.addConnection(site1);
							}
						} catch (final RemoteException e) {
							System.out.println("Impossible de connecter "
									+ siteName1 + " et " + siteName2);
						}
					}
				}
			} else if (line.startsWith("list")) {
				try {
					if (registry.list().length == 0) {
						System.out.println("Annuaire vide");
					}
					for (final String string : registry.list()) {
						System.out.println(string);
					}
				} catch (final Exception e) {
					throw new RuntimeException(
							"Impossible d'accéder à la liste", e);
				}
			} else {
				System.out.println("Commande non trouvée.");
			}

		}
		// empecher le programme de s'arrêter
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.currentThread().sleep(99999999);
				} catch (final InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}).start();
	}
}
