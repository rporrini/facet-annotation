package it.disco.unimib.labeller.benchmark;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Events{
	
	static{
		PropertyConfigurator.configureAndWatch("log4j.properties");
	}
	
	public static Events simple(){
		return new Events(cleanedLogger());
	}
	
	public static Events verbose(){
		return new Events(rootLogger());
	}
	
	private Logger logger;
	
	private Events(){
		this(rootLogger());
	}
	
	private Events(Logger logger){
		this.logger = logger;
	}
	
	public void error(Object message, Exception exception){
		logger.error(message, exception);
	}

	public void debug(Object message) {
		logger.debug(message);
	}
	
	private static Logger rootLogger() {
		return Logger.getLogger("");
	}
	
	private static Logger cleanedLogger(){
		return Logger.getLogger("cleaned");
	}
}