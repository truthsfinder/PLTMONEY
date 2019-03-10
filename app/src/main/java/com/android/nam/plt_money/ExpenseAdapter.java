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

public class ExpenseAdapter extends ArrayAdapter<String> {
    private String[] expense_id;
    private String[] expense_name;
    private String[] budget_id;
    private String[] category_name;
    private String[] expense_date;
    private String[] expense_amount;
    private Activity context;
    private ArrayList<Categories> categories;
    Helper helper = new Helper();

    public ExpenseAdapter(Activity context, String[] expense_id, String[] expense_name, String[] budget_id, String[] category_name, String[] expense_date, String[]expense_amount) {
        super(context, R.layout.expense_list, expense_id);
        this.context = context;
        this.expense_id = expense_id;
        this.expense_name = expense_name;
        this.budget_id = budget_id;
        this.category_name = category_name;
        this.expense_date = expense_date;
        this.expense_amount = expense_amount;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View listViewItem = inflater.inflate(R.layout.expense_list, null, true);
        TextView tv_expense_name = listViewItem.findViewById(R.id.expense_name);
        TextView tv_expense_date = listViewItem.findViewById(R.id.expense_date);
        TextView tv_expense_amount = listViewItem.findViewById(R.id.expense_amount);
        TextView tv_expense_category = listViewItem.findViewById(R.id.expense_category);
        DecimalFormat df = new DecimalFormat("#,#00.0#");

        tv_expense_date.setText(expense_date[position]);
        tv_expense_name.setText(expense_name[position]);
        tv_expense_amount.setText(df.format(Double.parseDouble(expense_amount[position])));
        tv_expense_category.setText(category_name[position]);

        return listViewItem;
    }
}