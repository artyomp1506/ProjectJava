import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Country {
    private String name;
    private Map<String, String> parameters = new HashMap<>();
    private static ArrayList<String> parameterKeys = Parser.getHeaders();

    public String getName() {
        return name;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public static ArrayList<String> getParameterKeys() {
        return parameterKeys;
    }

    public Country(String fileLine) {
        var parameterValues = fileLine.split(",");
        name = parameterValues[0];
        for (int i = 1; i < parameterValues.length; i++) {
            parameters.put(parameterKeys.get(i), parameterValues[i]);
        }

    }
}
