package com.android.nam.plt_money;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
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
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Savings extends AppCompatActivity{
    View view;
    String[] date_category = new String[]{"Daily", "Weekly", "Monthly"};
    Helper helper = new Helper();

    public static final String JSON_ARRAY = "result";

    private double savings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner spinner = (Spinner) findViewById(R.id.spinner_savings);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Savings.this, android.R.layout.simple_spinner_dropdown_item, date_category);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    getSavings("Daily");
                }else if (position == 1){
                    getSavings("Weekly");
                }else{
                    getSavings("Monthly");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //getData(view);
    }

    private void getSavings(String date_category){
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        final int user_id = prefs.getInt("user_id", 0);
        String url = "";

        if(date_category == "Daily"){
            url = helper.get_daily_savings_url();
        }else if(date_category == "Weekly"){
            url = helper.get_weekly_savings_url();
        }else{
            url = helper.get_monthly_savings_url();
        }

        //Showing the progress dialog
        final ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);

        //SET DISPLAY FOR BUDGET DETAILS
        if(!helper.IsReachable(Savings.this, url + user_id)){
            Toast.makeText(Savings.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
            bar.setVisibility(View.GONE);
        }else {
            StringRequest stringRequest = new StringRequest(Request.Method.GET,url + user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            savings = Double.parseDouble(response);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Savings.this, "No data available!", Toast.LENGTH_LONG).show();
                        }

                        try{
                            TextView tv_savings = (TextView)findViewById(R.id.txtSavingsMonthly);
                            DecimalFormat df = new DecimalFormat("#,#00.00#");

                            tv_savings.setText(df.format(savings));

                            bar.setVisibility(View.GONE);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        bar.setVisibility(View.GONE);
                        Toast.makeText(Savings.this, "Error in connecting the server!", Toast.LENGTH_LONG).show();
                    }
                }) {
            };

            RequestQueue requestQueue = Volley.newRequestQueue(Savings.this);
            requestQueue.add(stringRequest);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
