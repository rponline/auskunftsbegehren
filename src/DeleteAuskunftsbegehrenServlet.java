import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class DownloadAuskunftsbegehrenServlet extends HttpServlet
{
	private HttpSession session;

	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		// get session
		session = req.getSession();

		// get information from session
		String lastname = (String) session.getAttribute("lastname");
		String filename = (String) session.getAttribute("filename");
		boolean isSigned = ((Boolean) session.getAttribute("isSigned")).booleanValue();
		
		// Remove generated File
		File file = new File(realFilename);
		file.delete();

		if(isSigned) {
			String filenameSigned = (String) session.getAttribute("filenameSigned");
			file = new File(filenameSigned);
			file.delete();
		}

		// TODO: show success or fail
	}
}
