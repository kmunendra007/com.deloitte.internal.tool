package com.code.removal.sc.util;

import java.io.Serializable;

public class SessionModel implements Serializable{
	
	private static final long serialVersionUID = 4270707265050135962L;
	
	private String rootDirectory;
	private String sourcePath;
	private String classPath;
	
	public String getRootDirectory() {
		return rootDirectory;
	}
	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}
	public String getSourcePath() {
		return sourcePath;
	}
	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}
	public String getClassPath() {
		return classPath;
	}
	public void setClassPath(String classPath) {
		this.classPath = classPath;
	}
	
	
}
