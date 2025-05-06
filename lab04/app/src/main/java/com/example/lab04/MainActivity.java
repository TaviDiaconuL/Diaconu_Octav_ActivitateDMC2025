package com.example.lab04;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.lab04.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ArrayList<Conducator> listaConducatori = new ArrayList<>();
    private ConducatorRecyclerAdapter adapter;
    private DatabaseHelper dbHelper;
    private DatabaseReference firebaseRef;

    private final ActivityResultLauncher<Intent> addConducatorLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                                Conducator conducator = result.getData().getParcelableExtra("conducator");
                                if (conducator != null) {
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
        firebaseRef = FirebaseDatabase.getInstance().getReference("conducatori");

        // Initialize adapter for RecyclerView
        adapter = new ConducatorRecyclerAdapter(listaConducatori);
        binding.recyclerViewConducatori.setAdapter(adapter);
        binding.recyclerViewConducatori.setLayoutManager(new LinearLayoutManager(this));

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

        binding.btnShowAll.setOnClickListener(v -> refreshList());

        binding.btnSearchName.setOnClickListener(v -> {
            String name = binding.etSearchName.getText().toString();
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

        binding.btnSearchExpRange.setOnClickListener(v -> {
            try {
                int minExp = Integer.parseInt(binding.etMinExp.getText().toString());
                int maxExp = Integer.parseInt(binding.etMaxExp.getText().toString());
                listaConducatori.clear();
                listaConducatori.addAll(dbHelper.getConducatoriByExperienceRange(minExp, maxExp));
                adapter.notifyDataSetChanged();
                Toast.makeText(this, "Afișare conducători cu experiență între " + minExp + " și " + maxExp + " ani", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Introduceți valori numerice valide", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnDeleteExp.setOnClickListener(v -> {
            try {
                int threshold = Integer.parseInt(binding.etDeleteExp.getText().toString());
                int rowsDeleted = dbHelper.deleteConducatoriByExperience(threshold, true);
                refreshList();
                Toast.makeText(this, rowsDeleted + " conducători șterși", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Introduceți o valoare numerică validă", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnUpdateExp.setOnClickListener(v -> {
            String letter = binding.etUpdateLetter.getText().toString();
            if (!letter.isEmpty()) {
                int rowsUpdated = dbHelper.incrementExperienceByNameStart(letter.charAt(0));
                refreshList();
                Toast.makeText(this, rowsUpdated + " conducători actualizați", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Introduceți o literă", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnViewFavorites.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
            startActivity(intent);
        });

        // Listen for Firebase changes
        setupFirebaseListener();

        // Initial list load
        refreshList();
    }

    private void refreshList() {
        listaConducatori.clear();
        listaConducatori.addAll(dbHelper.getAllConducatori());
        adapter.notifyDataSetChanged();
    }

    private void setupFirebaseListener() {
        firebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Toast.makeText(MainActivity.this, "Modificări în baza de date Firebase", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this, "Eroare Firebase: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextSettingsUtil.applyTextSettings(this, binding.getRoot());
        adapter.notifyDataSetChanged();
    }
}