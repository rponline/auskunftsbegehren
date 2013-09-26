import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class DeleteAuskunftsbegehrenServlet extends HttpServlet
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
		File file = new File(filename);
		file.delete();

		if(isSigned) {
			String filenameSigned = (String) session.getAttribute("filenameSigned");
			file = new File(filenameSigned);
			file.delete();
		}

		// delete session
		session.invalidate();
	}
}
