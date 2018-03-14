package com.pwhiting.python.runner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Used to interact with a python executable.
 * 
 * @author phwhitin
 *
 */
public class Python extends CommandLineExecutable {

	private File pathToExecutable;
	private File workingDirectory;
	private VirtualEnv venv;
	private List<String> pythonPath = new ArrayList<>();
		
	public Python(VirtualEnv venv) {
		this.venv = venv;
	}
	
	public Python(File pathToExecutable) {
		this.pathToExecutable = pathToExecutable;
	}
	
	/**
	 * Returns the values to be added to the PYTHONPATH environmental variable. Add/remove paths to this for execution.
	 * 
	 * @return
	 */
	public List<String> getPythonPath() {
		return pythonPath;
	}
	
	public void setWorkingDirectory(File workingDir) {
		this.workingDirectory = workingDir;
	}
	
	/**
	 * Returns the version, or null if the python executable could not be found.
	 * 
	 * @return
	 */
	public String getVersion() {
		try {
			return executeWithArgs("--version").waitAndGetString();
		} catch (IOException ie) {
			return null;
		}
	}
	
	public ExecutableResult executeScript(File scriptFile, String...args) throws IOException {
		List<String> argList = new ArrayList<>();
		for (String arg : args) {
			argList.add(arg);
		}
		return executeScript(scriptFile, argList);
	}
	
	public ExecutableResult executeScript(String scriptFile, String... args) throws IOException {
		List<String> argList = new ArrayList<>();
		for (String arg : args) {
			argList.add(arg);
		}
		return executeScript(scriptFile, argList);
	}
	
	public ExecutableResult executeScript(File scriptFile, List<String> args) throws IOException {
		return executeScript(scriptFile.getAbsolutePath(), args);
	}
	
	public ExecutableResult executeScript(String scriptFile, List<String> args) throws IOException {

		List<String> allArgs = new ArrayList<>();
		allArgs.add(scriptFile);
		allArgs.addAll(args);
		
		return executeWithArgs(allArgs);
	}
	
	public ExecutableResult executeModule(String moduleName, String...args) throws IOException {
		List<String> argList = new ArrayList<>();
		for (String arg : args) {
			argList.add(arg);
		}
		return executeModule(moduleName, argList);
	}
	
	public ExecutableResult executeModule(String moduleName, List<String> args) throws IOException {
		List<String> allArgs = new ArrayList<>();
		allArgs.add("-m");
		allArgs.add(moduleName);
		allArgs.addAll(args);
		
		return executeWithArgs(allArgs); 
	}

	@Override
	public String getExecutable() {
		if (venv != null) {
			return venv.getExecutableDir() + File.separator + "python";
		} else if (pathToExecutable.exists()) {
			return pathToExecutable.getAbsolutePath();
		}
		
		return "python";
	}

	@Override
	public File getWorkingDirectory() {
		return workingDirectory;
	}

	@Override
	protected void configureEnvironment(Map<String, String> env) {
		if (venv != null) {
			venv.configureEnvironment(env);
		}
		
		if (!pythonPath.isEmpty()) {
			
			String path = env.getOrDefault("PYTHONPATH", "");
			StringBuilder builder = new StringBuilder();
			builder.append(path);
			pythonPath.forEach(p -> builder.append(File.pathSeparatorChar).append(path));
			env.put("PYTHONPATH", path);
			
		}
		
	}
	
}
