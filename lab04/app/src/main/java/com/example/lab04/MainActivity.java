package com.example.lab04;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lab04.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private final ActivityResultLauncher<Intent> addConducatorLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            Log.d("MainActivity", "Rezultat primit: " + result.getResultCode());
                            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                                Conducator conducator = result.getData().getParcelableExtra("conducator");
                                if (conducator != null) {
                                    String displayText = "Nume: " + conducator.getNume() + "\n" +
                                            "Are permis: " + conducator.isArePermis() + "\n" +
                                            "Experiență: " + conducator.getAniExperienta() + " ani\n" +
                                            "Tip permis: " + conducator.getTipPermis() + "\n" +
                                            "Vârsta: " + conducator.getVarsta();
                                    binding.tvResult.setText(displayText);
                                } else {
                                    Log.d("MainActivity", "Conducator este null");
                                }
                            } else {
                                Log.d("MainActivity", "Rezultat invalid sau date null");
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
            Log.d("MainActivity", "Layout setat cu succes");

            binding.tvResult.setText("Aștept date...");

            binding.btnAddConducator.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("MainActivity", "Buton apăsat");
                    Intent intent = new Intent(MainActivity.this, AddConducatorActivity.class);
                    addConducatorLauncher.launch(intent);
                }
            });
        } catch (Exception e) {
            Log.e("MainActivity", "Eroare la inițializare: " + e.getMessage());
        }
    }
}