/**
 * 
 */
package com.chinarewards.qqgbpvn.main.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

import org.junit.After;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 
 * @author Cyril
 * @since 0.1.0
 */
public abstract class BaseTest {

	protected Logger log = LoggerFactory.getLogger(getClass());

	protected Stack<File> tmpDirStack = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		tmpDirStack = new Stack<File>();
	}

	@After
	public void tearDown() throws Exception {
		clearTmpDirStack();
	}

	/**
	 * Clear the temporary directory stack.
	 */
	private void clearTmpDirStack() {
		if (tmpDirStack != null && !tmpDirStack.isEmpty()) {
			while (!tmpDirStack.isEmpty()) {
				File f = tmpDirStack.pop();
				f.delete();
			}
		}
	}

	/**
	 * Create a temp directory and push to stack.
	 * 
	 * @return
	 * @throws IOException
	 */
	protected File createTmpDir() throws IOException {
		File tmpDir = File.createTempFile("test", null);
		tmpDir.delete();
		tmpDir.mkdir();
		// remember this.
		tmpDirStack.push(tmpDir);
		return tmpDir;
	}

	/**
	 * Create a temp directory and push to stack. The file is not actually
	 * created.
	 * 
	 * @return
	 * @throws IOException
	 */
	protected File reserveEmtpyTmpFile(File baseDir) throws IOException {
		File tmp = File.createTempFile("file", null, baseDir);
		tmp.delete();
		// remember this.
		tmpDirStack.push(tmp);
		return tmp;
	}
	
	
	protected void copyToFile(InputStream src, File dst) throws IOException {
		FileOutputStream os = new FileOutputStream(dst);
		byte[] buf = new byte[4096];
		int actual = 0;
		while ((actual = src.read(buf)) != -1) {
			os.write(buf, 0, actual);
		}
		os.close();
	}

}
