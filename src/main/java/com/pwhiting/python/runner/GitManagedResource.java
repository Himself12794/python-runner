package com.pwhiting.python.runner;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface GitManagedResource {

	static final Logger LOGGER = LoggerFactory.getLogger(GitManagedResource.class);
	
	GitAuth getAuth();
	
	File getLocation();
	
	String getModuleURI();
	
	/**
	 * Clones the repository if it does not exist yet.
	 * Checks for updates using the given refspec, and pulls and merges changes if necessary.
	 * 
	 * @param refspec the commit this respository will be kept at
	 * 
	 * @throws GitAPIException
	 */
	default boolean updateModule(String refspec) throws IOException {
		UsernamePasswordCredentialsProvider userAuth = null;
		if (getAuth() != null) {
			userAuth = new UsernamePasswordCredentialsProvider(getAuth().getUsername(), getAuth().getPassword());
		}
		
		Git git;
		try {
			try {
				git = Git.open(getLocation());
			} catch (Exception e) {
				LOGGER.debug("Plugin repository doesn't exist, cloning", e);
				getLocation().getParentFile().mkdirs();
				getLocation().delete();
				
				CloneCommand clone = Git.cloneRepository();
				if (userAuth != null) {
					clone = clone.setCredentialsProvider(userAuth);
				}
				git = clone.setDirectory(getLocation()).setURI(getModuleURI()).call();
			}

			String branch = refspec;
			LOGGER.info("Ensuring plugin is up-to-date with refspec '{}'", branch);
			FetchCommand fetch = git.fetch();
			if (userAuth != null) {
				fetch = fetch.setCredentialsProvider(userAuth);
			}
			fetch.setRefSpecs(new RefSpec("refs/heads/*:refs/remotes/origin/*")).call();
			ObjectId refId = git.getRepository().resolve("refs/remotes/origin/" + branch);
			String refName = refId != null ? refId.name() : git.getRepository().resolve(branch).getName();
			git.checkout().setName(refName).setForce(true).call();
			
			LOGGER.info("Git repository is at commit {}", refName);
			
		} catch (GitAPIException ge) {
			LOGGER.error("Failed to checkout branch", ge);
			return false;
		}
		
		
		return true;
	}
	
}
