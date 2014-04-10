package lille1.car3.durieux_gouzer.rmi;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

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

	@Override
	public String getName() throws RemoteException {
		return this.name;
	}

	@Override
	public void addConnection(final Site connection) throws RemoteException {
		if (!this.connections.contains(connection)) {
			this.connections.add(connection);
			System.out.println("[" + this.name + "] connecté avec "
					+ connection.getName());
		}
	}

	@Override
	public synchronized void transferMessage(final Message message)
			throws RemoteException {

		// le message a déjà été transféré
		if (this.receivedMessages.contains(message)) {
			return;
		}
		this.receivedMessages.add(message);

		System.out.println("[" + this.name + "] \"" + message.getContent()
				+ "\" recieved message from " + message.getSender().getName());

		// transmettre le message aux noeuds connectés
		for (final Site connecion : this.connections) {
			// ne pas envoyer le message à l'émetteur
			if (!connecion.equals(message.getSender())) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							connecion.transferMessage(message);
							System.out.println("[" + SiteImpl.this.name
									+ "] transfer " + message.getContent()
									+ " to " + connecion.getName());
						} catch (final RemoteException e) {
							throw new RuntimeException(
									"Unable to send message to site", e);
						}
					}
				});

			}
		}

	}

	@Override
	public List<Message> getReceivedMessages() throws RemoteException {
		return this.receivedMessages;
	}

}
