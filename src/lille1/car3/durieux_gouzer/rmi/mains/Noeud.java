package lille1.car3.durieux_gouzer.rmi.mains;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import lille1.car3.durieux_gouzer.config.RMIConfiguration;
import lille1.car3.durieux_gouzer.rmi.Site;
import lille1.car3.durieux_gouzer.rmi.SiteImpl;

/**
 * est une classe exécutatble permettant de créer un nouveau site
 * 
 * @author Thomas Durieux
 * 
 */
public class Noeud {
	public static void main(final String[] args) {

		String host;
		int port;
		String siteName;
		final Registry registry;
		final Site site;

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

		try {
			siteName = args[0];
		} catch (final Exception e) {
			try {
				siteName = "Site" + registry.list().length;
			} catch (final Exception e1) {
				siteName = "Site0";
			}
		}

		try {
			site = new SiteImpl(siteName);

			registry.rebind(site.getName(), site);
			System.out.println("Site " + site.getName() + " créé.");
		} catch (final RemoteException e) {
			throw new RuntimeException("Impossible de créer un nouveau site", e);
		}

		// tue proprement le noeud
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				killSite(registry, site);
			}
		});
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

	private static void killSite(final Registry registry, final Site site) {
		try {
			registry.unbind(site.getName());
		} catch (final Exception e) {
			throw new RuntimeException("Impossible de tuer le site");
		}
	}
}
