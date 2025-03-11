package com.example.lab04;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lab04.databinding.ActivityAddConducatorBinding;

public class AddConducatorActivity extends AppCompatActivity {
    private ActivityAddConducatorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddConducatorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurare spinner
        ArrayAdapter<Conducator.LicenseType> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                Conducator.LicenseType.values());
        binding.spinnerTipPermis.setAdapter(adapter);

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

                Conducator conducator = new Conducator(nume, arePermis, aniExperienta,
                        tipPermis, varsta);

                Intent resultIntent = new Intent();
                resultIntent.putExtra("conducator", conducator);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}