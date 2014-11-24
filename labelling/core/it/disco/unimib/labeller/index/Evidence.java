package it.disco.unimib.labeller.index;

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

	public Evidence(Directory directory, EntityValues types, EntityValues labels, IndexFields fields) throws Exception {
		this.directory = directory;
		
		this.subjectTypes = new CachedStore(types, 1000);
		this.subjectLabels = new CachedStore(labels, 1000);
		
		this.objectTypes = new CachedStore(types, 1000);
		this.objectLabels = new CachedStore(labels, 1000);
		
		this.indexFields = fields;
	}
	
	public Evidence add(NTriple triple) throws Exception {
		Document document = new Document();
		
		document.add(new Field(indexFields.property(), triple.predicate().uri(), TextField.TYPE_STORED));
		String value = triple.object().contains("http://") ? "" : triple.object();
		for(CandidateResource label : this.objectLabels.get(triple.object())){
			value += " " + label.value();
		}		
		document.add(new Field(indexFields.literal(), value, TextField.TYPE_STORED));
		for(CandidateResource type : this.objectTypes.get(triple.object())){
			document.add(new Field(indexFields.objectType(), type.value(), TextField.TYPE_STORED));
		}
		String context = "";
		for(CandidateResource type : this.subjectTypes.get(triple.subject())){
			for(CandidateResource label : this.subjectLabels.get(type.value())){
				context += " " + label.value();
			}
			document.add(new Field(indexFields.subjectType(), type.value(), TextField.TYPE_STORED));
		}
		document.add(new Field(indexFields.context(), context, TextField.TYPE_STORED));
		document.add(new Field(indexFields.namespace(), triple.predicate().namespace(), TextField.TYPE_STORED));
		document.add(new Field(indexFields.label(), triple.predicate().label(), TextField.TYPE_STORED));
		
		openWriter().addDocument(document);
		return this;
	}
	
	public Evidence closeWriter() throws Exception {
		openWriter().close();
		return this;
	}
	
	private synchronized IndexWriter openWriter() throws Exception{
		if(writer == null){
			writer = new IndexWriter(directory, new IndexWriterConfig(Version.LUCENE_45, indexFields.analyzer())
																					.setRAMBufferSizeMB(95));
		}
		return writer;
	}
}