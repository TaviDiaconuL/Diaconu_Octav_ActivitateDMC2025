package com.example.lab04;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lab04.databinding.ActivityAddConducatorBinding;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddConducatorActivity extends AppCompatActivity {
    private ActivityAddConducatorBinding binding;
    private Date dataObtinerePermis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddConducatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurare spinner
        ArrayAdapter<Conducator.LicenseType> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Conducator.LicenseType.values());
        binding.spinnerTipPermis.setAdapter(adapter);

        // Check if we're editing an existing Conducator
        Intent intent = getIntent();
        Conducator conducator = intent.getParcelableExtra("conducator");
        int position = intent.getIntExtra("position", -1);

        if (conducator != null) {
            // Populate fields with existing data
            binding.etNume.setText(conducator.getNume());
            binding.cbPermis.setChecked(conducator.isArePermis());
            binding.etExperienta.setText(String.valueOf(conducator.getAniExperienta()));
            binding.spinnerTipPermis.setSelection(adapter.getPosition(conducator.getTipPermis()));
            binding.ratingVarsta.setRating(conducator.getVarsta());
            dataObtinerePermis = conducator.getDataObtinerePermis();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            binding.etDataObtinerePermis.setText(sdf.format(dataObtinerePermis));
        }

        // Configurare DatePicker pentru data permisului
        binding.etDataObtinerePermis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                if (dataObtinerePermis != null) {
                    calendar.setTime(dataObtinerePermis);
                }
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddConducatorActivity.this,
                        (view, selectedYear, selectedMonth, selectedDay) -> {
                            Calendar selectedDate = Calendar.getInstance();
                            selectedDate.set(selectedYear, selectedMonth, selectedDay);
                            dataObtinerePermis = selectedDate.getTime();

                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            binding.etDataObtinerePermis.setText(sdf.format(dataObtinerePermis));
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        binding.btnSalveaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nume = binding.etNume.getText().toString();
                boolean arePermis = binding.cbPermis.isChecked();
                int aniExperienta;
                try {
                    aniExperienta = Integer.parseInt(binding.etExperienta.getText().toString());
                } catch (NumberFormatException e) {
                    aniExperienta = 0;
                }
                Conducator.LicenseType tipPermis = (Conducator.LicenseType)
                        binding.spinnerTipPermis.getSelectedItem();
                float varsta = binding.ratingVarsta.getRating();

                if (dataObtinerePermis == null) {
                    dataObtinerePermis = new Date();
                }

                Conducator newConducator = new Conducator(nume, arePermis, aniExperienta,
                        tipPermis, varsta, dataObtinerePermis);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("conducator", newConducator);
                if (position != -1) {
                    resultIntent.putExtra("position", position); // Pass position back for editing
                }
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}