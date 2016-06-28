package tankgame.stage;

import java.io.*;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

public class StageValidator {
	
	private File schemaFile;
	
	public void validate(File stageFile) throws InvalidStageFileException {
		StreamSource stageSource = new StreamSource(stageFile);
		StreamSource schemaSource = new StreamSource(schemaFile);
		
		SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		
		try {
			Schema schema = sf.newSchema(schemaSource);
			Validator vl = schema.newValidator();
			vl.validate(stageSource);
		} catch (SAXException e) {
			throw new InvalidStageFileException();
		} catch (IOException e) {
		}	
	}
}