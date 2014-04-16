package lille1.car3.durieux_gouzer.rmi;

/**
 * is a class used to avoid cyclic messages, each Message has an unique ID.
 * 
 * @author Thomas Durieux
 * 
 */
public interface Message {
	String getContent();

	Site getSender();
}