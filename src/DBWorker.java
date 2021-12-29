import org.sqlite.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;


public class DBWorker {
    private Connection connection;
    private String pathToDatabase;


    public DBWorker(String path) {
        try {
            DriverManager.registerDriver(new JDBC());
            pathToDatabase = String.format("jdbc:sqlite:%s", path);


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void createTables() {
        try {
            connection = DriverManager.getConnection(pathToDatabase);
            createCountriesRegionsAndRanksTable();
            createCountriesAndParametersTable();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void insertData(ArrayList<Country> countries) {
        try {
            insertCountriesAndRegions(countries);
            insertCountriesAndParameters(countries);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    public String getMinimalGenerosity() {

        try {
            var generositySet = getLowestGenerosityResult();
            var minimalGenerosity=generositySet.getString("Country");
            connection.close();
            return minimalGenerosity;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public HashMap<String, Double> getGenerosityData() {
        var data = new HashMap<String, Double>();
        try {
            var resultSet = getAllGenerosity();
            while (resultSet.next()) {
                data.put(resultSet.getString("Country"), resultSet.getDouble("Generosity"));
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    private void createCountriesRegionsAndRanksTable() {
        try {

            connection.createStatement().execute("CREATE TABLE Countries_And_Regions_And_Rank(" +
                                                 "Country VARCHAR(20) CONSTRAINT Country_PK PRIMARY KEY," +
                                                 "Region VARCHAR(20) NOT NULL)");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void createCountriesAndParametersTable() {
        try {
            var query = getQueryToCreateParametersTable();
            connection.createStatement().execute(query);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void insertCountriesAndRegions(ArrayList<Country> countries) throws SQLException {
        connection = DriverManager.getConnection(pathToDatabase);
        for (var country : countries) {
            var query = getQueryToInsertCountriesTable(country);
            connection.createStatement().execute(query);

        }
        connection.close();
    }

    private void insertCountriesAndParameters(ArrayList<Country> countries) throws SQLException {
        connection = DriverManager.getConnection(pathToDatabase);
        for (var country : countries) {
            var query = getQueryToInsertParametersTable(country);
            connection.createStatement().execute(query);
        }
        connection.close();
    }

    private String getQueryToCreateParametersTable() {

        var names = Country.getParameterKeys();
        var builder = new StringBuilder();
        builder.append("CREATE TABLE Countries_and_parameters(\n");
        builder.append("Country VARCHAR(20) CONSTRAINT Country_PK PRIMARY KEY,\n");
        for (int i = 2; i < names.size(); i++) {
            builder.append(String.format("%s DOUBLE NOT NULL", names.get(i)));
            if (i != names.size() - 1) {
                builder.append(",\n");
            }
        }
        builder.append(");\n");
        return builder.toString();
    }

    private String getQueryToInsertCountriesTable(Country country) {

        return "INSERT INTO Countries_And_Regions_And_Rank VALUES\n" +
               String.format("('%s', '%s');\n", country.getName(), country.getParameters().get("Region"));
    }


    private String getQueryToInsertParametersTable(Country country) {
        var builder = new StringBuilder();
        builder.append("INSERT INTO Countries_and_parameters VALUES");
        builder.append(String.format("('%s',", country.getName()));
        for (var key : Country.getParameterKeys()) {
            if (!key.equals("Region") && !key.equals("Country"))
                builder.append(" ").append(country.getParameters().get(key)).append(',');

        }
        builder.append(");\n");
        builder.deleteCharAt(builder.lastIndexOf(")") - 1);


        return builder.toString();
    }

    private ResultSet getLowestGenerosityResult() throws SQLException {
        var query = """
                select Country
                from Countries_and_parameters
                where Generosity =
                      (select MIN(Generosity)
                       from (select Generosity
                             from (select Countries_and_parameters.Generosity
                                   from Countries_And_Regions_And_Rank,
                                        Countries_and_parameters
                                   where Region = 'Central and Eastern Europe' OR 'Middle East and Northern Africa'
                                     and Countries_And_Regions_And_Rank.Country = Countries_and_parameters.Country)))
                """;
        connection = DriverManager.getConnection(pathToDatabase);

        return connection.createStatement().executeQuery(query);
    }

    private ResultSet getAllGenerosity() throws SQLException {
        connection=DriverManager.getConnection(pathToDatabase);

        return connection.createStatement().
                executeQuery("select Country, Generosity from Countries_and_parameters;");
    }

}






