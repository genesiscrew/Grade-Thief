package saving;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LoadingXML {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			File inputFile = new File(
					"//am//state-opera//home1//javahemans//workspace//grade-thief//src//saving//sample.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;

			dBuilder = dbFactory.newDocumentBuilder();

			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			XPath xPath = XPathFactory.newInstance().newXPath();

			String expression = "/Players/Player";
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node nNode = nodeList.item(i);
				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					System.out.println("Player CharachterID : " + eElement.getAttribute("CharachterID"));
					System.out.println("Player CharachterName : " + eElement.getAttribute("CharachterName"));
					System.out.println("Player Floor : " + eElement.getAttribute("Floor"));


				}
			}

			System.out.println(
					"========================================================================================================================================================");

			String expression1 = "/Players/Player/items/item";
			NodeList nodeList1 = (NodeList) xPath.compile(expression1).evaluate(doc, XPathConstants.NODESET);

			for (int i = 0; i < nodeList1.getLength(); i++) {
				Node nNode = nodeList1.item(i);
				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					System.out.println("Item itemColor : " + eElement.getAttribute("itemColor"));
					System.out.println("Item itemHeight : " + eElement.getAttribute("itemHeight"));
					System.out.println("Item itemID : " + eElement.getAttribute("itemID"));
					System.out.println("Item itemLength : " + eElement.getAttribute("itemLength"));
					System.out.println("Item itemType : " + eElement.getAttribute("itemType"));
					System.out.println("Item itemWidth : " + eElement.getAttribute("itemWidth"));
					System.out.println("Item itemX : " + eElement.getAttribute("itemX"));
					System.out.println("Item itemY : " + eElement.getAttribute("itemY"));
					System.out.println("Item itemZ : " + eElement.getAttribute("itemZ"));

					// <item itemColor="NULL" itemHeight="NULL" itemID="NULL"
					// itemLength="NULL" itemType="NULL" itemWidth="NULL"
					// itemX="NULL" itemY="NULL" itemZ="NULL" />

					/*
					 * System.out.println("Items : " + eElement
					 * .getElementsByTagName("Items") .item(1)
					 * .getTextContent());
					 */

				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

}
