package com.chinarewards.qqgbpvn.testing.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.chinarewards.qqgbpvn.testing.context.TestContext;
import com.chinarewards.qqgbvpn.common.Tools;

/**
 * description：a socket util
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-26   上午10:34:32
 * @author Seek
 */
public final class SocketUtils {
	
	private static Logger logger = LoggerFactory.getLogger(SocketUtils.class);
	
	private static final int BUFF_SIZE = 1024;
	
	/**
	 * description：发送一个package，同时接收一个package
	 * @param sendPackage
	 * @return
	 * @time 2011-9-22   下午05:59:41
	 * @author Seek
	 */
	public static final byte[] sendPackageToServer(Socket outerSocket, byte[] sendPackage){
		logger.debug("SocketUtils sendPackageToServer() run...");
		
		Socket socket = null;
		OutputStream os = null;
		InputStream is = null;
		
		byte[] result = new byte[0];
		try{
			socket = outerSocket;
			os = socket.getOutputStream();
			is = socket.getInputStream();
			
			os.write(sendPackage);
			os.flush();
			
			
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
		}catch(Throwable e) {
			logger.error(e.getMessage(), e);
		}finally{
			//socket.close();
		}
		
		logger.debug("socket I/O end!");
		return result;
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
