package com.example.lab04;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lab04.databinding.ActivityAddConducatorBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AddConducatorActivity extends AppCompatActivity {
    private ActivityAddConducatorBinding binding;
    private Date dataObtinerePermis;
    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddConducatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase
        firebaseRef = FirebaseDatabase.getInstance().getReference("conducatori");

        // Apply text settings
        TextSettingsUtil.applyTextSettings(this, binding.getRoot());

        // Configure spinner
        ArrayAdapter<Conducator.LicenseType> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Conducator.LicenseType.values());
        binding.spinnerTipPermis.setAdapter(adapter);

        // Configure DatePicker
        binding.etDataObtinerePermis.setOnClickListener(v -> {
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
        });

        binding.btnSalveaza.setOnClickListener(v -> {
            String nume = binding.etNume.getText().toString();
            boolean arePermis = binding.cbPermis.isChecked();
            boolean disponibilOnline = binding.cbDisponibilOnline.isChecked();
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

            // Save to local database
            saveToDatabase(conducator);

            // Save to Firebase if checkbox is checked
            if (disponibilOnline) {
                saveToFirebase(conducator);
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("conducator", conducator);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void saveToDatabase(Conducator conducator) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.insertConducator(conducator);
    }

    private void saveToFirebase(Conducator conducator) {
        String key = firebaseRef.push().getKey();
        HashMap<String, Object> conducatorMap = new HashMap<>();
        conducatorMap.put("nume", conducator.getNume());
        conducatorMap.put("arePermis", conducator.isArePermis());
        conducatorMap.put("aniExperienta", conducator.getAniExperienta());
        conducatorMap.put("tipPermis", conducator.getTipPermis().toString());
        conducatorMap.put("varsta", conducator.getVarsta());
        conducatorMap.put("dataObtinerePermis", conducator.getDataObtinerePermis().getTime());
        conducatorMap.put("favorite", true); // Mark as favorite

        if (key != null) {
            firebaseRef.child(key).setValue(conducatorMap);
        }
    }
}