package dhan.frontend.server;

import static spark.Spark.exception;
import static spark.Spark.get;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dhan.frontend.server.routes.AuthorRoutesHandler;
import dhan.frontend.server.routes.LoginRoutesHandler;
import spark.ModelAndView;
import spark.template.pebble.PebbleTemplateEngine;

public class AllRoutesHandler {
	private static final Logger log = LoggerFactory.getLogger(AllRoutesHandler.class);
	private WebConfig webConfig;
	
	public AllRoutesHandler(WebConfig webConfig){
		this.webConfig = webConfig;
	}
	
	public void setupRoutes() {

		new LoginRoutesHandler(webConfig).addRoutes();
		new AuthorRoutesHandler(webConfig).addRoutes();
		
		get("/:title", (req, res) -> {
			String id = webConfig.getService().getIdForUrl(req.params(":title"));
			return new ModelAndView(getPost(id), "post");
        }, new PebbleTemplateEngine(webConfig.getEngine()));

		get("/", (req, res) -> {
			return new ModelAndView(getPosts("1", null), "list");
        }, new PebbleTemplateEngine(webConfig.getEngine()));

		get("/page/:pageNum", (req, res) -> {
			return new ModelAndView(getPosts(req.params(":pageNum"), null), "list");
        }, new PebbleTemplateEngine(webConfig.getEngine()));

		get("/tag/:tag/:pageNum", (req, res) -> {
			return new ModelAndView(getPosts(req.params(":pageNum"), req.params(":tag")), "list");
        }, new PebbleTemplateEngine(webConfig.getEngine()));
		
		get("/throwexception", (request, response) -> {
		    throw new Exception("Server error");
		});
		
		exception(Exception.class, (e, request, response) -> {
			log.error(e.getMessage(), e);
		    response.status(404);
		    response.body("Resource not found");
		});
	}
	private Map<String, Object> getPost(String id) throws Exception{
		Map<String, Object> map = new HashMap<>();
		map.put("post", webConfig.getService().getPost(id));
		map.put("tags", webConfig.getService().getTags());
		return map;
	}
	private Map<String, Object> getPosts(String pageNum, String tag){
		Map<String, Object> map = new HashMap<>();
		map.put("posts", webConfig.getService().getPosts(pageNum, tag));
		map.put("tags", webConfig.getService().getTags());
		return map;
	}
	/*		
		get("/:title", (req, res) -> {
			Map<String, Object> map = new HashMap<>();
			String id = webConfig.getService().getIdForUrl(req.params(":title"));
			return new ModelAndView(map, "posts"+File.separator+id);
        }, new PebbleTemplateEngine(webConfig.getEngine()));
        */
}
