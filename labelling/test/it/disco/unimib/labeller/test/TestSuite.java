package it.disco.unimib.labeller.test;

import it.disco.unimib.labeller.labelling.Events;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.extensions.cpsuite.ClasspathSuite;
import org.junit.extensions.cpsuite.ClasspathSuite.BeforeSuite;
import org.junit.extensions.cpsuite.ClasspathSuite.ClassnameFilters;
import org.junit.extensions.cpsuite.ClasspathSuite.IncludeJars;
import org.junit.runner.RunWith;

@RunWith(ClasspathSuite.class)
@IncludeJars(true)
@ClassnameFilters({".*Test", "!org.jopendocument.*.*.*"})
public class TestSuite {
	
	@BeforeSuite
	public static void setLogging(){
		File logDirectory = new File("logs");
		FileUtils.deleteQuietly(logDirectory);
		logDirectory.mkdir();
		new Events();
	}
}
