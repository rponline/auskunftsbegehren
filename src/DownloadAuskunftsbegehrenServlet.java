import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class DownloadAuskunftsbegehrenServlet extends HttpServlet
{
	private static final int BUFSIZE = 4096;
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

		// get information from session
		Address from = (Address) session.getAttribute("fromAddress");
		String filename = (String) session.getAttribute("filename");
		String filenameSigned = (String) session.getAttribute("filenameSigned");
		
		// check for valid session data
		if(from == null || filename == null) {
			res.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED );
			return;
		}

		if(filenameSigned != null) {
			filename = filenameSigned;
		}

		// Push it to the user
		String userFilename = "Auskunftsbegehren-"+from.getLastname()+".pdf";
		try {
			downloadFile(filename,userFilename,res);
		}
		catch(IOException e) {
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
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
