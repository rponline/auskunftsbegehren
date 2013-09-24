import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import org.apache.pdfbox.exceptions.COSVisitorException;
import java.util.Map;

public class AuskunftsbegehrenServlet extends HttpServlet
{
	private static final int BUFSIZE = 4096;
	private String path;

	public void init(ServletConfig config)
	{
		try {
			super.init(config);
		}
		catch(ServletException e) {
			e.printStackTrace();
			// TODO: show error message
		}
		// Save Current Path
		path = getServletContext().getRealPath("") + File.separator;
	}

	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		// Default User Data
		Address from = new Address("Erika","Mustermann","Christophgasse","13/5","4321","Wien","Österreich");
		Address to = new Address("Maximilia","Musterfrau","Musterstraße","7a","1234","Wien","Österreich");

		// Read User Data from Post Data
		Map<String,String[]> params = req.getParameterMap();
		if(!params.isEmpty()) {
			from = readSenderAddress(params);
			to = readRecipientAddress(params);
		}

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

	private Address readSenderAddress(Map<String,String[]> params)
	{
		String sFirstname = params.get("sFirstname")[0];
		String sLastname = params.get("sLastname")[0];
		String sStreet = params.get("sStreet")[0];
		String sNumber= params.get("sNumber")[0];
		String sPostal= params.get("sPostal")[0];
		String sCity= params.get("sCity")[0];
		String sCountry= params.get("sCountry")[0];

		return new Address(sFirstname,sLastname,sStreet,sNumber,sPostal,sCity,sCountry);
	}

	private Address readRecipientAddress(Map<String,String[]> params)
	{
		String rFirstname = params.get("rFirstname")[0];
		String rLastname = params.get("rLastname")[0];
		String rStreet = params.get("rStreet")[0];
		String rNumber= params.get("rNumber")[0];
		String rPostal= params.get("rPostal")[0];
		String rCity= params.get("rCity")[0];
		String rCountry= params.get("rCountry")[0];

		return new Address(rFirstname,rLastname,rStreet,rNumber,rPostal,rCity,rCountry);
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
