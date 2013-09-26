import java.lang.Exception;
import java.util.Scanner;
import java.util.Locale;

public class Auskunftsbegehren_Client
{
	public Address readAddress(Scanner sc, String identifier)
	{
		String firstName,lastName,street,number,postalcode,city,country;

		System.out.println("Enter Details for " + identifier + " Address:\n");
		System.out.println("Firstname: ");
		firstName = sc.nextLine();
		System.out.println("Lastname: ");
		lastName = sc.nextLine();
		System.out.println("Street: ");
		street = sc.nextLine();
		System.out.println("Street number: ");
		number = sc.nextLine();
		System.out.println("Postal Code: ");
		postalcode = sc.nextLine();
		System.out.println("City: ");
		city = sc.nextLine();
		System.out.println("Country: ");
		country = sc.nextLine();
		
		return new Address(firstName,lastName,street,number,postalcode,city,country);
	}
	
	public static void main(String[] args)
	{
		//Address from = new Address("Hermann","Wilhelm","Christophgasse","13/5","1050","Wien","Österreich");
		//Address to = new Address("Maximilia","Mustermann","Sternenstraße","7a","1234","Wien","Österreich");

		Auskunftsbegehren_Client abc = new Auskunftsbegehren_Client();
		Scanner scanner = new Scanner(System.in);
		Address from = abc.readAddress(scanner,"Sender");
		Address to = abc.readAddress(scanner,"Recipient");
		System.out.println("Path for generated PDF: ");
		String path = scanner.nextLine();

		try {
			Auskunftsbegehren ab = new Auskunftsbegehren(Locale.GERMAN);
			ab.setSender(from);
			ab.setRecipient(to);
			ab.save(path);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
