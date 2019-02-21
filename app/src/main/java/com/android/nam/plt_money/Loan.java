package com.android.nam.plt_money;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import java.util.ArrayList;

public class Loan extends AppCompatActivity {
    ArrayList<String> loan;

    private TableLayout loan_tbl;
    private Button createBtn, updateBtn, deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loan);

        myDB myDB = new myDB(getBaseContext());
        loan_tbl = findViewById(R.id.loan_tbl);
        createBtn = findViewById(R.id.createBtn);
        updateBtn = findViewById(R.id.updateBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Loan.this, AddUpdateLoan.class);
                startActivity(myIntent);
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Loan.this, AddUpdateLoan.class);
                startActivity(myIntent);
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
