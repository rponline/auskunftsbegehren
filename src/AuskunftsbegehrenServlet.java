import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import org.apache.pdfbox.exceptions.COSVisitorException;

public class AuskunftsbegehrenServlet extends HttpServlet
{
	private static final int BUFSIZE = 4096;
	private String path;

	public void init(ServletConfig config)
	{
		// Save Current Path
		path = getServletContext().getRealPath("") + File.separator;
	}

	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		// TODO: read user data
		Address from = new Address("Hermann","Wilhelm","Christophgasse","13/5","1050","Wien","Österreich");
		Address to = new Address("Maximilia","Mustermann","Sternenstraße","7a","1234","Wien","Österreich");

		// Generate unique file name for PDF
		String realFilename = generateUniqueFilename(req.getSession());

		// Generate Auskunftsbegehren PDF
		try {
			generateAuskunftsbegehren(from,to,realFilename);
		}
		catch(COSVisitorException e) {
			e.printStackTrace();
			// TODO: show error message
		}

		// Push it to the user
		String userFilename = "Auskunftsbegehren-"+from.getLastname()+".pdf";
		downloadFile(realFilename,userFilename,res);

		// Remove generated File
		File file = new File(realFilename);
		file.delete();
	}

	private String generateUniqueFilename(HttpSession session)
	{
		return path + session.getId() + ".pdf";
	}

	private void generateAuskunftsbegehren(Address from, Address to, String path) throws IOException, COSVisitorException
	{
		Auskunftsbegehren ab = new Auskunftsbegehren();
		ab.setSender(from);
		ab.setRecipient(to);
		ab.save(path);
	}

	private void downloadFile(String path, String userFilename, HttpServletResponse res) throws IOException
	{
		File file = new File(path);
		int length = 0;
		res.setContentType("application/octet-stream");
		res.setContentLength((int)file.length());
		
		res.setHeader("Content-Disposition", "attachment; filename=\"" + userFilename + "\"");
		
		byte[] byteBuffer = new byte[BUFSIZE];
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		ServletOutputStream out = res.getOutputStream();
		
		// reads the file's bytes and writes them to the response stream
		while ((in != null) && ((length = in.read(byteBuffer)) != -1))
		{
			out.write(byteBuffer,0,length);
		}
		in.close();
		out.close();
	}
}
