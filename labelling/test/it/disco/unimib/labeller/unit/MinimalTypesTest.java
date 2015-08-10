package it.disco.unimib.labeller.unit;

import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import it.disco.unimib.labeller.index.MinimalTypes;
import it.disco.unimib.labeller.index.RDFResource;
import it.disco.unimib.labeller.index.Type;

import org.junit.Test;
import org.semanticweb.yars.nx.Resource;

public class MinimalTypesTest {

	@Test
	public void anEmptyTypeSetShouldBeMinimal() {
		
		Type[] types = new MinimalTypes().minimize();
		
		assertThat(types, emptyArray());
	}
	
	@Test
	public void aLonelyTypeShouldBeMinimal() {
		
		Type type = asType("http://type");
		
		Type[] types = new MinimalTypes().minimize(type);
		
		assertThat(types[0].uri(), equalTo("http://type"));
	}

	@Test
	public void aSuperTypeShouldBeExcludedFromTheMinimalTypeSet() throws Exception {
		
		Type subType = asType("http://agent");
		Type superType = asType("http://thing");
		
		superType.addSubType(subType.addSuperType(superType));
		
		Type[] minimalTypes = new MinimalTypes().minimize(subType, superType);
		
		assertThat(minimalTypes.length, equalTo(1));
		assertThat(minimalTypes[0].uri(), equalTo("http://agent"));
	}
	
	@Test
	public void aSuperTypeShouldBeExcludedFromTheMinimalTypeSetInDifferentOrdering() throws Exception {
		
		Type subType = asType("http://agent");
		Type superType = asType("http://thing");
		
		superType.addSubType(subType.addSuperType(superType));
		
		Type[] minimalTypes = new MinimalTypes().minimize(superType, subType);
		
		assertThat(minimalTypes.length, equalTo(1));
		assertThat(minimalTypes[0].uri(), equalTo("http://agent"));
	}
	
	@Test
	public void multipleSyperTypes() throws Exception {
		
		Type subType = asType("http://agent");
		Type superType = asType("http://thing");
		Type otherSuperType = asType("http://anotherThing");
		
		superType.addSubType(subType.addSuperType(superType));
		otherSuperType.addSubType(subType.addSuperType(otherSuperType));
		
		Type[] minimalTypes = new MinimalTypes().minimize(superType, subType, otherSuperType);
		
		assertThat(minimalTypes.length, equalTo(1));
		assertThat(minimalTypes[0].uri(), equalTo("http://agent"));
	}
	
	private Type asType(String uri) {
		return new Type(new RDFResource(new Resource(uri)));
	}
}
