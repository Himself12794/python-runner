package com.pwhiting.python.runner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class TestUtilities {

	private VirtualEnv venv;
	private Python python;
	private GitResource gitResource;
	
	@Before
	public void setUp() {
		venv = new VirtualEnv(new File("C:\\Users\\phwhitin\\workspaces\\Python\\environments\\whatever")); 
		python = new Python(new File("C:\\Users\\phwhitin\\AppData\\Local\\Programs\\Python\\Python36-32\\python.exe")); 
		
		File gitLocation = new File("C:\\Users\\phwhitin\\git\\python-work");
		String url = "https://github.com/Himself12794/Python-Work.git";
		
		gitResource = new GitResource(gitLocation, url, "master", null);
	}
	
	@Test
	public void testGitResource() throws IOException {
		gitResource.ensureUpdated();
		assertTrue(gitResource.getLocation().exists());
	}
	
	@Test
	public void testVirtualEnv() {
		assertNotNull(venv.getVersion());
		venv.setInheritIO();
		venv.installModule("requests");
		venv.getInstalledComponents();
	}
	
	@Test
	public void testPython() {
		assertNotNull(python.getVersion());
	}
	
	@Test
	public void testAll() {
		
	}
	
}
