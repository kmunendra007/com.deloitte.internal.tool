package com.code.removal.sc.util;

public interface ISessionDataManager {
	
	String init(final String rootDirectory);
	
	String getRootDirectory(final String sessioId);
	
	String getSourcePath(final String sessioId);
	
	String getClassPath(final String sessioId);
	
	boolean destroy(final String sessioId);

}
