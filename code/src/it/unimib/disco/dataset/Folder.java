package it.unimib.disco.dataset;

import java.io.File;

public class Folder {

	public static Folder evaluation() throws Exception {
		return new Folder("..").child("evaluation");
	}

	private File root;
	
	private Folder(String path) {
		root = new File(path);
	}

	public Folder child(String name) throws Exception {
		return new Folder(new File(this.root, name).getCanonicalPath());
	}

	public File toFile(){
		return root;
	}
}