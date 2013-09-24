import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.util.TextPosition;
import java.util.GregorianCalendar;
import java.util.Calendar;

public class DINLetter
{
	private PDDocument document;
	private PDFont fontNormal = PDType1Font.TIMES_ROMAN;
	private PDFont fontBold = PDType1Font.TIMES_BOLD;
	private PDFont fontItalic = PDType1Font.TIMES_ITALIC;
	private int fontsize = 12;
	private int smallFontsize = 10;
	private int tinyFontsize = 8;
	private static final float LINESPACE = 1.1f;

	private static final int DIN_CONTENT_LEFT = 71;
	private static final int DIN_SENDER_ADDR_TOP = 751;
	private static final int DIN_RCPT_ADDR_TOP = 715;
	private static final int DIN_CONTENT_TOP = 567;

	private static final int LETTER_SUBJECT_TOP = DIN_CONTENT_TOP-25;
	private static final int LETTER_CONTENT_TOP = LETTER_SUBJECT_TOP-25;
	private static final int LETTER_DATE_LEFT = 420;

	private static final int DIN_FOLD1_TOP = 298;
	private static final int DIN_FOLD2_TOP = 595;
	private static final int DIN_FOLD_LENGTH = 14;

	private int contentHeight;
	private int greetingTop = LETTER_CONTENT_TOP;
	private int noticeTop;

	Address sender, recipient;
	String subject;
	String cityAndDate;
	String[] content;
	String[] notice;
	String[] greeting;
	boolean foldingmark;
	boolean isNoticeSet;
	
	public DINLetter() throws IOException
	{
		document = new PDDocument();
	}

	public void setSender(Address from)
	{
		Calendar calendar = new GregorianCalendar();
		this.sender = from;
		cityAndDate = from.getCity() + ", " + calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.MONTH)+1 + "." + calendar.get(Calendar.YEAR);
	}

	public void setRecipient(Address to)
	{
		this.recipient = to;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public void setContent(String text)
	{
		content = text.split("\n");
		contentHeight = (int) (content.length*fontsize*LINESPACE);
	}

	public void setNotice(String text)
	{
		isNoticeSet = true;
		notice = text.split("\n");
	}

	public void setGreeting(String greeting, Address greeter)
	{
		this.greeting = new String[2];
		this.greeting[0] = greeting + ",";
		this.greeting[1] = greeter.getFirstname() + " " + greeter.getLastname();
	}

	public void setFoldingmark(boolean active)
	{
		foldingmark = active;
	}

	public void save(String path) throws IOException, COSVisitorException
	{
		PDPage p = new PDPage(PDPage.PAGE_SIZE_A4);
		document.addPage(p);

		PDPageContentStream cStream = new PDPageContentStream(document, p);

		// draw folding lines
		if(foldingmark)
		{
			drawFoldingLines(cStream);
		}

		// write addresses
		writePDFText(cStream,DIN_SENDER_ADDR_TOP,DIN_CONTENT_LEFT,fontNormal,tinyFontsize,sender.toString());
		// TODO: underline sender address or make option for that
		writePDFText(cStream,DIN_RCPT_ADDR_TOP,DIN_CONTENT_LEFT,fontNormal,fontsize,recipient.toLines());

		// write location and date
		writePDFText(cStream, DIN_CONTENT_TOP,LETTER_DATE_LEFT,fontNormal,fontsize,cityAndDate);

		// write subject
		writePDFText(cStream, LETTER_SUBJECT_TOP,DIN_CONTENT_LEFT,fontBold,fontsize,subject);

		// write letter content
		writePDFText(cStream,LETTER_CONTENT_TOP,DIN_CONTENT_LEFT,fontNormal,fontsize,content);

		// write greeting
		greetingTop -= contentHeight+25;
		writePDFText(cStream,greetingTop,DIN_CONTENT_LEFT,fontNormal,fontsize,greeting);

		// print notice
		if(isNoticeSet)
		{
			noticeTop = greetingTop-35;
			writePDFText(cStream,noticeTop,DIN_CONTENT_LEFT,fontItalic,smallFontsize,notice);
		}

		cStream.close();

		// Save the newly created document
		document.save(path);

		// finally make sure that the document is properly
		// closed.
		document.close();
	}

	private void writePDFText(PDPageContentStream pcs, int x, int y, PDFont f, int fSize, String[] text) throws IOException
	{
		pcs.beginText();
		pcs.setFont(f,fSize);
		pcs.moveTextPositionByAmount(y,x);
		for(String line:text)
		{
			pcs.drawString(line);
			pcs.moveTextPositionByAmount(0,fSize*(-1)*LINESPACE);
		}
		pcs.endText();
	}

	private void writePDFText(PDPageContentStream pcs, int x, int y, PDFont font, int fontSize, String text) throws IOException
	{
		String[] textArray = new String[1];
		textArray[0] = text;
		writePDFText(pcs,x,y,font,fontSize,textArray);
	}

	private void drawFoldingLines(PDPageContentStream pcs) throws IOException
	{
		pcs.drawLine(0,DIN_FOLD1_TOP,DIN_FOLD_LENGTH,DIN_FOLD1_TOP);
		pcs.drawLine(0,DIN_FOLD2_TOP,DIN_FOLD_LENGTH,DIN_FOLD2_TOP);
	}
}
