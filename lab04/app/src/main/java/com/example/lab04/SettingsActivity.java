package com.example.lab04;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lab04.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefs = getSharedPreferences("SettingsPrefs", MODE_PRIVATE);

        // Aplicare setări text
        TextSettingsUtil.applyTextSettings(this, binding.getRoot());

        // Configurare spinner culori
        String[] colors = {"Negru", "Roșu", "Albastru", "Verde"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, colors);
        binding.spinnerTextColor.setAdapter(adapter);

        // Încărcare setări existente
        binding.etTextSize.setText(String.valueOf(prefs.getFloat("textSize", 16f)));
        binding.spinnerTextColor.setSelection(prefs.getInt("textColorIndex", 0));

        binding.btnSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float textSize;
                try {
                    textSize = Float.parseFloat(binding.etTextSize.getText().toString());
                } catch (NumberFormatException e) {
                    textSize = 16f; // Default
                }
                int colorIndex = binding.spinnerTextColor.getSelectedItemPosition();

                SharedPreferences.Editor editor = prefs.edit();
                editor.putFloat("textSize", textSize);
                editor.putInt("textColorIndex", colorIndex);
                editor.apply();

                // Reaplică setările imediat după salvare
                TextSettingsUtil.applyTextSettings(SettingsActivity.this, binding.getRoot());

                finish();
            }
        });
    }
}