package lille1.car3.durieux_gouzer.rmi;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * is a RMI object that represent a node and allows node to communicate with its
 * childrens.
 * 
 * @author Thomas Durieux
 * 
 */
public class SiteImpl extends UnicastRemoteObject implements Site, Serializable {

	private static final long serialVersionUID = -4318509254473189526L;

	private final String name;
	private final List<Site> connections;
	private final List<Message> receivedMessages;

	public SiteImpl(final String name) throws RemoteException {
		this.name = name;
		this.connections = new ArrayList<Site>();
		this.receivedMessages = new ArrayList<Message>();
	}

	/**
	 * @see Site
	 */
	@Override
	public String getName() throws RemoteException {
		return this.name;
	}

	/**
	 * @see Site
	 */
	@Override
	public void addConnection(final Site connection) throws RemoteException {
		if (!this.connections.contains(connection)) {
			this.connections.add(connection);
			System.out.println("[" + this.name + "] connected with "
					+ connection.getName());
		}
	}

	/**
	 * @see Site
	 */
	@Override
	public void transferMessage(final Message message) throws RemoteException {

		synchronized (this.receivedMessages) {
			if (this.receivedMessages.contains(message)) {
				// le message a déjà été transféré
				return;
			}
			this.receivedMessages.add(message);
		}

		System.out.println("[" + this.name + "] \"" + message.getContent()
				+ "\" receives message from " + message.getSender().getName());
		new Thread(new Runnable() {
			@Override
			public void run() {
				// transmettre le message aux noeuds connectés
				for (final Site connecion : SiteImpl.this.connections) {
					// ne pas envoyer le message à l'émetteur
					if (!connecion.equals(message.getSender())) {
						new Thread(new Runnable() {
							final Site c = connecion;

							@Override
							public void run() {
								try {
									this.c.transferMessage(message);
									System.out.println("[" + SiteImpl.this.name
											+ "] transfer "
											+ message.getContent() + " to "
											+ this.c.getName());
								} catch (final RemoteException e) {
									throw new RuntimeException(
											"Unable to send message to site", e);
								}
							}
						}).run();

					}
				}
			}
		}).run();

	}

	/**
	 * @see Site
	 */
	@Override
	public List<Message> getReceivedMessages() throws RemoteException {
		return this.receivedMessages;
	}

}
