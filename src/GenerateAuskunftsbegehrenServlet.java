import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import org.apache.pdfbox.exceptions.COSVisitorException;
import java.util.Map;
import java.util.Locale;

public class GenerateAuskunftsbegehrenServlet extends HttpServlet
{
	private String path;
	private HttpSession session;

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
		// get session
		session = req.getSession();

		// Default User Data
		Address from = new Address("Erika","Mustermann","Christophgasse","13/5","4321","Wien","Österreich");
		Address to = new Address("Maximilia","Musterfrau","Musterstraße","7a","1234","Wien","Österreich");

		// set encoding of request
		req.setCharacterEncoding("UTF-8");

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

		// save information to session
		session.setAttribute("lastname",from.getLastname());
		session.setAttribute("filename",realFilename);
		session.setAttribute("isSigned",new Boolean(false));
		session.setAttribute("isSent",new Boolean(false));

		// TODO: sent success
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
		Auskunftsbegehren ab = new Auskunftsbegehren(Locale.GERMAN);
		ab.setSender(from);
		ab.setRecipient(to);
		ab.save(path);
	}
}
