package com.android.nam.plt_money;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class Register extends AppCompatActivity {
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";

    private String username;
    private String password;
    private String password2;

    Helper helper = new Helper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void register(View view){
        //initialization
        EditText et_username = (EditText) findViewById(R.id.editRegUsername);
        EditText et_password = (EditText) findViewById(R.id.editRegPassword);
        EditText et_password2 = (EditText) findViewById(R.id.editConfirmPW);

        username = et_username.getText().toString().trim();
        password = et_password.getText().toString().trim();
        password2 = et_password2.getText().toString().trim();

        if(!helper.IsReachable(Register.this, helper.get_check_username_url())){
            Toast.makeText(Register.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }else {
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(Register.this, "Fields are empty!", Toast.LENGTH_LONG).show();
            }else if(!password.toString().trim().equals(password2)){
                Toast.makeText(Register.this, "Password does not match!", Toast.LENGTH_LONG).show();
            } else {
                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, helper.get_check_username_url(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(final String response1) {
                                if(response1.toString().equals("empty")){

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Register.this);
                                    alertDialogBuilder.setMessage("Are you sure you want to save this details?");
                                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, helper.get_register_url(),
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response2) {
                                                        if(response2.toString().trim().equals("success")){
                                                            Toast.makeText(Register.this, "Successfully Registered!", Toast.LENGTH_LONG).show();
                                                            startActivity(new Intent(Register.this, Login.class));
                                                            finish();
                                                        }else{
                                                            Toast.makeText(Register.this, "Registration Failed!", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                },
                                                new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(Register.this, "Error in communicating the server!", Toast.LENGTH_LONG).show();
                                                    }
                                                }) {

                                                @Override
                                                protected Map<String, String> getParams() {
                                                    Map<String, String> params2 = new HashMap<String, String>();
                                                    params2.put(KEY_USERNAME, username);
                                                    params2.put(KEY_PASSWORD, password);

                                                    return params2;
                                                }

                                            };

                                            RequestQueue requestQueue2 = Volley.newRequestQueue(Register.this);
                                            requestQueue2.add(stringRequest2);
                                        }
                                    });

                                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {

                                        }
                                    });

                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }else {
                                    Toast.makeText(Register.this, "Username already exists!", Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Register.this, "Error in connecting the server!", Toast.LENGTH_LONG).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params1 = new HashMap<String, String>();
                        params1.put("username", username);

                        return params1;
                    }
                };

                RequestQueue requestQueue1 = Volley.newRequestQueue(Register.this);
                requestQueue1.add(stringRequest1);
            }
        }
    }

    public void cancel(View view){
        finish();
    }
}
