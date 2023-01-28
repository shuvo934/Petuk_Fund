package com.shuvo.ttit.petukfund.profile;

//import static com.shuvo.ttit.petukfund.connection.OracleConnection.createConnection;
import static com.shuvo.ttit.petukfund.login.Login.userInfoLists;

//import androidx.activity.result.ActivityResult;
//import androidx.activity.result.ActivityResultCallback;
//import androidx.activity.result.ActivityResultLauncher;
//import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
import android.net.Uri;
//import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.shuvo.ttit.petukfund.R;
//import com.shuvo.ttit.petukfund.homePage.MainMenu;
import com.shuvo.ttit.petukfund.inouthistory.TransactionHistroy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
//import java.sql.Blob;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity {

    ScrollView fullLayout;
    CircularProgressIndicator circularProgressIndicator;

    TextView userName;
    ImageView editName;

    TextView userDesig;

    TextView userPhone;

    TextView userContribution;
    ImageView more;

    TextView userMail;
    ImageView editMail;

    CircleImageView userImage;
    ImageView camera;

    String user_name = "";
    String user_desig = "";
    String user_mail = "";
    String user_number = "";
    String user_contribution = "";
    public static String p_id = "";

//    Connection connection;
//    private AsyncTask mTask;
    private Boolean conn = false;
//    private Boolean connected = false;
    Bitmap bitmap;
    public static Bitmap selectedImage;

    Boolean imageFound = false;
    boolean loading = false;

    public static int RESULT_LOAD_IMG = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        fullLayout = findViewById(R.id.user_profile_layout);
        circularProgressIndicator = findViewById(R.id.progress_indicator_profile);
        circularProgressIndicator.setVisibility(View.GONE);

        userName = findViewById(R.id.user_profile_name);
        editName = findViewById(R.id.edit_image_name);

        userDesig = findViewById(R.id.user_profile_designation);

        userPhone = findViewById(R.id.user_profile_phone);

        userContribution = findViewById(R.id.user_profile_contribution);
        more = findViewById(R.id.more_info_amount);

        userMail = findViewById(R.id.user_profile_email);
        editMail = findViewById(R.id.edit_image_email);

        userImage = findViewById(R.id.profile_image);
        camera = findViewById(R.id.camera_view);

        p_id = userInfoLists.get(0).getP_id();

//        ActivityResultLauncher<Intent> imagePickerActivityResult = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if (result != null) {
//                            Uri imageUri = Objects.requireNonNull(result.getData()).getData();
//                            Glide.with(UserProfile.this)
//                                    .load(imageUri)
//                                    .into(userImage);
//                        }
//                    }
//                }
//        );

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
//                imagePickerActivityResult.launch(photoPickerIntent);
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);

            }
        });

        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, UpdateProfile.class);
                intent.putExtra("ATTRIBUTE", user_name);
                intent.putExtra("WHICH TO UPDATE", "NAME");
                startActivity(intent);
            }
        });

        editMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, UpdateProfile.class);
                intent.putExtra("ATTRIBUTE", user_mail);
                intent.putExtra("WHICH TO UPDATE", "EMAIL");
                startActivity(intent);
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this, TransactionHistroy.class);
                intent.putExtra("VALUE", "PERSONAL");
                startActivity(intent);
            }
        });



    }

    public static String getPath( Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                String picturePath = getPath(getApplicationContext(), imageUri);
                System.out.println(picturePath);
                selectedImage = modifyOrientation(selectedImage,picturePath);
                Intent intent = new Intent(UserProfile.this,UpdatePic.class);
                startActivity(intent);
//                ImageDialogue imageDialogue = new ImageDialogue();
//                imageDialogue.show(getSupportFragmentManager(),"IMAGE");
//                Glide.with(UserProfile.this)
//                        .load(selectedImage)
//                        .into(userImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(UserProfile.this, "Something went wrong", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            Toast.makeText(UserProfile.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }

    private void DataCheck() {
        user_name = userInfoLists.get(0).getP_name();
        userName.setText(user_name);

        user_desig = userInfoLists.get(0).getP_designation();
        userDesig.setText(user_desig);

        user_mail = userInfoLists.get(0).getP_email();
        userMail.setText(user_mail);

        user_number = userInfoLists.get(0).getP_phone();
        userPhone.setText(user_number);

    }

    @Override
    protected void onResume() {
        super.onResume();
        DataCheck();
//        mTask = new Check().execute();
        getUserData();

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
//                if (imageFound) {
//                    Glide.with(UserProfile.this)
//                            .load(bitmap)
//                            .fitCenter()
//                            .into(userImage);
//                }
//                else {
//                    userImage.setImageResource(R.drawable.profile);
//                }
//
//                user_contribution = user_contribution + " TK";
//                userContribution.setText(user_contribution);
//
//            } else {
//                Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
//                AlertDialog dialog = new AlertDialog.Builder(UserProfile.this)
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

//    public void UserData () {
//
//        try {
//            this.connection = createConnection();
//
//            Statement stmt = connection.createStatement();
//
//            ResultSet rs = stmt.executeQuery("SELECT PIMAGE FROM PETUKS WHERE PID = "+p_id+"");
//
//            while (rs.next()) {
//                Blob b=rs.getBlob(1);
//                if (b == null) {
//                    imageFound = false;
//                }
//                else {
//                    imageFound = true;
//                    byte[] barr =b.getBytes(1,(int)b.length());
//                    bitmap = BitmapFactory.decodeByteArray(barr,0,barr.length);
//                }
//            }
//            rs.close();
//
//            ResultSet resultSet = stmt.executeQuery("SELECT NVL(SUM(PC_AMOUNT),0) FROM PETUK_CREDIT WHERE PC_PID = "+p_id+"");
//            while (resultSet.next()) {
//                user_contribution = resultSet.getString(1);
//            }
//            resultSet.close();
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

    public void getUserData() {
        circularProgressIndicator.setVisibility(View.VISIBLE);
        fullLayout.setVisibility(View.GONE);
        loading = true;
        conn = false;
        String contr_url = "http://103.56.208.123:8001/apex/petuk_api/search/user_contribution?p_id="+p_id;
        String imageUrl = "http://103.56.208.123:8001/apex/petuk_api/user_image/"+p_id;

        RequestQueue requestQueue = Volley.newRequestQueue(UserProfile.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, contr_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String items = jsonObject.getString("items");
                    String count = jsonObject.getString("count");
                    if (!count.equals("0")) {
                        JSONArray array = new JSONArray(items);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject userInfo = array.getJSONObject(i);
                            user_contribution = userInfo.getString("contribution");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                conn = true;
                updateInterface();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                conn = false;
                updateInterface();
            }
        });

        StringRequest imageRequest = new StringRequest(Request.Method.GET, imageUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String items = jsonObject.getString("items");
                    String count = jsonObject.getString("count");

                    if (!count.equals("0")) {
                        JSONArray array = new JSONArray(items);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject userInfo = array.getJSONObject(i);
                            String image = userInfo.optString("pimage");

                            if (image.equals("null") || image.equals("") ) {
                                System.out.println("NULL IMAGE");
                                imageFound = false;
                            }
                            else {
                                byte[] decodedString = Base64.decode(image,Base64.DEFAULT);
                                bitmap = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
                                if (bitmap != null) {
                                    System.out.println("OK");
                                    imageFound = true;
                                }
                                else {
                                    System.out.println("NOT OK");
                                    imageFound = false;
                                }
//                                imageFound = true;
//                                System.out.println("IMAGE: "+imageFound );
//                                if (imageFound) {
//                                    Glide.with(MainMenu.this)
//                                            .load(bitmap)
//                                            .fitCenter()
//                                            .into(profile);
//                                }
//                                else {
//                                    profile.setImageResource(R.drawable.profile);
//                                }
                            }


                        }
                    }
                    else {
                        imageFound = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    imageFound = false;
                }
                requestQueue.add(stringRequest);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imageFound = false;
                conn = false;
                updateInterface();
            }
        });

        requestQueue.add(imageRequest);
    }

    public void updateInterface() {
        circularProgressIndicator.setVisibility(View.GONE);
        fullLayout.setVisibility(View.VISIBLE);
        loading = false;

        if (conn) {

            conn = false;

            if (imageFound) {
                Glide.with(UserProfile.this)
                        .load(bitmap)
                        .fitCenter()
                        .into(userImage);
            }
            else {
                userImage.setImageResource(R.drawable.profile);
            }

            user_contribution = user_contribution + " TK";
            userContribution.setText(user_contribution);

        }
        else {
            Toast.makeText(getApplicationContext(), "No Internet Connection or Slow Connection", Toast.LENGTH_SHORT).show();
            AlertDialog dialog = new AlertDialog.Builder(UserProfile.this)
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
                    getUserData();
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

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}