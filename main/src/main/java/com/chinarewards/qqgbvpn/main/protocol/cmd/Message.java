package com.chinarewards.qqgbvpn.main.protocol.cmd;


/**
 * message
 * 
 * @author huangwei
 * 
 */
public class Message {

	private ICommand bodyMessage;

	private HeadMessage headMessage;

	public Message() {

	}

	public Message(HeadMessage headMessage, ICommand bodyMessage) {
		this.headMessage = headMessage;
		this.bodyMessage = bodyMessage;
	}

	// -----------------------------------------//
	public ICommand getBodyMessage() {
		return bodyMessage;
	}

	public void setBodyMessage(ICommand bodyMessage) {
		this.bodyMessage = bodyMessage;
	}

	public HeadMessage getHeadMessage() {
		return headMessage;
	}

	public void setHeadMessage(HeadMessage headMessage) {
		this.headMessage = headMessage;
	}

}
