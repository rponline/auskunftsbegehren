public class Address
{
	private String firstname;
	private String lastname;
	private String street;
	private String number;
	private String postalcode;
	private String city;
	private String country;

	public Address(String firstname, String lastname, String street, String number, String postalcode, String city, String country)
	{
		this.firstname = firstname;
		this.lastname = lastname;
		this.street = street;
		this.number = number;
		this.postalcode = postalcode;
		this.city = city;
		this.country = country;
	}

	public String getFirstname()
	{
		return this.firstname;
	}

	public String getLastname()
	{
		return this.lastname;
	}

	public String getStreet()
	{
		return this.street;
	}

	public String getNumber()
	{
		return this.number;
	}

	public String getPostalcode()
	{
		return this.postalcode;
	}

	public String getCity()
	{
		return city;
	}

	public String getCountry()
	{
		return country;
	}

	public String[] toLines()
	{
		return new String[]{firstname + " " + lastname, street + " " + number, postalcode + " " + city, country};
	}
}
