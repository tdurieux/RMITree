package lille1.car3.durieux_gouzer.rmi;

/**
 * est un message qui est à commiquer entre les différents sites
 * 
 * @author Thomas Durieux
 * 
 */
public interface Message {
	/**
	 * Récupère le contenu du message
	 * 
	 * @return le message
	 */
	String getContent();

	/**
	 * Récupère l'expéditeur du message
	 * 
	 * @return l'expéditeur
	 */
	Site getSender();

}