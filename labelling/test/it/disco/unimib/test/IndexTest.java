package it.disco.unimib.test;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;
import it.disco.unimib.index.Index;

import java.util.List;

import org.junit.Test;

import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;

public class IndexTest {

	@Test
	public void whenALabelIsAddedItShouldBeReturned() throws Exception {
		Index index = new Index().add(
							new Triple(NodeFactory.createURI("http://entity"), 
									  NodeFactory.createURI("http://predicate"), 
									  NodeFactory.createLiteral("the label")))
									  	   .close();
		
		List<String> labels = index.get("http://entity");
		
		assertThat(labels, hasItem("\"the label\""));
	}
}
