package com.android.nam.plt_money;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddUpdateLoan extends AppCompatActivity{
    private EditText editName, editDate, editDue, editLoanAmount;
    private Button loancancelBtn, loansaveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_update_loan);

        editName = findViewById(R.id.editName);
        editDate = findViewById(R.id.editDate);
        editDue = findViewById(R.id.editDue);
        editLoanAmount = findViewById(R.id.editLoanAmount);
        loancancelBtn = findViewById(R.id.loancancelBtn);
        loansaveBtn = findViewById(R.id.loansaveBtn);

        loancancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(AddUpdateLoan.this, Loan.class);
                startActivity(myIntent);
            }
        });
        loansaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(AddUpdateLoan.this, Loan.class);
                startActivity(myIntent);
            }
        });
    }
}
