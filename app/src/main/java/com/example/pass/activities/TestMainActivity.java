package com.example.pass.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pass.R;
import com.example.pass.activities.chooseFileActivity.view.ChooseFileActivity;

public class TestMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testmain);

        initViews();
    }

    private void initViews() {
        Button btn_choosefile = findViewById(R.id.btn_choosefile);
        btn_choosefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestMainActivity.this, ChooseFileActivity.class);
                startActivity(intent);
            }
        });
    }
}
