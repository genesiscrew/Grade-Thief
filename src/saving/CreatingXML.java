package saving;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;

public class CreatingXML {

   public static void main(String argv[]) {

      try {
         DocumentBuilderFactory dbFactory =
         DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder =
            dbFactory.newDocumentBuilder();
         Document doc = dBuilder.newDocument();

         // root element
         Element rootElement = doc.createElement("Players");
         doc.appendChild(rootElement);

         //  Player element
         Element player = doc.createElement("Player");
         rootElement.appendChild(player);

         // setting attribute to element
         Attr attrFloor = doc.createAttribute("Floor");
         attrFloor.setValue("1");
         player.setAttributeNode(attrFloor);

         Attr charachterName = doc.createAttribute("CharachterName");
         charachterName.setValue("Mansour");
         player.setAttributeNode(charachterName);

         Attr charachterID = doc.createAttribute("CharachterID");
         charachterID.setValue("100");
         player.setAttributeNode(charachterID);

         // Location element
         Element location = doc.createElement("Location");

         Attr xAxis = doc.createAttribute("X");
         xAxis.setValue("50");
         location.setAttributeNode(xAxis);

         Attr yAxis = doc.createAttribute("Y");
         yAxis.setValue("0");
         location.setAttributeNode(yAxis);


         location.appendChild(
         doc.createTextNode("Message"));
         player.appendChild(location);


         // write the content into xml file
         TransformerFactory transformerFactory =
         TransformerFactory.newInstance();
         Transformer transformer =
         transformerFactory.newTransformer();
         DOMSource source = new DOMSource(doc);
         StreamResult result =
         new StreamResult(new File("//am//state-opera//home1//javahemans//workspace//grade-thief//src//saving//sample.xml"));
         transformer.transform(source, result);
         // Output to console for testing
         StreamResult consoleResult =
         new StreamResult(System.out);
         transformer.transform(source, consoleResult);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}