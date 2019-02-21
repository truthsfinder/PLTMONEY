package com.android.nam.plt_money;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.TextView;

public class Summary extends AppCompatActivity{
    private TableLayout summary_tbl;
    private TextView txtTotalAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);

        summary_tbl = findViewById(R.id.summary_tbl);
        txtTotalAmount = findViewById(R.id.txtTotalAmount);

    }
}
