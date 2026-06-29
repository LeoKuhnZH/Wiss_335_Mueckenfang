package com.example.mckenfang;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button startButton;
    TextView txtScoreShow;
    TextView txtHighscore;
    SharedPreferences sharedPreferences;

    private final ActivityResultLauncher<Intent> gameActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    int lastScore = result.getData().getIntExtra("Score", 0);
                    txtScoreShow.setText("Last Score: " + lastScore);
                }
            });

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
            gameActivityResultLauncher.launch(gameIntent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDisplay();
    }

    private void updateDisplay() {
        int highScore = sharedPreferences.getInt("Highscore", 0);
        txtHighscore.setText("Highscore: " + highScore);
    }
}