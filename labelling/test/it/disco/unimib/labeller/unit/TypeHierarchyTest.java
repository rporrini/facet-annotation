package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import it.disco.unimib.labeller.index.TypeHierarchy;

import org.junit.Test;

public class TypeHierarchyTest {

	@Test
	public void onEmptyFileShouldIndexNothing() throws Exception {
		InputFileTestDouble input = new InputFileTestDouble();
		
		TypeHierarchy hierarchy = new TypeHierarchy(input);
		
		assertThat(hierarchy.getRootCategories(), empty());
	}
	
	@Test
	public void shouldIndexASingleRoot() throws Exception {
		InputFileTestDouble input = new InputFileTestDouble()
											.withLine(new TripleBuilder()
															.withObject("type")
															.asNTriple());
		
		TypeHierarchy hierarchy = new TypeHierarchy(input);
		
		assertThat(hierarchy.getRootCategories(), hasSize(1));
	}
	
	@Test
	public void shouldIndexARootWithManyLevelsOfInheritance() throws Exception {
		InputFileTestDouble input = new InputFileTestDouble()
											.withLine(new TripleBuilder()
															.withSubject("person")
															.withObject("agent")
															.asNTriple())
											.withLine(new TripleBuilder()
															.withSubject("politician")
															.withObject("person")
															.asNTriple());

		TypeHierarchy hierarchy = new TypeHierarchy(input);
		
		assertThat(hierarchy.getRootCategories(), hasSize(1));
	}
}
