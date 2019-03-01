package com.android.nam.plt_money;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class LoanAdapter extends ArrayAdapter<String> {
    private String[] loan_id;
    private String[] user_id;
    private String[] loan_borrower;
    private String[] loan_date_borrowed;
    private String[] loan_due_date;
    private String[] loan_amount;
    private String[] loan_status;
    private Activity context;
    private ArrayList<Categories> categories;
    Helper helper = new Helper();

    public LoanAdapter(Activity context, String[] loan_id, String[] user_id, String[] loan_borrower, String[] loan_date_borrowed, String[]loan_due_date, String[]loan_amount, String[]loan_status) {
        super(context, R.layout.loan_list, loan_id);
        this.context = context;
        this.loan_id = loan_id;
        this.user_id = user_id;
        this.loan_borrower = loan_borrower;
        this.loan_date_borrowed = loan_date_borrowed;
        this.loan_due_date = loan_due_date;
        this.loan_amount = loan_amount;
        this.loan_status = loan_status;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View listViewItem = inflater.inflate(R.layout.loan_list, null, true);

        TextView tv_loan_borrower = listViewItem.findViewById(R.id.loan_borrower);
        TextView tv_loan_date_borrowed = listViewItem.findViewById(R.id.loan_date_borrowed);
        TextView tv_loan_due_date = listViewItem.findViewById(R.id.loan_due_date);
        TextView tv_loan_amount = listViewItem.findViewById(R.id.loan_amount);

        DecimalFormat df = new DecimalFormat("#,#00.0#");

        tv_loan_borrower.setText(loan_borrower[position]);
        tv_loan_date_borrowed.setText(loan_date_borrowed[position]);
        tv_loan_due_date.setText(loan_due_date[position]);
        tv_loan_amount.setText(df.format(Double.parseDouble(loan_amount[position])));

        return listViewItem;
    }
}