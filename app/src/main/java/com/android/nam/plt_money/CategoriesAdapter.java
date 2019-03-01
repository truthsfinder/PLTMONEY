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

import java.util.ArrayList;

public class CategoriesAdapter extends ArrayAdapter<String> {
    private String[] category_id;
    private String[] user_id;
    private String[] category_name;
    private String[] category_status;
    private Activity context;
    private ArrayList<Categories> categories;
    Helper helper = new Helper();
    Categories category = new Categories();

    public CategoriesAdapter(Activity context, String[] category_id, String[] user_id, String[] category_name, String[] category_status) {
        super(context, R.layout.categories_list, category_id);
        this.context = context;
        this.category_id = category_id;
        this.user_id = user_id;
        this.category_name = category_name;
        this.category_status = category_status;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View listViewItem = inflater.inflate(R.layout.categories_list, null, true);

        TextView tv_category_name = listViewItem.findViewById(R.id.newCategoryTV);
        Button btn_update = listViewItem.findViewById(R.id.categoriesEditBtn);
        Button btn_delete = listViewItem.findViewById(R.id.categoriesDelBtn);
        tv_category_name.setText(category_name[position]);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent = new Intent(getContext(), UpdateCategory.class);
            intent.putExtra("category_id", Integer.parseInt(category_id[position]));
            getContext().startActivity(intent);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getContext());

            alertDialogBuilder.setMessage("Are you sure you want to delete this category?");
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                StringRequest stringRequest = new StringRequest(Request.Method.GET, helper.get_delete_category_url() + category_id[position],
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(response.toString().trim().equals("success")){
                                Toast.makeText(getContext(), "Category was successfully deleted!", Toast.LENGTH_LONG).show();
                                context.startActivity(new Intent(getContext(), Categories.class));
                                context.finish();
                            }else{
                                Toast.makeText(getContext(), "Failed in deleting a category!", Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), "Failed in connecting the database!", Toast.LENGTH_LONG).show();
                        }
                    }) {
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
        });
        return listViewItem;
    }
}