package com.example.budgetreceipt.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.safframework.injectview.Injector;

public class BaseActivity extends AppCompatActivity{
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        Injector.injectInto(this);
    }
}
