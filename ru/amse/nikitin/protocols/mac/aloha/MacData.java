package ru.amse.nikitin.protocols.mac.aloha;

/* public */ class MacData {
	int messageId;
	
	public MacData(int messageId) {
		this.messageId = messageId;
	}

	public int getMessageId() {
		return messageId;
	}

	public String toString() {
		return "[AMD " + messageId + " ]";
	}

}
