package model.saving;

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

import controller.GameController;

/**
 *
 * This class is responsible for loading the game and game files. It gets and
 * file and try to parse the file in order to load the game. This class had been
 * encapsulated via private fields, setters and getters.
 *
 *
 * @author Mansour Javaher
 * @see FastLoad
 * @see FastSaving
 */
public class LoadGame {

	String fileName;

	public LoadGame(String fileName) {
		super();
		this.fileName = fileName;
	}

	public void load() {
		// TODO Auto-generated method stub

		try {
			File inputFile = new File(fileName);
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
					System.out.println("Player LocationX : " + eElement.getAttribute("CharachterID"));
					System.out.println("Player LocationY : " + eElement.getAttribute("CharachterID"));
					System.out.println("Player CharachterName : " + eElement.getAttribute("CharachterName"));
					System.out.println("Player Floor : " + eElement.getAttribute("Floor"));

				}
			}

			System.err.println(
					"========================================================================================================================================================");

			double[] tmp = new double[3];

			String expression1 = "/Players/Player/Location";
			NodeList nodeList1 = (NodeList) xPath.compile(expression1).evaluate(doc, XPathConstants.NODESET);

			for (int i = 0; i < nodeList1.getLength(); i++) {
				Node nNode = nodeList1.item(i);
				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					System.out.println("PlayerY: " + eElement.getAttribute("Y"));

					//Parsing player position from Sting to double for game.

					double x = Double.parseDouble(eElement.getAttribute("X"));
					double y = Double.parseDouble(eElement.getAttribute("Y"));

					//Setting player Position based on the loaded version
					GameController.getPlayer().setStartX(x);
					GameController.getPlayer().setStartY(y);
					GameController.getPlayer().setViewFrom(new double[] { x, y, 10 });
				}
			}

			System.out.println(GameController.getPlayerPosition());

			//double[] playerPostition = GameController.getPlayerPosition();

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
