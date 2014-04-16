package test.lille1.car3.durieux_gouzer;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;

import lille1.car3.durieux_gouzer.rmi.Message;
import lille1.car3.durieux_gouzer.rmi.MessageImpl;
import lille1.car3.durieux_gouzer.rmi.Site;
import lille1.car3.durieux_gouzer.rmi.SiteImpl;

import org.junit.Test;

public class SiteTest {

	@Test
	public void sendMessageToHimSelf() throws RemoteException {

		final Site sender = new SiteImpl("Site1");
		final Message m = new MessageImpl("Mon contenu", sender);

		sender.transferMessage(m);
		assertEquals(m.getContent(), sender.getReceivedMessages().get(0)
				.getContent());
	}

	/**
	 * Vérifie que les enfants recoivent les messages de son parent
	 * 
	 * @throws RemoteException
	 */
	@Test
	public void sendMessageToOther() throws RemoteException {

		final Site sender = new SiteImpl("Site1");
		final Site receiver = new SiteImpl("Site2");
		sender.addConnection(receiver);
		final Message m = new MessageImpl("Mon contenu", sender);

		sender.transferMessage(m);
		assertEquals(m.getContent(), receiver.getReceivedMessages().get(0)
				.getContent());
	}

	/**
	 * Vérifie que les parents ne recoivent pas les messages des enfants
	 * 
	 * @throws RemoteException
	 */
	@Test
	public void sendMessageParentNotReceiveMessage() throws RemoteException {

		final Site parent = new SiteImpl("Site1");
		final Site children = new SiteImpl("Site2");
		parent.addConnection(children);
		final Message m = new MessageImpl("Mon contenu", children);

		children.transferMessage(m);
		assertEquals(0, parent.getReceivedMessages().size());
	}

	/**
	 * Vérifie que les parents ne recoivent pas les messages des enfants
	 * 
	 * @throws RemoteException
	 */
	@Test
	public void sendMessageChildrenReceive() throws RemoteException {

		final Site parent = new SiteImpl("parent");
		final Site parentChildren = new SiteImpl("parentChildren");
		final Site children = new SiteImpl("children");
		parent.addConnection(parentChildren);
		parentChildren.addConnection(children);

		final Message m = new MessageImpl("Mon contenu", parent);

		parent.transferMessage(m);
		assertEquals(m.getContent(), parentChildren.getReceivedMessages()
				.get(0).getContent());
		assertEquals(m.getContent(), children.getReceivedMessages().get(0)
				.getContent());
	}

}
