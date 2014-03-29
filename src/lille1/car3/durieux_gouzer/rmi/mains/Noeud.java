package lille1.car3.durieux_gouzer.rmi.mains;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

import lille1.car3.durieux_gouzer.config.RMIConfiguration;
import lille1.car3.durieux_gouzer.rmi.Message;
import lille1.car3.durieux_gouzer.rmi.MessageImpl;
import lille1.car3.durieux_gouzer.rmi.Site;
import lille1.car3.durieux_gouzer.rmi.SiteImpl;

public class Noeud {
	public static void main(String[] args) {

		String host;
		int port;
		String siteName;
		Registry registry;
		Site site;

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

		try {
			siteName = args[0];
		} catch (Exception e) {
			try {
				siteName = "Site" + registry.list().length;
			} catch (Exception e1) {
				siteName = "Site0";
			}
		}
		
		try {
			site = new SiteImpl(siteName);

			registry.rebind(site.getName(), site);
			System.out.println("Site " + site.getName() + " créé.");
		} catch (RemoteException e) {
			throw new RuntimeException("Impossible de créer un nouveau site", e);
		}
		String line = "";
		Scanner s = new Scanner(System.in);
		while ((line = s.nextLine()) != null) {
			if (line.equals("help")) {
				System.out
						.println("Les commandes disponibles sont: help, quit et list");
			} else if (line.startsWith("kill")) {
				killSite(registry, site);
				s.close();
				System.exit(0);
			} else if (line.startsWith("send")) {
				sendMessage(site, line.replaceAll("send ", ""));
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

	private static void killSite(Registry registry, Site site) {
		try {
			registry.unbind(site.getName());
		} catch (Exception e) {
			throw new RuntimeException("Impossible de tuer le site");
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
