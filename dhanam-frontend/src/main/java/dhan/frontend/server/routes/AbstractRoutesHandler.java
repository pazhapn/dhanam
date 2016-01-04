package dhan.frontend.server.routes;

import dhan.frontend.model.Author;
import dhan.frontend.server.WebConfig;
import spark.Request;

public abstract class AbstractRoutesHandler {
	private static final String AUTHOR_SESSION_ID = "a";
	protected WebConfig webConfig;
	
	public abstract void addRoutes();
	
	public AbstractRoutesHandler(WebConfig webConfig){
		this.webConfig = webConfig;
	}
	
	protected void addAuthenticatedAuthor(Request request, Author u) {
		request.session().attribute(AUTHOR_SESSION_ID, u);		
	}

	protected void removeAuthenticatedAuthor(Request request) {
		request.session().removeAttribute(AUTHOR_SESSION_ID);		
	}

	protected Author getAuthenticatedAuthor(Request request) {
		return request.session().attribute(AUTHOR_SESSION_ID);
	}
}
