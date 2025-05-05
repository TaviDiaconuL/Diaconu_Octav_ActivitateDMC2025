package com.example.lab10;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText editTextCity;
    private Spinner spinnerDays;
    private Button buttonFetchWeather;
    private TextView textViewResult;
    private final String API_KEY = "GORFHVMiAFs7IsAbuQSQgZQxH0JqR70y";
    private final String BASE_URL = "https://dataservice.accuweather.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextCity = findViewById(R.id.editTextCity);
        spinnerDays = findViewById(R.id.spinnerDays);
        buttonFetchWeather = findViewById(R.id.buttonFetchWeather);
        textViewResult = findViewById(R.id.textViewResult);

        // Set up spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.forecast_days, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDays.setAdapter(adapter);

        // Button click listener
        buttonFetchWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editTextCity.getText().toString().trim();
                if (!city.isEmpty()) {
                    new FetchCityKeyTask().execute(city);
                } else {
                    textViewResult.setText("Please enter a city name.");
                }
            }
        });
    }

    // AsyncTask to fetch city key
    private class FetchCityKeyTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String city = params[0];
            OkHttpClient client = new OkHttpClient();
            String url = BASE_URL + "/locations/v1/cities/search?apikey=" + API_KEY + "&q=" + city;

            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    JSONArray jsonArray = new JSONArray(jsonData);
                    if (jsonArray.length() > 0) {
                        JSONObject cityObject = jsonArray.getJSONObject(0);
                        return cityObject.getString("Key");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String cityKey) {
            if (cityKey != null) {
                textViewResult.setText("City Key: " + cityKey);
                // Fetch weather forecast based on selected days
                String selectedDays = spinnerDays.getSelectedItem().toString();
                int days = selectedDays.equals("1 day") ? 1 : selectedDays.equals("5 days") ? 5 : 10;
                new FetchWeatherTask().execute(cityKey, String.valueOf(days));
            } else {
                textViewResult.setText("City not found.");
            }
        }
    }

    // AsyncTask to fetch weather forecast
    private class FetchWeatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String cityKey = params[0];
            int days = Integer.parseInt(params[1]);
            OkHttpClient client = new OkHttpClient();
            String endpoint = days == 1 ? "daily/1day" : days == 5 ? "daily/5day" : "daily/10day";
            String url = BASE_URL + "/forecasts/v1/" + endpoint + "/" + cityKey +
                    "?apikey=" + API_KEY + "&details=true&metric=true";

            Request request = new Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    return response.body().string();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            if (jsonData != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray dailyForecasts = jsonObject.getJSONArray("DailyForecasts");
                    StringBuilder result = new StringBuilder();

                    for (int i = 0; i < dailyForecasts.length(); i++) {
                        JSONObject forecast = dailyForecasts.getJSONObject(i);
                        String date = forecast.getString("Date").substring(0, 10);
                        JSONObject temperature = forecast.getJSONObject("Temperature");
                        double minTemp = temperature.getJSONObject("Minimum").getDouble("Value");
                        double maxTemp = temperature.getJSONObject("Maximum").getDouble("Value");

                        result.append(String.format("Date: %s\nMin Temp: %.1f°C\nMax Temp: %.1f°C\n\n",
                                date, minTemp, maxTemp));
                    }
                    textViewResult.setText(result.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    textViewResult.setText("Error parsing weather data.");
                }
            } else {
                textViewResult.setText("Failed to fetch weather data.");
            }
        }
    }
}