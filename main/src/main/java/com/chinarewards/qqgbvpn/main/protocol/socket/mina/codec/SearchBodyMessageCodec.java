package com.chinarewards.qqgbvpn.main.protocol.socket.mina.codec;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbvpn.common.Tools;
import com.chinarewards.qqgbvpn.main.exception.PackageException;
import com.chinarewards.qqgbvpn.main.protocol.cmd.CmdConstant;
import com.chinarewards.qqgbvpn.main.protocol.cmd.ICommand;
import com.chinarewards.qqgbvpn.main.protocol.cmd.SearchRequestMessage;
import com.chinarewards.qqgbvpn.main.protocol.cmd.SearchResponseDetail;
import com.chinarewards.qqgbvpn.main.protocol.cmd.SearchResponseMessage;
import com.chinarewards.qqgbvpn.main.protocol.socket.ProtocolLengths;

/**
 * Search BodyMessage Coder
 * 
 * @author huangwei
 *
 */
public class SearchBodyMessageCodec implements ICommandCodec {

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Override
	public ICommand decode(IoBuffer in, Charset charset)
			throws PackageException {
		log.debug("search message decode");
		SearchRequestMessage message = new SearchRequestMessage();
		if (in.remaining() != ProtocolLengths.COMMAND + ProtocolLengths.PAGE
				+ ProtocolLengths.SIZE) {
			throw new PackageException(
					"search packge message body error, body message is :" + in);
		}
		long cmdId = in.getUnsignedInt();
		int page = in.getUnsignedShort();
		int size = in.getUnsignedShort();
		
		message.setCmdId(cmdId);
		message.setPage(page);
		message.setSize(size);
		log.debug("search message request:cmdId is ({}) , page is ({}), size is ({})",new Object[]{cmdId,page,size});
		return message;
	}

	@Override
	public byte[] encode(ICommand bodyMessage, Charset charset) {
		log.debug("search message encode");

		SearchResponseMessage responseMessage = (SearchResponseMessage) bodyMessage;
		long cmdId = responseMessage.getCmdId();
		int result = responseMessage.getResult();
		int totalnum = responseMessage.getTotalnum();
		int curnum = responseMessage.getCurnum();
		int curpage = responseMessage.getCurpage();
		int totalpage = responseMessage.getTotalpage();
		List<SearchResponseDetail> detail = responseMessage.getDetail();
		log.debug("search reault result is ({}), totalnum is ({}),curnum is ({}), curpage is ({}), totalpage is ({})detail size is ({})",
				new Object[]{result,totalnum,curnum,curpage,totalpage,detail.size()});
		
		StringBuffer buffer = new StringBuffer();
		for(SearchResponseDetail searchResponseDetail:detail){
			buffer.append(searchResponseDetail.getGrouponId() == null?"":searchResponseDetail.getGrouponId()).append(CmdConstant.SEPARATOR);
			buffer.append(searchResponseDetail.getGrouponName()== null?"":searchResponseDetail.getGrouponName()).append(CmdConstant.SEPARATOR);
			buffer.append(searchResponseDetail.getMercName()== null?"":searchResponseDetail.getMercName()).append(CmdConstant.SEPARATOR);
			buffer.append(searchResponseDetail.getListName()== null?"":searchResponseDetail.getListName()).append(CmdConstant.SEPARATOR);
			buffer.append(searchResponseDetail.getDetailName()== null?"":searchResponseDetail.getDetailName()).append(CmdConstant.SEPARATOR);
		}
		String tmpStr = buffer.toString();
		log.debug("detail buffer is ({})",buffer);
		tmpStr = tmpStr.replaceAll("\\\\r\\\\n", String.valueOf(CmdConstant.ENTER));
		byte[] detailByte = tmpStr.getBytes(charset);
		log.debug("detail src is ({})",tmpStr);
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
