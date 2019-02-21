package com.android.nam.plt_money;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ForgotPassword extends AppCompatActivity {
    Helper helper = new Helper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button check_username = (Button) findViewById(R.id.check_username);
        final Button change_password = (Button) findViewById(R.id.change_password);

        check_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et_password1 = (EditText) findViewById(R.id.password1);
                final EditText et_password2 = (EditText) findViewById(R.id.password2);
                final EditText et_username = (EditText) findViewById(R.id.username);

                final String username = et_username.getText().toString().trim();

                //Showing the progress dialog
                final ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
                bar.setVisibility(View.VISIBLE);

                if (!helper.IsReachable(ForgotPassword.this, helper.forgot_password_url())) {
                    Toast.makeText(ForgotPassword.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                    bar.setVisibility(View.GONE);
                } else {
                    if (username.toString().trim().isEmpty()) {
                        Toast.makeText(ForgotPassword.this, "Username is empty!", Toast.LENGTH_LONG).show();
                        bar.setVisibility(View.GONE);
                    } else {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, helper.forgot_password_url(),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(final String response) {
                                        bar.setVisibility(View.GONE);
                                        if (response.toString().trim().equals("empty")) {
                                            Toast.makeText(ForgotPassword.this, "Username does not exists!", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(ForgotPassword.this, username + " was successfully retrieved in the records!", Toast.LENGTH_LONG).show();
                                            check_username.setVisibility(View.GONE);
                                            change_password.setVisibility(View.VISIBLE);
                                            et_password1.setVisibility(View.VISIBLE);
                                            et_password2.setVisibility(View.VISIBLE);
                                            et_username.setFocusable(false);

                                            SharedPreferences settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                            SharedPreferences.Editor prefEditor = settings.edit();

                                            prefEditor.putString("response", response);

                                            prefEditor.commit();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(ForgotPassword.this, "Error in connecting the server!", Toast.LENGTH_LONG).show();
                                        bar.setVisibility(View.GONE);
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("username", username);

                                return params;
                            }

                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(ForgotPassword.this);
                        requestQueue.add(stringRequest);
                    }
                }
            }
        });

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText et_password1 = (EditText) findViewById(R.id.password1);
                final EditText et_password2 = (EditText) findViewById(R.id.password2);

                final String password1 = et_password1.getText().toString().trim();
                final String password2 = et_password2.getText().toString().trim();

                //Showing the progress dialog
                final ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
                bar.setVisibility(View.VISIBLE);

                if (!helper.IsReachable(ForgotPassword.this, helper.change_password_url())) {
                    Toast.makeText(ForgotPassword.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                    bar.setVisibility(View.GONE);
                } else {
                    if (!(password1.toString().trim().equals(password2))) {
                        Toast.makeText(ForgotPassword.this, "Password does not match!", Toast.LENGTH_LONG).show();
                        bar.setVisibility(View.GONE);
                    }else if (password1.toString().trim().isEmpty() || password2.toString().trim().isEmpty()) {
                        Toast.makeText(ForgotPassword.this, "Password is empty!", Toast.LENGTH_LONG).show();
                        bar.setVisibility(View.GONE);
                    } else {
                        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, helper.change_password_url(),
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response2) {
                                        bar.setVisibility(View.GONE);
                                        if (response2.toString().trim().equals("failed")) {
                                            Toast.makeText(ForgotPassword.this, "The updating of password fails!", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(ForgotPassword.this, "You successfully changed your password!", Toast.LENGTH_LONG).show();

                                            SharedPreferences settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                            SharedPreferences.Editor prefEditor = settings.edit();

                                            //remove the user_id
                                            prefEditor.remove("response");
                                            prefEditor.commit();

                                            finish();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(ForgotPassword.this, "Error in connecting the server!", Toast.LENGTH_LONG).show();
                                        bar.setVisibility(View.GONE);
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params2 = new HashMap<String, String>();
                                SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                String response = prefs.getString("response", null);

                                params2.put("user_id", response);
                                params2.put("password", password1.toString().trim());

                                return params2;
                            }

                        };

                        RequestQueue requestQueue2 = Volley.newRequestQueue(ForgotPassword.this);
                        requestQueue2.add(stringRequest2);
                    }
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
