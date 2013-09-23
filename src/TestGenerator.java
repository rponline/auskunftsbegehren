import java.lang.Exception;

public class TestGenerator
{
	public static void main(String args[])
	{
		try {
			AuskunftsbegehrenGenerator ag = new AuskunftsbegehrenGenerator();
			Address from = new Address("Hermann","Wilhelm","Christophgasse","13/5","1050","Wien","Österreich");
			Address to = new Address("Maximilia","Mustermann","Sternenstraße","7a","1234","Wien","Österreich");
			ag.generateLetter(from,to);
			ag.saveTo("test.pdf");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
