package com.example.lab04;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lab04.databinding.ActivityMainBinding;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ArrayList<Conducator> listaConducatori = new ArrayList<>();
    private ConducatorAdapter adapter; // Updated to use custom adapter

    private final ActivityResultLauncher<Intent> addConducatorLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                                Conducator conducator = result.getData().getParcelableExtra("conducator");
                                int position = result.getData().getIntExtra("position", -1);
                                if (conducator != null) {
                                    if (position == -1) {
                                        // Add new item
                                        listaConducatori.add(conducator);
                                    } else {
                                        // Update existing item
                                        listaConducatori.set(position, conducator);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurare ListView
        adapter = new ConducatorAdapter(this, listaConducatori);
        binding.listViewConducatori.setAdapter(adapter);

        // Click simplu pe item pentru editare
        binding.listViewConducatori.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Conducator conducator = listaConducatori.get(position);
                Intent intent = new Intent(MainActivity.this, AddConducatorActivity.class);
                intent.putExtra("conducator", conducator);
                intent.putExtra("position", position); // Pass the position to identify the object
                addConducatorLauncher.launch(intent);
            }
        });

        // Click lung pe item (ștergere)
        binding.listViewConducatori.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                listaConducatori.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Conducător șters", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        // Buton adăugare
        binding.btnAddConducator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddConducatorActivity.class);
                addConducatorLauncher.launch(intent); // No position means adding a new item
            }
        });
    }
}