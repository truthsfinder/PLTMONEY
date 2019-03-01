package com.android.nam.plt_money;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Expenses extends AppCompatActivity {
    Helper helper = new Helper();

    public static final String JSON_ARRAY = "result";
    public static final String EXPENSE_DATE = "expense_date";
    public static final String EXPENSE_AMOUNT = "expense_amount";
    public static final String EXPENSE_CATEGORY = "expense_category";
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_NAME = "category_name";
    public static final String CATEGORY_STATUS = "status";

    private String expense_date;
    private String expense_amount;
    private String expense_category;
    private int category_id;
    private String category_name;
    private String category_status;

    private ArrayList<String> Categories = new ArrayList<String>();

    final Calendar myCalendar = Calendar.getInstance();
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expenses);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        getData();
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        EditText et_expense_date = (EditText) findViewById(R.id.editExpensesDate);

        et_expense_date.setText(new StringBuilder().append(year).append("/")
                .append(month).append("/").append(day));
    }

    public void getData(){
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        final int user_id = prefs.getInt("user_id", 0);
        final Spinner spinner = (Spinner) findViewById(R.id.spinnerExpensesCategory);

        //Showing the progress dialog
        final ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);

        //display added categories
        if(!helper.IsReachable(Expenses.this, helper.get_categories_url() + user_id)){
            Toast.makeText(Expenses.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
            bar.setVisibility(View.GONE);
        }else {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, helper.get_categories_url() + user_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getInt("success") == 1){
                                JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
                                for(int i=0; i<result.length(); i++){
                                    JSONObject stringData = result.getJSONObject(i);
                                    category_name = stringData.getString(CATEGORY_NAME);
                                    Categories.add(category_name);
                                }
                            }
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(Expenses.this, android.R.layout.simple_spinner_item, Categories);
                            spinner.setAdapter(spinnerArrayAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        bar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Expenses.this, "Failed in connecting the database!", Toast.LENGTH_LONG).show();
                        bar.setVisibility(View.GONE);
                    }
                }) {

            };

            RequestQueue requestQueue = Volley.newRequestQueue(Expenses.this);
            requestQueue.add(stringRequest);
        }
    }
    
    public void add_expense(View view){
        final int budget_id = getIntent().getExtras().getInt("budget_id");
        EditText et_expense_date = (EditText) findViewById(R.id.editExpensesDate);
        EditText et_expense_amount = (EditText) findViewById(R.id.editExpensesAmount);
        Spinner sp_category = (Spinner) findViewById(R.id.spinnerExpensesCategory);

        expense_date = et_expense_date.getText().toString().trim();
        expense_amount = et_expense_amount.getText().toString().trim();
        category_name = sp_category.getSelectedItem().toString().trim();

        //Showing the progress dialog
        final ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);

        if (!helper.IsReachable(Expenses.this, helper.get_add_expense_url() + budget_id)) {
            Toast.makeText(Expenses.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
            bar.setVisibility(View.GONE);
        } else {
            if (et_expense_date.getText().toString().trim().equals("") || et_expense_amount.getText().toString().trim().equals("") || sp_category.getSelectedItem().toString().trim().equals("")) {
                Toast.makeText(Expenses.this, "Expenses is empty!", Toast.LENGTH_LONG).show();
                bar.setVisibility(View.GONE);
            } else {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Expenses.this);

                alertDialogBuilder.setMessage("Are you sure you want to add this as your expense?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, helper.get_add_expense_url() + budget_id,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.toString().trim().equals("success")){
                                    Toast.makeText(Expenses.this, "Expenses was successfully added!", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(Expenses.this, Dashboard.class));
                                    finish();
                                }else{
                                    Toast.makeText(Expenses.this, "Failed in adding an expense!" + response, Toast.LENGTH_LONG).show();
                                }
                                bar.setVisibility(View.GONE);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(Expenses.this, "Failed in connecting the database!", Toast.LENGTH_LONG).show();
                                bar.setVisibility(View.GONE);
                            }
                        }) {

                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put(EXPENSE_DATE, expense_date);
                            params.put(EXPENSE_AMOUNT, expense_amount);
                            params.put(CATEGORY_NAME, category_name);

                            return params;
                        }

                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(Expenses.this);
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

    public void add_category(View view){
        startActivity(new Intent(Expenses.this, Categories.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}