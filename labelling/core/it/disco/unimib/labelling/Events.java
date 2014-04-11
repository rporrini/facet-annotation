package it.disco.unimib.labelling;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Events{
	
	static{
		PropertyConfigurator.configureAndWatch("log4j.properties");
	}
	
	public void error(Object message, Exception exception){
		logger().error(message, exception);
	}

	
	public void info(Object message){
		logger().info(message);
	}
	
	private Logger logger() {
		return Logger.getLogger("");
	}
}