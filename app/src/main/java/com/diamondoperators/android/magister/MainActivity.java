package com.diamondoperators.android.magister;

import android.app.Fragment;
import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int CONFIRM_CREDENTIALS_REQUEST_CODE = 1;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Toolbar toolbar;

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

        getFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, new AgendaFragment())
                .commit();
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
        Fragment newFragment = null;
        switch (menuItem.getItemId()) {
            case R.id.agenda:
                if (getFragmentManager().findFragmentById(R.id.fragmentContainer) instanceof AgendaFragment)
                    break;
                newFragment = new AgendaFragment();
                break;
            case R.id.cijfers:
                if (getFragmentManager().findFragmentById(R.id.cijfers) instanceof GradesFragment)
                    break;
                showGradesFragment();
                break;
            case R.id.instellingen:
                if (getFragmentManager().findFragmentById(R.id.cijfers) instanceof SettingsFragment)
                    break;
                newFragment = new SettingsFragment();
                break;
        }

        if (newFragment != null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, newFragment).commit();
        }

        drawerLayout.closeDrawer(navigationView);
        return true;
    }

    private void showGradesFragment() {
        KeyguardManager kManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        Intent secureIntent = kManager.createConfirmDeviceCredentialIntent(null, null);
        if (secureIntent != null) {
            startActivityForResult(secureIntent, CONFIRM_CREDENTIALS_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONFIRM_CREDENTIALS_REQUEST_CODE && resultCode == RESULT_OK) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new GradesFragment())
                    .commit();
        }
    }
}
