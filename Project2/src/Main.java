public class Main {
    public static void main(String[] args) {
        Parser.loadFromFile("путь до csv файла");
        var dbw=new DBWorker("имя файла файла БД");
        dbw.createTables();
        dbw.insertData(Parser.getCountryList());
        System.out.println(dbw.getMinimalGenerosity());
       var drawer=new HistogramDrawer(dbw.getGenerosityData());
       drawer.show();
    }
}
