public class Address
{
	private String firstname;
	private String lastname;
	private String company;
	private String street;
	private String number;
	private String postalcode;
	private String city;
	private String country;
	private String mailAddress;

	public Address(String firstname, String lastname, String company, String street, String number, String postalcode, String city, String country, String mail)
	{
		this.firstname = firstname;
		this.lastname = lastname;
		this.company = company;
		this.street = street;
		this.number = number;
		this.postalcode = postalcode;
		this.city = city;
		this.country = country;
		this.mailAddress = mail;
	}

	public String getFirstname()
	{
		return this.firstname;
	}

	public String getLastname()
	{
		return this.lastname;
	}

	public String getCompany()
	{
		return this.company;
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

	public String getMailAddress()
	{
		return this.mailAddress;
	}

	public String[] toLines()
	{
		return new String[]{firstname + " " + lastname, street + " " + number, postalcode + " " + city, country};
	}

	public String toString()
	{
		return firstname + " " + lastname + ", " + street + " " + number + ", " + postalcode + " " + city + ", " + country;
	}
}
