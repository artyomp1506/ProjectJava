import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Parser {
    private static final ArrayList<Country> countryList = new ArrayList<>();
    private static String fileHeaders;

    public static void loadFromFile(String pathToFile) {
        Path path = Path.of(pathToFile);
        try {
            var lines = Files.readAllLines(path);
            fileHeaders = lines.get(0);
            lines.stream().skip(1).forEach(line ->
            {
                var country = new Country(line);
                countryList.add(country);

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getHeaders() {
        var headersArray = fileHeaders.split(",");
        var headers = new ArrayList<String>();
        for (var header : headersArray) {
        var newHeader=getNewHeader(header);
        headers.add(newHeader);
        }
            return headers;
    }

    public static ArrayList<Country> getCountryList() {
        return countryList;
    }

    private static String getNewHeader(String header) {
        var newHeader = header;
        if (header.contains("(")) {
            newHeader = header.substring(0, header.indexOf("("));
        } else if (header.contains(" ")) {
            var headerWords = header.split(" ");
            newHeader = headerWords[0] + "_" + headerWords[1].toLowerCase();
        }
        return newHeader;
    }


}
