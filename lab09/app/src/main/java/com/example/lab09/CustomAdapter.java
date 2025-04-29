package com.example.lab09;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private List<CarItem> items;
    private LayoutInflater inflater;
    private ExecutorService executorService = Executors.newFixedThreadPool(4);
    private Map<String, Bitmap> imageCache = new HashMap<>();

    public CustomAdapter(Context context, List<CarItem> items) {
        this.context = context;
        this.items = items;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        ImageView imageView = view.findViewById(R.id.imageView);
        TextView textView = view.findViewById(R.id.textView);

        CarItem item = items.get(position);
        textView.setText(item.getDescription());

        if (imageCache.containsKey(item.getImageUrl())) {
            imageView.setImageBitmap(imageCache.get(item.getImageUrl()));
        } else {
            executorService.execute(() -> {
                try {
                    Log.d("CustomAdapter", "Loading image: " + item.getImageUrl());
                    URL url = new URL(item.getImageUrl());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36");
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream in = connection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    in.close();
                    connection.disconnect();
                    if (bitmap != null) {
                        imageCache.put(item.getImageUrl(), bitmap);
                        ((Activity) context).runOnUiThread(() -> {
                            imageView.setImageBitmap(bitmap);
                            Log.d("CustomAdapter", "Image loaded for: " + item.getDescription());
                        });
                    } else {
                        Log.e("CustomAdapter", "Failed to decode bitmap for: " + item.getImageUrl());
                    }
                } catch (Exception e) {
                    Log.e("CustomAdapter", "Error loading image: " + item.getImageUrl(), e);
                }
            });
        }

        return view;
    }
}