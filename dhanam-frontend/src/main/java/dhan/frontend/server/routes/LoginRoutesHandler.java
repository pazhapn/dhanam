package dhan.frontend.server.routes;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.post;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dhan.frontend.model.Author;
import dhan.frontend.model.LoginResult;
import dhan.frontend.server.WebConfig;
import spark.ModelAndView;
import spark.template.pebble.PebbleTemplateEngine;

public class LoginRoutesHandler extends AbstractRoutesHandler{
	private static final Logger log = LoggerFactory.getLogger(LoginRoutesHandler.class);
	
	public LoginRoutesHandler(WebConfig webConfig) {
		super(webConfig);
	}

	public void addRoutes(){
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
				res.redirect("/author/home");
				halt();
			} else {
				map.put("error", result.getError());
			}
			map.put("username", user.getAuthorId());
			return new ModelAndView(map, "login");
        }, new PebbleTemplateEngine(webConfig.getEngine()));
		/*
		 * Checks if the user is already authenticated
		 */
		before("/login", (req, res) -> {
			Author authUser = getAuthenticatedAuthor(req);
			if(authUser != null) {
				res.redirect("/author/home");
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
}
