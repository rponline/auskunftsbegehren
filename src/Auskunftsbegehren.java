import java.io.IOException;
import org.apache.pdfbox.exceptions.COSVisitorException;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.Locale;

public class Auskunftsbegehren
{
	private DINLetter ab;
	private Address from;
	private static final String baseName = "Auskunftsbegehren";
	private ResourceBundle bundle;
	private String participation;

	public Auskunftsbegehren(Locale targetLocale) throws IOException, MissingResourceException
	{
		ab = new DINLetter();
		bundle = ResourceBundle.getBundle(baseName,targetLocale);
	}

	public void setSender(Address from)
	{
		this.from = from;
		ab.setSender(from);
	}

	public void setRecipient(Address to)
	{
		ab.setRecipient(to);
	}

	public void setParticipation(String participation)
	{
		this.participation = participation;
	}

	public void save(String path) throws IOException, COSVisitorException
	{
		ab.setSubject(bundle.getString("subject"));
		// TODO: use participation data
		ab.setContent(bundle.getString("content"));
		ab.setGreeting(bundle.getString("greeting"),this.from);
		ab.setNotice(bundle.getString("notice"));
		ab.setFoldingmark(true);
		ab.save(path);
	}
}
