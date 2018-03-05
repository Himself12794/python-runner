package com.pwhiting.python.runner;

import java.io.File;
import java.io.IOException;

/**
 * Used to test the application.
 * 
 * @author phwhitin
 *
 */
public class Application {

	public static void main(String[] args) throws IOException {
		File gitLocation = new File("C:\\Users\\phwhitin\\git\\python-work");
		String url = "https://github.com/Himself12794/Python-Work.git";
		
		GitResource resource = new GitResource(gitLocation, url, "master", null);
		resource.ensureUpdated();
		
		
		VirtualEnv venv = new VirtualEnv(new File("C:\\Users\\phwhitin\\workspaces\\Python\\environments\\whatever"));
		venv.setInheritIO();
		//venv.installModule("requests");
		
		Python python = new Python(venv);
		python.setInheritIO();
		python.executeScript(new File(resource.getLocation(), "collatz.py"), "10000");
	}

}
