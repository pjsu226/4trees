package com.example.budgetreceipt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import com.example.budgetreceipt.R;


public class SuccessfullyLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = SuccessfullyLoginActivity.this;

    private NestedScrollView nestedScrollView;

    private AppCompatButton appCompatButtonContinue;

    private AppCompatTextView textViewLinkCongratulations;
    private AppCompatTextView textViewLinkCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_login);
        getSupportActionBar().hide();

        initViews();
        initListeners();
    }

    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        appCompatButtonContinue = (AppCompatButton) findViewById(R.id.appCompatButtonContinue);
        textViewLinkCongratulations = (AppCompatTextView) findViewById(R.id.textViewLinkCongratulations);
        textViewLinkCreated = (AppCompatTextView) findViewById(R.id.textViewLinkCreated);
    }

    /**
     * 이 메소드는 listeners를 초기화 하는 것입니다.
     */
    private void initListeners() {
        appCompatButtonContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonContinue:
                redirectOnHomeActivity();
                break;
        }
    }

    private void redirectOnHomeActivity(){
        Intent intentUserInter = new Intent(SuccessfullyLoginActivity.this, HomeActivity.class);
        startActivity(intentUserInter);
    }

}
