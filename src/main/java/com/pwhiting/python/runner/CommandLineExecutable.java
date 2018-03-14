package com.pwhiting.python.runner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class CommandLineExecutable {

	protected boolean inheritIO;
	
	public void setInheritIO() {
		inheritIO = true;
	}
	
	public void setPipeIO() {
		inheritIO = false;
	}
	
	protected ExecutableResult executeWithArgs(String...args) throws IOException {
		
		List<String> argList = new ArrayList<>();
		for (String arg : args) {
			argList.add(arg);
		}
		return executeWithArgs(argList);
		
	}
	
	/**
	 * Returns the output of the argument as a string
	 * 
	 * @param arguments
	 * @return
	 */
	protected ExecutableResult executeWithArgs(List<String> arguments) throws IOException {
		
		List<String> fullCommand = new ArrayList<>();
		fullCommand.add(getExecutable());
		fullCommand.addAll(arguments);
		
		ProcessBuilder procBuilder = new ProcessBuilder(fullCommand);
		procBuilder.directory(getWorkingDirectory());
		configureEnvironment(procBuilder.environment());
		if (inheritIO) {
			procBuilder.inheritIO();
		}
		
		Process proc = procBuilder.start();
		
		return new ExecutableResult(proc);
	}
	
	public abstract String getExecutable();

	public abstract File getWorkingDirectory();
	
	protected abstract void configureEnvironment(Map<String, String> env);
	
	public static String streamToString(InputStream in) {

		try (InputStream ins = in; 
				ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			
			int nRead;
		    byte[] data = new byte[1024];
		    while ((nRead = in.read(data, 0, data.length)) != -1) {
		        baos.write(data, 0, nRead);
		    }
			
		    baos.flush();
		    return baos.toString(StandardCharsets.UTF_8.displayName());
		    
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
		return "";
		
	}
	
}
