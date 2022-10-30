package com.shuvo.ttit.petukfund.inouthistory;

import static com.shuvo.ttit.petukfund.connection.OracleConnection.createConnection;
import static com.shuvo.ttit.petukfund.login.Login.userInfoLists;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.shuvo.ttit.petukfund.R;
import com.shuvo.ttit.petukfund.inouthistory.adapters.TransactionAdapter;
import com.shuvo.ttit.petukfund.inouthistory.lists.TransactionInfoList;
import com.shuvo.ttit.petukfund.profile.UserProfile;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class TransactionHistroy extends AppCompatActivity {

    TextView history_text;
    NestedScrollView fullLayout;
    CircularProgressIndicator circularProgressIndicator;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    TransactionAdapter transactionAdapter;

    ArrayList<TransactionInfoList> transactionInfoLists;

    private Boolean conn = false;
    private Boolean connected = false;

    Connection connection;
    private AsyncTask mTask;
    String val = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_histroy);

        fullLayout = findViewById(R.id.full_design_layout_trans_history);
        circularProgressIndicator = findViewById(R.id.progress_indicator_trans_history);
        circularProgressIndicator.setVisibility(View.GONE);

        history_text = findViewById(R.id.last_history_text);
        recyclerView = findViewById(R.id.transaction_history_view);

        transactionInfoLists = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        Intent intent= getIntent();
        val = intent.getStringExtra("VALUE");
        if (val.equals("ADD")) {
            String text = "Last Transaction History of Added Balance";
            history_text.setText(text);
        }
        else if (val.equals("DEDUCT")){
            String text = "Last Transaction History of Deducted Balance";
            history_text.setText(text);
        }
        else if (val.equals("PERSONAL")) {
            String text = "Personal Contribution History";
            history_text.setText(text);
        }

        mTask = new Check().execute();
    }

    @Override
    public void onBackPressed() {

        if (mTask != null) {
            if (mTask.getStatus().toString().equals("RUNNING")) {
                Toast.makeText(getApplicationContext(),"Please wait while loading",Toast.LENGTH_SHORT).show();
            } else {
                finish();
            }
        }
        else {
            finish();
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

    public class Check extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            circularProgressIndicator.setVisibility(View.VISIBLE);
            fullLayout.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                TransData();
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

                conn = false;
                connected = false;

                transactionAdapter = new TransactionAdapter(transactionInfoLists, TransactionHistroy.this);
                recyclerView.setAdapter(transactionAdapter);

            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(TransactionHistroy.this)
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

                        mTask = new Check().execute();
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

    public void TransData () {

        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();

            transactionInfoLists = new ArrayList<>();

            if (val.equals("ADD")) {
                ResultSet rs = stmt.executeQuery("Select PID, PNAME, PC_AMOUNT, TO_CHAR(PC_DATE,'DD-MON-YY')\n" +
                        "FROM PETUKS, PETUK_CREDIT\n" +
                        "WHERE PETUK_CREDIT.PC_PID = PETUKS.PID\n" +
                        "order by PC_DATE desc");

                while (rs.next()) {
                    transactionInfoLists.add(new TransactionInfoList(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
                    if (transactionInfoLists.size() > 99) {
                        break;
                    }
                }
                rs.close();
            }
            else if (val.equals("DEDUCT")){
                ResultSet rs = stmt.executeQuery("Select PID, PNAME, PD_AMOUNT, TO_CHAR(PD_DATE,'DD-MON-YY')\n" +
                        "FROM PETUKS, PETUK_DEBIT\n" +
                        "WHERE PETUK_DEBIT.PD_PID = PETUKS.PID\n" +
                        "order by PD_DATE desc");

                while (rs.next()) {
                    transactionInfoLists.add(new TransactionInfoList(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
                    if (transactionInfoLists.size() > 99) {
                        break;
                    }
                }
                rs.close();
            }
            else if (val.equals("PERSONAL")) {
                ResultSet rs = stmt.executeQuery("Select PID, PNAME, PC_AMOUNT, TO_CHAR(PC_DATE,'DD-MON-YY') \n" +
                        "FROM PETUKS, PETUK_CREDIT\n" +
                        "WHERE PETUK_CREDIT.PC_PID = PETUKS.PID\n" +
                        "and PETUKS.PID = "+userInfoLists.get(0).getP_id()+"\n" +
                        "order by PC_DATE desc");

                while (rs.next()) {
                    transactionInfoLists.add(new TransactionInfoList(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
                    if (transactionInfoLists.size() > 99) {
                        break;
                    }
                }
                rs.close();
            }


            connected = true;

            connection.close();

        } catch (Exception e) {

            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }
}