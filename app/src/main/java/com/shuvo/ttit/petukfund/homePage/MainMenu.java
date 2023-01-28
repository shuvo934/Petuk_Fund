package com.shuvo.ttit.petukfund.homePage;

//import static com.shuvo.ttit.petukfund.connection.OracleConnection.createConnection;
import static com.shuvo.ttit.petukfund.login.Login.userInfoLists;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.animation.ValueAnimator;
//import android.annotation.SuppressLint;
//import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
//import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.shuvo.ttit.petukfund.R;
import com.shuvo.ttit.petukfund.balanceadjustment.Balance;
import com.shuvo.ttit.petukfund.contribution.UserContribution;
import com.shuvo.ttit.petukfund.homePage.lists.MonthWiseDataList;
import com.shuvo.ttit.petukfund.inouthistory.TransactionHistroy;
import com.shuvo.ttit.petukfund.login.Login;
import com.shuvo.ttit.petukfund.monthlyHistory.MonthlyTransaction;
import com.shuvo.ttit.petukfund.profile.UserProfile;
import com.shuvo.ttit.petukfund.userInfoLists.UserInfoList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import java.io.IOException;
//import java.sql.Blob;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

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

    CircleImageView profile;
    ImageView logOut;

    Bitmap bitmap;
    Boolean imageFound = false;

    TextView totalBalance;
    TextView totalIn;
    TextView totalOut;

    Button addBalance;
    Button deductBalance;

    Button addHistory;
    Button deductHistory;
    ImageView contribution;

    TextView amountInCurrent;
    TextView amountOutCurrent;
    TextView balanceCurrent;

    int startNumber = 0;

    int inAmount = 0;
    int outAmount = 0;
    String total_balance = "";

    String month = "";
    String month_full_name = "";
    String yearNo = "";

    String selectedMonthNo = "";
    String selectedYearNo = "";
    String selectedMonthName = "";
    String selectedInAmount = "";
    String selectedOutAmount = "";
    String selectedBalanceAmount = "";

    Button monthHistory;

    int inAmountThisMonth = 0;
    int outAmountThisMonth = 0;

    private Boolean conn = false;
//    private Boolean connected = false;

//    Connection connection;
//    private AsyncTask mTask;
    boolean loading = false;

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
        addHistory = findViewById(R.id.add_transaction_history_button);
        deductHistory = findViewById(R.id.deduct_transaction_history_button);
        contribution = findViewById(R.id.total_contribution_of_all_user);

        amountInCurrent = findViewById(R.id.in_total_amount_current_month);
        amountOutCurrent = findViewById(R.id.out_total_amount_current_month);
        balanceCurrent = findViewById(R.id.total_fund_balance_current_month);
        monthHistory = findViewById(R.id.monthly_history_button);

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
            contribution.setVisibility(View.GONE);
        }
        else {
            addBalance.setVisibility(View.VISIBLE);
            deductBalance.setVisibility(View.VISIBLE);
            contribution.setVisibility(View.VISIBLE);
        }

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (mTask != null) {
//                    if (mTask.getClass().toString().equals("RUNNING")) {
//                        Toast.makeText(getApplicationContext(),"Please wait while loading",Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(MainMenu.this)
//                                .setTitle("LOG OUT!")
//                                .setMessage("Do you want to log out?")
//                                .setIcon(R.drawable.petuk_icon)
//                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        userInfoLists.clear();
//                                        userInfoLists = new ArrayList<>();
//
//                                        SharedPreferences.Editor editor1 = sharedPreferences.edit();
//                                        editor1.remove(P_ID);
//                                        editor1.remove(P_NAME);
//                                        editor1.remove(P_DESIG);
//                                        editor1.remove(P_PHONE);
//                                        editor1.remove(P_EMAIL);
//                                        editor1.remove(P_J_DATE);
//                                        editor1.remove(LOGIN_TF);
//                                        editor1.apply();
//                                        editor1.commit();
//                                        Toast.makeText(getApplicationContext(),"Log Out Successful",Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(MainMenu.this, Login.class);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                })
//                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        //Do nothing
//                                    }
//                                });
//                        AlertDialog alertDialog = alertDialogBuilder.create();
//                        alertDialog.show();
//                    }
//                }
//                else {
//                    MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(MainMenu.this)
//                            .setTitle("LOG OUT!")
//                            .setMessage("Do you want to log out?")
//                            .setIcon(R.drawable.petuk_icon)
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    userInfoLists.clear();
//                                    userInfoLists = new ArrayList<>();
//
//                                    SharedPreferences.Editor editor1 = sharedPreferences.edit();
//                                    editor1.remove(P_ID);
//                                    editor1.remove(P_NAME);
//                                    editor1.remove(P_DESIG);
//                                    editor1.remove(P_PHONE);
//                                    editor1.remove(P_EMAIL);
//                                    editor1.remove(P_J_DATE);
//                                    editor1.remove(LOGIN_TF);
//                                    editor1.apply();
//                                    editor1.commit();
//                                    Toast.makeText(getApplicationContext(),"Log Out Successful",Toast.LENGTH_SHORT).show();
//                                    Intent intent = new Intent(MainMenu.this, Login.class);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                            })
//                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    //Do nothing
//                                }
//                            });
//                    AlertDialog alertDialog = alertDialogBuilder.create();
//                    alertDialog.show();
//                }

                if (loading) {
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
        });

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MMMM",Locale.getDefault());
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy",Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        Date c = calendar.getTime();
        month_full_name = simpleDateFormat1.format(c);
        month_full_name = month_full_name.toUpperCase();
        month = simpleDateFormat.format(c);
        yearNo = simpleDateFormat2.format(c);

        monthWiseDataLists.add(new MonthWiseDataList(month,month_full_name,"","","",yearNo,false,false,false,false));
        System.out.println("MONTH: " + month);

        for (int i = 0 ; i < 5; i++) {
            calendar.add(Calendar.MONTH, -1);
            c = calendar.getTime();
            month_full_name = simpleDateFormat1.format(c);
            month_full_name = month_full_name.toUpperCase();
            month = simpleDateFormat.format(c);
            yearNo = simpleDateFormat2.format(c);

            monthWiseDataLists.add(new MonthWiseDataList(month,month_full_name,"","","",yearNo,false,false,false,false));
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

        addHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, TransactionHistroy.class);
                intent.putExtra("VALUE","ADD");
                startActivity(intent);
            }
        });

        deductHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, TransactionHistroy.class);
                intent.putExtra("VALUE","DEDUCT");
                startActivity(intent);
            }
        });

        contribution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, UserContribution.class);
                startActivity(intent);
            }
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

                    selectedMonthNo = monthWiseDataLists.get(month_i).getMonthNo();
                    selectedYearNo = monthWiseDataLists.get(month_i).getYear();
                    selectedMonthName = monthWiseDataLists.get(month_i).getMonthName();
                    selectedInAmount = monthWiseDataLists.get(month_i).getAmountIn();
                    selectedOutAmount = monthWiseDataLists.get(month_i).getAmountOut();
                    selectedBalanceAmount = monthWiseDataLists.get(month_i).getAmountBalance();

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

                    selectedMonthNo = monthWiseDataLists.get(month_i).getMonthNo();
                    selectedYearNo = monthWiseDataLists.get(month_i).getYear();
                    selectedMonthName = monthWiseDataLists.get(month_i).getMonthName();
                    selectedInAmount = monthWiseDataLists.get(month_i).getAmountIn();
                    selectedOutAmount = monthWiseDataLists.get(month_i).getAmountOut();
                    selectedBalanceAmount = monthWiseDataLists.get(month_i).getAmountBalance();

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

        monthHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, MonthlyTransaction.class);
                intent.putExtra("MONTH_NO",selectedMonthNo);
                intent.putExtra("YEAR_NO",selectedYearNo);
                intent.putExtra("MONTH_NAME",selectedMonthName);
                intent.putExtra("IN_AMOUNT",selectedInAmount);
                intent.putExtra("OUT_AMOUNT",selectedOutAmount);
                intent.putExtra("BALANCE",selectedBalanceAmount);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAmountData();
    }

    @Override
    public void onBackPressed() {

//        if (mTask != null) {
//            if (mTask.getStatus().toString().equals("RUNNING")) {
//                Toast.makeText(getApplicationContext(),"Please wait while loading",Toast.LENGTH_SHORT).show();
//            } else {
//                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(MainMenu.this)
//                        .setTitle("EXIT!")
//                        .setMessage("Do you want to exit?")
//                        .setIcon(R.drawable.petuk_icon)
//                        .setPositiveButton("Yes", (dialog, which) -> System.exit(0))
//                        .setNegativeButton("No", (dialog, which) -> {
//                            //Do nothing
//                        });
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//            }
//        }
//        else {
//            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(MainMenu.this)
//                    .setTitle("EXIT!")
//                    .setMessage("Do you want to exit?")
//                    .setIcon(R.drawable.petuk_icon)
//                    .setPositiveButton("Yes", (dialog, which) -> System.exit(0))
//                    .setNegativeButton("No", (dialog, which) -> {
//                        //Do nothing
//                    });
//            AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.show();
//        }

        if (loading) {
            Toast.makeText(getApplicationContext(),"Please wait while loading",Toast.LENGTH_SHORT).show();
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

//    public boolean isConnected () {
//        boolean connected = false;
//        try {
//            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo nInfo = cm.getActiveNetworkInfo();
//            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
//            return connected;
//        } catch (Exception e) {
//            Log.e("Connectivity Exception", e.getMessage());
//        }
//        return connected;
//    }
//
//    public boolean isOnline () {
//
//        Runtime runtime = Runtime.getRuntime();
//        try {
//            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
//            int exitValue = ipProcess.waitFor();
//            return (exitValue == 0);
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }

//    @SuppressLint("StaticFieldLeak")
//    public class CheckData extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            circularProgressIndicator.setVisibility(View.VISIBLE);
//            fullLayout.setVisibility(View.GONE);
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            if (isConnected() && isOnline()) {
//
//                AmountData();
//                if (connected) {
//                    conn = true;
//                }
//
//            } else {
//                conn = false;
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//
//            circularProgressIndicator.setVisibility(View.GONE);
//            fullLayout.setVisibility(View.VISIBLE);
//            if (conn) {
//
//                String text =  startNumber + " TK";
//                totalBalance.setText(text);
//
//                int balance = inAmount - outAmount;
//                total_balance = String.valueOf(balance);
//
//                ValueAnimator valueAnimator = ValueAnimator.ofInt(startNumber,balance);
//                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                        String bal = valueAnimator.getAnimatedValue().toString() + " TK";
//                        totalBalance.setText(bal);
//                    }
//                });
//
//                valueAnimator.setDuration(1000);
//                valueAnimator.start();
//
//                System.out.println(inAmount);
//                System.out.println(outAmount);
//
//                String in = inAmount + " TK";
//                totalIn.setText(in);
//
//                String out = outAmount + " TK";
//                totalOut.setText(out);
//
//                inAmountThisMonth = Integer.parseInt(monthWiseDataLists.get(0).getAmountIn());
//                outAmountThisMonth = Integer.parseInt(monthWiseDataLists.get(0).getAmountOut());
//
//                int thisMonthBalance = Integer.parseInt(monthWiseDataLists.get(0).getAmountBalance());
//
//                String th_bal = thisMonthBalance + " TK";
//                balanceCurrent.setText(th_bal);
//
//                String th_in = inAmountThisMonth + " TK";
//                amountInCurrent.setText(th_in);
//
//                String th_out = outAmountThisMonth + " TK";
//                amountOutCurrent.setText(th_out);
//
//                nextM.setBackgroundColor(Color.parseColor("#b2bec3"));
//                String mn = monthWiseDataLists.get(0).getMonthName();
//                monthSelected.setText(mn);
//                month_i = 0;
//                selectedMonthNo = monthWiseDataLists.get(0).getMonthNo();
//                selectedYearNo = monthWiseDataLists.get(0).getYear();
//                selectedMonthName = monthWiseDataLists.get(0).getMonthName();
//                selectedInAmount = monthWiseDataLists.get(0).getAmountIn();
//                selectedOutAmount = monthWiseDataLists.get(0).getAmountOut();
//                selectedBalanceAmount = monthWiseDataLists.get(0).getAmountBalance();
//
//
//
//                conn = false;
//                connected = false;
//
//
//            } else {
//                AlertDialog dialog = new AlertDialog.Builder(MainMenu.this)
//                        .setMessage("Slow Internet or Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .show();
//
//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(v -> {
//
//                    mTask = new CheckData().execute();
//                    dialog.dismiss();
//                });
//            }
//        }
//    }

//    public void AmountData () {
//
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//
//            ResultSet rs = stmt.executeQuery("Select NVL(SUM(PC_AMOUNT),0) from PETUK_CREDIT");
//
//            while (rs.next()) {
//                inAmount = rs.getInt(1);
//            }
//            rs.close();
//
//            ResultSet rs1 = stmt.executeQuery("Select NVL(SUM(PD_AMOUNT),0) from PETUK_DEBIT");
//
//            while (rs1.next()) {
//                outAmount = rs1.getInt(1);
//            }
//            rs1.close();
//
//            for (int i = 0 ; i < monthWiseDataLists.size(); i++) {
//                ResultSet rs2 = stmt.executeQuery("Select NVL(SUM(PC_AMOUNT),0) from PETUK_CREDIT WHERE PC_MONTH = '"+monthWiseDataLists.get(i).getMonthNo()+"' AND PC_YEAR = '"+monthWiseDataLists.get(i).getYear()+"'");
//
//                while (rs2.next()) {
//                    inAmountThisMonth = rs2.getInt(1);
//                }
//                rs2.close();
//
//                monthWiseDataLists.get(i).setAmountIn(String.valueOf(inAmountThisMonth));
//
//                ResultSet rs3 = stmt.executeQuery("Select NVL(SUM(PD_AMOUNT),0) from PETUK_DEBIT WHERE PD_MONTH = '"+monthWiseDataLists.get(i).getMonthNo()+"' AND PD_YEAR = '"+monthWiseDataLists.get(i).getYear()+"'");
//
//                while (rs3.next()) {
//                    outAmountThisMonth = rs3.getInt(1);
//                }
//                rs3.close();
//
//                monthWiseDataLists.get(i).setAmountOut(String.valueOf(outAmountThisMonth));
//
//                int thisMonthBalance = inAmountThisMonth - outAmountThisMonth;
//
//                monthWiseDataLists.get(i).setAmountBalance(String.valueOf(thisMonthBalance));
//            }
//
////            ResultSet resultSet = stmt.executeQuery("SELECT PIMAGE FROM PETUKS WHERE PID = "+userInfoLists.get(0).getP_id()+"");
////
////            while (resultSet.next()) {
////                Blob b=resultSet.getBlob(1);
////                if (b == null) {
////                    imageFound = false;
////                }
////                else {
////                    imageFound = true;
////                    byte[] barr =b.getBytes(1,(int)b.length());
////                    bitmap = BitmapFactory.decodeByteArray(barr,0,barr.length);
////                }
////            }
////            resultSet.close();
//
//
//
//            connected = true;
//
//            connection.close();
//
//        } catch (Exception e) {
//
//            Log.i("ERRRRR", e.getLocalizedMessage());
//            e.printStackTrace();
//        }
//
//    }

    public void getAmountData() {

        circularProgressIndicator.setVisibility(View.VISIBLE);
        fullLayout.setVisibility(View.GONE);
        loading = true;
        conn = false;
        for (int i = 0; i <monthWiseDataLists.size(); i++) {
            monthWiseDataLists.get(i).setMonthIn(false);
            monthWiseDataLists.get(i).setMonthOut(false);
            monthWiseDataLists.get(i).setUpdatedIn(false);
            monthWiseDataLists.get(i).setUpdatedOut(false);
        }

        String sum_credit_url = "http://103.56.208.123:8001/apex/petuk_api/balance_manage/sum_credit";
        String sum_debit_url = "http://103.56.208.123:8001/apex/petuk_api/balance_manage/sum_debit";
        String url = "http://103.56.208.123:8001/apex/petuk_api/user_image/"+userInfoLists.get(0).getP_id();

        RequestQueue requestQueue = Volley.newRequestQueue(MainMenu.this);

        StringRequest sum_debit_request = new StringRequest(Request.Method.GET, sum_debit_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String items = jsonObject.getString("items");
                    String count = jsonObject.getString("count");
//                    System.out.println("Count: "+ count);
                    if (!count.equals("0")) {
                        JSONArray array = new JSONArray(items);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject userInfo = array.getJSONObject(i);
                            outAmount = Integer.parseInt(userInfo.getString("sum_debit"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getMonthlyAmount();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                conn = false;
                updateInterface();
                Log.i("Error", error.toString());
            }
        });

        StringRequest sum_credit_request = new StringRequest(Request.Method.GET, sum_credit_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String items = jsonObject.getString("items");
                    String count = jsonObject.getString("count");
//                    System.out.println("Count: "+ count);
                    if (!count.equals("0")) {
                        JSONArray array = new JSONArray(items);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject userInfo = array.getJSONObject(i);
                            inAmount = Integer.parseInt(userInfo.getString("sum_credit"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                requestQueue.add(sum_debit_request);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error", error.toString());
                conn = false;
                updateInterface();
            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("101");
                    JSONObject jsonObject = new JSONObject(response);
                    String items = jsonObject.getString("items");
                    String count = jsonObject.getString("count");
                    if (!count.equals("0")) {
                        JSONArray array = new JSONArray(items);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject userInfo = array.getJSONObject(i);
                            String image = userInfo.optString("pimage");
//                                System.out.println(image);
                            if (image.equals("null") || image.equals("") ) {
                                System.out.println("NULL IMAGE");
                                imageFound = false;
                            }
                            else {
                                byte[] decodedString = Base64.decode(image,Base64.DEFAULT);
//                            System.out.println(Arrays.toString(decodedString));
                                bitmap = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
                                if (bitmap != null) {
                                    System.out.println("OK");
                                    imageFound = true;
                                }
                                else {
                                    System.out.println("NOT OK");
                                    imageFound = false;
                                }

                            }
                        }
                    }
                    else {
                        imageFound = false;
                    }

                } catch (JSONException e) {
                    imageFound = false;
                    System.out.println("102");
                    e.printStackTrace();
                }

                requestQueue.add(sum_credit_request);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("103");
                imageFound = false;
                conn = false;
                updateInterface();

            }
        });

        requestQueue.add(stringRequest);

    }

    public void getMonthlyAmount() {

        for (int i = 0 ; i < monthWiseDataLists.size(); i++) {
            String month_no = monthWiseDataLists.get(i).getMonthNo();
            String year = monthWiseDataLists.get(i).getYear();
            String month_sum_credit_url = "http://103.56.208.123:8001/apex/petuk_api/monthly_balance/sum_credit?PC_MONTH="+month_no+"&PC_YEAR="+year;
            String month_sum_debit_url = "http://103.56.208.123:8001/apex/petuk_api/monthly_balance/sum_debit?pd_month="+month_no+"&pd_year="+year;

            System.out.println("LOOP: "+i);
            getMonthlyCredit(month_sum_credit_url,i,month_no,year);

        }
    }

    public void getMonthlyCredit(String url, int index, String month_no, String year) {

        String month_sum_debit_url = "http://103.56.208.123:8001/apex/petuk_api/monthly_balance/sum_debit?pd_month="+month_no+"&pd_year="+year;

        RequestQueue requestQueue = Volley.newRequestQueue(MainMenu.this);

        StringRequest month_sum_credit_request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String items = jsonObject.getString("items");
                    String count = jsonObject.getString("count");
//                    System.out.println("Count: "+ count);
                    if (!count.equals("0")) {
                        JSONArray array = new JSONArray(items);
                        for (int a = 0; a < array.length(); a++) {
                            JSONObject userInfo = array.getJSONObject(a);
                            inAmountThisMonth = Integer.parseInt(userInfo.getString("monthly_sum_credit"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("RESPONSE FOR CREDIT: " + index);
                monthWiseDataLists.get(index).setAmountIn(String.valueOf(inAmountThisMonth));
                monthWiseDataLists.get(index).setMonthIn(true);
                monthWiseDataLists.get(index).setUpdatedIn(true);
                getMonthlyDebit(month_sum_debit_url,index,inAmountThisMonth,true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Error", error.toString());
                conn = false;
                monthWiseDataLists.get(index).setMonthIn(false);
                monthWiseDataLists.get(index).setUpdatedIn(true);
                getMonthlyDebit(month_sum_debit_url,index,inAmountThisMonth,false);

//                for (int a = 0; a < monthWiseDataLists.size(); a++) {
//                    if (monthWiseDataLists.get(a).isUpdatedIn()) {
//                        if(monthWiseDataLists.get(a).isMonthIn()) {
//                            conn = true;
//                            break;
//                        }
//                        else {
//                            conn = false;
//                        }
//                    }
//                    else {
//                        conn = true;
//                        break;
//                    }
//                }
//                if (!conn) {
//                    updateInterface();
//                }

            }
        });
        requestQueue.add(month_sum_credit_request);
    }

    public void getMonthlyDebit(String url, int index, int inTaka, boolean errorFree) {
        if (errorFree) {
            RequestQueue requestQueue = Volley.newRequestQueue(MainMenu.this);
            StringRequest month_sum_debit_request = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String items = jsonObject.getString("items");
                                String count = jsonObject.getString("count");
//                            System.out.println("Count: "+ count);
                                if (!count.equals("0")) {
                                    JSONArray array = new JSONArray(items);
                                    for (int a = 0; a < array.length(); a++) {
                                        JSONObject userInfo = array.getJSONObject(a);
                                        outAmountThisMonth = Integer.parseInt(userInfo.getString("monthly_sum_debit"));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            System.out.println("RESPONSE FOR DEBIT: " + index);
                            monthWiseDataLists.get(index).setAmountOut(String.valueOf(outAmountThisMonth));
                            int thisMonthBalance = inTaka - outAmountThisMonth;

                            monthWiseDataLists.get(index).setAmountBalance(String.valueOf(thisMonthBalance));
                            monthWiseDataLists.get(index).setMonthOut(true);
                            monthWiseDataLists.get(index).setUpdatedOut(true);

                            boolean falseAvail = false;
                            boolean notUpdated  = false;
                            for (int i = 0; i < monthWiseDataLists.size(); i++) {
                                if (monthWiseDataLists.get(i).isUpdatedIn() && monthWiseDataLists.get(i).isUpdatedOut()) {
                                    if (monthWiseDataLists.get(i).isMonthIn() && monthWiseDataLists.get(i).isMonthOut()) {
                                        conn = true;
                                    }
                                    else {
                                        conn = false;
                                        falseAvail = true;
                                    }
                                }
                                else {
                                    notUpdated = true;
                                    break;
                                }
                            }
                            if (!notUpdated) {
                                if (falseAvail) {
                                    conn = false;
                                    updateInterface();
                                }
                                else {
                                    conn = true;
                                    updateInterface();
                                }

                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Error", error.toString());
                            conn = false;
                            monthWiseDataLists.get(index).setMonthOut(false);
                            monthWiseDataLists.get(index).setUpdatedOut(true);

                            for (int i = 0; i < monthWiseDataLists.size(); i++) {
                                if (monthWiseDataLists.get(i).isUpdatedOut()) {
                                    if (!monthWiseDataLists.get(i).isMonthOut()) {
                                        conn = false;
                                    }
                                }
                                else {
                                    conn = true;
                                    break;
                                }
                            }
                            if (!conn) {
                                updateInterface();
                            }
                        }
                    });
            requestQueue.add(month_sum_debit_request);
        }
        else {
            monthWiseDataLists.get(index).setAmountOut(String.valueOf(outAmountThisMonth));
            int thisMonthBalance = inTaka - outAmountThisMonth;

            monthWiseDataLists.get(index).setAmountBalance(String.valueOf(thisMonthBalance));
            monthWiseDataLists.get(index).setMonthOut(false);
            monthWiseDataLists.get(index).setUpdatedOut(true);

            boolean falseAvail = false;
            boolean notUpdated  = false;
            for (int i = 0; i < monthWiseDataLists.size(); i++) {
                if (monthWiseDataLists.get(i).isUpdatedIn() && monthWiseDataLists.get(i).isUpdatedOut()) {
                    if (monthWiseDataLists.get(i).isMonthIn() && monthWiseDataLists.get(i).isMonthOut()) {
                        conn = true;
                    }
                    else {
                        conn = false;
                        falseAvail = true;
                    }
                }
                else {
                    notUpdated = true;
                    break;
                }
            }
            if (!notUpdated) {
                if (falseAvail) {
                    conn = false;
                    updateInterface();
                }
                else {
                    conn = true;
                    updateInterface();
                }

            }
        }

    }

    public void updateInterface() {
        circularProgressIndicator.setVisibility(View.GONE);
        fullLayout.setVisibility(View.VISIBLE);
        loading = false;
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
            selectedMonthNo = monthWiseDataLists.get(0).getMonthNo();
            selectedYearNo = monthWiseDataLists.get(0).getYear();
            selectedMonthName = monthWiseDataLists.get(0).getMonthName();
            selectedInAmount = monthWiseDataLists.get(0).getAmountIn();
            selectedOutAmount = monthWiseDataLists.get(0).getAmountOut();
            selectedBalanceAmount = monthWiseDataLists.get(0).getAmountBalance();

            System.out.println("IMAGE: "+imageFound );
            if (imageFound) {
                Glide.with(MainMenu.this)
                        .load(bitmap)
                        .fitCenter()
                        .into(profile);
            }
            else {
                profile.setImageResource(R.drawable.profile);
            }

            conn = false;
//            connected = false;

        } else {
            AlertDialog dialog = new AlertDialog.Builder(MainMenu.this)
                    .setMessage("Slow Internet or Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getAmountData();
                dialog.dismiss();
            });
        }
    }
}