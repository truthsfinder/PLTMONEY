package com.android.nam.plt_money;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

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

public class UpdateBudget extends AppCompatActivity{
    View view;
    public static final String JSON_ARRAY = "result";

    String[] date_category = new String[]{"Daily", "Weekly", "Monthly"};
    public static final String BUDGET_ID = "budget_id";
    public static final String USER_ID = "user_id";
    public static final String BUDGET_AMOUNT = "budget_amount";
    public static final String BUDGET_CATEGORY = "budget_category";
    public static final String STATUS = "status";

    private int budget_id_v1;
    private int user_id;
    private String budget_amount;
    private String budget_category;
    private String status;

    Helper helper = new Helper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_budget);

        Spinner spinner = (Spinner) findViewById(R.id.budget);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateBudget.this, android.R.layout.simple_spinner_dropdown_item, date_category);
        spinner.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button proceed = (Button) findViewById(R.id.proceedBtn);
        final EditText et_budget_amount = (EditText) findViewById(R.id.editBudget);
        final Spinner sp_budget_category = (Spinner) findViewById(R.id.budget);
        final ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
        final int budget_id = getIntent().getExtras().getInt("budget_id", 0);

        //Showing the progress dialog
        bar.setVisibility(View.VISIBLE);

        //display category name on edittext
        if(!helper.IsReachable(UpdateBudget.this, helper.get_display_budget_url() + budget_id)){
            Toast.makeText(UpdateBudget.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
            bar.setVisibility(View.GONE);
        }else {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, helper.get_display_budget_url() + budget_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
                            JSONObject stringData = result.getJSONObject(0);

                            budget_id_v1 = stringData.getInt(BUDGET_ID);
                            user_id = stringData.getInt(USER_ID);
                            budget_amount = stringData.getString(BUDGET_AMOUNT);
                            budget_category = stringData.getString(BUDGET_CATEGORY);
                            status = stringData.getString(STATUS);

                            if(status.toString().trim().equals("success")){
                                et_budget_amount.setText(budget_amount + "");
                                sp_budget_category.setSelection(adapter.getPosition(budget_category));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        bar.setVisibility(View.GONE);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UpdateBudget.this, "Failed in connecting the database!", Toast.LENGTH_LONG).show();
                        bar.setVisibility(View.GONE);
                    }
                }) {

            };

            RequestQueue requestQueue = Volley.newRequestQueue(UpdateBudget.this);
            requestQueue.add(stringRequest);
        }

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            budget_amount = et_budget_amount.getText().toString().trim();
            budget_category = sp_budget_category.getSelectedItem().toString().trim();

            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            final int user_id = prefs.getInt("user_id", 0);

            //Showing the progress dialog
            bar.setVisibility(View.VISIBLE);

            if (!helper.IsReachable(UpdateBudget.this, helper.get_add_budget_url() + user_id)) {
                Toast.makeText(UpdateBudget.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                bar.setVisibility(View.GONE);
            } else {
                if (et_budget_amount.getText().toString().trim().equals("")) {
                    Toast.makeText(UpdateBudget.this, "Budget is empty!", Toast.LENGTH_LONG).show();
                    bar.setVisibility(View.GONE);
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UpdateBudget.this);

                    alertDialogBuilder.setMessage("Are you sure you want to update this budget?");
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, helper.get_add_budget_url() + user_id,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    bar.setVisibility(View.GONE);

                                    if(response.toString().trim().equals("success")){
                                        Toast.makeText(UpdateBudget.this, "Budget was successfully updated!", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(UpdateBudget.this, Dashboard.class));
                                        finish();
                                    }else{
                                        Toast.makeText(UpdateBudget.this, "Failed in updating a budget!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    bar.setVisibility(View.GONE);
                                    Toast.makeText(UpdateBudget.this, "Failed in connecting the database!", Toast.LENGTH_LONG).show();
                                }
                            }) {

                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put(BUDGET_AMOUNT, budget_amount);
                                params.put(BUDGET_CATEGORY, budget_category);

                                return params;
                            }

                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(UpdateBudget.this);
                        requestQueue.add(stringRequest);
                        }
                    });

                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
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
}
