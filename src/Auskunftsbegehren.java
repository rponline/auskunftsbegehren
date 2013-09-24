import java.lang.Exception;

public class Auskunftsbegehren
{
	private static final String SUBJECT = "Auskunftsbegehren gemäß DSG § 26";
	private static final String CONTENT = "Gemäß DSG 2000 begehre ich Auskunft über alle über mich gespeicherten\npersonenbezogenen Daten.\n\nDie Auskunft hat die verarbeiteten Daten, die Informationen über ihre Herkunft, allfällige\nEmpfänger oder Empfängerkreise von Übermittlungen, den Zweck der Datenverwendung\nsowie die Rechtsgrundlagen hiefür in allgemein verständlicher Form anzuführen.\n\nGemäß DSG besteht eine Verpflichtung zur Auskunft binnen 8 Wochen.";
	private static final String GREETING = "Mit freundlichen Grüßen";
	private static final String NOTICE = "Hinweis: Dieses Dokument ist (mittels Bürgerkarte) mit einer qualifizierten elektronischen Signatur\nversehen, diese ist laut SigG § 4 rechtlich einer händischen Unterschrift gleichgestellt. Diese Signatur\nkann unter anderem unter http://www.signaturpruefung.gv.at überprüft werden. Eine qualifizierte\nelektronische Signatur ist, da ich als Signator unter alleiniger Kontrolle des Schlüssels bin, ein\nIdentitätsnachweis im Sinne von DSG §26 (1)";


	public static void main(String args[])
	{
		try {
			Address from = new Address("Hermann","Wilhelm","Christophgasse","13/5","1050","Wien","Österreich");
			Address to = new Address("Maximilia","Mustermann","Sternenstraße","7a","1234","Wien","Österreich");

			DINLetter dl = new DINLetter();
			dl.setSender(from);
			dl.setRecipient(to);
			dl.setSubject(SUBJECT);
			dl.setContent(CONTENT);
			dl.setGreeting(GREETING,from);
			dl.setNotice(NOTICE);
			dl.setFoldingmark(true);
			dl.save("test.pdf");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
