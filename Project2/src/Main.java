public class Main {
    public static void main(String[] args) {
        Parser.loadFromFile("X:\\Project\\csv.csv");
        var dbw=new DBWorker("test5.db");
        dbw.createTables();
        dbw.insertData(Parser.getCountryList());
        System.out.println(dbw.getMinimalGenerosity());
       var drawer=new HistogramDrawer(dbw.getGenerosityData());
       drawer.show();
    }
}
