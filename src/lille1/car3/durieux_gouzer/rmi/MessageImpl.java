package lille1.car3.durieux_gouzer.rmi;

import java.io.Serializable;

public class MessageImpl implements Message, Serializable {
	private String content;
	private Site sender;
	private float time = System.nanoTime();
	
	public MessageImpl(String content, Site sender) {
		super();
		this.content = content;
		this.sender = sender;
	}

	/**
	 * @see lille1.car3.durieux_gouzer.rmi.Message#getContent()
	 */
	@Override
	public String getContent() {
		return content;
	}

	/**
	 * @see lille1.car3.durieux_gouzer.rmi.Message#getSender()
	 */
	@Override
	public Site getSender() {
		return sender;
	}	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(time);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof MessageImpl)) {
			return false;
		}
		MessageImpl other = (MessageImpl) obj;
		if (Float.floatToIntBits(time) != Float.floatToIntBits(other.time)) {
			return false;
		}
		return true;
	}
}
