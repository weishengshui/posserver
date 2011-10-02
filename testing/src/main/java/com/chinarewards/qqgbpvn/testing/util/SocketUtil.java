package com.chinarewards.qqgbpvn.testing.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbpvn.testing.exception.ReadBytesFromPosServerException;
import com.chinarewards.qqgbpvn.testing.exception.WriteBytesToPosServerException;
import com.chinarewards.qqgbvpn.common.Tools;

/**
 * description：a socket util
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-26   上午10:34:32
 * @author Seek
 */
public final class SocketUtil {
	
	private static Logger logger = LoggerFactory.getLogger(SocketUtil.class);
	
	private static final int BUFF_SIZE = 1024;
	
	/**
	 * description：read bytes from inputStream	
	 * 				{restraint qq group buy pos message protocol}
	 * @param is a InputStream
	 * @return
	 * @time 2011-9-30   下午03:10:05
	 * @author Seek
	 */
	public static final byte[] readFromInputStream(InputStream is) 
				throws ReadBytesFromPosServerException {
		logger.debug("SocketUtils readFromInputStream() run...");
		
		byte[] result = new byte[0];
		try{
			byte[] transit = new byte[BUFF_SIZE];
			int amt = -1;
			while(true){
				amt = is.read(transit);
				logger.debug("read bytes amt:"+amt);
				
				byte[] tempBytes = new byte[result.length + amt];
				System.arraycopy(result, 0, tempBytes, 0, result.length);
				System.arraycopy(transit, 0, tempBytes, result.length, amt);
				result = tempBytes;
				
				if(result.length > (12+4) ){	//message size 位置
					byte[] messageSizeBytes = new byte[4];
					System.arraycopy(result, 12, messageSizeBytes, 0, 4);
					long messageSize = Tools.byteToUnsignedInt(messageSizeBytes);
					
					if(new Long(""+result.length).equals(messageSize)){
						break;
					}
				}
			}
		}catch(Throwable e){
			throw new ReadBytesFromPosServerException(e);
		}
		
		logger.debug("read bytes from socket:"+Arrays.toString(result)+
				", length="+result.length);
		return result;
	}
	
	/**
	 * description：write bytes to Socket
	 * @param os a OutputStream
	 * @param sendPackage
	 * @time 2011-9-30   下午03:10:28
	 * @author Seek
	 */
	public static final void writeFromOutputStream(OutputStream os, byte[] sendPackage) 
			throws WriteBytesToPosServerException {
		logger.debug("SocketUtils writeFromOutputStream() run...");
		try{
			logger.debug("write bytes to socket:"+Arrays.toString(sendPackage) +
					", length="+sendPackage.length);
			os.write(sendPackage);
			os.flush();
		}catch(Throwable e) {
			throw new WriteBytesToPosServerException(e);
		}
	}
	
	public static void main(String[] args) throws Exception{
		Socket s = new Socket("127.0.0.1", 1234);
		OutputStream out = s.getOutputStream();
		InputStream in = s.getInputStream();
		
		byte[] x = {0, 0, 0, 1, 32, 0, 0, 4, 0, 0, -102, -74, 0, 0, 0, 32, 0, 0, 0, 5, 84, 45, 48, 48, 48, 48, 48, 48, 48, 48, 48, 49};
		System.out.println("package length:"+x.length);
		
		out.write(x);
		out.flush();
		
		byte[] res = new byte[0];
		byte[] b = new byte[BUFF_SIZE];
		int amt = -1;
		
		while(true){
			amt=in.read(b);
			logger.debug("read bytes amt:"+amt);
			
			byte[] temp = new byte[res.length + amt];
			System.arraycopy(res, 0, temp, 0, res.length);
			System.arraycopy(b, 0, temp, res.length, amt);
			res = temp;
			
			if(res.length > (12+4) ){
				byte[] messageSizeBytes = new byte[4];
				System.arraycopy(res, 12, messageSizeBytes, 0, 4);
				long messageSize = Tools.byteToUnsignedInt(messageSizeBytes);
				
				if(new Long(""+res.length).equals(messageSize)){
					break;
				}
			}
		}
		
		out.close();
		s.close();
		
		System.out.println(Arrays.toString(res));
	}
	
}
