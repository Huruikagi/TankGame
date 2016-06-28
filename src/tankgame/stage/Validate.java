package tankgame.stage;
import java.io.*;
import javax.xml.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.*;
import org.xml.sax.*;

public class Validate {
	public static void main(String[] args) throws Exception {
		StreamSource in = new StreamSource(new File(args[0]));
		StreamSource xs = new StreamSource(new File(args[1]));

		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema sc = sf.newSchema(xs);

		Validator vl = sc.newValidator();

		try {
			vl.validate(in);
			System.out.println("妥当な文章です。");
		} catch (SAXException e) {
			System.out.println(e);
		}
	}
}