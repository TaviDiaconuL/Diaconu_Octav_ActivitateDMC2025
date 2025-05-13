package com.example.lab11e;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText valuesInput;
    private Spinner chartTypeSpinner;
    private Button showChartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        valuesInput = findViewById(R.id.valuesInput);
        chartTypeSpinner = findViewById(R.id.chartTypeSpinner);
        showChartButton = findViewById(R.id.showChartButton);

        // Configurare Spinner
        String[] chartTypes = {"PieChart", "BarChart", "ColumnChart"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, chartTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chartTypeSpinner.setAdapter(adapter);

        // Configurare buton
        showChartButton.setOnClickListener(v -> {
            String input = valuesInput.getText().toString().trim();
            String chartType = chartTypeSpinner.getSelectedItem().toString();

            if (!input.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, ChartActivity.class);
                intent.putExtra("values", input);
                intent.putExtra("chartType", chartType);
                startActivity(intent);
            } else {
                valuesInput.setError("Introduceți valori!");
            }
        });
    }
}