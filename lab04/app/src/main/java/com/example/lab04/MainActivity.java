package com.example.lab04;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lab04.databinding.ActivityMainBinding;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ArrayList<Conducator> listaConducatori = new ArrayList<>();
    private ArrayAdapter<Conducator> adapter;

    private final ActivityResultLauncher<Intent> addConducatorLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                                Conducator conducator = result.getData().getParcelableExtra("conducator");
                                if (conducator != null) {
                                    listaConducatori.add(conducator);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

    private final ActivityResultLauncher<Intent> settingsLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            Log.d("MainActivity", "Rezultat primit: " + result.getResultCode());
                            if (result.getResultCode() == RESULT_OK) {
                                Log.d("MainActivity", "RESULT_OK detectat, aplicăm setările");
                                TextSettingsUtil.applyTextSettings(MainActivity.this, binding.getRoot());
                                adapter.notifyDataSetChanged();
                            } else {
                                Log.d("MainActivity", "Nu e RESULT_OK, cod: " + result.getResultCode());
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Aplicare setări inițiale
        TextSettingsUtil.applyTextSettings(this, binding.getRoot());

        // Configurare ListView cu adapter personalizat
        adapter = new ArrayAdapter<Conducator>(this, android.R.layout.simple_list_item_1, listaConducatori) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                SharedPreferences prefs = getSharedPreferences("SettingsPrefs", MODE_PRIVATE);
                float textSize = prefs.getFloat("textSize", 16f);
                int colorIndex = prefs.getInt("textColorIndex", 0);
                int textColor = getColorFromIndex(colorIndex);

                textView.setTextSize(textSize);
                textView.setTextColor(textColor);
                return view;
            }

            private int getColorFromIndex(int index) {
                switch (index) {
                    case 1: return Color.RED;
                    case 2: return Color.BLUE;
                    case 3: return Color.GREEN;
                    default: return Color.BLACK;
                }
            }
        };
        binding.listViewConducatori.setAdapter(adapter);

        // Click simplu pe item
        binding.listViewConducatori.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Conducator conducator = listaConducatori.get(position);
                Toast.makeText(MainActivity.this, conducator.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // Click lung pe item (salvare în favoriți)
        binding.listViewConducatori.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Conducator conducator = listaConducatori.get(position);
                saveToFavorites(conducator);
                Toast.makeText(MainActivity.this, "Adăugat la favoriți", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        // Buton adăugare
        binding.btnAddConducator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddConducatorActivity.class);
                addConducatorLauncher.launch(intent);
            }
        });

        // Buton setări
        binding.btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                settingsLauncher.launch(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume apelat, reaplicăm setările");
        TextSettingsUtil.applyTextSettings(this, binding.getRoot());
        adapter.notifyDataSetChanged();
    }

    private void saveToFavorites(Conducator conducator) {
        String fileName = "favorite.txt";
        String data = conducator.toString() + "\n";
        try (FileOutputStream fos = openFileOutput(fileName, MODE_APPEND)) {
            fos.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}