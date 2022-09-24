package com.shuvo.ttit.petukfund.login;

import static com.shuvo.ttit.petukfund.connection.OracleConnection.createConnection;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.shuvo.ttit.petukfund.R;
import com.shuvo.ttit.petukfund.homePage.MainMenu;
import com.shuvo.ttit.petukfund.userInfoLists.UserInfoList;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

public class Login extends AppCompatActivity {

    TextInputEditText mob;
    TextInputEditText pass;

    TextView login_failed;

    Button login;

    CheckBox checkBox;

    String mobile = "";
    String password = "";

    private Boolean conn = false;
    private Boolean connected = false;

    Connection connection;
    private AsyncTask mTask;

    public static final String MyPREFERENCES = "UserPassPETUK" ;
    public static final String user_emp_code = "nameKey";
    public static final String user_password = "passKey";
    public static final String checked = "trueFalse";

    public static final String LOGIN_ACTIVITY_FILE = "LOGIN_ACTIVITY_FILE_PETUK";

    public static final String P_ID = "P_ID";
    public static final String P_NAME = "P_NAME";
    public static final String P_DESIG = "P_DESIG";
    public static final String P_PHONE = "P_PHONE";
    public static final String P_EMAIL = "P_EMAIL";
    public static final String P_J_DATE = "P_J_DATE";
    public static final String P_TYPE = "P_TYPE";
    public static final String LOGIN_TF = "TRUE_FALSE";

    SharedPreferences sharedpreferences;

    SharedPreferences sharedLogin;

    String getUserName = "";
    String getPassword = "";
    boolean getChecked = false;

    public static ArrayList<UserInfoList> userInfoLists = new ArrayList<>();

    LinearLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        userInfoLists = new ArrayList<>();

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        fullLayout = findViewById(R.id.log_in_design_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_log_in);
        circularProgressIndicator.setVisibility(View.GONE);

        mob = findViewById(R.id.mobile_number_given_log_in);
        pass = findViewById(R.id.password_given_log_in);
        checkBox = findViewById(R.id.remember_checkbox);

        login_failed = findViewById(R.id.email_pass_miss);

        login = findViewById(R.id.log_in_button);

        sharedLogin = getSharedPreferences(LOGIN_ACTIVITY_FILE,MODE_PRIVATE);

        sharedpreferences = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        getUserName = sharedpreferences.getString(user_emp_code,null);
        getPassword = sharedpreferences.getString(user_password,null);
        getChecked = sharedpreferences.getBoolean(checked,false);

        if (getUserName != null) {
            mob.setText(getUserName);
        }
        if (getPassword != null) {
            pass.setText(getPassword);
        }
        checkBox.setChecked(getChecked);

        pass.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    pass.clearFocus();
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    closeKeyBoard();

                    return false; // consume.
                }
            }
            return false;
        });

        login.setOnClickListener(v -> {

            closeKeyBoard();

            login_failed.setVisibility(View.GONE);
            mobile = Objects.requireNonNull(mob.getText()).toString();
            password = Objects.requireNonNull(pass.getText()).toString();

            if (!mobile.isEmpty() && !password.isEmpty()) {

                mTask = new CheckLogin().execute();

            } else {
                Toast.makeText(getApplicationContext(), "Please Give Mobile number and Password", Toast.LENGTH_SHORT).show();
            }

        });

        closeKeyBoard();
        mob.clearFocus();
        pass.clearFocus();
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(mob.getWindowToken(), 0);
    }

    @Override
    public boolean onTouchEvent (MotionEvent event){
        closeKeyBoard();
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {

        if (mTask != null) {
            if (mTask.getStatus().toString().equals("RUNNING")) {
                Toast.makeText(getApplicationContext(),"Please wait while loading",Toast.LENGTH_SHORT).show();
            } else {
                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(Login.this)
                        .setTitle("EXIT!")
                        .setMessage("Do you want to exit?")
                        .setIcon(R.drawable.petuk_icon)
                        .setPositiveButton("Yes", (dialog, which) -> System.exit(0))
                        .setNegativeButton("No", (dialog, which) -> {
                            //Do nothing
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
        else {
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(Login.this)
                    .setTitle("EXIT!")
                    .setMessage("Do you want to exit?")
                    .setIcon(R.drawable.petuk_icon)
                    .setPositiveButton("Yes", (dialog, which) -> System.exit(0))
                    .setNegativeButton("No", (dialog, which) -> {
                        //Do nothing
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private void closeKeyBoard () {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public boolean isConnected () {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    public boolean isOnline () {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    @SuppressLint("StaticFieldLeak")
    public class CheckLogin extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            circularProgressIndicator.setVisibility(View.VISIBLE);
            fullLayout.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                LoginQuery();
                if (connected) {
                    conn = true;
                }

            } else {
                conn = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            circularProgressIndicator.setVisibility(View.GONE);
            fullLayout.setVisibility(View.VISIBLE);
            if (conn) {

                boolean infoConnected = userInfoLists.size() != 0;

                if (infoConnected) {

                    if (checkBox.isChecked()) {
                        System.out.println("Remembered");
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.remove(user_emp_code);
                        editor.remove(user_password);
                        editor.remove(checked);
                        editor.putString(user_emp_code,mobile);
                        editor.putString(user_password,password);
                        editor.putBoolean(checked,true);
                        editor.apply();
                        editor.commit();

                        SharedPreferences.Editor editor1 = sharedLogin.edit();
                        editor1.remove(P_ID);
                        editor1.remove(P_NAME);
                        editor1.remove(P_DESIG);
                        editor1.remove(P_PHONE);
                        editor1.remove(P_EMAIL);
                        editor1.remove(P_J_DATE);
                        editor1.remove(P_TYPE);
                        editor1.remove(LOGIN_TF);

                        editor1.putString(P_ID, userInfoLists.get(0).getP_id());
                        editor1.putString(P_NAME, userInfoLists.get(0).getP_name());
                        editor1.putString(P_DESIG, userInfoLists.get(0).getP_designation());
                        editor1.putString(P_PHONE, userInfoLists.get(0).getP_phone());
                        editor1.putString(P_EMAIL, userInfoLists.get(0).getP_email());
                        editor1.putString(P_J_DATE,userInfoLists.get(0).getP_jdate());
                        editor1.putString(P_TYPE,userInfoLists.get(0).getP_type());
                        editor1.putBoolean(LOGIN_TF,true);

                        editor1.apply();
                        editor1.commit();
                    }
                    else {
                        System.out.println("Not Remembered");
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.remove(user_emp_code);
                        editor.remove(user_password);
                        editor.remove(checked);

                        editor.apply();
                        editor.commit();

                        SharedPreferences.Editor editor1 = sharedLogin.edit();
                        editor1.remove(P_ID);
                        editor1.remove(P_NAME);
                        editor1.remove(P_DESIG);
                        editor1.remove(P_PHONE);
                        editor1.remove(P_EMAIL);
                        editor1.remove(P_J_DATE);
                        editor1.remove(P_TYPE);
                        editor1.remove(LOGIN_TF);

                        editor1.apply();
                        editor1.commit();
                    }

                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                    startActivity(intent);
                    finish();

                }
                else {
                    login_failed.setVisibility(View.VISIBLE);
                }

                conn = false;
                connected = false;


            } else {
                AlertDialog dialog = new AlertDialog.Builder(Login.this)
                        .setMessage("Slow Internet or Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    mTask = new CheckLogin().execute();
                    dialog.dismiss();
                });
            }
        }
    }

    public void LoginQuery () {

        try {
            this.connection = createConnection();

            userInfoLists = new ArrayList<>();

            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("Select pid , pname, desig, phone, email, jdate, ptype \n" +
                    "from petuks \n" +
                    "where  phone = '"+mobile+"' and \n" +
                    "pword = HAMID_ENCRYPT_DESCRIPTION_PACK.HEDP_ENCRYPT('"+password+"')");

            while (rs.next()) {
                userInfoLists.add(new UserInfoList(rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6),rs.getString(7)));
            }

            connected = true;

            connection.close();

        } catch (Exception e) {

            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }
}