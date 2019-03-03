package com.android.nam.plt_money;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class Loan extends AppCompatActivity {
    Helper helper = new Helper();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button btn_create = (Button) findViewById(R.id.createBtn);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Loan.this, AddLoan.class));
            }
        });

        getData();
    }

    private void getData() {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        final int user_id = prefs.getInt("user_id", 0);

        if(!helper.IsReachable(Loan.this, helper.get_all_loan_url() + user_id)){
            Toast.makeText(Loan.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
        }else {
            StringRequest stringRequest = new StringRequest(helper.get_all_loan_url() + user_id, new Response.Listener<String>() {
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
                        Toast.makeText(Loan.this, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });

            RequestQueue requestQueue = Volley.newRequestQueue(Loan.this);
            requestQueue.add(stringRequest);
        }
    }

    private void showJSON(String json){
        ListView listView = (ListView) findViewById(R.id.loan_listview);
        ParseLoan parse = new ParseLoan(json);
        parse.parseLoan();

        final LoanAdapter loanAdapter = new LoanAdapter(Loan.this, ParseLoan.loan_id, ParseLoan.user_id, ParseLoan.loan_borrower, ParseLoan.loan_date_borrowed, ParseLoan.loan_due_date, ParseLoan.loan_due_time, ParseLoan.loan_amount, ParseLoan.loan_status);
        listView.setAdapter(loanAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final int loan_id = Integer.parseInt(loanAdapter.getItem(position));

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Loan.this);
            alertDialogBuilder.setMessage("Choose action: ");
            alertDialogBuilder.setNegativeButton("UPDATE", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(Loan.this, UpdateLoan.class);
                intent.putExtra("loan_id", loan_id);
                startActivity(intent);
                }
            });

            alertDialogBuilder.setPositiveButton("DELETE",new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    delete_loan(loan_id);
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            }
        });
    }

    private void delete_loan(final int loan_id) {
        if (!helper.IsReachable(Loan.this, helper.get_delete_loan_url() + loan_id)) {
            Toast.makeText(Loan.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
        } else {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Loan.this);

            alertDialogBuilder.setMessage("Are you sure you want to delete this loan?");
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, helper.get_delete_loan_url() + loan_id,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.toString().trim().equals("success")) {
                                Toast.makeText(Loan.this, "Loan was successfully deleted!", Toast.LENGTH_LONG).show();
                                getData();
                            } else {
                                Toast.makeText(Loan.this, "Failed in deleting a loan!" + response, Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(Loan.this, "Failed in connecting the database!", Toast.LENGTH_LONG).show();
                        }
                    }) {
                };

                RequestQueue requestQueue = Volley.newRequestQueue(Loan.this);
                requestQueue.add(stringRequest);
                }
            });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });

            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
