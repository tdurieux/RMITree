package lille1.car3.durieux_gouzer.rmi;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SiteImpl extends UnicastRemoteObject implements Site, Serializable {

	private static final long serialVersionUID = -4318509254473189526L;

	private String name;
	private List<Site> connections;
	private List<Message> receivedMessages;

	public SiteImpl(String name) throws RemoteException {
		this.name = name;
		this.connections = new ArrayList<Site>();
		this.receivedMessages = new ArrayList<Message>();
	}

	@Override
	public String getName() throws RemoteException {
		return this.name;
	}

	@Override
	public void addConnection(Site connection) throws RemoteException {
		if (!connections.contains(connection)) {
			this.connections.add(connection);
			System.out.println("[" + this.name + "] connecté avec "
					+ connection.getName());
		}
	}

	@Override
	public synchronized void transferMessage(Message message)
			throws RemoteException {

		// le message a déjà été transféré
		if (receivedMessages.contains(message)) {
			return;
		}
		receivedMessages.add(message);
		
		System.out.println("[" + this.name + "] \"" + message.getContent()
				+ "\" recieved message from " + message.getSender().getName());

		// transmettre le message aux noeuds connectés
		for (Site connecion : connections) {
			// ne pas envoyer le message à l'émetteur
			if (!connecion.equals(message.getSender())) {
				connecion.transferMessage(message);
				System.out.println("[" + this.name + "] transfer "
						+ message.getContent() + " to " + connecion.getName());
			}
		}

	}

}
