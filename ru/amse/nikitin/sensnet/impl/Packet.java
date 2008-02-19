package ru.amse.nikitin.sensnet.impl;

import ru.amse.nikitin.sensnet.IPacket;

public class Packet implements IPacket {
	protected int dest;
	protected int[] data;
	protected int length;
	protected IPacket packet = null;
	protected String name;
	
	public Packet(int dest) {
		this.dest = dest;
	}
	
	/* package-private */ Packet(int[] memo, int offset) {
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
}

/* import ru.amse.nikitin.sensnet.IPacketInfo;

public class Packet implements IPacket {
	private String name = "";
	private IPacket data = null;
	private IPacketInfo info;
	private int length = 0;
	
	public Packet(String name) {
		this.name = name;
	}
	
	public boolean encapsulate(IPacket p) {
		if (data == null) {
			data = p;
			length += p.getLength();
			return true;
		} else {
			return false;
		}
	}
	
	public String getName() {
		return name;
	}
	
	public IPacket decapsulate() {
		if (data != null) {
			length -= data.getLength();
		}
		IPacket p = data;
		data = null;
		return p;
	}
	
	public boolean isEncapsulating() {
		return (data == null);
	}
	
	public IPacketInfo getInfo() {
		return info;
	}
	
	public void setInfo(IPacketInfo i) {
		info = i;
	}
	
	public int getLength() {
		return length + info.getLength();
	}
}

*/