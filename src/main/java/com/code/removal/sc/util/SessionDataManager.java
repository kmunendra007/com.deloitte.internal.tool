package com.code.removal.sc.util;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class SessionDataManager implements ISessionDataManager {

	@Autowired
	private HttpSession session;


	@Override
	public String init(String rootDirectory) {

		final String sessionId = UUID.randomUUID().toString();
		
		SessionModel sessionModel = new SessionModel();
		sessionModel.setRootDirectory(rootDirectory);
		sessionModel.setSourcePath(rootDirectory + "\\src\\main\\java\\");
		sessionModel.setClassPath(rootDirectory + "\\target\\classes\\");
		session.setAttribute(sessionId, sessionModel);

		return sessionId;
	}

	@Override
	public String getRootDirectory(String sessionId) {

		return getSessionModel(sessionId).getRootDirectory();
	}

	@Override
	public String getSourcePath(String sessionId) {

		return getSessionModel(sessionId).getSourcePath();
	}

	@Override
	public String getClassPath(String sessionId) {
		return getSessionModel(sessionId).getClassPath();
	}

	@Override
	public boolean destroy(String sessionId) {
		return false;
	}

	private SessionModel getSessionModel(String sessionId) {

		return (SessionModel) session.getAttribute(sessionId);
	}

}
