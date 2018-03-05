package com.pwhiting.python.runner;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Interact with a virtual environment by through using pip.
 * 
 * @author phwhitin
 *
 */
public class VirtualEnv extends CommandLineExecutable {

	private final File virtualEnvPath;
	
	public VirtualEnv(File virtualEnvPath) {
		this.virtualEnvPath = virtualEnvPath;
	}

	public String getVersion() {
		boolean currentRedirect = inheritIO;
		try {
			setPipeIO();
			String result = executeWithArgs("--version").waitAndGetString();
			this.inheritIO = currentRedirect;
			return result;
		} catch (IOException e) {
			return null;
		}
	}

	public void uninstallModule(String module) {
		try {
			ExecutableResult result = executeWithArgs("uninstall", module, "-y");
			result.waitFor();
		} catch (InterruptedException | IOException ioe) {
			
		}
	}
	
	public void installModule(String module) {
		installModule(module, null, true);
	}
	
	public void installModule(String module, String version, boolean update) {
		
		try {
			ExecutableResult result = executeWithArgs("install", module + (version != null ? "==" + version : ""), (update ? "-U" : ""));
			result.waitFor();
		} catch (InterruptedException | IOException ioe) {
			
		}
		
	} 
	
	public Map<String, String> getInstalledComponents() {
		ExecutableResult result = null;
		Map<String, String> components = new HashMap<>();
		boolean currentRedirect = inheritIO;
		try {
			setPipeIO();
			result = executeWithArgs("freeze");
			this.inheritIO = currentRedirect;
		} catch (IOException e) {
		}
		
		if (result != null) {
			List<String> lines = result.waitAndGetLines();
			lines.forEach(line -> {
				String[] moduleAndVersion = line.split("==");
				if (moduleAndVersion.length == 2) {
					components.put(moduleAndVersion[0], moduleAndVersion[1]);
				}
			});
		}
		return components;
	}
	
	@Override
	public String getExecutable() {
		return new File(getExecutableDir() + File.separator + "pip").getAbsolutePath();
	}
	
	public String getExecutableDir() {
		return new File(virtualEnvPath, isWindows() ? "Scripts" : "bin").getAbsolutePath();
	}
	
	@Override
	public File getWorkingDirectory() {
		return new File(".");
	}
	
	protected void configureEnvironment(Map<String, String> env) {
		env.put("VIRTUAL_ENV", virtualEnvPath.getAbsolutePath());
		String path = env.getOrDefault("PATH", "");
		
		String pythonPath = virtualEnvPath.getAbsolutePath() + File.separator + (isWindows() ? "Script" : "bin"); 
		env.put("PATH", pythonPath + File.pathSeparator + path);
	}
	
	private boolean isWindows() {
		if (new File(virtualEnvPath, "Scripts/").exists()) {
			return true;
		} 
		
		return false;
	}
	
}
