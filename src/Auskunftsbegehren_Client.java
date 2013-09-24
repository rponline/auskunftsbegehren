public class Auskunftsbegehren_Client
{
	public static void main(String[] args)
	{
		// TODO: read addresses from cli
		Address from = new Address("Hermann","Wilhelm","Christophgasse","13/5","1050","Wien","Österreich");
		Address to = new Address("Maximilia","Mustermann","Sternenstraße","7a","1234","Wien","Österreich");

		Auskunftsbegehren ab = new Auskunftsbegehren();
		ab.setSender(from);
		ab.setRecipient(to);
		ab.save("test.pdf");
	}
}
