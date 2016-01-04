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
import dhan.frontend.model.AuthorPosts;
import dhan.frontend.model.Post;
import dhan.frontend.server.WebConfig;
import spark.ModelAndView;
import spark.template.pebble.PebbleTemplateEngine;

public class AuthorRoutesHandler extends AbstractRoutesHandler{
	private static final Logger log = LoggerFactory.getLogger(AuthorRoutesHandler.class);
	
	public AuthorRoutesHandler(WebConfig webConfig) {
		super(webConfig);
	}

	public void addRoutes(){
		get("/author/home", (req, res) -> {
			Author author = getAuthenticatedAuthor(req);
			Map<String, Object> map = new HashMap<>();
			map.put("authorId", author.getAuthorId());
			AuthorPosts ap = webConfig.getService().getAuthorPosts(author.getAuthorId());
			map.put("drafts", ap.getDrafts());
			map.put("draftKeys", ap.getDrafts().keySet());
			map.put("posts", ap.getPosts());
			map.put("postKeys", ap.getPosts().keySet());
			return new ModelAndView(map, "author");
        }, new PebbleTemplateEngine(webConfig.getEngine()));
		
		get("/edit/publish/:id", (req, res) -> {
			webConfig.getService().publish(req.params(":id"));
			res.redirect("/author/home");
			halt();
			return null;
        }, new PebbleTemplateEngine(webConfig.getEngine()));
		
		post("/edit/draft", (req, res) -> {
			log.debug("post draft");
			Post post = new Post();
			try {
				MultiMap<String> params = new MultiMap<String>();
				UrlEncoded.decodeTo(req.body(), params, "UTF-8");
				BeanUtils.populate(post, params);
				webConfig.getService().savePost(post);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				halt(501);
				return null;
			}
			res.redirect("/author/home");
			halt();
			return null;
        }, new PebbleTemplateEngine(webConfig.getEngine()));
		
		get("/edit/draft/:authorId/:id", (req, res) -> {
			Author author = getAuthenticatedAuthor(req);
			Map<String, Object> map = new HashMap<>();
			Post post = webConfig.getService().getPost(req.params(":id"));
			post.setAuthorId(author.getAuthorId());
			post.setAuthorName(author.getAuthorName());
			map.put("post", post);
			return new ModelAndView(map, "edit");
        }, new PebbleTemplateEngine(webConfig.getEngine()));
		
		before("/edit/draft/:authorId/:id", (req, res) -> {
			Author authUser = getAuthenticatedAuthor(req);
			if(authUser == null) {
				res.redirect("/login");
				halt();
			}
		});
		
		before("/author/*", (req, res) -> {
			Author authUser = getAuthenticatedAuthor(req);
			if(authUser == null) {
				res.redirect("/login");
				halt();
			}
		});
	}
}
