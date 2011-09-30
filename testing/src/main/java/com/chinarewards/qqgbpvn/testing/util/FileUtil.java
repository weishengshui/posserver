package com.chinarewards.qqgbpvn.testing.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chinarewards.qqgbpvn.testing.exception.FileProcessException;

public final class FileUtil {
	
	private static Logger logger = LoggerFactory.getLogger(SocketUtil.class);
	
	private static final int BUFF_SIZE = 1024;
	
	/**
	 * description：write bytes to file, 
	 * 				when file is not exists,then new file()...
	 * @param bytes
	 * @param aFile
	 * @param isAppend  {true append file, false is not append}
	 * @return
	 * @time 2011-9-30   上午09:24:54
	 * @author Seek
	 */
	public final static File writeBytesToFile(byte[] bytes, File aFile, boolean isAppend) 
				throws FileProcessException {
		BufferedOutputStream stream = null;
		File file = null;
		try {
			file = aFile;
			if(!file.exists()){
				file.createNewFile();	//创建
			}
			
			FileOutputStream fstream = new FileOutputStream(file, isAppend);
			stream = new BufferedOutputStream(fstream);
			stream.write(bytes);
			logger.debug("write bytes("+Arrays.toString(bytes)+") to "+file.getAbsolutePath()+" !");
		} catch (Throwable e) {
			throw new FileProcessException(e);
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e1) {
					throw new FileProcessException(e1);
				}
			}
		}
		return file;
	}
	
	/**
	 * 获得文件byte
	 * @param file
	 */
	public static final byte[] getBytesFromFile(File file) throws FileProcessException {
		if (file == null) {
			return null;
		}
		FileInputStream stream = null;
		ByteArrayOutputStream out = null;
		try {
			stream = new FileInputStream(file);
			out = new ByteArrayOutputStream(BUFF_SIZE);
			byte[] b = new byte[BUFF_SIZE];
			int n;
			while ((n = stream.read(b)) != -1) {
				out.write(b, 0, n);
			}
			
			byte[] result = out.toByteArray();
			logger.debug("read bytes("+Arrays.toString(result)+") from "+
					file.getAbsolutePath()+" !");
			
			return result;
		} catch (Throwable e) {
			throw new FileProcessException(e);
		} finally {
			try {
				if (stream != null) {
					stream.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				throw new FileProcessException(e);
			}
		}
	}
	
}
