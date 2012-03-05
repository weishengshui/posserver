package com.chinarewards.qq.meishi.main;

import org.junit.Ignore;
import org.mortbay.jetty.servlet.ServletHolder;

/**
 * description：ServletHolder factory
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-5   下午05:32:23
 * @author Seek
 */
@Ignore
public final class ServletHolderFactory {
	
	/**
	 * description：build a ServletHolder
	 * @param responseContent
	 * @param charset
	 * @return
	 * @throws Exception
	 * @time 2012-3-5   下午05:32:04
	 * @author Seek
	 */
	public static ServletHolder getServletHolder(String responseContent,
			String charset) throws Exception {
		HardCodedServlet s = new HardCodedServlet();
		s.init();
		StringBuffer sb = new StringBuffer();
		
		sb.append(responseContent);
		s.setResponse(new String(sb.toString().getBytes(charset), "iso-8859-1"));
		
		ServletHolder h = new ServletHolder();
		h.setServlet(s);
		return h;
	}
	
}
