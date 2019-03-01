package com.android.nam.plt_money;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class Dashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent intent = getIntent();
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String name = prefs.getString("username", null);
        toolbar.setTitle("Hello, " + name);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Fragment home = new Home();
        FragmentManager FM = getFragmentManager();
        FM
                .beginTransaction()
                .replace(R.id.content_frame, home)
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        if(id == R.id.nav_home) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new Home()).commit();
        }else if (id == R.id.nav_budget) {
            startActivity(new Intent(Dashboard.this, Budget.class));
        } else if (id == R.id.nav_loan) {
            startActivity(new Intent(Dashboard.this, Loan.class));
        } else if (id == R.id.nav_expenses) {
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            int budget_id = prefs.getInt("budget_id", 0);

            Intent intent = new Intent(Dashboard.this, Expenses.class);
            intent.putExtra("budget_id", budget_id);
            startActivity(intent);

        } else if(id == R.id.nav_category){
            Intent intent = new Intent(Dashboard.this, Categories.class);
            startActivity(intent);
        }else if(id == R.id.nav_savings){
            Intent intent = new Intent(Dashboard.this, Savings.class);
            startActivity(intent);
        }else if (id == R.id.nav_summary) {
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            int budget_id = prefs.getInt("budget_id", 0);

            Intent intent = new Intent(Dashboard.this, Summary.class);
            intent.putExtra("budget_id", budget_id);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            SharedPreferences settings = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            final SharedPreferences.Editor prefEditor = settings.edit();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to logout?");
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                    prefEditor.clear();
                    prefEditor.commit();

                    Intent intent = new Intent(Dashboard.this, Login.class);
                    startActivity(intent);
                    finish();
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
