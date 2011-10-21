package com.chinarewards.qqgbvpn.main.protocol.cmd;


/**
 * Search Request Message
 * 
 * @author huangwei
 *
 */
public class SearchRequestMessage implements ICommand {

	private long cmdId;
	
	private int page;
	
	private int size;

	@Override
	public String toString() {
		return " [cmdId=" + cmdId + ", page=" + page + ", size=" + size + "]";
	}

	@Override
	public long getCmdId() {
		return cmdId;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setCmdId(long cmdId) {
		this.cmdId = cmdId;
	}
	
}
