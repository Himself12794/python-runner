package com.pwhiting.python.runner;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.pwhiting.python.util.Util;

/**
 * A wrapper around a Process that has already been started. Provides utility in getting output.
 * 
 * @author phwhitin
 *
 */
public class ExecutableResult {

	private final Process process;
	private int status;
	
	ExecutableResult(Process process) {
		this.process = process;
	}
	
	public int waitFor() throws InterruptedException {
		status = process.waitFor();
		return status;
	}
	
	/**
	 * Waits for the process to terminate and returns the result as a list of lines.
	 * 
	 * @return
	 */
	public List<String> waitAndGetLines() {

		try {
			waitFor();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		
		return Util.getAsLines(getInputStream());
	}
	
	public String waitAndGetString() {

		try {
			waitFor();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		
		return Util.getAsString(getInputStream());
	}
	
	public OutputStream getOutputStream() {
		return process.getOutputStream();
	}
	
	public InputStream getInputStream() {
		return process.getInputStream();
	}

	public InputStream getErrorStream() {
		return process.getErrorStream();
	}
	
	public Process getProcess() {
		return process;
	}
	
	public int getStatus() {
		return status;
	}
	
}
