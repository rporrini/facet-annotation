package it.disco.unimib.labeller.index;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;

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
		for(CandidateProperty label : this.objectLabels.get(triple.object().uri())){
			value += " " + label.uri();
		}		
		document.add(new Field(indexFields.literal(), value, TextField.TYPE_STORED));
		
		if(!object.isLiteral()){
			add(document, this.objectTypes.get(object.uri()), indexFields.objectType());
		}else{
			document.add(new Field(indexFields.objectType(), object.datatype().uri(), TextField.TYPE_STORED));
		}
		
		String context = "";
		List<CandidateProperty> subjectTypes = this.subjectTypes.get(subject.uri());
		add(document, subjectTypes, indexFields.subjectType());
		
		for(CandidateProperty type : subjectTypes){
			for(CandidateProperty label : this.subjectLabels.get(type.uri())){
				context += " " + label.uri();
			}
		}
		
		document.add(new Field(indexFields.context(), context, TextField.TYPE_STORED));
		document.add(new Field(indexFields.namespace(), property.namespace(), TextField.TYPE_STORED));
		document.add(new Field(indexFields.label(), property.label(), TextField.TYPE_STORED));
		
		openWriter().addDocument(document);
		return this;
	}

	private void add(Document document, List<CandidateProperty> types, String field) {
		List<CandidateProperty> toMinimize = new ArrayList<CandidateProperty>();
		for(CandidateProperty type : types){
			if(!type.uri().contains("/resource/Category:")) toMinimize.add(type);
		}
		
		for(Type minimalType : new EntityTypes(hierarchy).minimize(toMinimize.toArray(new CandidateProperty[toMinimize.size()]))){
			document.add(new Field(field, minimalType.uri(), TextField.TYPE_STORED));
		}
	}
	
	public Evidence closeWriter() throws Exception {
		openWriter().close();
		return this;
	}
	
	private synchronized IndexWriter openWriter() throws Exception{
		if(writer == null){
			writer = new IndexWriter(directory, new IndexWriterConfig(indexFields.analyzer()).setRAMBufferSizeMB(95));
		}
		return writer;
	}
}