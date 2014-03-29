package lille1.car3.durieux_gouzer.rmi.mains;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import lille1.car3.durieux_gouzer.config.RMIConfiguration;
import lille1.car3.durieux_gouzer.rmi.Message;
import lille1.car3.durieux_gouzer.rmi.MessageImpl;
import lille1.car3.durieux_gouzer.rmi.Site;

/**
 * est une classe exécutable qui permet de lancer des messages sur les
 * différents sites
 * 
 * @author Thomas Durieux
 * 
 */
public class SiteSendMessage {
	public static void main(final String[] args) {

		String host;
		int port;
		Registry registry;

		try {
			host = args[1];
		} catch (final Exception e) {
			host = RMIConfiguration.INSTANCE.getProperty("registryHost");
		}

		try {
			port = Integer.parseInt(args[2]);
		} catch (final Exception e) {
			port = RMIConfiguration.INSTANCE.getIntProperty("registryPort");
		}

		try {
			registry = LocateRegistry.getRegistry(host, port);
		} catch (final RemoteException e1) {
			throw new RuntimeException("Impossible de trouver le registry", e1);
		}

		String line = "";
		final Scanner s = new Scanner(System.in);
		while ((line = s.nextLine()) != null) {
			if (line.equals("help")) {
				System.out
				.println("Les commandes disponibles sont: help, send ");
			} else if (line.startsWith("send")) {
				final String[] splittedLine = line.split(" ");
				final String siteName = splittedLine[1];
				try {
					final Site site = (Site) registry.lookup(siteName);
					sendMessage(site,
							line.replaceAll("send " + siteName + " ", ""));
				} catch (final Exception e) {
					System.out.println("Site " + siteName + " non trouvé");
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
				System.out.println("Commande: " + line + " non trouvée.");
			}
		}

	}

	private static void sendMessage(final Site site, final String message) {
		final Message m = new MessageImpl(message, site);
		try {
			site.transferMessage(m);
		} catch (final Exception e) {
			throw new RuntimeException("Impossible d'envoyer un message");
		}
	}
}
