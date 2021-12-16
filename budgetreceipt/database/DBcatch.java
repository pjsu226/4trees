package com.example.budgetreceipt.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.budgetreceipt.models.Bills;
import com.example.budgetreceipt.models.Category;
import com.example.budgetreceipt.models.Settings;
import com.example.budgetreceipt.models.User;

import java.util.ArrayList;
import java.util.List;

public class DBcatch extends SQLiteOpenHelper {

    // 데이터베이스 버전
    private static final int DATABASE_VERSION = 1;

    // 데이터베이스 이름
    private static final String DATABASE_NAME = "ETracker.db";
    //---------------------------------------------------------------------------------------
    // 테이블 이름
    private static final String TABLE_USER = "user";

    // user 테이블 열 이름
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";

    // sql query로 테이블 만들기
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT,"
            + COLUMN_USER_PASSWORD + " TEXT" + ")";
    //---------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------
    // 테이블 이름
    private static final String TABLE_SETTINGS = "settings";

    // settings 테이블 열 이름
    private static final String COLUMN_SETTINGS_ID = "settings_id";
    private static final String COLUMN_SETTINGS_USER_ID = "user_id";
    private static final String COLUMN_USER_PIE_CHART = "pie_chart";
    private static final String COLUMN_USER_BAR_CHART = "bar_chart";
    private static final String COLUMN_USER_RADAR_CHART = "radar_chart";

    // sql query로 테이블 만들기
    private String CREATE_TABLE_SETTINGS_TABLE = "CREATE TABLE " + TABLE_SETTINGS + "("
            + COLUMN_SETTINGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_SETTINGS_USER_ID + " INTEGER,"
            + COLUMN_USER_PIE_CHART + " BOOLEAN,"
            + COLUMN_USER_RADAR_CHART + " BOOLEAN,"
            + COLUMN_USER_BAR_CHART + " BOOLEAN,"
            + " FOREIGN KEY ("+COLUMN_SETTINGS_USER_ID+") REFERENCES "+TABLE_USER+"("+COLUMN_USER_ID+"));";
    //---------------------------------------------------------------------------------------
    // 테이블 이름
    private static final String TABLE_CATEGORIES = "categories";

    // Bills 테이블 열 이름
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_CATEGORY_USER_ID = "user_ID";
    private static final String COLUMN_CATEGORY_NAME = "name";

    // sql query로 테이블 만들기
    private String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
            + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_CATEGORY_USER_ID + " TEXT,"
            + COLUMN_CATEGORY_NAME + " TEXT ,"
            + " FOREIGN KEY ("+COLUMN_CATEGORY_USER_ID+") REFERENCES "+TABLE_USER+"("+COLUMN_USER_ID+"));";
    //---------------------------------------------------------------------------------------
    private static final String TABLE_BILLS = "bills";

    // Bills 테이블 열 이름.
    private static final String COLUMN_BILL_ID = "bill_id";
    private static final String COLUMN_BILL_USER_ID = "user_id";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DATE_STRING = "dateString";
    private static final String COLUMN_COMPANY_NAME = "company_name";
    private static final String COLUMN_CATEGORY = "category";

    private String CREATE_BILLS_TABLE = "CREATE TABLE " + TABLE_BILLS + "("
            + COLUMN_BILL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_BILL_USER_ID + " INTEGER,"
            + COLUMN_DESCRIPTION + " TEXT,"
            + COLUMN_AMOUNT + " FLOAT,"
            + COLUMN_DATE_STRING + " TEXT,"
            + COLUMN_COMPANY_NAME + " TEXT,"
            + COLUMN_CATEGORY + " TEXT,"
            + " FOREIGN KEY ("+COLUMN_BILL_USER_ID+") REFERENCES "+TABLE_USER+"("+COLUMN_USER_ID+"));";
    //---------------------------------------------------------------------------------------
    // sql query로 테이블 드랍
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private String DROP_BILLS_TABLE = "DROP TABLE IF EXISTS " + TABLE_BILLS;
    private String DROP_CATEGORIES_TABLE = "DROP TABLE IF EXISTS " + TABLE_CATEGORIES;
    private String DROP_TABLE_SETTINGS_TABLE = "DROP TABLE IF EXISTS " + TABLE_SETTINGS;
    /**
     * Constructor
     *
     * @param context
     */
    public DBcatch(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_BILLS_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_TABLE_SETTINGS_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_BILLS_TABLE);
        db.execSQL(DROP_CATEGORIES_TABLE);
        db.execSQL(DROP_TABLE_SETTINGS_TABLE);
        // Create tables again
        onCreate(db);

    }
    //---------------------------------------------------------------------------------------
    /**
     * 사용자 레코드를 생성하는 메소드
     *
     * @param user
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    /**
     * 모든 사용자를 가져오고 사용자 레코드 목록을 반환하는 메소드
     *
     * @return list
     */
    public List<User> getAllUser() {
        // 가져올 열의 배열
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD
        };
        // 졍렬 
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // 모든 행을 순회하고 목록에 추가
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // user list 리턴
        return userList;
    }

    public User getUser(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USER, new String[] { COLUMN_USER_ID,
                        COLUMN_USER_NAME, COLUMN_USER_EMAIL,COLUMN_USER_PASSWORD }, COLUMN_USER_EMAIL + "=?",
                new String[] { String.valueOf(email) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        User user = new User();
        user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
        user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
        user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
        user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
        return user;
    }
    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }
    public void updateUserPassword(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_PASSWORD, user.getPassword());

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }
    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // 아이디로 사용자 기록 삭제
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * 사용자의 존재 여부를 확인하는 방법
     *
     * @param email
     * @return true/false
     */
    public boolean checkUser(String email) {

        // 가져올 열의 배열
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // 선택 기준
        String selection = COLUMN_USER_EMAIL + " = ?";

        // 선택 인수
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {

        // 가져올 열의 배열
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // 선택 기준
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // 선택 인수
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public ArrayList<User> getUserByID(int userID){
        SQLiteDatabase db = this.getReadableDatabase();
        // 정렬 순서
        ArrayList<User> categoryUser = new ArrayList<User>();

        Cursor cursor = db.query(TABLE_USER, new String[] { COLUMN_USER_ID,
                        COLUMN_USER_NAME, COLUMN_USER_EMAIL,COLUMN_USER_PASSWORD }, COLUMN_USER_ID + "=?",
                new String[] { String.valueOf(userID) }, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                // 목록에 레코드 추가
                categoryUser.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // category list 리턴
        return categoryUser;
    }
    //---------------------------------------------------------------------------------------
    public void addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_USER_ID, category.getUserID());
        values.put(COLUMN_CATEGORY_NAME, category.getName());
        // 행 삽입
        db.insert(TABLE_CATEGORIES, null, values);
        db.close();
    }

    public List<Category> getCategoriesByUserID(int userID){
        SQLiteDatabase db = this.getReadableDatabase();
        // 정렬 순서
        String sortOrder =
                COLUMN_CATEGORY_ID;
        List<Category> categoryList = new ArrayList<Category>();

        Cursor cursor = db.query(TABLE_CATEGORIES, new String[] { COLUMN_CATEGORY_ID,
                        COLUMN_CATEGORY_USER_ID, COLUMN_CATEGORY_NAME }, COLUMN_CATEGORY_USER_ID + "=?",
                new String[] { String.valueOf(userID) }, null, null, sortOrder, null);

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_ID))));
                category.setUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_USER_ID))));
                category.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME)));
                // 목록에 레코드 추가
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // category list 리턴
        return categoryList;
    }
    public List<Category> getCategsadoriesByUserID(int userID){
        SQLiteDatabase db = this.getReadableDatabase();
        // 정렬 순서
        String sortOrder =
                COLUMN_CATEGORY_ID;
        List<Category> categoryList = new ArrayList<Category>();

        Cursor cursor = db.query(TABLE_CATEGORIES, new String[] { COLUMN_CATEGORY_ID,
                        COLUMN_CATEGORY_USER_ID, COLUMN_CATEGORY_NAME }, COLUMN_CATEGORY_USER_ID + "=?",
                new String[] { String.valueOf(userID) }, null, null, sortOrder, null);

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_ID))));
                category.setUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_USER_ID))));
                category.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME)));
                // 목록에 레코드 추가
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // category list 리턴
        return categoryList;
    }
    public List<Category> getAllCategories() {
        // 가져올 열의 배열
        String[] columns = {
                COLUMN_CATEGORY_ID,
                COLUMN_CATEGORY_USER_ID,
                COLUMN_CATEGORY_NAME,
        };
        // 정렬 순서
        String sortOrder =
                COLUMN_CATEGORY_ID;
        List<Category> categoryList = new ArrayList<Category>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT columns FROM category ORDER BY id;
         */
        Cursor cursor = db.query(TABLE_CATEGORIES, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order

        // 모든 행을 순회하고 목록에 추가
        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_ID))));
                category.setUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_USER_ID))));
                category.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY_NAME)));
                // 목록에 레코드 추가
                categoryList.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // category list 리턴
        return categoryList;
    }

    public void deleteCategory(int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        // id로 카테고리 레코드 삭제
        db.delete(TABLE_CATEGORIES, COLUMN_CATEGORY_ID + "=\"" + categoryId+"\"", null);
        db.close();
    }

    public void updateCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, category.getName());

        // updating row
        db.update(TABLE_CATEGORIES, values, COLUMN_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(category.getId())});
        db.close();
    }

    public boolean checkCategory(String category, String user_id) {

        // 가져올 열의 배열
        String[] columns = {
                COLUMN_CATEGORY_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // 선택 기준
        String selection = COLUMN_CATEGORY_NAME + " = ?" + " AND " + COLUMN_CATEGORY_USER_ID + " = ?";
        // 선택 인수
        String[] selectionArgs = {category,user_id};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT id FROM category WHERE name ="String category";
         */
        Cursor cursor = db.query(TABLE_CATEGORIES, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    //---------------------------------------------------------------------------------------
    public void addBill(Bills bill) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_BILL_USER_ID, bill.getUserID());
        values.put(COLUMN_DESCRIPTION, bill.getDescription());
        values.put(COLUMN_AMOUNT, bill.getAmount());
        values.put(COLUMN_DATE_STRING, bill.getDateString());
        values.put(COLUMN_COMPANY_NAME, bill.getCompany_name());
        values.put(COLUMN_CATEGORY, bill.getCategory());

        // 행 삽입
        db.insert(TABLE_BILLS, null, values);
        db.close();
    }
    public List<Bills> getBillsByUserID(int userID){
        SQLiteDatabase db = this.getReadableDatabase();
        // 정렬 순서
        List<Bills> billsList = new ArrayList<Bills>();

        Cursor cursor = db.query(TABLE_BILLS, new String[] { COLUMN_BILL_ID,
                        COLUMN_BILL_USER_ID, COLUMN_DESCRIPTION, COLUMN_AMOUNT, COLUMN_DATE_STRING, COLUMN_COMPANY_NAME, COLUMN_CATEGORY}, COLUMN_CATEGORY_USER_ID + "=?",
                new String[] { String.valueOf(userID) }, null, null, COLUMN_BILL_ID, null);

        if (cursor.moveToFirst()) {
            do {
                Bills bills = new Bills();
                bills.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_BILL_ID))));
                bills.setUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_BILL_USER_ID))));
                bills.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                bills.setAmount(cursor.getFloat(cursor.getColumnIndex(COLUMN_AMOUNT)));
                bills.setDateString(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_STRING)));
                bills.setCompany_name(cursor.getString(cursor.getColumnIndex(COLUMN_COMPANY_NAME)));
                bills.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));
                // 목록에 레코드 추가
                billsList.add(bills);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // category list 리턴
        return billsList;
    }
    public ArrayList<Bills> getMonthDateByUserID(int userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String tmpcol_monthly_total = "Monthly_Total";
        String tmpcol_month_year = "Month_and_Year";
        String tmpcol_year_total = "Year Total";
        String[] columns = new String[]{
                "sum(" + COLUMN_AMOUNT + ") AS " + tmpcol_monthly_total,
                "substr(" + COLUMN_DATE_STRING + ",4) AS " + tmpcol_month_year
        };
        String whereclause = COLUMN_BILL_USER_ID + "=?";
        String[] whereargs = new String[]{String.valueOf(userID)};
        String groupbyclause = "substr(" + COLUMN_DATE_STRING + ",4)";
        String orderbyclause = "substr(" + COLUMN_DATE_STRING + ",7,2)||substr(" + COLUMN_DATE_STRING + ",4,2)";
        ArrayList<Bills> listBillsDates = new ArrayList<Bills>();

        Cursor cursor = db.query(TABLE_BILLS, columns, whereclause,
                whereargs, groupbyclause, null, orderbyclause, null);
        if (cursor.moveToFirst()) {
            do {
                Bills bills = new Bills();
                bills.setAmount(cursor.getInt(cursor.getColumnIndex(tmpcol_monthly_total)));
                bills.setDateString(cursor.getString(cursor.getColumnIndex(tmpcol_month_year))); //<<<<<<<<<< 참고 데이터는 MM/YY입니다.
                // 목록에 레코드 추가
                listBillsDates.add(bills);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // category list 리턴
        return listBillsDates;
    }
    public ArrayList<Bills> getBillByID(int bill_id){
        SQLiteDatabase db = this.getReadableDatabase();
        // 정렬 순서
        ArrayList<Bills> billsList = new ArrayList<Bills>();

        Cursor cursor = db.query(TABLE_BILLS, new String[] { COLUMN_BILL_ID,
                        COLUMN_BILL_USER_ID, COLUMN_DESCRIPTION, COLUMN_AMOUNT, COLUMN_DATE_STRING, COLUMN_COMPANY_NAME, COLUMN_CATEGORY}, COLUMN_BILL_ID + "=?",
                new String[] { String.valueOf(bill_id) }, null, null, COLUMN_BILL_ID, null);

        if (cursor.moveToFirst()) {
            do {
                Bills bills = new Bills();
                bills.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_BILL_ID))));
                bills.setUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_BILL_USER_ID))));
                bills.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                bills.setAmount(cursor.getFloat(cursor.getColumnIndex(COLUMN_AMOUNT)));
                bills.setDateString(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_STRING)));
                bills.setCompany_name(cursor.getString(cursor.getColumnIndex(COLUMN_COMPANY_NAME)));
                bills.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));
                // 목록에 레코드 추가
                billsList.add(bills);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // category list 반환
        return billsList;
    }
    public ArrayList<Bills> getYearDateByUserID(int userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String tmpcol_year_total = "Year_Total";
        String tmpcol_month_year = "Month_and_Year";
        String[] columns = new String[]{
                "sum(" + COLUMN_AMOUNT + ") AS " + tmpcol_year_total,
                "substr(" + COLUMN_DATE_STRING + ",7) AS " + tmpcol_month_year
        };
        String whereclause = COLUMN_BILL_USER_ID + "=?";
        String[] whereargs = new String[]{String.valueOf(userID)};
        /**
         substr(X,Y,Z);  X beginning with the Y-th. The left-most character of X is number 1.
         If Y is negative then the first character of the substring is found by counting
         from the right rather than the left. If Z is negative then the abs(Z) characters
         preceding the Y-th character are returned.*/
        String groupbyclause = "substr(" + COLUMN_DATE_STRING + ",7)";
        String orderbyclause = "substr(" + COLUMN_DATE_STRING + ",7,2)";

        ArrayList<Bills> listBillsDates = new ArrayList<Bills>();

        Cursor cursor = db.query(TABLE_BILLS, columns, whereclause,
                whereargs, groupbyclause, null, orderbyclause, null);
        if (cursor.moveToFirst()) {
            do {
                Bills bills = new Bills();
                bills.setAmount(cursor.getInt(cursor.getColumnIndex(tmpcol_year_total)));
                bills.setDateString(cursor.getString(cursor.getColumnIndex(tmpcol_month_year))); //<<<<<<<<<< 참고 데이터는 MM/YY입니다. (고려 결과는 임의적입니다.)
                // 목록에 레코드 추가
                listBillsDates.add(bills);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // category list 리턴
        return listBillsDates;
    }
    public List<Bills> getAllBills() {
        // 가져올 열의 배열
        String[] columns = {
                COLUMN_BILL_ID,
                COLUMN_BILL_USER_ID,
                COLUMN_DESCRIPTION,
                COLUMN_AMOUNT,
                COLUMN_DATE_STRING,
                COLUMN_COMPANY_NAME,
                COLUMN_CATEGORY
        };
        // 정렬 순서
        String sortOrder =
                COLUMN_BILL_ID;
        List<Bills> billsList = new ArrayList<Bills>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT columns FROM bills ORDER BY bill_id;
         */
        Cursor cursor = db.query(TABLE_BILLS, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // 모든 행을 순회하고 목록에 추가
        if (cursor.moveToFirst()) {
            do {
                Bills bill = new Bills();
                bill.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_BILL_ID))));
                bill.setUserID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_BILL_USER_ID))));
                bill.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                bill.setAmount(cursor.getFloat(cursor.getColumnIndex(COLUMN_AMOUNT)));
                bill.setDateString(cursor.getString(cursor.getColumnIndex(COLUMN_DATE_STRING)));
                bill.setCompany_name(cursor.getString(cursor.getColumnIndex(COLUMN_COMPANY_NAME)));
                bill.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));
                // 목록에 레코드 추가
                billsList.add(bill);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // bills list 리턴
        return billsList;
    }

    public void deleteBill(int billId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BILLS, COLUMN_BILL_ID + "=\"" + billId+"\"", null);
        db.close();
    }

    public void updateBill(Bills bills) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_BILL_USER_ID, String.valueOf(bills.getUserID()));
        values.put(COLUMN_DESCRIPTION, bills.getDescription());
        values.put(COLUMN_AMOUNT, String.valueOf((bills.getAmount())));
        values.put(COLUMN_DATE_STRING, bills.getDateString());
        values.put(COLUMN_COMPANY_NAME, bills.getCompany_name());
        values.put(COLUMN_CATEGORY, bills.getCategory());

        // updating row
        db.update(TABLE_BILLS, values, COLUMN_BILL_ID + " = ?", new String[]{String.valueOf(bills.getId())});
        db.close();
    }
    //---------------------------------------------------------------------------------------
    /**
     * 사용자 레코드를 생성하는 메소드
     *
     * @param settings
     */
    public void createSettings(Settings settings) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SETTINGS_USER_ID, settings.getUser_ID());
        values.put(COLUMN_USER_PIE_CHART, settings.isPieChart());
        values.put(COLUMN_USER_BAR_CHART, settings.isBarChart());
        values.put(COLUMN_USER_RADAR_CHART, settings.isRadarChart());

        // 행 삽입
        db.insert(TABLE_SETTINGS, null, values);
        db.close();
    }
    /**
     * 사용자 기록을 업데이트 하는 메소드
     *
     * @param settings
     */
    public void updateSettings(Settings settings) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SETTINGS_USER_ID, String.valueOf(settings.getUser_ID()));
        values.put(COLUMN_USER_PIE_CHART, settings.isPieChart());
        values.put(COLUMN_USER_BAR_CHART, settings.isBarChart());
        values.put(COLUMN_USER_RADAR_CHART, settings.isRadarChart());

        // updating row
        db.update(TABLE_SETTINGS, values, COLUMN_SETTINGS_ID + " = ?",
                new String[]{String.valueOf(settings.getId())});
        db.close();
    }

    public ArrayList<Settings> getSettingsByID(int userID){
        SQLiteDatabase db = this.getReadableDatabase();
        // 정렬 순서
        ArrayList<Settings> settingsArrayList = new ArrayList<Settings>();

        Cursor cursor = db.query(TABLE_SETTINGS, new String[] { COLUMN_SETTINGS_ID,
                        COLUMN_SETTINGS_USER_ID, COLUMN_USER_PIE_CHART, COLUMN_USER_RADAR_CHART, COLUMN_USER_BAR_CHART }, COLUMN_SETTINGS_USER_ID + "=?",
                new String[] { String.valueOf(userID) }, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Settings settings = new Settings();
                settings.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SETTINGS_ID))));
                settings.setUser_ID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SETTINGS_USER_ID))));
                settings.setPieChart(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_PIE_CHART))> 0);
                settings.setRadarChart(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_RADAR_CHART))> 0);
                settings.setBarChart(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_BAR_CHART)) > 0);
                // 목록에 레코드 추가
                settingsArrayList.add(settings);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // category list 리턴
        return settingsArrayList;
    }
}
