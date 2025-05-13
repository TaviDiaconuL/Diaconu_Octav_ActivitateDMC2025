package com.example.lab11e;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ChartActivity extends AppCompatActivity {

    private CustomChartView customChartView;
    private TextView chartTitle;
    private LinearLayout legendContainer;
    private int[] colors = {
            Color.parseColor("#FF5722"),
            Color.parseColor("#3F51B5"),
            Color.parseColor("#4CAF50"),
            Color.parseColor("#FFC107"),
            Color.parseColor("#E91E63")
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        customChartView = findViewById(R.id.customChartView);
        chartTitle = findViewById(R.id.chartTitle);
        legendContainer = findViewById(R.id.legendContainer);

        // Preluare date din Intent
        String valuesString = getIntent().getStringExtra("values");
        String chartType = getIntent().getStringExtra("chartType");

        // Setează titlul dinamic
        String title;
        switch (chartType) {
            case "PieChart":
                title = "Piechart";
                break;
            case "BarChart":
                title = "Barchart";
                break;
            case "ColumnChart":
                title = "Columnchart";
                break;
            default:
                title = "Grafice";
        }
        chartTitle.setText(title);

        // Convertire valori din String în float array
        String[] valuesArray = valuesString.split(",");
        float[] values = new float[valuesArray.length];
        for (int i = 0; i < valuesArray.length; i++) {
            try {
                values[i] = Float.parseFloat(valuesArray[i].trim());
            } catch (NumberFormatException e) {
                values[i] = 0f;
            }
        }

        // Adaugă legenda
        legendContainer.removeAllViews(); // Curăță legenda existentă
        for (int i = 0; i < values.length; i++) {
            TextView legendItem = new TextView(this);
            legendItem.setText("Valoare " + (i + 1) + ": " + String.format("%.1f", values[i]));
            legendItem.setTextColor(colors[i % colors.length]);
            legendItem.setTextSize(16f);
            legendItem.setPadding(8, 8, 8, 8);
            legendContainer.addView(legendItem);
        }

        // Setează datele și tipul graficului
        customChartView.setChartData(values, chartType);
    }
}