package dhan.frontend.server;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.halt;
import static spark.Spark.before;
import static spark.Spark.exception;

import java.util.HashMap;
import java.util.Map;

import dhan.frontend.model.Author;
import dhan.frontend.model.LoginResult;
import spark.ModelAndView;
import spark.Request;
import spark.template.pebble.PebbleTemplateEngine;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;

public class RoutesHandler {
	private static final String AUTHOR_SESSION_ID = "a";
	private WebConfig webConfig;
	
	public RoutesHandler(WebConfig webConfig){
		this.webConfig = webConfig;
	}
	
	public void setupRoutes() {
		get("/", (req, res) -> {
			Map<String, Object> map = new HashMap<>();
			return new ModelAndView(map, "base");
        }, new PebbleTemplateEngine(webConfig.getEngine()));

		get("/:title", (req, res) -> {
			Map<String, Object> map = new HashMap<>();
			return new ModelAndView(map, req.params(":title"));
        }, new PebbleTemplateEngine(webConfig.getEngine()));
		
		get("/throwexception", (request, response) -> {
		    throw new Exception();
		});

		exception(Exception.class, (e, request, response) -> {
		    response.status(404);
		    response.body("Resource not found");
		});
	}
	
	private void authRoutes(){
		get("/login", (req, res) -> {
			return new ModelAndView(new HashMap<>(), "login");
        }, new PebbleTemplateEngine(webConfig.getEngine()));
		/*
		 * Logs the user in.
		 */
		post("/login", (req, res) -> {
			Map<String, Object> map = new HashMap<>();
			Author user = new Author();
			try {
				MultiMap<String> params = new MultiMap<String>();
				UrlEncoded.decodeTo(req.body(), params, "UTF-8");
				BeanUtils.populate(user, params);
			} catch (Exception e) {
				halt(501);
				return null;
			}
			LoginResult result = webConfig.getService().checkAuthor(user);
			if(result.getAuthor() != null) {
				addAuthenticatedAuthor(req, result.getAuthor());
				res.redirect("/");
				halt();
			} else {
				map.put("error", result.getError());
			}
			map.put("username", user.getAuthorId());
			return new ModelAndView(map, "login.ftl");
        }, new PebbleTemplateEngine(webConfig.getEngine()));
		/*
		 * Checks if the user is already authenticated
		 */
		before("/login", (req, res) -> {
			Author authUser = getAuthenticatedAuthor(req);
			if(authUser != null) {
				res.redirect("/");
				halt();
			}
		});
		/*
		 * Logs the user out and redirects to the public timeline
		 */
		get("/logout", (req, res) -> {
			removeAuthenticatedAuthor(req);
			res.redirect("/");
			return null;
        });
	}
	private void addAuthenticatedAuthor(Request request, Author u) {
		request.session().attribute(AUTHOR_SESSION_ID, u);
		
	}

	private void removeAuthenticatedAuthor(Request request) {
		request.session().removeAttribute(AUTHOR_SESSION_ID);
		
	}

	private Author getAuthenticatedAuthor(Request request) {
		return request.session().attribute(AUTHOR_SESSION_ID);
	}
}
