package ru.amse.nikitin.tests;

import javax.swing.ImageIcon;

import junit.framework.TestCase;

import ru.amse.nikitin.simulator.EMessageType;
import ru.amse.nikitin.simulator.IActiveObject;
import ru.amse.nikitin.simulator.IActiveObjectDesc;
import ru.amse.nikitin.simulator.IMessage;
import ru.amse.nikitin.simulator.impl.Dispatcher;
import ru.amse.nikitin.simulator.impl.Message;
import ru.amse.nikitin.simulator.impl.Time;
import ru.amse.nikitin.simulator.impl.VoidMessageFilter;

public class TestDispatcher extends TestCase {
	protected Dispatcher disp;
	protected int inits = 0;
	protected int turn = 0;
	protected int[] messagecount;
	protected boolean recieved = false;
	
	public TestDispatcher() {
		disp = Dispatcher.getInstance();
		disp.addMessageFilter(new VoidMessageFilter());
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		disp.removeAllListeners();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	class MsgTester implements IActiveObject {
		protected int id;
		protected Dispatcher disp;
		public MsgTester() {
			disp = Dispatcher.getInstance();
		}
		public int getID() {
			return id;
		}
		public void setID(int newID) {
			id = newID;
		}
		public IActiveObjectDesc newDesc(ImageIcon image, String name, int x, int y) {
			return null;
		}
		public IActiveObjectDesc getDesc() {
			return null;
		}
		public boolean recieveMessage(IMessage m) {
			return false;
		}
		public boolean sendMessage(IMessage m) {
			return false;
		}
	}
	
	class InitMsgTester extends MsgTester {
		public boolean recieveMessage(IMessage m) {
			assertEquals("Message Type", EMessageType.INIT,
				m.getType());
			inits++;
			return true;
		}
	}
	
	public void testInitMessage() {
		int n = 10;
		for (int i = 0; i < n; i++) disp.addActiveObjectListener(new InitMsgTester());
		disp.init();
		assertEquals("Inits Count", n, inits);
	}
	
	class CountMsgRecieverTester extends MsgTester {
		public boolean recieveMessage(IMessage m) {
			if (m.getDest() == id) {
				messagecount[id]++;
				return true;
			} else {
				return false;
			}
		}
	}
	class CountMsgSenderTester extends MsgTester {
		public boolean recieveMessage(IMessage m) {
			if (m.getType() == EMessageType.INIT) {
				for (int i = 1; i < 15; i++) {
					IMessage msg = new Message(disp.getMessageInitData());
					disp.assignMessage(this, m);
					msg.setDest(i);
					msg.setType(EMessageType.DATA);
					disp.sendMessage(msg);
				}
			}
			return true;
		}
	}
	
	public void testCountMessage() {
		int n = 15;
		messagecount = new int[n];
		disp.addActiveObjectListener(new CountMsgSenderTester());
		for (int i = 1; i < n; i++) { 
			disp.addActiveObjectListener(new CountMsgRecieverTester());
		}
		disp.init();
		disp.step();
		for (int i = 1; i < n; i++) { 
			assertEquals("Message Count (" + i + ")", 1, messagecount[i]);
		}
	}
	
	class TimerMsgTester extends MsgTester {
		public boolean recieveMessage(IMessage m) {
			if (m.getType() == EMessageType.INIT) {
				IMessage msg = new Message(disp.getMessageInitData());
				disp.assignMessage(this, m);
				disp.scheduleMessage(msg, new Time(8));
			}
			if (m.getType() == EMessageType.TIMER) {
				recieved = true;
				assertEquals("Turn", 9, turn);
				assertEquals("Sender", id, m.getSource());
				assertEquals("Reciever", id, m.getDest());
			}
			return true;
		}
	}
	
	public void testTimerMessage() {
		int n = 15;
		messagecount = new int[n];
		disp.addActiveObjectListener(new TimerMsgTester());
		for (int i = 1; i < n; i++) { 
			disp.addActiveObjectListener(new CountMsgSenderTester());
		}
		disp.init();
		for (int i = 0; i < 30; i++) {
			turn++;
			disp.step();
		}
		assertEquals("Recieved", true, recieved);
	}}
