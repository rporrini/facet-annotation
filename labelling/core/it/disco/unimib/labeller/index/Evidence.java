package it.disco.unimib.labeller.index;

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

public class Evidence implements WriteStore{

	private ReadAndWriteStore subjectTypes;
	private ReadAndWriteStore subjectLabels;
	private ReadAndWriteStore objectTypes;
	private ReadAndWriteStore objectLabels;
	
	private IndexWriter writer;
	private Directory directory;
	private IndexFields indexFields;
	private TypeHierarchy hierarchy;

	public Evidence(Directory directory, TypeHierarchy hierarchy, EntityValues types, EntityValues labels, IndexFields fields) throws Exception {
		this.directory = directory;
		
		this.subjectTypes = new CachedStore(types, 1000);
		this.subjectLabels = new CachedStore(labels, 1000);
		
		this.objectTypes = new CachedStore(types, 1000);
		this.objectLabels = new CachedStore(labels, 1000);
		
		this.indexFields = fields;
		
		this.hierarchy = hierarchy;
	}
	
	public Evidence add(NTriple triple) throws Exception {
		RDFResource object = triple.object();
		RDFResource property = triple.property();
		RDFResource subject = triple.subject();
		
		Document document = new Document();
		
		document.add(new Field(indexFields.property(), property.uri(), TextField.TYPE_STORED));
		
		String value = object.uri().contains("http://") ? "" : triple.object().uri();
		for(CandidateResource label : this.objectLabels.get(triple.object().uri())){
			value += " " + label.id();
		}		
		document.add(new Field(indexFields.literal(), value, TextField.TYPE_STORED));
		
		if(!object.isLiteral()){
			List<CandidateResource> types = this.objectTypes.get(object.uri());
			for(Type minimalType : new EntityTypes(hierarchy).minimize(types.toArray(new CandidateResource[types.size()]))){
				document.add(new Field(indexFields.objectType(), minimalType.uri(), TextField.TYPE_STORED));
			}
		}else{
			document.add(new Field(indexFields.objectType(), object.datatype().uri(), TextField.TYPE_STORED));
		}
		
		String context = "";
		List<CandidateResource> subjectTypes = this.subjectTypes.get(subject.uri());
		for(Type minimalType : new EntityTypes(hierarchy).minimize(subjectTypes.toArray(new CandidateResource[subjectTypes.size()]))){
			document.add(new Field(indexFields.subjectType(), minimalType.uri(), TextField.TYPE_STORED));
		}		
		for(CandidateResource type : subjectTypes){
			for(CandidateResource label : this.subjectLabels.get(type.id())){
				context += " " + label.id();
			}
		}
		
		
		document.add(new Field(indexFields.context(), context, TextField.TYPE_STORED));
		document.add(new Field(indexFields.namespace(), property.namespace(), TextField.TYPE_STORED));
		document.add(new Field(indexFields.label(), property.label(), TextField.TYPE_STORED));
		
		openWriter().addDocument(document);
		return this;
	}
	
	public Evidence closeWriter() throws Exception {
		openWriter().close();
		return this;
	}
	
	private synchronized IndexWriter openWriter() throws Exception{
		if(writer == null){
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_45, indexFields.analyzer()).setRAMBufferSizeMB(95));
		}
		return writer;
	}
}