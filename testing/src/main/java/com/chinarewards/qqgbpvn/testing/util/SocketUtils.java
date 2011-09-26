package com.chinarewards.qqgbpvn.testing.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbpvn.testing.context.TestContext;

/**
 * description：a socket util
 * @copyright binfen.cc
 * @projectName testing
 * @time 2011-9-26   上午10:34:32
 * @author Seek
 */
public final class SocketUtils {
	
	private static Logger logger = LoggerFactory.getLogger(SocketUtils.class);
	
	/**
	 * description：发送一个package，同时接收一个package
	 * @param sendPackage
	 * @return
	 * @time 2011-9-22   下午05:59:41
	 * @author Seek
	 */
	public static final byte[] sendPackageToServer(byte[] sendPackage){
		logger.debug("SocketUtils sendPackageToServer() run...");
		
		Socket socket = null;
		
		StringBuffer responsStr = new StringBuffer();
		try{
			socket = TestContext.getBasePosConfig().getSocket();
			OutputStream os = socket.getOutputStream();
			InputStream is = socket.getInputStream();
			
			os.write(sendPackage);
			
			byte[] b = new byte[1024];
			
			int amt = -1;
			
			while( (amt=is.read(b)) != -1){
				String buff = new String(b, 0, amt);
				responsStr.append(buff);
			}
			
		}catch(Throwable e) {
			logger.error(e.getMessage(), e);
		}finally{
			//socket.close();
		}
		return responsStr.toString().getBytes();
	}
	
}
