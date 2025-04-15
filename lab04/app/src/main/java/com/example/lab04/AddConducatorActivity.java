package com.example.lab04;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lab04.databinding.ActivityAddConducatorBinding;
import java.io.FileOutputStream;
import java.io.IOException;
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

        // Aplicare setări text
        TextSettingsUtil.applyTextSettings(this, binding.getRoot());

        // Configurare spinner
        ArrayAdapter<Conducator.LicenseType> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Conducator.LicenseType.values());
        binding.spinnerTipPermis.setAdapter(adapter);

        // Configurare DatePicker
        binding.etDataObtinerePermis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
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

                Conducator conducator = new Conducator(nume, arePermis, aniExperienta,
                        tipPermis, varsta, dataObtinerePermis);

                //saveToFile(conducator);
                saveToDatabase(conducator);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("conducator", conducator);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

   /* private void saveToFile(Conducator conducator) {
        String fileName = "conducatori.txt";
        String data = conducator.toString() + "\n";
        try (FileOutputStream fos = openFileOutput(fileName, MODE_APPEND)) {
            fos.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
   private void saveToDatabase(Conducator conducator) {
       DatabaseHelper dbHelper = new DatabaseHelper(this);
       dbHelper.insertConducator(conducator);
   }
}