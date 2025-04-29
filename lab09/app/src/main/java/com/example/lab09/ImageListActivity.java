package com.example.lab09;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class ImageListActivity extends AppCompatActivity {
    ListView listView;
    List<CarItem> carItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        listView = findViewById(R.id.listView);
        carItems = getCarItems();

        CustomAdapter adapter = new CustomAdapter(this, carItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            try {
                String encodedUrl = URLEncoder.encode(carItems.get(position).getWebUrl(), "UTF-8");
                Intent intent = new Intent(ImageListActivity.this, DetailActivity.class);
                intent.putExtra("webUrl", encodedUrl);
                startActivity(intent);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        });
    }

    private List<CarItem> getCarItems() {
        List<CarItem> list = new ArrayList<>();
        list.add(new CarItem(
                "https://upload.wikimedia.org/wikipedia/commons/3/35/RO_licence_back.jpg",
                "Model permis auto - actual romania 2025",
                "https://ro.wikipedia.org/wiki/Permis_de_conducere"
        ));
        list.add(new CarItem(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d8/Romanian_driver%27s_licence_-_1970s.jpg/800px-Romanian_driver%27s_licence_-_1970s.jpg",
                "Permis conducere vechi de pe vremea lui Raposatu",
                "https://en.wikipedia.org/wiki/Driving_licence_in_Romania"
        ));
        list.add(new CarItem(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e0/Porsche_911_Dakar_at_the_2022_Greater_Los_Angeles_Auto_Show_%28cropped%29.jpg/1280px-Porsche_911_Dakar_at_the_2022_Greater_Los_Angeles_Auto_Show_%28cropped%29.jpg",
                "Porsche 911 - masina de influenceri mid budget",
                "https://ro.wikipedia.org/wiki/Porsche_911"
        ));
        list.add(new CarItem(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7c/2018_Nissan_GT-R_Premium_in_Super_Silver%2C_Front_Right%2C_10-11-2022.jpg/1920px-2018_Nissan_GT-R_Premium_in_Super_Silver%2C_Front_Right%2C_10-11-2022.jpg",
                "Nissan GT-R - japoneza corecta",
                "https://en.wikipedia.org/wiki/Nissan_GT-R"
        ));
        list.add(new CarItem(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/2/23/2018_McLaren_720S_V8_S-A_4.0.jpg/1920px-2018_McLaren_720S_V8_S-A_4.0.jpg",
                "McLaren 720S - masina de oameni f bogati",
                "https://en.wikipedia.org/wiki/McLaren_720S"
        ));
        return list;
    }
}

