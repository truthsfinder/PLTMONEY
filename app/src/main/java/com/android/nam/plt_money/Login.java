package com.android.nam.plt_money;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {
    public static final String JSON_ARRAY = "result";

    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_STATUS = "status";

    private String user_id;
    private String username;
    private String password;
    private String status;

    Helper helper = new Helper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        try {
            Intent intent = getIntent();
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            String name = prefs.getString("username", null);

            if(name!=null){
                startActivity(new Intent(this, Dashboard.class));
                finish();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void login(View view) {

        EditText et_username = (EditText) findViewById(R.id.username);
        EditText et_password = (EditText) findViewById(R.id.password);

        username = et_username.getText().toString().trim();
        password = et_password.getText().toString().trim();

        //Showing the progress dialog
        final ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);

        if(!helper.IsReachable(Login.this, helper.get_login_url())){
            Toast.makeText(Login.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
            bar.setVisibility(View.GONE);
        }else {
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(Login.this, "Some fields are missing!", Toast.LENGTH_LONG).show();
                bar.setVisibility(View.GONE);
            }else {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "" + helper.get_login_url(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
                                    JSONObject stringData = result.getJSONObject(0);

                                    user_id = stringData.getString(KEY_USER_ID);
                                    username = stringData.getString(KEY_USERNAME);
                                    password = stringData.getString(KEY_PASSWORD);
                                    status = stringData.getString(KEY_STATUS);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                bar.setVisibility(View.GONE);
                                Log.e("Check", response);
                                try{
                                    if(status.toString().trim().equals("success")){
                                        Toast.makeText(Login.this, "Welcome " + username, Toast.LENGTH_LONG).show();

                                        SharedPreferences settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                        SharedPreferences.Editor prefEditor = settings.edit();
                                        prefEditor.putInt("user_id", Integer.parseInt(user_id));
                                        prefEditor.putString("username", username);
                                        prefEditor.commit();

                                        Intent intent = new Intent(Login.this, Dashboard.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        Toast.makeText(Login.this, "Incorrect login credentials!", Toast.LENGTH_LONG).show();
                                    }
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Login.this, "Cannot connect to the server!" + error, Toast.LENGTH_LONG).show();
                                bar.setVisibility(View.GONE);
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put(KEY_USERNAME, username);
                        map.put(KEY_PASSWORD, password);
                        return map;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }
        }
    }

    //sends to forgot password activity
    public void forgot_password(View view) {
        startActivity(new Intent(Login.this, ForgotPassword.class));
    }

    //sends to register activity
    public void register(View view) {
        startActivity(new Intent(Login.this, Register.class));
    }
}

