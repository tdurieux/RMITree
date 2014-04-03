package lille1.car3.durieux_gouzer.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * est un noeud de l'arbre de communication
 * 
 * @author Thomas Durieux
 * 
 */
public interface Site extends Remote {

	String getName() throws RemoteException;

	/**
	 * Ajoute un connection au site
	 * 
	 * @param connection
	 *            le site au quel se connecter
	 * @throws RemoteException
	 *             si le site n'est plus accéssible
	 */
	void addConnection(Site connection) throws RemoteException;

	/**
	 * Transfert un message à tous les sites connectés
	 * 
	 * @param message
	 *            le message a transmettre
	 * @throws RemoteException
	 *             si le site n'est plus accéssible
	 */
	void transferMessage(Message message) throws RemoteException;

	/**
	 * 
	 * @return
	 * @throws RemoteException
	 */
	List<Message> getReceivedMessages() throws RemoteException;
}