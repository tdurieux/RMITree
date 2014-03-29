package lille1.car3.durieux_gouzer.rmi;

import java.io.Serializable;

/**
 * est un message qui est à commiquer entre les différents sites
 * 
 * @author Thomas Durieux
 * 
 */
public class MessageImpl implements Message, Serializable {
	private final String content;
	private final Site sender;
	private final float time = System.nanoTime();

	public MessageImpl(final String content, final Site sender) {
		super();
		this.content = content;
		this.sender = sender;
	}

	/**
	 * @see lille1.car3.durieux_gouzer.rmi.Message#getContent()
	 */
	@Override
	public String getContent() {
		return this.content;
	}

	/**
	 * @see lille1.car3.durieux_gouzer.rmi.Message#getSender()
	 */
	@Override
	public Site getSender() {
		return this.sender;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(this.time);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MessageImpl)) {
			return false;
		}
		final MessageImpl other = (MessageImpl) obj;
		if (Float.floatToIntBits(this.time) != Float.floatToIntBits(other.time)) {
			return false;
		}
		return true;
	}
}
