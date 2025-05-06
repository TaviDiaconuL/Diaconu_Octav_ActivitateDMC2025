package com.example.lab04;

import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Date;

public class FavoritesActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Conducator> favoriteConducatori;
    private ConducatorAdapter adapter;
    private DatabaseReference firebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        listView = findViewById(R.id.listViewFavorites);
        favoriteConducatori = new ArrayList<>();
        adapter = new ConducatorAdapter(this, favoriteConducatori);
        listView.setAdapter(adapter);

        firebaseRef = FirebaseDatabase.getInstance().getReference("conducatori");

        loadFavorites();
    }

    private void loadFavorites() {
        firebaseRef.orderByChild("favorite").equalTo(true).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                favoriteConducatori.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    String nume = data.child("nume").getValue(String.class);
                    Boolean arePermis = data.child("arePermis").getValue(Boolean.class);
                    Integer aniExperienta = data.child("aniExperienta").getValue(Integer.class);
                    String tipPermisStr = data.child("tipPermis").getValue(String.class);
                    Float varsta = data.child("varsta").getValue(Float.class);
                    Long dataObtinere = data.child("dataObtinerePermis").getValue(Long.class);

                    if (nume != null && arePermis != null && aniExperienta != null && tipPermisStr != null && varsta != null && dataObtinere != null) {
                        Conducator conducator = new Conducator(
                                nume,
                                arePermis,
                                aniExperienta,
                                Conducator.LicenseType.valueOf(tipPermisStr),
                                varsta,
                                new Date(dataObtinere)
                        );
                        favoriteConducatori.add(conducator);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
    }
}