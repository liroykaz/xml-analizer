package root;

import org.apache.commons.lang3.StringUtils;
import root.analizer.Analizer;
import root.analizer.XmlAnalizer;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] arg) {
       // String[] args = {"test.xml", "5"};

        for (String str : arg) {
            System.out.println("Input param : " + str);
        }

        testAnalize(arg);
    }

    private static void testAnalize(String[] arg) {
        String fileName = null;
        List<Integer> numbersList = new ArrayList<>();

        String[] args = arg;
        for (String argument : args) {
            if (argument.contains(".xml")) {
                fileName = argument;
            } else if (StringUtils.isNumeric(argument)) {
                numbersList.add(Integer.valueOf(argument));
                System.out.println("number param = " + Integer.valueOf(argument));
            }
        }

        if (StringUtils.isEmpty(fileName)) {
            System.out.println("Please try again and set a fileName with extension .xml");
        }

        if (numbersList.isEmpty()) {
            System.out.println("Please try again and set a array of numbers");
        }

        if (!numbersList.isEmpty() && StringUtils.isNotEmpty(fileName)) {
            Analizer analizer = new XmlAnalizer(numbersList, fileName);

            analizer.analizeDocument();
        }
    }
}
