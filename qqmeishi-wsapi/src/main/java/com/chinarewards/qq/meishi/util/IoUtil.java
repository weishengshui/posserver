package com.chinarewards.qq.meishi.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.chinarewards.qq.meishi.exception.QQMeishiReadStreamException;

/**
 * description：io util
 * @copyright binfen.cc
 * @projectName qqmeishi-wsapi
 * @time 2012-3-5   下午05:27:10
 * @author Seek
 */
public final class IoUtil {
	
	/**
	 * description：read String from inputStarem
	 * @param in a inputStream
	 * @param charset 字符编码
	 * @return 读取到的内容
	 * @time 2012-3-2   下午05:37:51
	 * @author Seek
	 */
	public static String readStream(InputStream in, String charset) throws 
			QQMeishiReadStreamException {
		StringBuilder sb = new StringBuilder("");
		if (in != null) {
			BufferedReader reader = null;
			String line;
			try {
				if (charset != null && !"".equals(charset.trim())) {
					reader = new BufferedReader(new InputStreamReader(in, charset));
				} else {
					reader = new BufferedReader(new InputStreamReader(in));
				}
				while ((line = reader.readLine()) != null) {
					sb.append(line.trim());
				}
			} catch (Throwable e) {
				throw new QQMeishiReadStreamException(e);
			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
					if (in != null) {
						in.close();
					}
				} catch (IOException e) {
					throw new QQMeishiReadStreamException(e);
				}
			}
		}
		return sb.toString().trim();
	}
	
}
