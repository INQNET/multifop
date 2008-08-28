package at.inqnet.multifop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;


public class Options {
	private String destinationDirectory;
	private ArrayList<String> foFiles;
	
	public Options(String args[]) throws FileNotFoundException, IllegalArgumentException {
		foFiles = new ArrayList<String>(args.length);
		
		for (int i = 0; i < args.length; ++i) {
			if (args[i].charAt(0) == '-') {
				switch (args[i].charAt(1)) {
				case 'h':
					Main.usage(true);
					break;
				case 'd':
					File dir = new File(args[++i]);
					if (!dir.exists()) {
						throw new FileNotFoundException(dir.getPath());
					}
					if ((!dir.isDirectory() && !dir.mkdirs()) || !dir.canWrite()) {
						throw new IllegalArgumentException("not a writable directory: " + dir.getPath());
					}
					destinationDirectory = dir.getAbsolutePath();
					break;
				}
			} else {
				foFiles.add(args[i]);
			}
		}
		
	}
	
	public Iterator<String> getFoFiles() {
		return foFiles.iterator();
	}
	
	public String getDestinationDirectory() {
		return destinationDirectory;
	}
}
