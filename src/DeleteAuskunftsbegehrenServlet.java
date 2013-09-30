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
		String filename = (String) session.getAttribute("filename");
		String filenameSigned = (String) session.getAttribute("filenameSigned");
		
		// remove generated file
		if(filename != null) {
			File file = new File(filename);
			file.delete();
		}

		// remove signed file
		if(filenameSigned != null) {
			File signedFile = new File(filenameSigned);
			signedFile.delete();
		}

		// delete session
		session.invalidate();
	}
}
