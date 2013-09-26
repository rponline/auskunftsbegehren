import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.util.*;

public class SendAuskunftsbegehrenServlet extends HttpServlet
{
	private static final String baseName = "Auskunftsbegehren";
	private ResourceBundle bundle;
	private HttpSession session;

	public void init(ServletConfig config)
	{
		// TODO: don't hardcode locale
		bundle = ResourceBundle.getBundle(baseName,Locale.GERMAN);
	}

	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		// get session
		session = req.getSession();

		// get information from session
		String lastname = (String) session.getAttribute("lastname");
		String firstname = (String) session.getAttribute("firstname");
		boolean isSigned = ((Boolean) session.getAttribute("isSigned")).booleanValue();
		String filename = (String) session.getAttribute("filename");
		String mailRecipient = (String) session.getAttribute("mailRecipient");
		String mailSender = (String) session.getAttribute("mailSender");
		if(isSigned) {
			filename = (String) session.getAttribute("filenameSigned");
		}

		// generate mail content
		String text = bundle.getString("mailText");
		text += "\n\n" + bundle.getString("greeting") + ",\n";
		text += firstname + " " + lastname;
		String userFilename = "Auskunftsbegehren-"+lastname+".pdf";

		// send email
		try {
			sendMail(mailRecipient,mailSender,bundle.getString("subject"),text,filename,userFilename);
		}
		catch(MessagingException e) {
			e.printStackTrace();
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	public void sendMail(String recipient,String from,String subject,String message,String pathAttachment,String filename) throws MessagingException
	{
		MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
		mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
		CommandMap.setDefaultCommandMap(mc);

		Properties props = new Properties();
		props.put( "mail.smtp.host", "localhost" );
		Session session = Session.getDefaultInstance(props);

		Message msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);
		InternetAddress addressTo = new InternetAddress(recipient);
		msg.setRecipient(Message.RecipientType.TO,addressTo);
		msg.setSubject(subject);

        Multipart multipart = new MimeMultipart();

		BodyPart textPart = new MimeBodyPart();
		textPart.setText(message);
		multipart.addBodyPart(textPart);

		BodyPart attachmentPart = new MimeBodyPart();
		DataSource source = new FileDataSource(pathAttachment);
		attachmentPart.setDataHandler(new DataHandler(source));
		attachmentPart.setFileName(filename);
		multipart.addBodyPart(attachmentPart);

		msg.setContent(multipart);

		Transport.send(msg);
	}
}
