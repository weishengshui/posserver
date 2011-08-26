package com.chinarewards.qqgbvpn.main.protocol.socket.message;

/**
 * message
 * 
 * @author huangwei
 * 
 */
public class Message {

	private IBodyMessage bodyMessage;

	private HeadMessage headMessage;

	public Message() {

	}

	public Message(HeadMessage headMessage, IBodyMessage bodyMessage) {
		this.headMessage = headMessage;
		this.bodyMessage = bodyMessage;
	}

	// -----------------------------------------//
	public IBodyMessage getBodyMessage() {
		return bodyMessage;
	}

	public void setBodyMessage(IBodyMessage bodyMessage) {
		this.bodyMessage = bodyMessage;
	}

	public HeadMessage getHeadMessage() {
		return headMessage;
	}

	public void setHeadMessage(HeadMessage headMessage) {
		this.headMessage = headMessage;
	}

}
