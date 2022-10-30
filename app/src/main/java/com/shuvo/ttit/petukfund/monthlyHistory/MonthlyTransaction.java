package com.shuvo.ttit.petukfund.monthlyHistory;

import static com.shuvo.ttit.petukfund.connection.OracleConnection.createConnection;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.shuvo.ttit.petukfund.R;
import com.shuvo.ttit.petukfund.contribution.UserContribution;
import com.shuvo.ttit.petukfund.contribution.adapters.ContributorAdapter;
import com.shuvo.ttit.petukfund.contribution.lists.ContributionLIst;
import com.shuvo.ttit.petukfund.monthlyHistory.adapters.InOutBalanceAdapter;
import com.shuvo.ttit.petukfund.monthlyHistory.lists.InOutBalanceSheetList;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MonthlyTransaction extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ContributorAdapter contributorAdapter;
    TextView monthName;

    NestedScrollView fullLayout;
    CircularProgressIndicator circularProgressIndicator;

    ArrayList<ContributionLIst> contributionLIsts;

    private Boolean conn = false;
    private Boolean connected = false;

    Connection connection;
    private AsyncTask mTask;

    String month = "";
    String year = "";
    String month_Name = "";
    String total_in_amount = "";
    String total_out_amount = "";
    String balance_amount = "";

    RecyclerView inView;
    RecyclerView.LayoutManager layoutManager1;
    InOutBalanceAdapter inBalanceAdapter;

    TextView totalIn;

    ArrayList<InOutBalanceSheetList> inBalanceSheetLists;

    RecyclerView outView;
    RecyclerView.LayoutManager layoutManager2;
    InOutBalanceAdapter outBalanceAdapter;

    TextView totalOut;
    TextView totalBalance;

    ArrayList<InOutBalanceSheetList> outBalanceSheetLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_transaction);

        fullLayout = findViewById(R.id.full_design_layout_month_history);
        circularProgressIndicator = findViewById(R.id.progress_indicator_month_history);
        circularProgressIndicator.setVisibility(View.GONE);

        recyclerView = findViewById(R.id.month_history_contribution_view);
        monthName = findViewById(R.id.month_name_of_the_month_history);

        inView = findViewById(R.id.balance_sheet_in_balance_view);
        totalIn = findViewById(R.id.total_in_amount_month_history);

        outView = findViewById(R.id.balance_sheet_out_balance_view);
        totalOut = findViewById(R.id.total_out_amount_month_history);
        totalBalance = findViewById(R.id.total_balance_month_history);

        Intent intent = getIntent();
        month = intent.getStringExtra("MONTH_NO");
        year = intent.getStringExtra("YEAR_NO");
        month_Name = intent.getStringExtra("MONTH_NAME");
        total_in_amount = intent.getStringExtra("IN_AMOUNT");
        total_out_amount = intent.getStringExtra("OUT_AMOUNT");
        balance_amount = intent.getStringExtra("BALANCE");

        monthName.setText(month_Name);

        contributionLIsts = new ArrayList<>();
        inBalanceSheetLists = new ArrayList<>();
        outBalanceSheetLists = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        inView.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(getApplicationContext());
        inView.setLayoutManager(layoutManager1);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(inView.getContext(),DividerItemDecoration.VERTICAL);
        inView.addItemDecoration(dividerItemDecoration);

        outView.setHasFixedSize(true);
        layoutManager2 = new LinearLayoutManager(getApplicationContext());
        outView.setLayoutManager(layoutManager2);
        DividerItemDecoration dividerItemDecoration1 = new DividerItemDecoration(outView.getContext(),DividerItemDecoration.VERTICAL);
        outView.addItemDecoration(dividerItemDecoration1);

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

                MonthData();
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

                contributorAdapter = new ContributorAdapter(contributionLIsts, MonthlyTransaction.this);
                recyclerView.setAdapter(contributorAdapter);

                inBalanceAdapter = new InOutBalanceAdapter(inBalanceSheetLists,MonthlyTransaction.this);
                inView.setAdapter(inBalanceAdapter);

                totalIn.setText(total_in_amount);

                outBalanceAdapter = new InOutBalanceAdapter(outBalanceSheetLists,MonthlyTransaction.this);
                outView.setAdapter(outBalanceAdapter);

                totalOut.setText(total_out_amount);
                totalBalance.setText(balance_amount);

            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(MonthlyTransaction.this)
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

    public void MonthData () {

        try {
            this.connection = createConnection();

            Statement stmt = connection.createStatement();

            contributionLIsts = new ArrayList<>();
            inBalanceSheetLists = new ArrayList<>();
            outBalanceSheetLists = new ArrayList<>();

            ResultSet rs = stmt.executeQuery("Select PID, PNAME, SUM(PETUK_CREDIT.PC_AMOUNT) P\n" +
                    "FROM PETUKS,PETUK_CREDIT\n" +
                    "WHERE PETUK_CREDIT.PC_PID = PETUKS.PID\n" +
                    "AND PC_MONTH = '"+month+"'\n" +
                    "AND PC_YEAR = '"+year+"'\n" +
                    "GROUP BY PID,PNAME\n" +
                    "ORDER BY P desc");
            while (rs.next()) {
                contributionLIsts.add(new ContributionLIst(rs.getString(2),rs.getString(3) + " TK"));
            }
            rs.close();

            ResultSet rs1 = stmt.executeQuery("SELECT TO_CHAR(PC_DATE,'DD-MON-YY'), PNAME, PC_AMOUNT FROM PETUKS, petuk_credit\n" +
                    "WHERE PETUKS.PID = PETUK_CREDIT.PC_PID\n" +
                    "AND PETUK_CREDIT.PC_MONTH = '"+month+"'\n" +
                    "AND PETUK_CREDIT.PC_YEAR = '"+year+"'\n" +
                    "order by PC_DATE desc");
            int i = 0;
            while (rs1.next()) {
                i++;
                inBalanceSheetLists.add(new InOutBalanceSheetList(String.valueOf(i),rs1.getString(1),rs1.getString(2),
                        rs1.getString(3),""));
            }
            rs1.close();

            ResultSet rs2 = stmt.executeQuery("SELECT TO_CHAR(PD_DATE,'DD-MON-YY'), PNAME, PD_AMOUNT FROM PETUKS, petuk_debit\n" +
                    "WHERE PETUKS.PID = petuk_debit.PD_PID\n" +
                    "AND petuk_debit.PD_MONTH = '"+month+"'\n" +
                    "AND petuk_debit.PD_YEAR = '"+year+"'\n" +
                    "order by PD_DATE desc");
            int j = 0;
            while (rs2.next()) {
                j++;
                outBalanceSheetLists.add(new InOutBalanceSheetList(String.valueOf(j),rs2.getString(1),rs2.getString(2),"",
                        rs2.getString(3)));
            }
            rs2.close();

            connected = true;

            connection.close();

        } catch (Exception e) {

            Log.i("ERRRRR", e.getLocalizedMessage());
            e.printStackTrace();
        }

    }
}