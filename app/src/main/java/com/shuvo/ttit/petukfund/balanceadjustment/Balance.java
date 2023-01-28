package com.shuvo.ttit.petukfund.balanceadjustment;

//import static com.shuvo.ttit.petukfund.connection.OracleConnection.createConnection;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.rosemaryapp.amazingspinner.AmazingSpinner;
import com.shuvo.ttit.petukfund.R;
//import com.shuvo.ttit.petukfund.contribution.lists.ContributionLIst;
import com.shuvo.ttit.petukfund.userInfoLists.UsersList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Balance extends AppCompatActivity {

    AmazingSpinner userChoice;
    TextInputEditText date;
    TextInputEditText amount;
    Button balanceAdjust;

    TextView noUser;
    TextView noAmount;

    RelativeLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;

//    Connection connection;
//    private AsyncTask mTask;
    private Boolean conn = false;
//    private Boolean connected = false;
    boolean loading = false;

    String p_id = "";
    String p_name = "";
    String tkAmount = "";
    String full_date = "";
    String month = "";
    String year = "";
    TextView appBarTitle;
    String total_balance = "";

    ArrayList<UsersList> usersLists = new ArrayList<>();
    Boolean addBalanceQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        fullLayout = findViewById(R.id.full_design_layout_add_menu);
        circularProgressIndicator = findViewById(R.id.progress_indicator_add_balance);
        circularProgressIndicator.setVisibility(View.GONE);

        userChoice = findViewById(R.id.user_spinner_add_balance);
        noUser = findViewById(R.id.no_user_msg_add_balance);
        date = findViewById(R.id.add_balance_date);
        amount = findViewById(R.id.amount_given_for_add_balance);
        noAmount = findViewById(R.id.no_amount_msg_add_balance);
        balanceAdjust = findViewById(R.id.add_balance_of_user);
        appBarTitle = findViewById(R.id.app_bar_name_balance);

        usersLists = new ArrayList<>();

        Intent intent = getIntent();
        String type = intent.getStringExtra("BALANCE TYPE");
        total_balance = intent.getStringExtra("TOTAL");
        if (type.equals("ADD")) {
            balanceAdjust.setText("ADD BALANCE");
            appBarTitle.setText("ADD BALANCE");
            addBalanceQuery = true;
        }
        else if (type.equals("DEDUCT")) {
            balanceAdjust.setText("DEDUCT BALANCE");
            appBarTitle.setText("DEDUCT BALANCE");
            addBalanceQuery = false;
        }

        userChoice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                p_id = "";

                String name = adapterView.getItemAtPosition(i).toString();
                for (int j = 0; j < usersLists.size(); j++) {
                    if (name.equals(usersLists.get(j).getP_name())) {
                        p_id = usersLists.get(j).getP_id();
                        p_name = name;
                    }
                }

                noUser.setVisibility(View.GONE);

            }
        });

        amount.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    amount.clearFocus();
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    closeKeyBoard();

                    return false; // consume.
                }
            }
            return false;
        });

        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("MM",Locale.getDefault());
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy", Locale.getDefault());

        full_date = simpleDateFormat.format(d);
        month = simpleDateFormat1.format(d);
        year = simpleDateFormat2.format(d);

        date.setText(full_date);

        amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                noAmount.setVisibility(View.GONE);
            }
        });

        balanceAdjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tkAmount = Objects.requireNonNull(amount.getText()).toString();

                if (!p_id.isEmpty()) {
                    noUser.setVisibility(View.GONE);
                        if (!tkAmount.isEmpty()) {
                            noAmount.setVisibility(View.GONE);
                            int tk = 0;
                            boolean valid = false;
                            try {
                                Integer.parseInt(tkAmount);
                                valid = true;

                            }
                            catch (NumberFormatException e) {
                                noAmount.setText("Invalid Amount");
                            }
                            if (valid) {
                                if (addBalanceQuery) {
                                    String message = p_name + " will add " + tkAmount + " TK to the fund. Are you sure about this information? if you are sure then press 'YES'.";
                                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Balance.this,R.style.Theme_MyApp_Dialog_Alert);
                                    builder.setTitle("ALERT!")
                                            .setMessage(message)
                                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
//                                                    mTask = new AddCheck().execute();
                                                    insertBalanceData();
                                                }
                                            })
                                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                                else {
                                    int bal = Integer.parseInt(total_balance);
                                    tk = Integer.parseInt(tkAmount);
                                    if (tk <= bal) {
                                        String message = p_name + " will withdraw " + tkAmount + " TK from the fund. Are you sure about this information? if you are sure then press 'YES'.";
                                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Balance.this,R.style.Theme_MyApp_Dialog_Alert);
                                        builder.setTitle("ALERT!")
                                                .setMessage(message)
                                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
//                                                        mTask = new AddCheck().execute();
                                                        insertBalanceData();
                                                    }
                                                })
                                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                    else {
                                        noAmount.setText("Insufficient Balance");
                                        noAmount.setVisibility(View.VISIBLE);
                                    }

                                }

                            }
                            else {
                                noAmount.setVisibility(View.VISIBLE);
                            }
                        }
                        else {
                            noAmount.setVisibility(View.VISIBLE);
                            noAmount.setText("Please Provide Amount");
                    }
                }
                else {
                    noUser.setVisibility(View.VISIBLE);
                }

            }
        });

        closeKeyBoard();
        amount.clearFocus();
        date.clearFocus();
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(amount.getWindowToken(), 0);

//        mTask = new Check().execute();
        getAllUser();
    }

    @Override
    public void onBackPressed() {

//        if (mTask != null) {
//            if (mTask.getStatus().toString().equals("RUNNING")) {
//                Toast.makeText(getApplicationContext(),"Please wait while loading",Toast.LENGTH_SHORT).show();
//            } else {
//                finish();
//            }
//        }
//        else {
//            finish();
//        }
        if (loading) {
            Toast.makeText(getApplicationContext(),"Please wait while loading",Toast.LENGTH_SHORT).show();
        }
        else {
            finish();
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

    @Override
    public boolean onTouchEvent (MotionEvent event){
        closeKeyBoard();
        return super.onTouchEvent(event);
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

//    public class Check extends AsyncTask<Void, Void, Void> {
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
//                UserData();
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
//
//            if (conn) {
//
//                conn = false;
//                connected = false;
//
//                ArrayList<String> type = new ArrayList<>();
//                for(int i = 0; i < usersLists.size(); i++) {
//                    type.add(usersLists.get(i).getP_name());
//                }
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);
//
//                userChoice.setAdapter(arrayAdapter);
//
//            } else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(Balance.this)
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .setNegativeButton("Cancel",null)
//                        .show();
//
//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        mTask = new Check().execute();
//                        dialog.dismiss();
//                    }
//                });
//                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negative.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                        finish();
//                    }
//                });
//            }
//        }
//    }

//    public class AddCheck extends AsyncTask<Void, Void, Void> {
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
//                InsertBalanceData();
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
//
//            if (conn) {
//
//                conn = false;
//                connected = false;
//
//                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Balance.this,R.style.Theme_MyApp_Dialog_Alert);
//                if (addBalanceQuery) {
//                    builder.setTitle("SUCCESS!")
//                            .setMessage(tkAmount + " TK added to the balance.")
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                    finish();
//                                }
//                            });
//                    AlertDialog alert = builder.create();
//                    alert.setCancelable(false);
//                    alert.setCanceledOnTouchOutside(false);
//                    alert.show();
//                }
//                else {
//                    builder.setTitle("SUCCESS!")
//                            .setMessage(tkAmount + " TK withdrew from the balance.")
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                    finish();
//                                }
//                            });
//                    AlertDialog alert = builder.create();
//                    alert.setCancelable(false);
//                    alert.setCanceledOnTouchOutside(false);
//                    alert.show();
//                }
//
//
//
//
//            }else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(Balance.this)
//                        .setMessage("Please Check Your Internet Connection")
//                        .setPositiveButton("Retry", null)
//                        .show();
//
////                dialog.setCancelable(false);
////                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        mTask = new AddCheck().execute();
//                        dialog.dismiss();
//                    }
//                });
//            }
//        }
//    }

//    public void UserData () {
//
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//
//            usersLists = new ArrayList<>();
//
//            ResultSet rs = stmt.executeQuery("Select PID, PNAME from PETUKS");
//
//            while (rs.next()) {
//               usersLists.add(new UsersList(rs.getString(1),rs.getString(2)));
//            }
//            rs.close();
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

//    public void InsertBalanceData () {
//
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//
//            String pc_id = "";
//            String pd_id = "";
//
//            if (addBalanceQuery) {
//                ResultSet rs = stmt.executeQuery("SELECT NVL(MAX(PCID),0)+1 FROM PETUK_CREDIT");
//
//                while (rs.next()) {
//                    pc_id = rs.getString(1);
//                }
//                rs.close();
//
//                stmt.executeUpdate("INSERT INTO PETUK_CREDIT(PCID, PC_YEAR, PC_MONTH, PC_PID, PC_AMOUNT, PC_DATE) \n" +
//                        "VALUES("+pc_id+", '"+year+"', '"+month+"', "+p_id+", "+tkAmount+", SYSDATE)");
//            }
//            else {
//                ResultSet rs = stmt.executeQuery("SELECT NVL(MAX(PDID),0)+1 FROM PETUK_DEBIT");
//
//                while (rs.next()) {
//                    pd_id = rs.getString(1);
//                }
//                rs.close();
//
//                stmt.executeUpdate("INSERT INTO PETUK_DEBIT(PDID, PD_YEAR, PD_MONTH, PD_PID, PD_AMOUNT, PD_DATE) \n" +
//                        "VALUES("+pd_id+", '"+year+"', '"+month+"', "+p_id+", "+tkAmount+", SYSDATE)");
//            }
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

    public void getAllUser() {
        circularProgressIndicator.setVisibility(View.VISIBLE);
        fullLayout.setVisibility(View.GONE);
        loading = true;
        conn = false;
        String url = "http://103.56.208.123:8001/apex/petuk_api/search/all_user";
        usersLists = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(Balance.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String items = jsonObject.getString("items");
                    String count = jsonObject.getString("count");
                    if (!count.equals("0")) {

                        JSONArray array = new JSONArray(items);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject transInfo = array.getJSONObject(i);
                            String pid = transInfo.getString("pid");
                            String pname = transInfo.getString("pname");

                            usersLists.add(new UsersList(pid,pname));
                        }
                    }
                    conn = true;
                    updateUserData();
                } catch (JSONException e) {
                    e.printStackTrace();
                    conn = false;
                    updateUserData();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                conn = false;
                updateUserData();
            }
        });

        requestQueue.add(stringRequest);
    }

    public void insertBalanceData() {
        circularProgressIndicator.setVisibility(View.VISIBLE);
        fullLayout.setVisibility(View.GONE);
        loading = true;
        conn = false;

        if (addBalanceQuery) {
            String url = "http://103.56.208.123:8001/apex/petuk_api/balance/add";

            RequestQueue requestQueue = Volley.newRequestQueue(Balance.this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    conn = true;
                    updateInsertData();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    conn = false;
                    updateInsertData();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("pc_year",year);
                    params.put("pc_month",month);
                    params.put("pc_pid",p_id);
                    params.put("pc_amount",tkAmount);
                    return params;
                }
            };

            requestQueue.add(stringRequest);


        }
        else {
            String url = "http://103.56.208.123:8001/apex/petuk_api/balance/deduct";

            RequestQueue requestQueue = Volley.newRequestQueue(Balance.this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    conn = true;
                    updateInsertData();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    conn = false;
                    updateInsertData();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("pd_year",year);
                    params.put("pd_month",month);
                    params.put("pd_pid",p_id);
                    params.put("pd_amount",tkAmount);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    public void updateUserData() {
        circularProgressIndicator.setVisibility(View.GONE);
        fullLayout.setVisibility(View.VISIBLE);
        loading = false;

        if (conn) {

            conn = false;

            ArrayList<String> type = new ArrayList<>();
            for(int i = 0; i < usersLists.size(); i++) {
                type.add(usersLists.get(i).getP_name());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

            userChoice.setAdapter(arrayAdapter);

        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
            AlertDialog dialog = new AlertDialog.Builder(Balance.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getAllUser();
                    dialog.dismiss();
                }
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    finish();
                }
            });
        }

    }

    public void updateInsertData() {
        circularProgressIndicator.setVisibility(View.GONE);
        fullLayout.setVisibility(View.VISIBLE);
        loading = false;

        if (conn) {

            conn = false;

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Balance.this,R.style.Theme_MyApp_Dialog_Alert);
            if (addBalanceQuery) {
                builder.setTitle("SUCCESS!")
                        .setMessage(tkAmount + " TK added to the balance.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setCancelable(false);
                alert.setCanceledOnTouchOutside(false);
                alert.show();
            }
            else {
                builder.setTitle("SUCCESS!")
                        .setMessage(tkAmount + " TK withdrew from the balance.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setCancelable(false);
                alert.setCanceledOnTouchOutside(false);
                alert.show();
            }

        }else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            AlertDialog dialog = new AlertDialog.Builder(Balance.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    insertBalanceData();
                    dialog.dismiss();
                }
            });
        }
    }
}