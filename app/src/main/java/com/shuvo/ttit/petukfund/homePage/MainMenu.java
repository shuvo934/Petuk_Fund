package com.shuvo.ttit.petukfund.homePage;

import static com.shuvo.ttit.petukfund.connection.OracleConnection.createConnection;
import static com.shuvo.ttit.petukfund.login.Login.userInfoLists;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.shuvo.ttit.petukfund.R;
import com.shuvo.ttit.petukfund.balanceadjustment.Balance;
import com.shuvo.ttit.petukfund.homePage.lists.MonthWiseDataList;
import com.shuvo.ttit.petukfund.login.Login;
import com.shuvo.ttit.petukfund.profile.UserProfile;
import com.shuvo.ttit.petukfund.userInfoLists.UserInfoList;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainMenu extends AppCompatActivity {

    public static final String LOGIN_ACTIVITY_FILE = "LOGIN_ACTIVITY_FILE_PETUK";

    public static final String P_ID = "P_ID";
    public static final String P_NAME = "P_NAME";
    public static final String P_DESIG = "P_DESIG";
    public static final String P_PHONE = "P_PHONE";
    public static final String P_EMAIL = "P_EMAIL";
    public static final String P_J_DATE = "P_J_DATE";
    public static final String P_TYPE = "P_TYPE";
    public static final String LOGIN_TF = "TRUE_FALSE";

    SharedPreferences sharedPreferences;

    NestedScrollView fullLayout;
    CircularProgressIndicator circularProgressIndicator;

    ImageView profile;
    ImageView logOut;

    TextView totalBalance;
    TextView totalIn;
    TextView totalOut;

    Button addBalance;
    Button deductBalance;

    TextView amountInCurrent;
    TextView amountOutCurrent;
    TextView balanceCurrent;

    int startNumber = 0;

    int inAmount = 0;
    int outAmount = 0;
    String total_balance = "";

    String month = "";
    String month_full_name = "";

    int inAmountThisMonth = 0;
    int outAmountThisMonth = 0;

    private Boolean conn = false;
    private Boolean connected = false;

    Connection connection;
    private AsyncTask mTask;

    MaterialButton previousM;
    MaterialButton nextM;
    TextView monthSelected;
    int month_i = 0;

    ArrayList<MonthWiseDataList> monthWiseDataLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);

        fullLayout = findViewById(R.id.full_design_layout_main_menu);
        circularProgressIndicator = findViewById(R.id.progress_indicator_main_menu);
        circularProgressIndicator.setVisibility(View.GONE);

        profile = findViewById(R.id.nav_icon_profile);
        logOut = findViewById(R.id.log_out_icon_main_menu);

        totalBalance = findViewById(R.id.total_fund_balance);
        totalIn = findViewById(R.id.in_total_amount);
        totalOut = findViewById(R.id.out_total_amount);

        addBalance = findViewById(R.id.add_balance_button);
        deductBalance = findViewById(R.id.deduct_balance_button);

        amountInCurrent = findViewById(R.id.in_total_amount_current_month);
        amountOutCurrent = findViewById(R.id.out_total_amount_current_month);
        balanceCurrent = findViewById(R.id.total_fund_balance_current_month);

        previousM = findViewById(R.id.previous_month_button);
        nextM = findViewById(R.id.next_month_button);
        monthSelected = findViewById(R.id.selected_month_name);

        monthWiseDataLists = new ArrayList<>();

        sharedPreferences = getSharedPreferences(LOGIN_ACTIVITY_FILE, MODE_PRIVATE);
        boolean loginfile = sharedPreferences.getBoolean(LOGIN_TF,false);

        if (loginfile) {

            String ad_id = sharedPreferences.getString(P_ID, null);
            String ad_name = sharedPreferences.getString(P_NAME,null);
            String ad_desig = sharedPreferences.getString(P_DESIG,null);
            String ad_phone = sharedPreferences.getString(P_PHONE, null);
            String ad_email = sharedPreferences.getString(P_EMAIL,null);
            String ad_join_date = sharedPreferences.getString(P_J_DATE, null);
            String type = sharedPreferences.getString(P_TYPE,null);

            userInfoLists = new ArrayList<>();
            userInfoLists.add(new UserInfoList(ad_id,ad_name,ad_desig,ad_phone,ad_email,ad_join_date,type));

        }

        String userType = userInfoLists.get(0).getP_type();
        if (userType.contains("U")) {
            addBalance.setVisibility(View.GONE);
            deductBalance.setVisibility(View.GONE);
        }
        else {
            addBalance.setVisibility(View.VISIBLE);
            deductBalance.setVisibility(View.VISIBLE);
        }

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTask != null) {
                    if (mTask.getClass().toString().equals("RUNNING")) {
                        Toast.makeText(getApplicationContext(),"Please wait while loading",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(MainMenu.this)
                                .setTitle("LOG OUT!")
                                .setMessage("Do you want to log out?")
                                .setIcon(R.drawable.petuk_icon)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        userInfoLists.clear();
                                        userInfoLists = new ArrayList<>();

                                        SharedPreferences.Editor editor1 = sharedPreferences.edit();
                                        editor1.remove(P_ID);
                                        editor1.remove(P_NAME);
                                        editor1.remove(P_DESIG);
                                        editor1.remove(P_PHONE);
                                        editor1.remove(P_EMAIL);
                                        editor1.remove(P_J_DATE);
                                        editor1.remove(LOGIN_TF);
                                        editor1.apply();
                                        editor1.commit();
                                        Toast.makeText(getApplicationContext(),"Log Out Successful",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainMenu.this, Login.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //Do nothing
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                }
                else {
                    MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(MainMenu.this)
                            .setTitle("LOG OUT!")
                            .setMessage("Do you want to log out?")
                            .setIcon(R.drawable.petuk_icon)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    userInfoLists.clear();
                                    userInfoLists = new ArrayList<>();

                                    SharedPreferences.Editor editor1 = sharedPreferences.edit();
                                    editor1.remove(P_ID);
                                    editor1.remove(P_NAME);
                                    editor1.remove(P_DESIG);
                                    editor1.remove(P_PHONE);
                                    editor1.remove(P_EMAIL);
                                    editor1.remove(P_J_DATE);
                                    editor1.remove(LOGIN_TF);
                                    editor1.apply();
                                    editor1.commit();
                                    Toast.makeText(getApplicationContext(),"Log Out Successful",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainMenu.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Do nothing
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMMM",Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Date c = calendar.getTime();
        month_full_name = simpleDateFormat1.format(c);
        month_full_name = month_full_name.toUpperCase();
        month = simpleDateFormat.format(c);

        monthWiseDataLists.add(new MonthWiseDataList(month,month_full_name,"","",""));
        System.out.println("MONTH: " + month);

        for (int i = 0 ; i < 5; i++) {
            calendar.add(Calendar.MONTH, -1);
            c = calendar.getTime();
            month_full_name = simpleDateFormat1.format(c);
            month_full_name = month_full_name.toUpperCase();
            month = simpleDateFormat.format(c);

            monthWiseDataLists.add(new MonthWiseDataList(month,month_full_name,"","",""));
            System.out.println("MONTH: " + month);
        }


        addBalance.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenu.this, Balance.class);
            intent.putExtra("BALANCE TYPE","ADD");
            intent.putExtra("TOTAL", total_balance);
            startActivity(intent);
        });

        deductBalance.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenu.this, Balance.class);
            intent.putExtra("BALANCE TYPE","DEDUCT");
            intent.putExtra("TOTAL", total_balance);
            startActivity(intent);
        });

        profile.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenu.this, UserProfile.class);
            startActivity(intent);
        });

        previousM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (month_i < 5) {
                    month_i++;
                    Animation animation = AnimationUtils.loadAnimation(MainMenu.this,R.anim.translate_p);
                    animation.reset();
                    monthSelected.setText("");
                    String mn = monthWiseDataLists.get(month_i).getMonthName();
                    monthSelected.setText(mn);
                    monthSelected.clearAnimation();
                    monthSelected.startAnimation(animation);

                    inAmountThisMonth = Integer.parseInt(monthWiseDataLists.get(month_i).getAmountIn());
                    outAmountThisMonth = Integer.parseInt(monthWiseDataLists.get(month_i).getAmountOut());

                    int thisMonthBalance = Integer.parseInt(monthWiseDataLists.get(month_i).getAmountBalance());

                    String th_bal = thisMonthBalance + " TK";
                    balanceCurrent.setText(th_bal);

                    String th_in = inAmountThisMonth + " TK";
                    amountInCurrent.setText(th_in);

                    String th_out = outAmountThisMonth + " TK";
                    amountOutCurrent.setText(th_out);

                    if (month_i == 5) {
                        previousM.setBackgroundColor(Color.parseColor("#b2bec3"));
                    }else if (month_i > 0){
                        nextM.setBackgroundColor(Color.parseColor("#CA6A52"));
                    }
                }


            }
        });

        nextM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (month_i > 0) {
                    month_i = month_i - 1;
                    Animation animation = AnimationUtils.loadAnimation(MainMenu.this,R.anim.translate_n);
                    animation.reset();
                    monthSelected.setText("");
                    String mn = monthWiseDataLists.get(month_i).getMonthName();
                    monthSelected.setText(mn);
                    monthSelected.clearAnimation();
                    monthSelected.startAnimation(animation);

                    inAmountThisMonth = Integer.parseInt(monthWiseDataLists.get(month_i).getAmountIn());
                    outAmountThisMonth = Integer.parseInt(monthWiseDataLists.get(month_i).getAmountOut());

                    int thisMonthBalance = Integer.parseInt(monthWiseDataLists.get(month_i).getAmountBalance());

                    String th_bal = thisMonthBalance + " TK";
                    balanceCurrent.setText(th_bal);

                    String th_in = inAmountThisMonth + " TK";
                    amountInCurrent.setText(th_in);

                    String th_out = outAmountThisMonth + " TK";
                    amountOutCurrent.setText(th_out);
                    if (month_i == 0) {
                        nextM.setBackgroundColor(Color.parseColor("#b2bec3"));
                    }
                    else if (month_i < 5){
                        previousM.setBackgroundColor(Color.parseColor("#CA6A52"));
                    }
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mTask = new CheckData().execute();
    }

    @Override
    public void onBackPressed() {

        if (mTask != null) {
            if (mTask.getStatus().toString().equals("RUNNING")) {
                Toast.makeText(getApplicationContext(),"Please wait while loading",Toast.LENGTH_SHORT).show();
            } else {
                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(MainMenu.this)
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
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(MainMenu.this)
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
    public class CheckData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            circularProgressIndicator.setVisibility(View.VISIBLE);
            fullLayout.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                AmountData();
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

                String text =  startNumber + " TK";
                totalBalance.setText(text);

                int balance = inAmount - outAmount;
                total_balance = String.valueOf(balance);

                ValueAnimator valueAnimator = ValueAnimator.ofInt(startNumber,balance);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        String bal = valueAnimator.getAnimatedValue().toString() + " TK";
                        totalBalance.setText(bal);
                    }
                });

                valueAnimator.setDuration(1000);
                valueAnimator.start();

                System.out.println(inAmount);
                System.out.println(outAmount);

                String in = inAmount + " TK";
                totalIn.setText(in);

                String out = outAmount + " TK";
                totalOut.setText(out);

                inAmountThisMonth = Integer.parseInt(monthWiseDataLists.get(0).getAmountIn());
                outAmountThisMonth = Integer.parseInt(monthWiseDataLists.get(0).getAmountOut());

                int thisMonthBalance = Integer.parseInt(monthWiseDataLists.get(0).getAmountBalance());

                String th_bal = thisMonthBalance + " TK";
                balanceCurrent.setText(th_bal);

                String th_in = inAmountThisMonth + " TK";
                amountInCurrent.setText(th_in);

                String th_out = outAmountThisMonth + " TK";
                amountOutCurrent.setText(th_out);

                nextM.setBackgroundColor(Color.parseColor("#b2bec3"));
                String mn = monthWiseDataLists.get(0).getMonthName();
                monthSelected.setText(mn);
                month_i = 0;

                conn = false;
                connected = false;


            } else {
                AlertDialog dialog = new AlertDialog.Builder(MainMenu.this)
                        .setMessage("Slow Internet or Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    mTask = new CheckData().execute();
                    dialog.dismiss();
                });
            }
        }
    }

    public void AmountData () {

        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("Select NVL(SUM(PC_AMOUNT),0) from PETUK_CREDIT");

            while (rs.next()) {
                inAmount = rs.getInt(1);
            }
            rs.close();

            ResultSet rs1 = stmt.executeQuery("Select NVL(SUM(PD_AMOUNT),0) from PETUK_DEBIT");

            while (rs1.next()) {
                outAmount = rs1.getInt(1);
            }
            rs1.close();

            for (int i = 0 ; i < monthWiseDataLists.size(); i++) {
                ResultSet rs2 = stmt.executeQuery("Select NVL(SUM(PC_AMOUNT),0) from PETUK_CREDIT WHERE PC_MONTH = '"+monthWiseDataLists.get(i).getMonthNo()+"'");

                while (rs2.next()) {
                    inAmountThisMonth = rs2.getInt(1);
                }
                rs2.close();

                monthWiseDataLists.get(i).setAmountIn(String.valueOf(inAmountThisMonth));

                ResultSet rs3 = stmt.executeQuery("Select NVL(SUM(PD_AMOUNT),0) from PETUK_DEBIT WHERE PD_MONTH = '"+monthWiseDataLists.get(i).getMonthNo()+"'");

                while (rs3.next()) {
                    outAmountThisMonth = rs3.getInt(1);
                }
                rs3.close();

                monthWiseDataLists.get(i).setAmountOut(String.valueOf(outAmountThisMonth));

                int thisMonthBalance = inAmountThisMonth - outAmountThisMonth;

                monthWiseDataLists.get(i).setAmountBalance(String.valueOf(thisMonthBalance));
            }


            connected = true;

            connection.close();

        } catch (Exception e) {

            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }
}