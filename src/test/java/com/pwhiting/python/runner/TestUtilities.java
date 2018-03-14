package com.pwhiting.python.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class TestUtilities {

	private Python python;
	private GitResource gitResource;
	
	@Before
	public void setUp() {
		File pythonWin = new File("C:\\Users\\phwhitin\\AppData\\Local\\Programs\\Python\\Python36-32\\python.exe");
		File pythonUnix = new File("/usr/bin/python");
		
		python = new Python(pythonWin.exists() ? pythonWin : pythonUnix); 
		
		File tempDir = new File(System.getProperty("java.io.tmpdir"));
		System.out.println(tempDir);
		
		File gitLocation = new File(tempDir, "library-testing/Python-Work");
		String url = "https://github.com/Himself12794/Python-Work.git";
		
		gitResource = new GitResource(gitLocation, url, "master", null);
	}
	
	@Test
	public void testGitResource() throws IOException {
		gitResource.ensureUpdated();
		assertTrue(gitResource.getLocation().exists());
	}
	
	@Test
	public void testPython() throws IOException, InterruptedException {
		System.out.println(python.getVersion());
		assertNotNull(python.getVersion());
		python.setWorkingDirectory(gitResource.getLocation());
		python.setInheritIO();
		python.executeScript("collatz.py", "100000").waitFor();
		python.executeScript("-c", "\"from __future__ import print_function; print('Hello, world!')\"").waitFor();
	}
	
}
