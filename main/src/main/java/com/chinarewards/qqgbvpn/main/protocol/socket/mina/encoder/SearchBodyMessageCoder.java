package com.chinarewards.qqgbvpn.main.protocol.socket.mina.encoder;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackgeException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.IBodyMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.SearchRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.SearchResponseDetail;
import com.chinarewards.qqgbvpn.main.protocol.socket.message.SearchResponseMessage;

/**
 * Search BodyMessage Coder
 * 
 * @author huangwei
 *
 */
public class SearchBodyMessageCoder implements IBodyMessageCoder {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public IBodyMessage decode(IoBuffer in, Charset charset)
			throws PackgeException {
		log.debug("search message decode");
		SearchRequestMessage message = new SearchRequestMessage();
		if (in.remaining() != ProtocolLengths.COMMAND + ProtocolLengths.PAGE
				+ ProtocolLengths.SIZE) {
			throw new PackgeException(
					"search packge message body error, body message is :" + in);
		}
		long cmdId = in.getUnsignedInt();
		int page = in.getUnsignedShort();
		int size = in.getUnsignedShort();
		
		message.setCmdId(cmdId);
		message.setPage(page);
		message.setSize(size);
		log.debug("search message request:cmdId is() , page is(), size is()",new Object[]{cmdId,page,size});
		return message;
	}

	@Override
	public byte[] encode(IBodyMessage bodyMessage, Charset charset) {
		log.debug("search message encode");

		SearchResponseMessage responseMessage = (SearchResponseMessage) bodyMessage;
		long cmdId = responseMessage.getCmdId();
		int result = responseMessage.getResult();
		int totalnum = responseMessage.getTotalnum();
		int curnum = responseMessage.getCurnum();
		int curpage = responseMessage.getCurpage();
		int totalpage = responseMessage.getTotalpage();
		List<SearchResponseDetail> detail = responseMessage.getDetail();
		
		StringBuffer buffer = new StringBuffer();
		for(SearchResponseDetail searchResponseDetail:detail){
			buffer.append(searchResponseDetail.getGrouponId()).append(CmdConstant.END_PRIEX);
			buffer.append(searchResponseDetail.getGrouponName()).append(CmdConstant.END_PRIEX);
			buffer.append(searchResponseDetail.getMercName()).append(CmdConstant.END_PRIEX);
			buffer.append(searchResponseDetail.getListName()).append(CmdConstant.END_PRIEX);
			buffer.append(searchResponseDetail.getDetailName()).append(CmdConstant.END_PRIEX);
		}
		byte[] detailByte = buffer.toString().getBytes(charset);
		byte[] resultByte = new byte[ProtocolLengths.COMMAND + 10 + detailByte.length];
		
		Tools.putUnsignedInt(resultByte, cmdId, 0);
		Tools.putUnsignedShort(resultByte, result, ProtocolLengths.COMMAND);
		Tools.putUnsignedShort(resultByte, totalnum, ProtocolLengths.COMMAND+2);
		Tools.putUnsignedShort(resultByte, curnum, ProtocolLengths.COMMAND+4);
		Tools.putUnsignedShort(resultByte, curpage, ProtocolLengths.COMMAND+6);
		Tools.putUnsignedShort(resultByte, totalpage, ProtocolLengths.COMMAND+8);
		Tools.putBytes(resultByte, detailByte, ProtocolLengths.COMMAND + 10);

		return resultByte;
	}

}
