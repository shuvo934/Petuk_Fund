package com.shuvo.ttit.petukfund.profile;

//import static com.shuvo.ttit.petukfund.connection.OracleConnection.createConnection;
import static com.shuvo.ttit.petukfund.login.Login.userInfoLists;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
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
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shuvo.ttit.petukfund.R;

//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends AppCompatActivity {

    TextView actionbarText;

    RelativeLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;

    TextInputLayout textInputLayout;
    TextInputEditText editText;
    Button save;

    String text = "";
    String updateInfo = "";
    String p_id = "";

//    Connection connection;
    private Boolean conn = false;
//    private Boolean connected = false;
    boolean loading = false;

    public static final String LOGIN_ACTIVITY_FILE = "LOGIN_ACTIVITY_FILE_PETUK";

    SharedPreferences sharedLogin;

    public static final String P_NAME = "P_NAME";
    public static final String P_EMAIL = "P_EMAIL";
//    private AsyncTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        sharedLogin = getSharedPreferences(LOGIN_ACTIVITY_FILE,MODE_PRIVATE);

        actionbarText = findViewById(R.id.update_profile_text);

        fullLayout = findViewById(R.id.update_profile_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_update_profile);
        circularProgressIndicator.setVisibility(View.GONE);

        textInputLayout = findViewById(R.id.update_text_layout_update_profile);
        editText = findViewById(R.id.update_text_given_update_profile);
        save = findViewById(R.id.update_profile_button);

        p_id = userInfoLists.get(0).getP_id();

        Intent intent = getIntent();
        text = intent.getStringExtra("ATTRIBUTE");
        updateInfo = intent.getStringExtra("WHICH TO UPDATE");

        String action = "UPDATE " + updateInfo;
        actionbarText.setText(action);

        textInputLayout.setHint(updateInfo);

        editText.setText(text);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                text = editText.getText().toString();
                if (!text.isEmpty()) {
//                    mTask = new UpdateCheck().execute();
                    updateUserInfo();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please give information to update",Toast.LENGTH_SHORT).show();
                }
            }
        });

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
//
//    public class UpdateCheck extends AsyncTask<Void, Void, Void> {
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
//                UpdateQuery();
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
//                SharedPreferences.Editor editor1 = sharedLogin.edit();
//                switch (updateInfo) {
//                    case "NAME":
//                        userInfoLists.get(0).setP_name(text);
//                        editor1.remove(P_NAME);
//                        editor1.putString(P_NAME, userInfoLists.get(0).getP_name());
//                        editor1.apply();
//                        editor1.commit();
//                        break;
//                    case "EMAIL":
//                        userInfoLists.get(0).setP_email(text);
//                        editor1.remove(P_EMAIL);
//                        editor1.putString(P_EMAIL, userInfoLists.get(0).getP_email());
//                        editor1.apply();
//                        editor1.commit();
//                        break;
////                    case "ADDRESS":
////                        userInfoLists.get(0).setAd_address(text);
////                        editor1.remove(AD_ADDRESS);
////                        editor1.putString(AD_ADDRESS, userInfoLists.get(0).getAd_address());
////                        editor1.apply();
////                        editor1.commit();
////                        break;
//                }
//                Toast.makeText(getApplicationContext(), "Successfully Updated",Toast.LENGTH_SHORT).show();
//                finish();
//
//            }else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(UpdateProfile.this)
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
//                        mTask = new UpdateCheck().execute();
//                        dialog.dismiss();
//                    }
//                });
//            }
//        }
//    }
//
//    public void UpdateQuery () {
//
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//
//            switch (updateInfo) {
//                case "NAME":
//                    stmt.executeUpdate("UPDATE PETUKS SET PNAME = '"+ text +"' WHERE PID = "+ p_id +"");
//                    break;
//                case "EMAIL":
//                    stmt.executeUpdate("UPDATE PETUKS SET EMAIL = '"+ text +"' WHERE PID = "+ p_id +"");
//                    break;
//            }
//
//
//            stmt.close();
//
//            connected = true;
//
//            connection.close();
//
//        } catch (Exception e) {
//
//            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
//            Log.i("ERRRRR", e.getLocalizedMessage());
//            e.printStackTrace();
//        }
//
//    }

    public void updateUserInfo() {
        String url = "";
        String valueKey = "";
        conn = false;
        circularProgressIndicator.setVisibility(View.VISIBLE);
        fullLayout.setVisibility(View.GONE);
        loading = true;

        switch (updateInfo) {
            case "NAME":
                url = "http://103.56.208.123:8001/apex/petuk_api/update/name";
                valueKey = "pname";
                break;
            case "EMAIL":
                url = "http://103.56.208.123:8001/apex/petuk_api/update/email";
                valueKey = "email";
                break;
        }

        RequestQueue requestQueue = Volley.newRequestQueue(UpdateProfile.this);

        String finalValueKey = valueKey;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("RESPONSE ADDED: 101");
                conn = true;
                updateInterface();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Failed to Upload Data: 102");
                conn = false;
                updateInterface();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                System.out.println("103");
                params.put("pid",p_id);
                params.put(finalValueKey, text);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    public void updateInterface() {
        circularProgressIndicator.setVisibility(View.GONE);
        fullLayout.setVisibility(View.VISIBLE);
        loading = false;

        if (conn) {

            SharedPreferences.Editor editor1 = sharedLogin.edit();
            switch (updateInfo) {
                case "NAME":
                    userInfoLists.get(0).setP_name(text);
                    editor1.remove(P_NAME);
                    editor1.putString(P_NAME, userInfoLists.get(0).getP_name());
                    editor1.apply();
                    editor1.commit();
                    break;
                case "EMAIL":
                    userInfoLists.get(0).setP_email(text);
                    editor1.remove(P_EMAIL);
                    editor1.putString(P_EMAIL, userInfoLists.get(0).getP_email());
                    editor1.apply();
                    editor1.commit();
                    break;
            }
            conn = false;
            Toast.makeText(getApplicationContext(), "Successfully Updated",Toast.LENGTH_SHORT).show();
            finish();

        }else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            AlertDialog dialog = new AlertDialog.Builder(UpdateProfile.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    updateUserInfo();
                    dialog.dismiss();
                }
            });
        }
    }
}