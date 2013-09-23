import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.util.TextPosition;

public class AuskunftsbegehrenGenerator
{
	private PDDocument document;
	private PDFont font = PDType1Font.HELVETICA_BOLD;


	public AuskunftsbegehrenGenerator() throws IOException
	{
		// Create a new empty document
		document = new PDDocument();
	}

	public void generateLetter(Address from, Address to) throws IOException
	{
		PDPage p = new PDPage();
		document.addPage(p);

		PDPageContentStream contentStream = new PDPageContentStream(document, p);

		// TODO: make position of addresses generic
		writeAddress(contentStream,700,100,from);
		writeAddress(contentStream,500,100,to);

		contentStream.close();
	}

	public void writeAddress(PDPageContentStream pcs, int x, int y, Address addr) throws IOException
	{
		// TODO: make fontsize and linespacing generic
		int fontsize = 12;
		float linespace = 1.3f;

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
