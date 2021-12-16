package com.example.budgetreceipt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.NestedScrollView;

import com.example.budgetreceipt.R;
import com.example.budgetreceipt.adapters.CategoryAdapter;
import com.example.budgetreceipt.database.DBcatch;
import com.example.budgetreceipt.global.GlobalProperties;
import com.example.budgetreceipt.models.Category;
import com.example.budgetreceipt.models.Settings;
import com.example.budgetreceipt.models.User;
import com.example.budgetreceipt.validation.InputValidation;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity  extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = RegisterActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;
    private TextInputLayout textInputLayoutConfirmPassword;

    private TextInputEditText textInputEditTextName;
    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;
    private TextInputEditText textInputEditTextConfirmPassword;

    private AppCompatButton appCompatButtonRegister;
    private AppCompatImageView appCompatTextViewLoginLink;

    private InputValidation inputValidation;
    private DBcatch dbCatch;
    private User user;
    private List<Category> categoryList;
    private CategoryAdapter categoryAdapter;
    private Category category;
    private String retrivedUserID;
    private Settings settings;
    private int user_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * //R.layout.activity_register) : activity_register.xml layout은 앱의 UI가 됩니다.
         * 시스템이 리소스/레이아웃 디렉토리에서 레이앙수에 쉽게 액세스 할수 있도록 도와줍니다
         */
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        initViews();
        initListeners();
        initObjects();
    }

    /**
     * views 초기화 메소드
     */
    private void initViews() {
        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);

        textInputLayoutName = (TextInputLayout) findViewById(R.id.textInputLayoutName);
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = (TextInputLayout) findViewById(R.id.textInputLayoutConfirmPassword);

        textInputEditTextName = (TextInputEditText) findViewById(R.id.textInputEditTextName);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.textInputEditTextConfirmPassword);

        appCompatButtonRegister = (AppCompatButton) findViewById(R.id.appCompatButtonRegister);

        appCompatTextViewLoginLink = (AppCompatImageView) findViewById(R.id.appCompatTextViewLoginLink);

    }

    /**
     * listeners 초기화 메소드.
     */
    private void initListeners() {
        appCompatButtonRegister.setOnClickListener(this);
        appCompatTextViewLoginLink.setOnClickListener(this);
    }

    /**
     * 사용할 개체 초기화 메소드
     */
    private void initObjects() {
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryList);
        category = new Category();
        inputValidation = new InputValidation(activity);
        dbCatch = new DBcatch(activity);
        user = new User();
        settings = new Settings();
    }


    /**
     * 클릭 수신 메소드
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonRegister:
                postDataToSQLite();
                break;

            case R.id.appCompatTextViewLoginLink:
                finish();
                break;
        }
    }

    /**
     * 입력 텍스트 필드의 유효성을 검사하며 SQLite에 데이터를 넣는다.
     */
    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.error_message_name))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }

        if (!dbCatch.checkUser(textInputEditTextEmail.getText().toString().trim())) {

            user.setName(textInputEditTextName.getText().toString().trim());
            user.setEmail(textInputEditTextEmail.getText().toString().trim());
            user.setPassword(textInputEditTextPassword.getText().toString().trim());

            dbCatch.addUser(user);
            user_id = dbCatch.getUser(textInputEditTextEmail.getText().toString().trim()).getId();
            ((GlobalProperties) this.getApplication()).setUserTokenVariable(user_id);
            retrivedUserID = Integer.toString(user_id);
            setDefault();
            settings.setUser_ID(user_id);
            settings.setPieChart(true);
            settings.setBarChart(false);
            settings.setRadarChart(false);
            dbCatch.createSettings(settings);
            emptyInputEditText();
            Intent intentUserInter = new Intent(RegisterActivity.this, SuccessfullyLoginActivity.class);
            startActivity(intentUserInter);


        } else {
            // 레코드가 이미 있다는 메시지를 표시하는 스낵바
            Snackbar.make(nestedScrollView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show();
        }
    }
    /**
     * 모든 입력 편집 텍스트를 비우는 매소드
     */
    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }
    /**
     * SQLite에서 모든 사용자 카테고리를 가져오는 메소드
     */
    public void setDefault(){
        if (!dbCatch.checkCategory("음식&음료",retrivedUserID)) {
            category.setUserID(user_id);
            category.setName("음식&음료");
            dbCatch.addCategory(category);
        } if (!dbCatch.checkCategory("교통",retrivedUserID)) {
            category.setUserID(user_id);
            category.setName("교통");
            dbCatch.addCategory(category);
        } if (!dbCatch.checkCategory("숙박",retrivedUserID)) {
            category.setUserID(user_id);
            category.setName("숙박");
            dbCatch.addCategory(category);
        } if (!dbCatch.checkCategory("교육",retrivedUserID)) {
            category.setUserID(user_id);
            category.setName("교육");
            dbCatch.addCategory(category);
        }  if (!dbCatch.checkCategory("오락",retrivedUserID)) {
            category.setUserID(user_id);
            category.setName("오락");
            dbCatch.addCategory(category);
        }  if (!dbCatch.checkCategory("금융",retrivedUserID)) {
            category.setUserID(user_id);
            category.setName("금융");
            dbCatch.addCategory(category);
        } if (!dbCatch.checkCategory("보험",retrivedUserID)) {
            category.setUserID(user_id);
            category.setName(보험");
            dbCatch.addCategory(category);
        } if (!dbCatch.checkCategory("스포츠",retrivedUserID)) {
            category.setUserID(user_id);
            category.setName("스포츠");
            dbCatch.addCategory(category);
        } if (!dbCatch.checkCategory("통신",retrivedUserID)) {
            category.setUserID(user_id);
            category.setName("통신");
            dbCatch.addCategory(category);
        } if(!dbCatch.checkCategory("기타",retrivedUserID)) {
            category.setUserID(user_id);
            category.setName("기타");
            dbCatch.addCategory(category);
        }
    }
}
