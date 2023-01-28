package com.shuvo.ttit.petukfund.profile;

//import static com.shuvo.ttit.petukfund.connection.OracleConnection.createConnection;
import static com.shuvo.ttit.petukfund.profile.UserProfile.p_id;
import static com.shuvo.ttit.petukfund.profile.UserProfile.selectedImage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Base64;
//import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.shuvo.ttit.petukfund.R;

//import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

public class UpdatePic extends AppCompatActivity {

    ImageView imageView;
    Button save;


//    Connection connection;
//    private AsyncTask mTask;
    private Boolean conn = false;
//    private Boolean connected = false;
    boolean loading = false;

    RelativeLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pic);

        imageView = findViewById(R.id.image_captured);
        save = findViewById(R.id.upload_image);
        fullLayout = findViewById(R.id.upload_image_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_upload_image);
        circularProgressIndicator.setVisibility(View.GONE);

        if (selectedImage != null){
            Glide.with(UpdatePic.this)
                    .load(selectedImage)
                    .fitCenter()
                    .into(imageView);
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                mTask = new Check().execute();
                updateUserImage();
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
//                ItemData();
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
//                Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
//                finish();
//
//            } else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(UpdatePic.this)
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

//    public void ItemData() {
//        try {
//            this.connection = createConnection();
//            //    Toast.makeText(MainActivity.this, "Connected",Toast.LENGTH_SHORT).show();
//
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            selectedImage.compress(Bitmap.CompressFormat.JPEG, 60, bos);
//            byte[] bArray = bos.toByteArray();
//            ByteArrayInputStream bs = new ByteArrayInputStream(bArray);
//            InputStream in = new ByteArrayInputStream(bArray);
////
////            Statement stmt = connection.createStatement();
////
////            stmt.executeUpdate("UPDATE PETUKS SET PIMAGE = utl_raw.cast_to_raw('"+bs +"') WHERE PID = "+p_id+"");
//
//
//
//            PreparedStatement ps = connection.prepareStatement("UPDATE PETUKS SET PIMAGE = ? WHERE PID = "+p_id+"");
//            ps.setBinaryStream(1,in,bArray.length);
//            int count = ps.executeUpdate();
//            if (count == 0) {
//                System.out.println("DATA NOT INSERTED");
//            }
//            else {
//                System.out.println("DATA INSERTED");
//            }
//
//            ps.close();
//
//            connected = true;
//
//            connection.close();
//
//        }
//        catch (Exception e) {
//
//            //   Toast.makeText(MainActivity.this, ""+e,Toast.LENGTH_LONG).show();
//            Log.i("ERRRRR", e.getLocalizedMessage());
//            e.printStackTrace();
//        }
//    }

    public void updateUserImage() {
        String url = "http://103.56.208.123:8001/apex/petuk_api/update/image";

        conn = false;
        circularProgressIndicator.setVisibility(View.VISIBLE);
        fullLayout.setVisibility(View.GONE);
        loading = true;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 60, bos);
        byte[] bArray = bos.toByteArray();
//        String encodedImage = Base64.encodeToString(bArray, Base64.DEFAULT);
//        InputStream in = new ByteArrayInputStream(bArray);

//        System.out.println(encodedImage);

        RequestQueue requestQueue = Volley.newRequestQueue(UpdatePic.this);

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
        })
        {
            @Override
            public byte[] getBody() throws AuthFailureError {
                System.out.println("104");
                return bArray;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                System.out.println("103");
                params.put("pid",p_id);
//                params.put("pimage", encodedImage);
                return params;
            }

            @Override
            public String getBodyContentType() {
                System.out.println("105");
                return "application/binary";
            }
        };

        requestQueue.add(stringRequest);
    }

    public void updateInterface() {

        circularProgressIndicator.setVisibility(View.GONE);
        fullLayout.setVisibility(View.VISIBLE);
        loading = false;

        if (conn) {

            conn = false;
//            connected = false;

            Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
            finish();

        }
        else {
            Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
            AlertDialog dialog = new AlertDialog.Builder(UpdatePic.this)
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

                    updateUserImage();
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