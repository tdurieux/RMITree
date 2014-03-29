package lille1.car3.durieux_gouzer.rmi.mains;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import lille1.car3.durieux_gouzer.config.RMIConfiguration;
import lille1.car3.durieux_gouzer.rmi.Message;
import lille1.car3.durieux_gouzer.rmi.MessageImpl;
import lille1.car3.durieux_gouzer.rmi.Site;

public class SiteSendMessage {
	public static void main(String[] args) {

		String host;
		int port;
		Registry registry;

		try {
			host = args[1];
		} catch (Exception e) {
			host = RMIConfiguration.INSTANCE.getProperty("registryHost");
		}

		try {
			port = Integer.parseInt(args[2]);
		} catch (Exception e) {
			port = RMIConfiguration.INSTANCE.getIntProperty("registryPort");
		}

		try {
			registry = LocateRegistry.getRegistry(host, port);
		} catch (RemoteException e1) {
			throw new RuntimeException("Impossible de trouver le registry", e1);
		}

		String line = "";
		Scanner s = new Scanner(System.in);
		while ((line = s.nextLine()) != null) {
			if (line.equals("help")) {
				System.out
						.println("Les commandes disponibles sont: help, send ");
			} else if (line.startsWith("send")) {
				String[] splittedLine = line.split(" ");
				String siteName = splittedLine[1];
				try {
					Site site = (Site) registry.lookup(siteName);
					sendMessage(site,
							line.replaceAll("send " + siteName + " ", ""));
				} catch (Exception e) {
					System.out.println("Site " + siteName + " non trouvé");
				}
			} else if (line.startsWith("list")) {
				try {
					if (registry.list().length == 0) {
						System.out.println("Annuaire vide");
					}
					for (String string : registry.list()) {
						System.out.println(string);
					}
				} catch (Exception e) {
					throw new RuntimeException(
							"Impossible d'accéder à la liste", e);
				}
			} else {
				System.out.println("Commande: " + line + " non trouvée.");
			}
		}

	}

	private static void sendMessage(Site site, String message) {
		Message m = new MessageImpl(message, site);
		try {
			site.transferMessage(m);
		} catch (Exception e) {
			throw new RuntimeException("Impossible d'envoyer un message");
		}
	}
}
