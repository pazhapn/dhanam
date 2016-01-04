package dhan.frontend.server;

import static spark.Spark.externalStaticFileLocation;
import static spark.Spark.port;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.FileLoader;

import dhan.frontend.service.impl.BlogService;

public class WebConfig {
	private final BlogService service;
	private final PebbleEngine engine;
	private final Properties prop;
	
	public WebConfig(String propsFilePath) throws Exception {
		this.prop = getProperties(propsFilePath);
		externalStaticFileLocation(this.prop.getProperty("staticFolder")); // Static files
    	port(Integer.parseInt(this.prop.getProperty("port")));
    	FileLoader loader = new FileLoader();
		loader.setPrefix(this.prop.getProperty("templates"));
		loader.setSuffix(".html");
		String mode = this.prop.getProperty("mode");
		if(mode.equalsIgnoreCase("dev")){
			this.engine = new PebbleEngine.Builder().loader(loader).templateCache(null).build();
		}else{
			this.engine = new PebbleEngine.Builder().loader(loader).build();
		}
		this.service = new BlogService(this.prop.getProperty("dataFolder"), this.prop.getProperty("createDB"));
	}

	public static Properties getProperties(String propsFilePath) throws Exception{
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream(propsFilePath);
			// load a properties file
			prop.load(input);

		} finally {
			if (input != null) {
				input.close();
			}
		}
		return prop;
	}
	
	public String render(Map<String, Object> context, String peb) throws Exception{
		Writer writer = new StringWriter();
		if(context != null && !context.isEmpty())
			engine.getTemplate(peb).evaluate(writer, context);
		else
			engine.getTemplate(peb).evaluate(writer);
		return writer.toString();
	}
	
	public String render(String peb) throws Exception{
		return render(null, peb);
	}
	public PebbleEngine getEngine() {
		return engine;
	}

	public BlogService getService() {
		return service;
	}
	
}