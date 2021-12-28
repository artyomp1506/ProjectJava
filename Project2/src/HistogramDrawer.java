import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.util.HashMap;

public class HistogramDrawer {
    private final HashMap<String, Double> graphicsData;

    public HistogramDrawer(HashMap<String, Double> data) {
        graphicsData = data;
    }

    public void show() {
    ApplicationFrame frame=new ApplicationFrame("График");
        ChartPanel chartPanel=new ChartPanel(createChart());
        chartPanel.setPreferredSize(new Dimension(640, 480));

        frame.setContentPane(chartPanel);
        frame.setSize(1186,617);
        frame.setVisible(true);
    }


    private CategoryDataset createDataset() {
        var dataset = new DefaultCategoryDataset();
        for (var key : graphicsData.keySet()) {
            dataset.addValue(graphicsData.get(key), "Страна", key);
        }
        return dataset;
    }

    private JFreeChart createChart() {

           return ChartFactory.createBarChart(
                    "Показатели старан и щедрости",
                    "Страна",
                    "Щедрость",
                    createDataset(),
                    PlotOrientation.HORIZONTAL,
                    false, true, false);
        }

}
