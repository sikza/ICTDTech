package marshallers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import userGate.classes.person;

public class UniversalMarshaller {

	public String toXML(person psn) {
		person p = psn;
		File file;
		String out = null;
		try {
			file = File.createTempFile("person", ".xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(person.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshaller.marshal(p, file);
			out = new String(Files.readAllBytes(file.toPath()));
			file.deleteOnExit();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return out;
	}

}
