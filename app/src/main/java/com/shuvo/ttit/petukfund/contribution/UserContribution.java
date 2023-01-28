package com.shuvo.ttit.petukfund.contribution;

//import static com.shuvo.ttit.petukfund.connection.OracleConnection.createConnection;
//import static com.shuvo.ttit.petukfund.login.Login.userInfoLists;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import android.content.Context;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.shuvo.ttit.petukfund.R;
import com.shuvo.ttit.petukfund.contribution.adapters.ContributorAdapter;
import com.shuvo.ttit.petukfund.contribution.lists.ContributionLIst;
//import com.shuvo.ttit.petukfund.inouthistory.TransactionHistroy;
//import com.shuvo.ttit.petukfund.inouthistory.adapters.TransactionAdapter;
//import com.shuvo.ttit.petukfund.inouthistory.lists.TransactionInfoList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import java.io.IOException;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.Statement;
import java.util.ArrayList;

public class UserContribution extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ContributorAdapter contributorAdapter;

    NestedScrollView fullLayout;
    CircularProgressIndicator circularProgressIndicator;

    ArrayList<ContributionLIst> contributionLIsts;

    private Boolean conn = false;
//    private Boolean connected = false;
    boolean loading = false;
    String url = "http://103.56.208.123:8001/apex/petuk_api/balance_manage/all_credit_by_user";

//    Connection connection;
//    private AsyncTask mTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_contribution);

        fullLayout = findViewById(R.id.full_design_layout_contribution);
        circularProgressIndicator = findViewById(R.id.progress_indicator_contribution);
        circularProgressIndicator.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.contribution_view);

        contributionLIsts = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

//        mTask = new Check().execute();
        getContributionData();
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
//                ContributionData();
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
//                contributorAdapter = new ContributorAdapter(contributionLIsts, UserContribution.this);
//                recyclerView.setAdapter(contributorAdapter);
//
//            } else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(UserContribution.this)
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

//    public void ContributionData () {
//
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//
//            contributionLIsts = new ArrayList<>();
//
//            ResultSet rs = stmt.executeQuery("Select PID, PNAME, SUM(PETUK_CREDIT.PC_AMOUNT) P \n" +
//                    "FROM PETUKS,PETUK_CREDIT\n" +
//                    "WHERE PETUK_CREDIT.PC_PID = PETUKS.PID\n" +
//                    "GROUP BY PID,PNAME\n" +
//                    "ORDER BY P desc");
//            while (rs.next()) {
//                contributionLIsts.add(new ContributionLIst(rs.getString(2),rs.getString(3)));
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

    public void getContributionData() {

        circularProgressIndicator.setVisibility(View.VISIBLE);
        fullLayout.setVisibility(View.GONE);
        loading = true;
        conn = false;
        contributionLIsts = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(UserContribution.this);

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
                            String p = transInfo.getString("p");

                            contributionLIsts.add(new ContributionLIst(pname,p));
                        }
                    }
                    conn = true;
                    updateInterface();
                } catch (JSONException e) {
                    e.printStackTrace();
                    conn = false;
                    updateInterface();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                conn = false;
                updateInterface();
            }
        });

        requestQueue.add(stringRequest);

    }

    public void updateInterface() {

        circularProgressIndicator.setVisibility(View.GONE);
        fullLayout.setVisibility(View.VISIBLE);
        loading = false;

        if (conn) {

            conn = false;

            contributorAdapter = new ContributorAdapter(contributionLIsts, UserContribution.this);
            recyclerView.setAdapter(contributorAdapter);

        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
            AlertDialog dialog = new AlertDialog.Builder(UserContribution.this)
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

                    getContributionData();
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
}