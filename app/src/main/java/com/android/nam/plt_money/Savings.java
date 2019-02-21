package com.android.nam.plt_money;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Savings extends AppCompatActivity{
    private TextView txtSavingsWeekly, txtSavingsMonthly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.savings);
        myDB myDB = new myDB(getBaseContext());
        DateFormat df = new SimpleDateFormat("MM-DD-YYYY");
        Date budgetDate = null;
        try{
            budgetDate = (Date)myDB.getObject("BudgetDate", Date.class);
        }catch (Exception e){
            budgetDate = new Date();
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(budgetDate);
        cal1.add(Calendar.MONTH, 1);
        Date monthago = cal1.getTime();
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(budgetDate);
        cal2.add(Calendar.DATE, 7);
        Date future = cal1.getTime();
        Date today = new Date();
        txtSavingsWeekly = findViewById(R.id.txtSavingsWeekly);
        txtSavingsMonthly = findViewById(R.id.txtSavingsMonthly);

        if(today.after(future)){
            double weekly = myDB.getDouble("SavingsWeekly", 0);
            double monthly = myDB.getDouble("SavingsMonthly", 0);
            txtSavingsWeekly.setText("" + monthly/4);
            txtSavingsMonthly.setText("" +monthly);
        }
    }
}
