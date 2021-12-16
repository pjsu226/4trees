package com.example.budgetreceipt.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.budgetreceipt.R;
import com.example.budgetreceipt.adapters.BillAdapter;
import com.example.budgetreceipt.adapters.BillRecognizeAdapter;
import com.example.budgetreceipt.adapters.CategorySelectAdapter;
import com.example.budgetreceipt.adapters.NothingSelectedSpinnerAdapter;
import com.example.budgetreceipt.controllers.NumberTextController;
import com.example.budgetreceipt.database.DBcatch;
import com.example.budgetreceipt.global.GlobalProperties;
import com.example.budgetreceipt.models.Bills;
import com.example.budgetreceipt.models.Category;
import com.example.budgetreceipt.validation.InputValidation;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BillDialogActivity extends AppCompatActivity{
    final Calendar myCalendar = Calendar.getInstance();
    final Context context = this;

    private TextInputEditText textInputEditTextDate;
    private AppCompatActivity activity = BillDialogActivity.this;

    private TextInputLayout textInputLayoutAmount, textInputLayoutDescription, textInputLayoutDate,textInputLayoutCompany,textInputLayoutCategory;

    private AppCompatButton appCompatButtonModify, appCompatButtonAdd;
    private TextInputEditText textInputEditTextAmount, textInputEditTextDescription, textInputEditTextCompany;
    private Spinner textInputEditTextCategory;
    private AppCompatTextView appCompatTextViewCancelLink;

    private Dialog dialog;
    private InputValidation inputValidation;
    private BillAdapter billAdapter;
    private DBcatch dbCatch;
    private List<Bills> listBills;
    private Category category;
    private Bills bills;
    private String getTextAmount, refactorAmount, amountScanned, dateScanned;
    private List<Category> categoryList;
    private CategorySelectAdapter categorySelectAdapter;
    private int user_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("");

        BillRecognizeAdapter billRecognizeAdapter = new BillRecognizeAdapter();
        dateScanned = billRecognizeAdapter.getDateScanned();
        amountScanned = billRecognizeAdapter.getAmountScanned();
        initObjects();
        try {
            callDialog();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private void callDialog() throws ParseException {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.activity_popup_bill);
        dialog.setTitle("Add Bill");
        initDialogViews();
        BillRecognizeAdapter billRecognizeAdapter = new BillRecognizeAdapter();

        dateScanned = billRecognizeAdapter.getDateScanned();
        amountScanned = billRecognizeAdapter.getAmountScanned();
        String result = amountScanned;

        if(amountScanned != null){
            String[] exclude = {"Total","Grand","To‘al","RM","SUBTOTAL","Balance","Due","SUB","TOTAL","CHF",
                    "Tota!","Amount","$","_","‘",":","«","“",",",";","-","."," "};
            for (String s : exclude) result = result.replace(s, "");
        }

        int convert = Integer.parseInt(result);

        convert = convert / 100;

        textInputEditTextAmount.setText(String.valueOf(convert));

        if(dateScanned != null){
            DateFormat originalFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
            Date date;
            try {
                DateFormat targetFormat = new SimpleDateFormat("dd/MM/yy");
                date = originalFormat.parse(dateScanned);
                String formattedDate = targetFormat.format(date);;
                textInputEditTextDate.setText(formattedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

        //항목이 표시되는 방법을 설명하는 어댑터를 만들고, 어댑터는 안드로이드에서 여러 장소에서 사용된다.
        ArrayAdapter<Category> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.contact_spinner_row_nothing_selected, categoryList);
        //스피너 어댑터를 이전에 만든 어댑터로 설정합니다
        textInputEditTextCategory.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        adapter,
                        R.layout.contact_spinner_row_nothing_selected,
                        BillDialogActivity.this));

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        textInputEditTextDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(BillDialogActivity.this, R.style.DialogTheme, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //모서리 뒤 투명하게 하기
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 단추를 클릭하면 사용자 지정 대화 상자를 닫는다
        appCompatButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDataToSQLite();
            }
        });
        appCompatTextViewCancelLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);

        textInputEditTextDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void initDialogViews() {
        appCompatButtonModify = (AppCompatButton) dialog.findViewById(R.id.appCompatButtonModify);
        textInputLayoutAmount = (TextInputLayout) dialog.findViewById(R.id.textInputLayoutAmount);
        textInputLayoutDescription = (TextInputLayout) dialog.findViewById(R.id.textInputLayoutDescription);
        textInputLayoutDate = (TextInputLayout) dialog.findViewById(R.id.textInputLayoutDate);
        textInputLayoutCompany = (TextInputLayout) dialog.findViewById(R.id.textInputLayoutCompany);
        textInputLayoutCategory = (TextInputLayout) dialog.findViewById(R.id.textInputLayoutCategory);

        textInputEditTextAmount = (TextInputEditText) dialog.findViewById(R.id.textInputEditTextAmount);
        textInputEditTextAmount.addTextChangedListener(new NumberTextController(textInputEditTextAmount, "#,###.##"));

        textInputEditTextDescription = (TextInputEditText) dialog.findViewById(R.id.textInputEditTextDescription);
        textInputEditTextDate = (TextInputEditText) dialog.findViewById(R.id.textInputEditTextDate);
        textInputEditTextCompany = (TextInputEditText) dialog.findViewById(R.id.textInputEditTextCompany);
        textInputEditTextCategory = (Spinner) dialog.findViewById(R.id.textInputEditTextCategory);

        appCompatButtonAdd = (AppCompatButton) dialog.findViewById(R.id.appCompatButtonAdd);
        appCompatTextViewCancelLink = (AppCompatTextView) dialog.findViewById(R.id.appCompatTextViewCancelLink);
    }

    private void initObjects() {
        inputValidation = new InputValidation(activity);
        dbCatch = new DBcatch(activity);
        listBills = new ArrayList<>();
        billAdapter = new BillAdapter(listBills);

        categoryList = new ArrayList<>();
        categorySelectAdapter = new CategorySelectAdapter(categoryList);
        category = new Category();
        bills = new Bills();

        user_id = ((GlobalProperties) this.getApplication()).getUserTokenVariable();
        getCategoryFromSQLite();
    }

    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(textInputEditTextAmount, textInputLayoutAmount, getString(R.string.error_empty_amount))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextDate, textInputLayoutDate, getString(R.string.error_empty_date))) {
            return;
        }
        if (textInputEditTextCategory.getSelectedItem() == null) {

            Toast toast = Toast.makeText(BillDialogActivity.this, getString(R.string.error_empty_category), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            ViewGroup group = (ViewGroup) toast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(20);
            toast.show();
            return;
        }

        bills.setUserID(user_id);
        bills.setDescription(textInputEditTextDescription.getText().toString().trim());
        getTextAmount = textInputEditTextAmount.getText().toString();

        refactorAmount = getTextAmount.replaceAll("[$,.\\s+]", "");

        float convertStringToInt = Float.parseFloat(refactorAmount);
        float convert = convertStringToInt / 100;
        String refactor_Amount = Float.toString(convert);

        bills.setAmount(Float.parseFloat(refactor_Amount));
        bills.setDateString(textInputEditTextDate.getText().toString().trim());
        bills.setCompany_name(textInputEditTextCompany.getText().toString().trim());
        bills.setCategory(textInputEditTextCategory.getSelectedItem().toString().trim());

        dbCatch.addBill(bills);

        Intent intentUserInter = new Intent(getApplicationContext(), BillsListActivity.class);
        startActivity(intentUserInter);
    }
    /**
     * This method is to fetch all user records from SQLite
     */
    private void getCategoryFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                categoryList.clear();
                categoryList.addAll(dbCatch.getCategoriesByUserID(user_id));

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();
    }
}
