package com.chinarewards.qqgbvpn.main.protocol.socket.message;

/**
 * login request message body
 * 
 * @author huangwei
 *
 */
public class LoginRequestMessage implements IBodyMessage {

	private long cmdId;

	private String posid;
	
	private byte[] challengeResponse; 

	
	public long getCmdId() {
		return cmdId;
	}

	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;

	}

	public String getPosid() {
		return posid;
	}

	public void setPosid(String posid) {
		this.posid = posid;
	}

	public byte[] getChallengeResponse() {
		return challengeResponse;
	}

	public void setChallengeResponse(byte[] challeugeresponse) {
		this.challengeResponse = challeugeresponse;
	}
}
