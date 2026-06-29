package com.example.mckenfang;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static final int GAME_REQUEST_CODE = 1;
    Button startButton;
    TextView txtScoreShow;
    TextView txtHighscore;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        sharedPreferences = getSharedPreferences("MckenFangPrefs", Context.MODE_PRIVATE);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        startButton = findViewById(R.id.btnStart);
        txtScoreShow = findViewById(R.id.txt_score_show);
        txtHighscore = findViewById(R.id.txt_highscore);

        updateDisplay();

        startButton.setOnClickListener(v -> {
            Intent gameIntent = new Intent(this, GameActivity.class);
            startActivityForResult(gameIntent, GAME_REQUEST_CODE);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDisplay();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GAME_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            int lastScore = data.getIntExtra("Score", 0);
            txtScoreShow.setText("Last Score: " + lastScore);
        }
    }

    private void updateDisplay() {
        int highScore = sharedPreferences.getInt("Highscore", 0);
        txtHighscore.setText("Highscore: " + highScore);
    }
}