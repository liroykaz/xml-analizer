package root.analizer;

import org.apache.log4j.Logger;
import root.Item;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import root.converter.Converter;
import root.converter.JsonConverter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Lebedev
 */
public class XmlAnalizer implements Analizer {

    private static final Logger log = Logger.getLogger(XmlAnalizer.class);

    private List<Integer> numbers;
    private String fileName;
    private List<Item> items;
    private Converter converter;

    public XmlAnalizer(List<Integer> numbers, String fileName) {
        this.numbers = numbers;
        this.fileName = fileName;
        this.items = new ArrayList<>();
        this.converter = new JsonConverter();
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public String getFileName() {
        return fileName;
    }

    public List<Item> getItems() {
        return items;
    }

    @Override
    public double analizeDocument() {
        log.info("Input params: File name = " + getFileName() + " | Numbers = " + getNumbers());

        convertFile();
        Double sum = getItems().stream().mapToDouble(Item::getValue).sum();

        log.info("Sum of items = " + sum + " (rub)");

        converter.rubToEuroConvert(sum);
        System.out.println("Sum of items = " + sum + "(RUB)");

        return getItems().stream().mapToDouble(Item::getValue).sum();
    }

    private void convertFile() {
        try {
            //Path path = Paths.get(getFileName());
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;

            List<String> fileStrings = Files.lines(Paths.get(getFileName()), StandardCharsets.UTF_8).collect(Collectors.toList());

            StringBuilder sb = new StringBuilder();
            for (String string : fileStrings) {
                if (string.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
                    string += "<root>";
                }

                if (string.equals(fileStrings.get(fileStrings.size() - 1))) {
                    string += "</root>";
                }

                sb.append(string).append("\n");
            }

            builder = builderFactory.newDocumentBuilder();

            Document xmlDocument = builder.parse(new ByteArrayInputStream(sb.toString().getBytes()));
            XPath xPath = XPathFactory.newInstance().newXPath();

            String expression = generateNodeExpression();
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
            log.info("XML file is open");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node != null && node.getFirstChild() != null && node.getAttributes().getNamedItem("num") != null) {
                    Double value = parseDouble(node.getFirstChild().getNodeValue());
                    Integer num = Integer.valueOf(node.getAttributes().getNamedItem("num").getNodeValue());
                    Item item = new Item(num, value);
                    getItems().add(item);

                    log.info("Item # " + num + " | value = " + value + " | was added");
                }
            }
        } catch (ParserConfigurationException | SAXException | XPathExpressionException | IOException e) {
            log.error("Error in converting file process " + e.getMessage());
        }
    }

    private String generateNodeExpression() {
        StringBuilder sb = new StringBuilder();
        List<Integer> numbers = getNumbers();
        for (Integer number : numbers) {
            sb.append("@num = '");
            sb.append(number);
            sb.append("'");
            if (!number.equals(numbers.get(numbers.size() - 1))) {
                sb.append(" or ");
            }
        }

        String query = "//item[not(contains(@exclude,'false')) and (" + sb.toString() + ")]";

        log.info("Generated Node query '" + query + "'");

        return query;
    }

    private static Double parseDouble(String value) {
        return Double.parseDouble(value.replaceAll(",", "."));
    }
}
