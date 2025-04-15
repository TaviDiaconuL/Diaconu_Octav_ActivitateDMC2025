// MainActivity.java
package com.example.lab04;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lab04.databinding.ActivityMainBinding;
import java.util.ArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ArrayList<Conducator> listaConducatori = new ArrayList<>();
    private ConducatorRecyclerAdapter adapter; // Change to RecyclerView adapter
    private DatabaseHelper dbHelper;
    private EditText etSearchName, etMinExp, etMaxExp, etDeleteExp, etUpdateLetter;
    private final ActivityResultLauncher<Intent> addConducatorLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                                Conducator conducator = result.getData().getParcelableExtra("conducator");
                                if (conducator != null) {
                                   // dbHelper.insertConducator(conducator);
                                    refreshList();
                                }
                            }
                        }
                    });

    private final ActivityResultLauncher<Intent> settingsLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == RESULT_OK) {
                                TextSettingsUtil.applyTextSettings(MainActivity.this, binding.getRoot());
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);

        // Initialize adapter for RecyclerView
        adapter = new ConducatorRecyclerAdapter(listaConducatori);
        binding.recyclerViewConducatori.setAdapter(adapter);
        binding.recyclerViewConducatori.setLayoutManager(new LinearLayoutManager(this));

        // Initialize additional UI elements
        etSearchName = findViewById(R.id.etSearchName);
        etMinExp = findViewById(R.id.etMinExp);
        etMaxExp = findViewById(R.id.etMaxExp);
        etDeleteExp = findViewById(R.id.etDeleteExp);
        etUpdateLetter = findViewById(R.id.etUpdateLetter);

        // Apply initial text settings
        TextSettingsUtil.applyTextSettings(this, binding.getRoot());

        // Button listeners
        binding.btnAddConducator.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddConducatorActivity.class);
            addConducatorLauncher.launch(intent);
        });

        binding.btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            settingsLauncher.launch(intent);
        });

        findViewById(R.id.btnShowAll).setOnClickListener(v -> refreshList());

        findViewById(R.id.btnSearchName).setOnClickListener(v -> {
            String name = etSearchName.getText().toString();
            if (!name.isEmpty()) {
                Conducator conducator = dbHelper.getConducatorByName(name);
                listaConducatori.clear();
                if (conducator != null) {
                    listaConducatori.add(conducator);
                    Toast.makeText(this, "Conducător găsit", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Niciun conducător găsit", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }
        });

        findViewById(R.id.btnSearchExpRange).setOnClickListener(v -> {
            try {
                int minExp = Integer.parseInt(etMinExp.getText().toString());
                int maxExp = Integer.parseInt(etMaxExp.getText().toString());
                listaConducatori.clear();
                listaConducatori.addAll(dbHelper.getConducatoriByExperienceRange(minExp, maxExp));
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Afișare conducători cu experiență între " + minExp + " și " + maxExp + " ani", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Introduceți valori numerice valide", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnDeleteExp).setOnClickListener(v -> {
            try {
                int threshold = Integer.parseInt(etDeleteExp.getText().toString());
                int rowsDeleted = dbHelper.deleteConducatoriByExperience(threshold, true);
                refreshList();
                Toast.makeText(this, rowsDeleted + " conducători șterși", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Introduceți o valoare numerică validă", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnUpdateExp).setOnClickListener(v -> {
            String letter = etUpdateLetter.getText().toString();
            if (!letter.isEmpty()) {
                int rowsUpdated = dbHelper.incrementExperienceByNameStart(letter.charAt(0));
                refreshList();
                Toast.makeText(this, rowsUpdated + " conducători actualizați", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Introduceți o literă", Toast.LENGTH_SHORT).show();
            }
        });

        // Initial list load
        refreshList();
    }

    private void refreshList() {
        Log.d("MainActivity", "Refreshing list...");
        listaConducatori.clear();
        Log.d("MainActivity", "List cleared. Size: " + listaConducatori.size());
        listaConducatori.addAll(dbHelper.getAllConducatori());
        Log.d("MainActivity", "List populated. Size: " + listaConducatori.size());
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextSettingsUtil.applyTextSettings(this, binding.getRoot());
        adapter.notifyDataSetChanged();
    }
}