package com.pwhiting.python.runner;

import java.io.File;
import java.io.IOException;

public class GitResource implements GitManagedResource {

	private File targetLocation;
	private String url;
	private String targetRefSpec;
	private GitAuth gitAuth;
	
	public GitResource(File targetLocation, String url, String targetRefSpec, GitAuth gitAuth) {
		this.targetLocation = targetLocation;
		this.url = url;
		this.targetRefSpec = targetRefSpec;
		this.gitAuth = gitAuth;
	}

	@Override
	public GitAuth getAuth() {
		return gitAuth;
	}

	@Override
	public File getLocation() {
		return targetLocation;
	}

	@Override
	public String getModuleURI() {
		return url;
	}
	
	/**
	 * Handles the cloning and updating of this resource.
	 * 
	 * @throws IOException
	 */
	public boolean ensureUpdated() throws IOException {
		return updateModule(targetRefSpec);
	}
	
}
