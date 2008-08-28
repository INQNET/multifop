package at.inqnet.multifop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.NoOpLog;
import org.apache.fop.apps.FormattingResults;
import org.apache.fop.Version;

public class Main {

	/**
	 * @param args command line options
	 */
	public static void main(String[] args) {
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", NoOpLog.class.getName());
		

		if (args.length == 0) {
			usage(true);
		} else {
			Options opt;
			Iterator<String> fos;
			Processor prc;
			
			try {
				opt = new Options(args);
				prc = new Processor(opt);
				fos = opt.getFoFiles();
				
				while (fos.hasNext()) {
					String foPath = fos.next();
					
					try {
						handleResult(prc, foPath);
					} catch (Exception e) {
						handleException(e, foPath, false);
					}
				}
			} catch (FileNotFoundException e) {
				handleException(e, -1);
			} catch (IllegalArgumentException e) {
				usage(false);
				handleException(e, -1);
			}
		}
	}

	public static void usage(boolean exit) {
		System.out.println("multifop using org.apache.fop v" + Version.getVersion());
		System.out.println();
		System.out.println("Usage:");
		System.out.println("\tmultifop [-d pdf destination directory] <xml.fo> [<xml.fo> ...]");
		System.out.println();
		
		if (exit) {
			System.exit(0);
		}
	}
	
	public static void handleResult(Processor prc, String fo) throws Exception {
		FormattingResults res;
		String pdf;
		
		pdf = prc.convert(new File(fo)).getPath();
		res = prc.getResults();
		
		System.err.println("SUCCESS: " + fo + " => " + res.getPageCount() + " Page(s) " + pdf);
	}

	public static void handleException(Exception e) {
		handleException(e, null, false, 0);
	}
	
	public static void handleException(Exception e, String msg, boolean exit) {
		handleException(e, msg, exit, -1);
	}
	
	public static void handleException(Exception e, int code) {
		handleException(e, null, true, code);
	}
	
	public static void handleException(Exception e, String msg, boolean exit, int code) {
		String message = e.getMessage();
		
		System.err.print("FAILURE: ");
		if ((message != null) || (msg != null)) {
			if (msg != null) {
				System.err.print(msg);
			}
			if (msg != null && message != null) {
				System.err.print(" ");
			}
			if (message != null) {
				System.err.print(message);
			}
		} else {
			System.err.print(e.getClass().getName());
		}
		System.err.println();
		
		// e.printStackTrace(System.err);
		
		if (exit) {
			System.exit(code);
		}
	}
}
