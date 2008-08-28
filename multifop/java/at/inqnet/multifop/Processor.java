package at.inqnet.multifop;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.sax.SAXResult;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FormattingResults;
import org.apache.fop.apps.MimeConstants;


public class Processor {
	private FopFactory fopFactory;
	private Transformer transformer;
	private Options options;
	private FormattingResults lastResults;
	
	public Processor() {
		this(null);
	}
	
	public Processor(Options opts) {
		options = opts;
		try {
			fopFactory = FopFactory.newInstance();
			transformer = TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException e) {
			Main.handleException(e, "Could not initialize XSL (Transformer)", true);
		}
	}
	
	public Options getOptions() {
		return options;
	}
	
	public FormattingResults getResults() throws IllegalStateException {
		if (lastResults == null) {
			throw new IllegalStateException("no processing done yet");
		}
		return lastResults;
	}
	
	/**
	 * Converts an FO file to a PDF file using FOP
	 *
	 * @param fo the FO file
	 * @throws IOException In case of an I/O problem
	 * @throws FOPException In case of a FOP problem
	 * @throws TransfromerException In case of a XSL problem
	 */
	public File convert(File fo) throws IOException, FOPException, TransformerException {
		File pdfFile;
		String pdfPath;
		String dirPath = options.getDestinationDirectory();
		String foPath = fo.getPath();
		
		if (dirPath == null) {
			pdfPath = foPath.substring(0, foPath.lastIndexOf('.')) + ".pdf";
		} else {
			pdfPath = foPath.substring(foPath.lastIndexOf('/'), foPath.lastIndexOf('.')) + ".pdf";
		}
		
		pdfFile = new File(dirPath, pdfPath);
		convert(fo, pdfFile);
		return pdfFile;
	}
	
	/**
	 * Converts an FO file to a PDF file using FOP
	 *
	 * @param fo the FO file
	 * @param pdf the target PDF file
	 * @throws IOException In case of an I/O problem
	 * @throws FOPException In case of a FOP problem
	 * @throws TransfromerException In case of a XSL problem
	 */
	public FormattingResults convert(File fo, File pdf) throws IOException, FOPException, TransformerException {
		OutputStream out = null;
		FormattingResults res = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(pdf));
			res = convert(new StreamSource(fo), out);
		} finally {
			if (out != null) {
				out.close();
			}
		}
		
		return res;
	}
	
	/**
	 * Converts an FO file to a PDF file using FOP
	 *
	 * @param src a StreamSource
	 * @param out a OUtputStream
	 * @throws FOPException In case of a FOP problem
	 * @throws TransfromerException In case of a XSL problem
	 */
	public FormattingResults convert(StreamSource src, OutputStream out) throws FOPException, TransformerException {
		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, fopFactory.newFOUserAgent(), out);
		transformer.transform(src, new SAXResult(fop.getDefaultHandler()));
		return lastResults = fop.getResults();
	}
	
}
