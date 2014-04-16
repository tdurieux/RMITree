package lille1.car3.durieux_gouzer.rmi.mains;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import lille1.car3.durieux_gouzer.config.RMIConfiguration;
import lille1.car3.durieux_gouzer.rmi.Message;
import lille1.car3.durieux_gouzer.rmi.MessageImpl;
import lille1.car3.durieux_gouzer.rmi.Site;

/**
 * is an executable class used to allow user to send message from a specific
 * site. The main has parameters: siteName message [rmiHost rmiPort].
 * 
 * @author Thomas Durieux
 * 
 */
public class SiteSendMessage {
	public static void main(final String[] args) {

		String host;
		int port;
		Registry registry;
		String siteName;
		String message;

		try {
			siteName = args[0];
			message = args[1];
		} catch (final Exception e) {
			System.err
			.println("Usage: \033[4msiteName\033[0m \033[4mmessage\033[0m [port] [host]");
			return;
		}

		try {
			host = args[3];
		} catch (final Exception e) {
			host = RMIConfiguration.INSTANCE.getProperty("registryHost");
		}

		try {
			port = Integer.parseInt(args[4]);
		} catch (final Exception e) {
			port = RMIConfiguration.INSTANCE.getIntProperty("registryPort");
		}

		try {
			registry = LocateRegistry.getRegistry(host, port);
		} catch (final RemoteException e1) {
			throw new RuntimeException("Impossible de trouver le registry", e1);
		}

		try {
			final Site site = (Site) registry.lookup(siteName);
			sendMessage(site, message);
		} catch (final Exception e) {
			System.err.println("Site " + siteName + " non trouvé");
		}

	}

	private static void sendMessage(final Site site, final String message) {
		final Message m = new MessageImpl(message, site);
		try {
			site.transferMessage(m);
		} catch (final Exception e) {
			throw new RuntimeException("Impossible d'envoyer le message");
		}
	}
}
