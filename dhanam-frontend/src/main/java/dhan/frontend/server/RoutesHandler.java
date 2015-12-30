package dhan.frontend.server;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.halt;
import static spark.Spark.before;
import static spark.Spark.exception;

import java.util.HashMap;
import java.util.Map;

import dhan.frontend.model.Author;
import dhan.frontend.model.AuthorPosts;
import dhan.frontend.model.LoginResult;
import dhan.frontend.model.Post;
import dhan.frontend.util.JSONUtil;
import spark.ModelAndView;
import spark.Request;
import spark.template.pebble.PebbleTemplateEngine;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoutesHandler {
	private static final Logger log = LoggerFactory.getLogger(RoutesHandler.class);
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

		this.loginRoutes();
		this.authorRoutes();
		this.postRoutes();
		get("/throwexception", (request, response) -> {
		    throw new Exception();
		});
		exception(Exception.class, (e, request, response) -> {
		    response.status(404);
		    response.body("Resource not found");
		});
	}
	
	private void postRoutes(){
		get("/:title", (req, res) -> {
			Map<String, Object> map = new HashMap<>();
			return new ModelAndView(map, req.params(":title"));
        }, new PebbleTemplateEngine(webConfig.getEngine()));
	}
	
	private void authorRoutes(){
		get("/author/home", (req, res) -> {
			Author author = getAuthenticatedAuthor(req);
			Map<String, Object> map = new HashMap<>();
			map.put("authorId", author.getAuthorId());
			AuthorPosts ap = webConfig.getService().getDraftsAndPosts(author);
			map.put("drafts", ap.getDrafts());
			map.put("posts", ap.getPosts());
			return new ModelAndView(map, "author");
        }, new PebbleTemplateEngine(webConfig.getEngine()));
		
		post("/edit/draft/", (req, res) -> {
			Map<String, Object> map = new HashMap<>();
			log.debug("entered post");
			Post post = new Post();
			try {
				MultiMap<String> params = new MultiMap<String>();
				UrlEncoded.decodeTo(req.body(), params, "UTF-8");
				BeanUtils.populate(post, params);
			} catch (Exception e) {
				halt(501);
				return null;
			}
			log.debug("entered post r {}", JSONUtil.write(post));
			res.redirect("/author/home");
			halt();
			return new ModelAndView(map, "");
        }, new PebbleTemplateEngine(webConfig.getEngine()));
		
		get("/edit/draft/:authorId/:id", (req, res) -> {
			Map<String, Object> map = new HashMap<>();
			map.put("authorId", req.params(":authorId"));
			Post post = webConfig.getService().getDraft("draft", req.params(":authorId"), req.params(":id"));
			map.put("id", post.getId());
			map.put("authorId", req.params(":authorId"));
			map.put("status", "draft");
			map.put("postDate", post.getPostDate());
			map.put("title", post.getTitle());
			map.put("tags", post.getTags());
			map.put("initialParas", post.getInitialParas());
			map.put("remainingParas", post.getRemainingParas());
			return new ModelAndView(map, "edit");
        }, new PebbleTemplateEngine(webConfig.getEngine()));
		
		before("/author/*", (req, res) -> {
			Author authUser = getAuthenticatedAuthor(req);
			if(authUser == null) {
				res.redirect("/login");
				halt();
			}
		});
	}
	//TODO login failed message
	private void loginRoutes(){
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
