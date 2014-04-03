package test.lille1.car3.durieux_gouzer;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import lille1.car3.durieux_gouzer.rmi.Message;
import lille1.car3.durieux_gouzer.rmi.MessageImpl;
import lille1.car3.durieux_gouzer.rmi.Site;
import lille1.car3.durieux_gouzer.rmi.SiteImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SiteTest {
	Registry registry;
	int port = 56765;

	@Before
	public void beforeTest() throws RemoteException {
		this.registry = LocateRegistry.createRegistry(this.port);
	}

	@After
	public void afterTest() throws IOException {
		JMXServiceURL url = null;
		final String urlString = "service:jmx:rmi://localhost:"
				+ (this.port + 1) + "/jndi/rmi://:" + this.port + "/jmxrmi";
		url = new JMXServiceURL(urlString);

		final JMXConnectorServer connector = JMXConnectorServerFactory
				.newJMXConnectorServer(url, new HashMap<String, Object>(),
						ManagementFactory.getPlatformMBeanServer());
		connector.stop();
		UnicastRemoteObject.unexportObject(this.registry, true);
	}

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
