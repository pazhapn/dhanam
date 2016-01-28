package dhan.frontend.server;

import static spark.Spark.awaitInitialization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
	private static final Logger log = LoggerFactory.getLogger(App.class);
	private WebConfig webConfig;
	private AllRoutesHandler routesHandler;
	
	public App(String mode) throws Exception{
		this.webConfig = new WebConfig(mode);
		
	}
	public void run(){
		this.routesHandler = new AllRoutesHandler(webConfig);
		this.routesHandler.setupRoutes();
		awaitInitialization();
	}
	public static void main(String[] args) {
		App app = null;
		try{
	    	app = new App(args[0]);
	    	app.run();
	    	if(log.isInfoEnabled()) log.info("server started ");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
    }
    
    
}
