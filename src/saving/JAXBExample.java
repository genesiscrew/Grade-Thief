package saving;

import characters.Player;

import java.io.File;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class JAXBExample {
	public static void main(String[] args) {

		ArrayList<Player> sample = new ArrayList<Player>();

		for (int i = 0; i < 100; i++) {

			Player customer = new Player(0,null,null,0);
//			customer.setId(i);
//			customer.setName("Mansour");
//			customer.setAge(21);
			sample.add(customer);
		}

		try {
			/// am/state-opera/home1/javahemans/workspace/grade-thief/src/saving
			File file = new File("//am//state-opera//home1//javahemans//workspace//grade-thief//src//sample.xml");

			JAXBContext jaxbContext = JAXBContext.newInstance(ArrayList.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			jaxbMarshaller.marshal(sample, file);
			jaxbMarshaller.marshal(sample, System.out);

		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}
}