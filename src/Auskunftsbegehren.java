import java.lang.Exception;

public class Auskunftsbegehren
{
	public static void main(String args[])
	{
		try {
			DINLetter dl = new DINLetter();
			Address from = new Address("Hermann","Wilhelm","Christophgasse","13/5","1050","Wien","Österreich");
			Address to = new Address("Maximilia","Mustermann","Sternenstraße","7a","1234","Wien","Österreich");
			String subject = "Auskunftsbegehren gemäß DSG § 26";
			dl.generateLetter(from,to,subject);
			dl.saveTo("test.pdf");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
