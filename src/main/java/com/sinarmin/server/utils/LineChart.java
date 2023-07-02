package com.sinarmin.server.utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LineChart extends JFrame {
	XYSeriesCollection xyDataset;
	public LineChart() {
		xyDataset = new XYSeriesCollection();
		initUI();
	}

	private void initUI() {
		XYDataset dataset = xyDataset;
		JFreeChart chart = createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chartPanel.setBackground(Color.white);

		add(chartPanel);

		pack();
		setTitle("Line chart");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void addSeries(String key, HashMap<Integer, Double> map) {
		var series = new XYSeries(key, true);
		for (Map.Entry<Integer, Double> x : map.entrySet()) {
			series.add(x.getKey(), x.getValue());
		}
		xyDataset.addSeries(series);
	}

	private JFreeChart createChart(final XYDataset dataset) {
		JFreeChart chart = ChartFactory.createXYLineChart(
				"Hashtag Trend last Month",
				"day(s) ago",
				"Tweets %",
				dataset,
				PlotOrientation.VERTICAL,
				true,
				true,
				false
		);

		XYPlot plot = chart.getXYPlot();

		var renderer = new XYLineAndShapeRenderer();

		renderer.setSeriesPaint(0, Color.RED);
		renderer.setSeriesStroke(0, new BasicStroke(2.0f));
		if (xyDataset.getSeriesCount() > 1) {
			renderer.setSeriesPaint(1, Color.BLUE);
			renderer.setSeriesStroke(1, new BasicStroke(2.0f));
		}
		if (xyDataset.getSeriesCount() > 2) {
			renderer.setSeriesPaint(2, Color.GREEN);
			renderer.setSeriesStroke(2, new BasicStroke(2.0f));
		}

		plot.setRenderer(renderer);
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinesVisible(false);
		plot.setDomainGridlinesVisible(false);

		chart.getLegend().setFrame(BlockBorder.NONE);

		chart.setTitle(new TextTitle("Hashtag Trend",
						new Font("Serif", Font.BOLD, 18)
				)
		);

		return chart;
	}

	public void make() {
		EventQueue.invokeLater(() -> {
			this.setVisible(true);
		});
	}
}