package com.android.nam.plt_money;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import java.text.DecimalFormat;

public class Summary extends AppCompatActivity{
    Helper helper = new Helper();
    String[] date_category = new String[]{"Daily", "Weekly", "Monthly"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Spinner sp_date = (Spinner)findViewById(R.id.txtDate);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Summary.this, android.R.layout.simple_spinner_dropdown_item, date_category);
        sp_date.setAdapter(adapter);

        getData(0);
        getTotal(0);

        sp_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    getData(0);
                    getTotal(0);
                }else if (position == 1){
                    getData(1);
                    getTotal(1);
                }else{
                    getData(2);
                    getTotal(2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getTotal(int budget_category){

        //Showing the progress dialog
        final ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);

        //SET DISPLAY FOR BUDGET DETAILS
        if(!helper.IsReachable(Summary.this, helper.get_total_expenses_url() + budget_category)){
            Toast.makeText(Summary.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
            bar.setVisibility(View.GONE);
        }else {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, helper.get_total_expenses_url() + budget_category,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            TextView tv_total = (TextView) findViewById(R.id.txtTotalAmount);
                            DecimalFormat df = new DecimalFormat("#,#00.00#");

                            try {
                                tv_total.setText(df.format(Double.parseDouble(response)));
                            } catch (Exception e) {
                                e.printStackTrace();
                                Toast.makeText(Summary.this, "No budget available!", Toast.LENGTH_LONG).show();
                                tv_total.setText(df.format(0));
                            }

                            bar.setVisibility(View.GONE);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            bar.setVisibility(View.GONE);
                            Toast.makeText(Summary.this, "Error in connecting the server!", Toast.LENGTH_LONG).show();
                        }
                    }) {
            };

            RequestQueue requestQueue = Volley.newRequestQueue(Summary.this);
            requestQueue.add(stringRequest);
        }
    }

    private void getData(int budget_category) {

        if(!helper.IsReachable(Summary.this, helper.get_summary_expenses_url() + budget_category)){
            Toast.makeText(Summary.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }else {
            StringRequest stringRequest = new StringRequest(helper.get_summary_expenses_url() + budget_category, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        showJSON(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Summary.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(Summary.this);
            requestQueue.add(stringRequest);
        }
    }

    private void showJSON(String json){
        ListView listView = (ListView) findViewById(R.id.summary_listview);
        ParseExpense parse = new ParseExpense(json);
        parse.parseExpense();

        SummaryAdapter summaryAdapter = new SummaryAdapter(Summary.this, ParseExpense.expense_id, ParseExpense.budget_id, ParseExpense.category_name, ParseExpense.expense_date, ParseExpense.expense_amount);
        listView.setAdapter(summaryAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
