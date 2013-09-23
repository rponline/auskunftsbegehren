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

public class AuskunftsbegehrenGenerator
{
	private PDDocument document;
	private PDFont font = PDType1Font.HELVETICA_BOLD;
	private int fontsize = 12;
	private int smallfontsize = 8;
	private float linespace = 1.3f;


	public AuskunftsbegehrenGenerator() throws IOException
	{
		// Create a new empty document
		document = new PDDocument();
	}

	public void generateLetter(Address from, Address to, String subject) throws IOException
	{
		PDPage p = new PDPage(PDPage.PAGE_SIZE_A4);
		document.addPage(p);

		PDPageContentStream contentStream = new PDPageContentStream(document, p);

		// write addresses
		writeFromAddress(contentStream,751,71,from);
		writeToAddress(contentStream,715,71,to);

		// write date and location
		writeDateCity(contentStream,567,420,from.getCity());

		// print subject
		writeSubject(contentStream,543,71,subject);

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
		pcs.setFont(font,fontsize);
		pcs.moveTextPositionByAmount(y,x);
		pcs.drawString("Betreff: " + subj);
		pcs.drawLine(y,x-5,y+260,x-5);
		pcs.endText();
	}

	public void writeFromAddress(PDPageContentStream pcs, int x, int y, Address addr) throws IOException
	{
		pcs.beginText();
		pcs.setFont(font,smallfontsize);
		pcs.moveTextPositionByAmount(y,x);
		pcs.drawString(addr.toString());
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

	public void saveTo(String path) throws IOException, COSVisitorException
	{
		// Save the newly created document
		document.save(path);

		// finally make sure that the document is properly
		// closed.
		document.close();
	}
}
