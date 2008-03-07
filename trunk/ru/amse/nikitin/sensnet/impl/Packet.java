package ru.amse.nikitin.sensnet.impl;

import ru.amse.nikitin.sensnet.IPacket;
import ru.amse.nikitin.sensnet.ISendCallback;

/* public class Packet implements IPacket {
	protected int dest;
	protected int[] data;
	protected int length;
	protected IPacket packet = null;
	protected String name;
	protected ISendCallback onSendAction = null;
	
	public Packet(int dest) {
		this.dest = dest;
	}
	
	Packet(int[] memo, int offset) {
		dest = memo[offset];
		int datalen = memo[offset + 1];
		if (memo.length >= offset + datalen + 2) {
			length = memo.length - offset - datalen - 2;
			if (datalen > 0) {
				data = new int [datalen];
				for (int i = 0; i < datalen; i++) {
					data[i] = memo[i + offset + 2];
				}
			}
			if (length > 0) {
				packet = new Packet(memo, offset + datalen + 2);
			}
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}
	
	public boolean encapsulate(IPacket p) {
		if (packet == null) {
			length += p.getLength();
			packet = p;
			return true;
		} else {
			return false;
		}
	}
	
	public IPacket decapsulate() {
		if (packet != null) {
			length -= packet.getLength();
		}
		IPacket p = packet;
		packet = null;
		return p;
	}
	
	public boolean isEncapsulating() {
		return (packet != null);
	}
	
	public void toIntArr(int[] arr, int offset) {
		int datalen = (data == null) ? 0 : data.length;
		if (arr.length <= offset + datalen + length + 2) {
			arr[offset] = dest;
			arr[offset + 1] = datalen;
			for (int i = 0; i < datalen; i++) {
				arr[offset + i + 2] = data[i];
			}
			if (packet != null) {
				packet.toIntArr(arr, offset + datalen + 2);
			}
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}
	
	public void setData(int[] data) {
		this.data = data;
	}
	
	public int getID() {
		return dest;
	}
	
	public int[] getData() {
		return data;
	}
	
	public String getName() {
		return name;
	}
	
	public int getLength() {
		int datalen = (data == null) ? 0 : data.length;
		return length + datalen + 2;
	}

	public ISendCallback getOnSendAction() {
		return onSendAction;
	}

	public void setOnSendAction(ISendCallback onSendAction) {
		this.onSendAction = onSendAction;
	}
	
} */

public class Packet implements IPacket {
	private Object data = null;
	private int length;
	private int id = 0;
	private boolean isIncapsulating = false;
	
	protected ISendCallback onSendAction = null;
	
	public Packet(int id) {
		this.id = id;
		length = 1;
	}
	
	public boolean encapsulate(IPacket p) {
		if (!isIncapsulating) {
			data = p;
			length += p.getLength();
			isIncapsulating = true;
			return true;
		} else {
			return false;
		}
	}
	
	public IPacket decapsulate() {
		if (isIncapsulating) {
			IPacket p = (IPacket)data;
			length -= p.getLength();
			data = null;
			isIncapsulating = false;
			return p;
		} else {
			return null;
		}
	}
	
	public boolean isEncapsulating() {
		return isIncapsulating;
	}
	
	public int getLength() {
		return length;
	}
	
	public ISendCallback getOnSendAction() {
		return onSendAction;
	}

	public void setOnSendAction(ISendCallback onSendAction) {
		this.onSendAction = onSendAction;
	}

	public int getID() {
		return id;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		System.err.println(data.getClass());
		this.data = data;
	}

}
