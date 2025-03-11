package com.example.lab3var2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ThirdActivity extends AppCompatActivity {
    private static final String TAG = "ThirdActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Bundle extras = getIntent().getExtras();
        String message = extras != null ? extras.getString("MESSAGE", "No message") : "No message";
        int value1 = extras != null ? extras.getInt("VALUE1", 0) : 0;
        int value2 = extras != null ? extras.getInt("VALUE2", 0) : 0;
        Toast.makeText(this, message + " - Values: " + value1 + ", " + value2, Toast.LENGTH_LONG).show();

        Button buttonReturn = findViewById(R.id.button_return);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                int sum = value1 + value2;
                returnIntent.putExtra("RETURN_MESSAGE", "Returning from Third Activity");
                returnIntent.putExtra("SUM", sum);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        Log.v(TAG, "onCreate called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w(TAG, "onPause called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop called");
    }
}