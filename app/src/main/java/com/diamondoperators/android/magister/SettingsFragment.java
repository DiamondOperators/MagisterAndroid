package com.diamondoperators.android.magister;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment {
    private static final int SECURE_GRADES_CONFIRM_CREDENTIALS_REQUEST_CODE = 2;

    private CheckBoxPreference secureGradesPf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        secureGradesPf = (CheckBoxPreference) getPreferenceScreen().getPreference(0);
        secureGradesPf.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (!secureGradesPf.isChecked()) {
                    // Security has been switched off, switch it on again:
                    secureGradesPf.setChecked(true);

                    // Because first confirm user credentials
                    KeyguardManager kManager = (KeyguardManager) getActivity().getSystemService(Context.KEYGUARD_SERVICE);
                    Intent secureIntent = kManager.createConfirmDeviceCredentialIntent(null, null);
                    if (secureIntent != null) {
                        startActivityForResult(secureIntent, SECURE_GRADES_CONFIRM_CREDENTIALS_REQUEST_CODE);
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SECURE_GRADES_CONFIRM_CREDENTIALS_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            secureGradesPf.setChecked(false);
        }
    }
}
