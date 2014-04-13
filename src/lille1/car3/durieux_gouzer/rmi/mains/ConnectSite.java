package lille1.car3.durieux_gouzer.rmi.mains;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
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
public class ConnectSite {
	public static void main(final String[] args) {
		int port;
		Registry registry;
		String connectCommand;

		try {
			connectCommand = args[0];
		} catch (final Exception e) {
			// utiliser le port par défaut
			System.err
					.println("Usage: \033[4msiteName1\033[0m->\033[4msiteName2\033[0m [port]");
			return;
		}

		try {
			port = Integer.parseInt(args[1]);
		} catch (final Exception e) {
			// utiliser le port par défaut
			port = RMIConfiguration.INSTANCE.getIntProperty("registryPort");
		}
		try {
			registry = LocateRegistry.getRegistry(port);
		} catch (final RemoteException e) {
			throw new RuntimeException("Impossible de démarrer l'annuaire", e);
		}

		final Pattern pattern = Pattern.compile("(.*)(<-|->)(.*)");
		final Matcher matcher = pattern.matcher(connectCommand);
		if (!matcher.find()) {
			System.err
			.println("Usage: "
					+ args[0]
							+ " \033[4msiteName1\033[0m->\033[4msiteName2\033[0m [port]");
			return;
		}
		final String siteName1 = matcher.group(1);
		final String siteName2 = matcher.group(3);
		Site site1;
		Site site2;
		try {
			site1 = (Site) registry.lookup(siteName1);
		} catch (final Exception e) {
			System.err.println("Impossible de touver le site " + siteName1);
			return;
		}
		try {
			site2 = (Site) registry.lookup(siteName2);
		} catch (final Exception e) {
			System.err.println("Impossible de touver le site " + siteName2);
			return;
		}
		try {
			if (matcher.group(2).equals("->")) {
				site1.addConnection(site2);
			} else {
				site2.addConnection(site1);
			}
		} catch (final RemoteException e) {
			System.err.println("Impossible de connecter " + siteName1 + " et "
					+ siteName2);
		}
	}
}
