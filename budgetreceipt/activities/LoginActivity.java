package com.example.budgetreceipt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import com.example.budgetreceipt.R;
import com.example.budgetreceipt.database.DBcatch;
import com.example.budgetreceipt.global.GlobalProperties;
import com.example.budgetreceipt.validation.InputValidation;
import com.facebook.stetho.Stetho;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = LoginActivity.this;

    private NestedScrollView nestedScrollView;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;

    private AppCompatButton appCompatButtonLogin;

    private AppCompatTextView textViewLinkRegister;
    private InputValidation inputValidation;
    private DBcatch dbCatch;
    private int user_id;

    //To set constant admin
    private String email_admin = "admin@admin.com";
    private String password_admin = "admin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * //R.layout.activity_login) : activity_login.xml 레이아웃이 로그인의 UI로 설정됩니다.
         * 이 시스템은 리소스 디렉토리에서 레이아웃에 쉽게 접근하도록 도와줄 것입니다.
         */
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        //chrome://inspect/#devices
        Stetho.initializeWithDefaults(this);
        initViews();
        initListeners();
        initObjects();
    }

    /**
     * 이 메소드는 view를 초기화하는 것입니다.
     */
    private void initViews() {

        nestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        /**
         * TextInputLayout view 초기화
         */
        textInputLayoutEmail = (TextInputLayout) findViewById(R.id.textInputLayoutEmail);
        textInputLayoutPassword = (TextInputLayout) findViewById(R.id.textInputLayoutPassword);
        /**
         * TextInputEditText view 초기화
         */
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.textInputEditTextEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.textInputEditTextPassword);

        appCompatButtonLogin = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);

        textViewLinkRegister = (AppCompatTextView) findViewById(R.id.textViewLinkRegister);
    }

    /**
     * 이 메소드는 listeners를 초기화 하는 것입니다.
     */
    private void initListeners() {
        appCompatButtonLogin.setOnClickListener(this);
        textViewLinkRegister.setOnClickListener(this);
    }

    /**
     * 이 메소드는 사용된 객체를 초기화 하는 것입니다.
     */
    private void initObjects() {
        dbCatch = new DBcatch(activity);
        inputValidation = new InputValidation(activity);

    }

    /**
     * This implemented method is to listen the click on view
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
                verifyFromSQLite();
                break;
            case R.id.textViewLinkRegister:
                // Navigate to RegisterActivity
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    /**
     * 이 메소드는 입력 텍스트 필드의 유효성을 검사하고 SQLite의 로그인 자격 증명을 확인하는 것입니다.
     */
    private void verifyFromSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.error_message_email))) {
            return;
        }

        if (dbCatch.checkUser(textInputEditTextEmail.getText().toString().trim()
                , textInputEditTextPassword.getText().toString().trim())) {

            DBcatch dbCatch = new DBcatch(LoginActivity.this);
            /**
             * 1) USER_ID를 가져오고 GlobalProperties로 전달
             */
            user_id = dbCatch.getUser(textInputEditTextEmail.getText().toString().trim()).getId();
            ((GlobalProperties) this.getApplication()).setUserTokenVariable(user_id);
            /**
             * 2) HomeActivity 인터페이스 수정
             */
            Intent intentUserInter = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intentUserInter);

        } else if (textInputEditTextEmail.getText().toString().trim().equals(email_admin) &&
                textInputEditTextPassword.getText().toString().trim().equals(password_admin)) {
            /**
             * 1) EMAIL을 받아 HomeActivity로 전달
             * 1.1) UsersListActivity 인터페이스 수정
             */
            Intent accountsIntent = new Intent(activity, UsersListActivity.class);
            accountsIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);

        } else {
            // Snack Bar to show success message that record is wrong
            Snackbar.make(nestedScrollView, getString(R.string.error_valid_email_password), Snackbar.LENGTH_LONG).show();
        }
    }
    @Override
    public void onBackPressed() {
        Intent intentUserInter = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intentUserInter);
    }
    /**
     * 이 메소드는 모든 입력 수정 텍스트를 비우는 것이다.
     */
    private void emptyInputEditText() {
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }

}
