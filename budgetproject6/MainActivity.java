package com.example.budgetproject6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton btn_bglist;
    private ImageButton btn_bgfinal;
    private ImageButton btn_camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_bglist = findViewById(R.id.btn_bglist);
        btn_bglist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BudgetList.class);
                startActivity(intent); // 액티비티 이동
            }
        });

        btn_bgfinal = findViewById(R.id.btn_bgfinal);
        btn_bgfinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BudgetFinal.class);
                startActivity(intent); // 액티비티 이동
            }
        });

        btn_camera = findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BudgetCamera.class);
                startActivity(intent); // 액티비티 이동
            }
        });
    }
}