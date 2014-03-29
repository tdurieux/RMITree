package lille1.car3.durieux_gouzer.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Site extends Remote {

	String getName() throws RemoteException;

	void addConnection(Site connection) throws RemoteException;

	void transferMessage(Message message) throws RemoteException;

}