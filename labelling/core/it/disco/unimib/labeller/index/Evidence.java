package it.disco.unimib.labeller.index;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

public class Evidence implements TripleStore{

	private IndexWriter writer;
	private Directory directory;
	private EntityValues types;
	private EntityValues labels;
	private IndexFields indexFields;

	public Evidence(Directory directory, EntityValues types, EntityValues labels, IndexFields fields) throws Exception {
		this.directory = directory;
		this.types = types;
		this.labels = labels;
		this.indexFields = fields;
	}
	
	public Evidence add(NTriple triple) throws Exception {
		Document document = new Document();
		
		document.add(new Field(indexFields.property(), triple.predicate().uri(), TextField.TYPE_STORED));
		String value = triple.object().contains("http://") ? "" : triple.object();
		for(CandidateResource label : this.labels.get(triple.object(), "any")){
			value += " " + label.value();
		}		
		document.add(new Field(indexFields.literal(), value, TextField.TYPE_STORED));
		for(CandidateResource type : this.types.get(triple.object(), "any")){
			document.add(new Field(indexFields.objectType(), type.value(), TextField.TYPE_STORED));
		}
		String context = "";
		for(CandidateResource type : this.types.get(triple.subject(), "any")){
			for(CandidateResource label : this.labels.get(type.value(), "any")){
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