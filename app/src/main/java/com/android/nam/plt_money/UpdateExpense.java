package com.android.nam.plt_money;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateExpense extends AppCompatActivity {
    Helper helper = new Helper();

    public static final String JSON_ARRAY = "result";
    public static final String EXPENSE_DATE = "expense_date";
    public static final String EXPENSE_NAME = "expense_name";
    public static final String EXPENSE_AMOUNT = "expense_amount";
    public static final String EXPENSE_CATEGORY = "expense_category";
    public static final String EXPENSE_ID = "expense_id";
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_NAME = "category_name";
    public static final String CATEGORY_STATUS = "status";

    private String expense_date;
    private String expense_amount;
    private String expense_name;
    private String expense_category;
    private int category_id;
    private String expense;
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
        setContentView(R.layout.activity_update_expense);

        getData();
        getCategories();

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        Button btn_updateBtn = (Button) findViewById(R.id.updateBtn);

        btn_updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int expense_id = getIntent().getExtras().getInt("expense_id");
                final Spinner spinner = (Spinner) findViewById(R.id.spinnerExpensesCategory);
                final EditText et_name = (EditText) findViewById(R.id.editExpensesName);
                final EditText et_date = (EditText) findViewById(R.id.editExpensesDate);
                final EditText et_amount = (EditText) findViewById(R.id.editExpensesAmount);

                if (!helper.IsReachable(UpdateExpense.this, helper.get_update_expense_url() + expense_id)) {
                    Toast.makeText(UpdateExpense.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                } else {
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(UpdateExpense.this);

                    alertDialogBuilder.setMessage("Are you sure you want to update this expense?");
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, helper.get_update_expense_url() + expense_id,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            Log.e("Check", response);
                                            if(response.toString().trim().equals("success")){
                                                Toast.makeText(UpdateExpense.this, "Expenses was successfully updated!", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(UpdateExpense.this, Dashboard.class));
                                                finish();
                                            }else{
                                                Toast.makeText(UpdateExpense.this, "Failed in updating an expense!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(UpdateExpense.this, "Failed in connecting the database!", Toast.LENGTH_LONG).show();
                                        }
                                    }) {

                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("expense_date", et_date.getText().toString().trim());
                                    params.put("expense_name", et_name.getText().toString().trim());
                                    params.put("expense_amount", et_amount.getText().toString().trim());
                                    params.put("expense_category", spinner.getSelectedItem().toString().trim());

                                    return params;
                                }
                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(UpdateExpense.this);
                            requestQueue.add(stringRequest);
                        }
                    });

                    alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });

                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
    }

    public void getCategories(){
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        final int user_id = prefs.getInt("user_id", 0);
        final Spinner spinner = (Spinner) findViewById(R.id.spinnerExpensesCategory);

        //display added categories
        if(!helper.IsReachable(UpdateExpense.this, helper.get_categories_url() + user_id)){
            Toast.makeText(UpdateExpense.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
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
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(UpdateExpense.this, android.R.layout.simple_spinner_item, Categories);
                            spinner.setAdapter(spinnerArrayAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UpdateExpense.this, "Failed in connecting the database!", Toast.LENGTH_LONG).show();
                    }
                }) {
            };

            RequestQueue requestQueue = Volley.newRequestQueue(UpdateExpense.this);
            requestQueue.add(stringRequest);
        }
    }

    public void getData(){
        final int expense_id = getIntent().getExtras().getInt("expense_id");
        final Spinner spinner = (Spinner) findViewById(R.id.spinnerExpensesCategory);
        final EditText et_date = (EditText) findViewById(R.id.editExpensesDate);
        final EditText et_name = (EditText) findViewById(R.id.editExpensesName);
        final EditText et_amount = (EditText) findViewById(R.id.editExpensesAmount);

        //Showing the progress dialog
        final ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);

        //display added categories
        if(!helper.IsReachable(UpdateExpense.this, helper.get_expense_by_id_url() + expense_id)){
            Toast.makeText(UpdateExpense.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
            bar.setVisibility(View.GONE);
        }else {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, helper.get_expense_by_id_url() + expense_id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
                            JSONObject stringData = result.getJSONObject(0);

                            expense = stringData.getString(EXPENSE_ID);
                            expense_name = stringData.getString(EXPENSE_NAME);
                            expense_date = stringData.getString(EXPENSE_DATE);
                            expense_amount = stringData.getString(EXPENSE_AMOUNT);
                            expense_category = stringData.getString(CATEGORY_NAME);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        bar.setVisibility(View.GONE);
                        try{
                            et_name.setText(expense_name);
                            et_date.setText(expense_date);
                            et_amount.setText(expense_amount);
                            spinner.setSelected(true);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UpdateExpense.this, "Failed in connecting the database!", Toast.LENGTH_LONG).show();
                        bar.setVisibility(View.GONE);
                    }
                }) {
            };

            RequestQueue requestQueue = Volley.newRequestQueue(UpdateExpense.this);
            requestQueue.add(stringRequest);
        }
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

    public void cancel(View view){
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
