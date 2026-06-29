package com.example.mckenfang;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameActivity extends AppCompatActivity {

    TextView txtPoints;
    FrameLayout gameArea;
    Button btnBack;
    private int currentScore = 0;
    private int highScore = 0;
    private SharedPreferences sharedPreferences;
    private Drawable mosquitoDrawable;
    private final Runnable createMosquitoRunnable = this::createMosquito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        
        sharedPreferences = getSharedPreferences("MckenFangPrefs", Context.MODE_PRIVATE);
        highScore = sharedPreferences.getInt("Highscore", 0);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtPoints = findViewById(R.id.txtPoints);
        gameArea = findViewById(R.id.gameArea);
        btnBack = findViewById(R.id.btnBack);

        mosquitoDrawable = ContextCompat.getDrawable(this, R.drawable.mosquito);

        btnBack.setOnClickListener(v -> finish());

        txtPoints.setText("0");
        gameArea.post(createMosquitoRunnable);
    }

    void createMosquito() {
        if (isFinishing() || isDestroyed()) return;

        ImageView mosquito = new ImageView(this);
        if (mosquitoDrawable != null) {
            mosquito.setImageDrawable(mosquitoDrawable);
        } else {
            mosquito.setImageResource(R.drawable.mosquito);
        }

        mosquito.setLongClickable(false);

        int size = 200;
        gameArea.addView(mosquito, size, size);

        float maxX = gameArea.getWidth() - size;
        float maxY = gameArea.getHeight() - size;

        mosquito.setX((float) (Math.random() * maxX));
        mosquito.setY((float) (Math.random() * maxY));

        mosquito.setClickable(true);
        mosquito.setOnClickListener(v -> {
            gameArea.removeView(v);
            currentScore++;
            txtPoints.setText(String.valueOf(currentScore));

            if (currentScore > highScore) {
                highScore = currentScore;
                sharedPreferences.edit().putInt("Highscore", highScore).apply();
            }
            
            createMosquito();
        });
    }

    @Override
    public void finish() {
        Intent scoreIntent = new Intent();
        scoreIntent.putExtra("Score", currentScore);
        setResult(RESULT_OK, scoreIntent);
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameArea != null) {
            gameArea.removeCallbacks(createMosquitoRunnable);
            gameArea.removeAllViews();
        }
        mosquitoDrawable = null;
    }
}