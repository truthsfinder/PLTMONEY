package com.android.nam.plt_money;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateLoan extends AppCompatActivity {
    Helper helper = new Helper();
    public static final String JSON_ARRAY = "result";
    public static final String LOAN_ID = "loan_id";
    public static final String USER_ID = "user_id";
    public static final String LOAN_BORROWER = "loan_borrower";
    public static final String LOAN_DATE_BORROWED = "loan_date_borrowed";
    public static final String LOAN_DUE_DATE = "loan_due_date";
    public static final String LOAN_AMOUNT = "loan_amount";
    public static final String LOAN_STATUS = "loan_status";


    private static String loan_id;
    private static String user_id;
    private static String loan_borrower;
    private static String loan_date_borrowed;
    private static String loan_due_date;
    private static String loan_amount;
    private static String loan_status;

    final Calendar myCalendar = Calendar.getInstance();
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_loan);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        showDate(year, month+1, day);
        showDate2(year, month+1, day);

        getData();

        Button btn_cancel = (Button) findViewById(R.id.loancancelBtn);
        Button btn_save = (Button) findViewById(R.id.loansaveBtn);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int loan_id_data = getIntent().getExtras().getInt("loan_id");
                final EditText et_loan_borrower = (EditText) findViewById(R.id.editName);
                final EditText et_loan_date_borrowed = (EditText) findViewById(R.id.editDate);
                final EditText et_loan_due_date = (EditText) findViewById(R.id.editDue);
                final EditText et_loan_amount = (EditText) findViewById(R.id.editLoanAmount);

                //Showing the progress dialog
                final ProgressBar bar = (ProgressBar)findViewById(R.id.progressBar);
                bar.setVisibility(View.VISIBLE);

                if (!helper.IsReachable(UpdateLoan.this, helper.get_update_loan_url() + loan_id_data)) {
                    Toast.makeText(UpdateLoan.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                    bar.setVisibility(View.GONE);
                } else {
                    if (et_loan_borrower.getText().toString().trim().equals("") || et_loan_date_borrowed.getText().toString().trim().equals("") || et_loan_due_date.getText().toString().trim().equals("") || et_loan_amount.getText().toString().trim().equals("")) {
                        Toast.makeText(UpdateLoan.this, "Some fields are missing!", Toast.LENGTH_LONG).show();
                        bar.setVisibility(View.GONE);
                    } else {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(UpdateLoan.this);

                        alertDialogBuilder.setMessage("Are you sure you want to update this loan?");
                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, helper.get_update_loan_url() + loan_id_data,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        bar.setVisibility(View.GONE);

                                        if(response.toString().trim().equals("success")){
                                            Toast.makeText(UpdateLoan.this, "Loan was successfully updated!", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(UpdateLoan.this, Loan.class));
                                            finish();
                                        }else{
                                            Toast.makeText(UpdateLoan.this, "Failed in updating a loan!", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        bar.setVisibility(View.GONE);
                                        Toast.makeText(UpdateLoan.this, "Failed in connecting the database!", Toast.LENGTH_LONG).show();
                                    }
                                }) {

                                @Override
                                protected Map<String, String> getParams() {
                                    Map<String, String> params = new HashMap<String, String>();
                                    params.put("loan_borrower", et_loan_borrower.getText().toString().trim());
                                    params.put("loan_date_borrowed", et_loan_date_borrowed.getText().toString().trim());
                                    params.put("loan_due_date", et_loan_due_date.getText().toString().trim());
                                    params.put("loan_amount", et_loan_amount.getText().toString().trim());

                                    return params;
                                }

                            };

                            RequestQueue requestQueue = Volley.newRequestQueue(UpdateLoan.this);
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

    public void getData(){
        final int loan_id_data = getIntent().getExtras().getInt("loan_id");
        final EditText et_loan_borrower = (EditText) findViewById(R.id.editName);
        final EditText et_loan_date_borrowed = (EditText) findViewById(R.id.editDate);
        final EditText et_loan_due_date = (EditText) findViewById(R.id.editDue);
        final EditText et_loan_amount = (EditText) findViewById(R.id.editLoanAmount);

        //Showing the progress dialog
        final ProgressBar bar = (ProgressBar) findViewById(R.id.progressBar);
        bar.setVisibility(View.VISIBLE);

        //display added categories
        if(!helper.IsReachable(UpdateLoan.this, helper.get_loan_by_id_url() + loan_id_data)){
            Toast.makeText(UpdateLoan.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
            bar.setVisibility(View.GONE);
        }else {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, helper.get_loan_by_id_url() + loan_id_data,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);
                            JSONObject stringData = result.getJSONObject(0);

                            loan_id = stringData.getString(LOAN_ID);
                            user_id = stringData.getString(USER_ID);
                            loan_borrower = stringData.getString(LOAN_BORROWER);
                            loan_date_borrowed = stringData.getString(LOAN_DATE_BORROWED);
                            loan_due_date = stringData.getString(LOAN_DUE_DATE);
                            loan_amount = stringData.getString(LOAN_AMOUNT);
                            loan_status = stringData.getString(LOAN_STATUS);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        bar.setVisibility(View.GONE);
                        try{
                            et_loan_borrower.setText(loan_borrower);
                            et_loan_date_borrowed.setText(loan_date_borrowed);
                            et_loan_due_date.setText(loan_due_date);
                            et_loan_amount.setText(loan_amount);

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UpdateLoan.this, "Failed in connecting the database!", Toast.LENGTH_LONG).show();
                        bar.setVisibility(View.GONE);
                    }
                }) {
            };

            RequestQueue requestQueue = Volley.newRequestQueue(UpdateLoan.this);
            requestQueue.add(stringRequest);
        }
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(998);
    }

    @SuppressWarnings("deprecation")
    public void setDate2(View view) {
        showDialog(999);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 998) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }

        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener2, year, month, day);
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

    private DatePickerDialog.OnDateSetListener myDateListener2 = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate2(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        EditText editDate = (EditText) findViewById(R.id.editDate);

        editDate.setText(new StringBuilder().append(year).append("/")
                .append(month).append("/").append(day));
    }

    private void showDate2(int year, int month, int day) {
        EditText editDue = (EditText) findViewById(R.id.editDue);

        editDue.setText(new StringBuilder().append(year).append("/")
                .append(month).append("/").append(day));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
