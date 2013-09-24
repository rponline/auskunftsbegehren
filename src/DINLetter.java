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
	private PDFont font = PDType1Font.TIMES_ROMAN;
	private PDFont fontBold = PDType1Font.TIMES_BOLD;
	private PDFont fontItalic = PDType1Font.TIMES_ITALIC;
	private int fontsize = 12;
	private int smallFontsize = 10;
	private int tinyFontsize = 8;
	private float linespace = 1.1f;

	private static final int DIN_CONTENT_LEFT = 71;
	private static final int DIN_SENDER_ADDR_TOP = 751;
	private static final int DIN_RCPT_ADDR_TOP = 715;
	private static final int DIN_CONTENT_TOP = 567;

	private static final int LETTER_SUBJECT_TOP = DIN_CONTENT_TOP-25;
	private static final int LETTER_CONTENT_TOP = LETTER_SUBJECT_TOP-25;

	private static final int DIN_FOLD1_TOP = 298;
	private static final int DIN_FOLD2_TOP = 595;
	private static final int DIN_FOLD_LENGTH = 14;

	private int contentHeight;
	private int greetingTop = LETTER_CONTENT_TOP;
	private int noticeTop;

	Address sender, recipient;
	String Subject;
	String[] content;
	String[] notice;
	String[] greeting;
	
	public DINLetter() throws IOException
	{
		document = new PDDocument();
	}

	public void generateLetter(Address from, Address to, String subject) throws IOException
	{
		PDPage p = new PDPage(PDPage.PAGE_SIZE_A4);
		document.addPage(p);

		PDPageContentStream contentStream = new PDPageContentStream(document, p);

		// draw folding lines
		drawFoldingLines(contentStream);

		// write addresses
		writeFromAddress(contentStream,DIN_SENDER_ADDR_TOP,DIN_CONTENT_LEFT,from);
		writeToAddress(contentStream,DIN_RCPT_ADDR_TOP,DIN_CONTENT_LEFT,to);

		// write date and location
		writeDateCity(contentStream,DIN_CONTENT_TOP,420,from.getCity());

		// print subject
		writeSubject(contentStream,LETTER_SUBJECT_TOP,DIN_CONTENT_LEFT,subject);

		writeText(contentStream,LETTER_CONTENT_TOP,DIN_CONTENT_LEFT);

		// print greeting
		greetingTop -= contentHeight+25;
		writeGreeting(contentStream,greetingTop,DIN_CONTENT_LEFT,from);

		// print notice
		noticeTop = greetingTop-35;
		writeNotice(contentStream,noticeTop,DIN_CONTENT_LEFT);

		contentStream.close();
	}

	public void writeDateCity(PDPageContentStream pcs, int x, int y, String city) throws IOException
	{
		Calendar calendar = new GregorianCalendar();
		pcs.beginText();
		pcs.setFont(font,fontsize);
		pcs.moveTextPositionByAmount(y,x);
		pcs.drawString(city + ", " + calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.YEAR));
		pcs.endText();
	}

	public void writeSubject(PDPageContentStream pcs, int x, int y, String subj) throws IOException
	{
		pcs.beginText();
		pcs.setFont(fontBold,fontsize);
		pcs.moveTextPositionByAmount(y,x);
		// TODO: subject name should not be hard coded
		pcs.drawString("Betrifft: " + subj);
		// TODO: line beneath subject should not be of hardcoded length
		//pcs.drawLine(y,x-5,y+240,x-5);
		pcs.endText();
	}

	public void writeFromAddress(PDPageContentStream pcs, int x, int y, Address addr) throws IOException
	{
		pcs.beginText();
		pcs.setFont(font,tinyFontsize);
		pcs.moveTextPositionByAmount(y,x);
		pcs.drawString(addr.toString());
		// TODO: make line under address generic
		pcs.drawLine(y,x-2,y+240,x-2);
		pcs.endText();
	}

	public void writeToAddress(PDPageContentStream pcs, int x, int y, Address addr) throws IOException
	{
		pcs.beginText();
		pcs.setFont(font,fontsize);
		pcs.moveTextPositionByAmount(y,x);
		for(String line:addr.toLines())
		{
			pcs.drawString(line);
			pcs.moveTextPositionByAmount(0,fontsize*(-1)*linespace);
		}
		pcs.endText();
	}

	private void drawFoldingLines(PDPageContentStream pcs) throws IOException
	{
		pcs.drawLine(0,DIN_FOLD1_TOP,DIN_FOLD_LENGTH,DIN_FOLD1_TOP);
		pcs.drawLine(0,DIN_FOLD2_TOP,DIN_FOLD_LENGTH,DIN_FOLD2_TOP);
	}

	private void writeText(PDPageContentStream pcs, int x, int y) throws IOException
	{
		String contentText = "Gemäß DSG 2000 begehre ich Auskunft über alle über mich gespeicherten\npersonenbezogenen Daten.\n\nDie Auskunft hat die verarbeiteten Daten, die Informationen über ihre Herkunft, allfällige\nEmpfänger oder Empfängerkreise von Übermittlungen, den Zweck der Datenverwendung\nsowie die Rechtsgrundlagen hiefür in allgemein verständlicher Form anzuführen.\n\nGemäß DSG besteht eine Verpflichtung zur Auskunft binnen 8 Wochen.";

		String content[] = contentText.split("\n");

		contentHeight = (int) (content.length*fontsize*linespace);
		pcs.beginText();
		pcs.setFont(font,fontsize);
		pcs.moveTextPositionByAmount(y,x);
		for(String line:content)
		{
			pcs.drawString(line);
			pcs.moveTextPositionByAmount(0,fontsize*(-1)*linespace);
		}
		pcs.endText();
	}

	private void writeGreeting(PDPageContentStream pcs, int x, int y, Address from) throws IOException
	{
		pcs.beginText();
		pcs.setFont(font,fontsize);
		pcs.moveTextPositionByAmount(y,x);
		// TODO: make greeting generic and not hard coded
		pcs.drawString("Mit freundlichen Grüßen,");
		pcs.moveTextPositionByAmount(0,fontsize*(-1)*linespace);
		pcs.drawString(from.getFirstname() + " " + from.getLastname());
		pcs.endText();
	}

	private void writeNotice(PDPageContentStream pcs, int x, int y) throws IOException
	{
		// TODO: make this notice language independant
		String noticeText = "Hinweis: Dieses Dokument ist (mittels Bürgerkarte) mit einer qualifizierten elektronischen Signatur\nversehen, diese ist laut SigG § 4 rechtlich einer händischen Unterschrift gleichgestellt. Diese Signatur\nkann unter anderem unter http://www.signaturpruefung.gv.at überprüft werden. Eine qualifizierte\nelektronische Signatur ist, da ich als Signator unter alleiniger Kontrolle des Schlüssels bin, ein\nIdentitätsnachweis im Sinne von DSG §26 (1)";

		String notice[] = noticeText.split("\n");

		pcs.beginText();
		pcs.setFont(fontItalic,smallFontsize);
		pcs.moveTextPositionByAmount(y,x);
		for(String line:notice)
		{
			pcs.drawString(line);
			pcs.moveTextPositionByAmount(0,smallFontsize*(-1)*linespace);
		}
		pcs.endText();
	}

	public void saveTo(String path) throws IOException, COSVisitorException
	{
		// Save the newly created document
		document.save(path);

		// finally make sure that the document is properly
		// closed.
		document.close();
	}

}
