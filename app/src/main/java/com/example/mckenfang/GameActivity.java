package com.example.mckenfang;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameActivity extends AppCompatActivity {

    TextView txtPoints;
    FrameLayout gameArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        findViewById(R.id.txtPoints).bringToFront();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtPoints = findViewById(R.id.txtPoints);
        gameArea = findViewById(R.id.gameArea);

        txtPoints.setText("0");
        gameArea.post(this::createMosquito);
    }

    void createMosquito() {
        ImageView mosquito = new ImageView(this);
        mosquito.setImageResource(R.drawable.mosquito);

        int size = 200;
        gameArea.addView(mosquito, size, size);

        float maxX = gameArea.getWidth() - size;
        float maxY = gameArea.getHeight() - size;

        mosquito.setX((float) (Math.random() * maxX));
        mosquito.setY((float) (Math.random() * maxY));

        mosquito.setClickable(true);
        mosquito.setOnClickListener(v -> {
            gameArea.removeView(v);
            createMosquito();
            txtPoints.setText(String.valueOf(Integer.parseInt(txtPoints.getText().toString()) + 1));
        });
    }
}