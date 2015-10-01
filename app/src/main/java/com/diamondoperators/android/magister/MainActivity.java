package com.diamondoperators.android.magister;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        AgendaView.OnAppointmentClickListener {

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        setupDrawerToggle();
        navigationView.setCheckedItem(R.id.agenda);
        navigationView.setNavigationItemSelectedListener(this);

        AgendaView agendaView = (AgendaView) findViewById(R.id.agendaView);
        agendaView.setOnAppointmentClickListener(this);
    }

    @Override
    public void onClickAppointment(Appointment appt) {
        DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT);
        String startTime = format.format(new Date(appt.getStartTime() * 1000));
        String endTime = format.format(new Date(appt.getEndTime() * 1000));

        String title = appt.getSubject() != null ? appt.getSubject() : "Onbekend vak";
        StringBuilder msg = new StringBuilder();
        if (appt.getTeacher() != null) msg.append("Leraar: ").append(appt.getTeacher());
        if (appt.getLocation() != null) msg.append("\nLocatie: ").append(appt.getLocation());
        if (appt.getGroup() != null) msg.append("\nGroep: ").append(appt.getGroup());
        if (appt.getStartTime() != 0) msg.append("\nStarttijd: ").append(startTime);
        if (appt.getEndTime() != 0) msg.append("\nEindtijd: ").append(endTime);
        if (appt.getDescription() != null)
            msg.append("\nBeschrijving: ").append(appt.getDescription());

        new AlertDialog.Builder(this)
                .setTitle(title).setMessage(msg)
                .setPositiveButton(android.R.string.ok, null)
                .create().show();
    }

    private void setupDrawerToggle() {
        drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        drawerLayout.closeDrawer(navigationView);
        return false;
    }
}
