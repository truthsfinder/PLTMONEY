package com.android.nam.plt_money;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddLoan extends AppCompatActivity{
    View view;
    private EditText editName, editDate, editDue, editLoanAmount, etTime;
    private Button loancancelBtn, loansaveBtn;
    Helper helper = new Helper();

    private ArrayList<String> Categories = new ArrayList<String>();

    final Calendar myCalendar = Calendar.getInstance();
    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_loan);

        calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        showDate(year, month+1, day);
        showDate2(year, month+1, day);

        editName = findViewById(R.id.editName);
        editDate = findViewById(R.id.editDate);
        editDue = findViewById(R.id.editDue);
        editLoanAmount = findViewById(R.id.editLoanAmount);
        etTime = findViewById(R.id.etTime);

        loancancelBtn = findViewById(R.id.loancancelBtn);
        loansaveBtn = findViewById(R.id.loansaveBtn);

        loancancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loansaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            final int user_id = prefs.getInt("user_id", 0);

            //Showing the progress dialog
            final ProgressBar bar = (ProgressBar)findViewById(R.id.progressBar);
            bar.setVisibility(View.VISIBLE);

            if (!helper.IsReachable(AddLoan.this, helper.get_add_loan_url() + user_id)) {
                Toast.makeText(AddLoan.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
                bar.setVisibility(View.GONE);
            } else {
                if (editName.getText().toString().trim().equals("") || editDate.getText().toString().trim().equals("") || editDue.getText().toString().trim().equals("") || editLoanAmount.getText().toString().trim().equals("")) {
                    Toast.makeText(AddLoan.this, "Some fields are missing!", Toast.LENGTH_LONG).show();
                    bar.setVisibility(View.GONE);
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddLoan.this);

                    alertDialogBuilder.setMessage("Are you sure you want to add this loan?");
                    alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, helper.get_add_loan_url() + user_id,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    bar.setVisibility(View.GONE);

                                    if(response.toString().trim().equals("success")){
                                        Toast.makeText(AddLoan.this, "Loan was successfully added!", Toast.LENGTH_LONG).show();
                                        sendNotification("Your loan will be due on " + editDue.getText().toString().trim() + " at " + etTime.getText().toString().trim());
                                        startActivity(new Intent(AddLoan.this, Loan.class));
                                        finish();
                                    }else{
                                        Toast.makeText(AddLoan.this, "Failed in adding a loan!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    bar.setVisibility(View.GONE);
                                    Toast.makeText(AddLoan.this, "Failed in connecting the database!", Toast.LENGTH_LONG).show();
                                }
                            }) {

                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("editName", editName.getText().toString().trim());
                                params.put("editDate", editDate.getText().toString().trim());
                                params.put("editDue", editDue.getText().toString().trim());
                                params.put("etTime", etTime.getText().toString().trim());
                                params.put("editLoanAmount", editLoanAmount.getText().toString().trim());

                                return params;
                            }

                        };

                        RequestQueue requestQueue = Volley.newRequestQueue(AddLoan.this);
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

    public void sendNotification(String message) {

        Intent intent = new Intent(AddLoan.this, Loan.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(AddLoan.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(AddLoan.this,"default")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Loan Due Date")
                .setContentText(message)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId("com.android.nam.plt_money");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "com.android.nam.plt_money",
                    "My App",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        notificationManager.notify(2,builder.build());
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
}
