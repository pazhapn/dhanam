package dhan.frontend.server;

import static spark.Spark.awaitInitialization;

public class App {
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
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
    }
    
    
}
