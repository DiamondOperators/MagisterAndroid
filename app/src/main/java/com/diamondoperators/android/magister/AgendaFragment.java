package com.diamondoperators.android.magister;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.util.Date;

public class AgendaFragment extends Fragment implements AgendaView.OnAppointmentClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View theView = inflater.inflate(R.layout.fragment_agenda, container, false);

        AgendaView agendaView = (AgendaView) theView.findViewById(R.id.agendaView);
        agendaView.setOnAppointmentClickListener(this);

        return theView;
    }

    @Override
    public void onClickAppointment(Appointment appt) {
        DateFormat format = DateFormat.getTimeInstance(DateFormat.SHORT);
        String startTime = format.format(new Date(appt.getStartTime() * 1000));
        String endTime = format.format(new Date(appt.getEndTime() * 1000));

        Context c = getContext();

        String title = appt.getSubject() != null ? appt.getSubject() : c.getString(R.string.onbekend_vak);

        StringBuilder msg = new StringBuilder();
        if (appt.getTeacher() != null)
            msg.append(c.getString(R.string.leraar)).append(appt.getTeacher());
        if (appt.getLocation() != null)
            msg.append(c.getString(R.string.locatie)).append(appt.getLocation());
        if (appt.getGroup() != null)
            msg.append(c.getString(R.string.groep)).append(appt.getGroup());
        if (appt.getStartTime() != 0)
            msg.append(c.getString(R.string.starttijd)).append(startTime);
        if (appt.getEndTime() != 0)
            msg.append(c.getString(R.string.eindtijd)).append(endTime);
        if (appt.getDescription() != null)
            msg.append(c.getString(R.string.beschrijving)).append(appt.getDescription());

        new AlertDialog.Builder(getContext())
                .setTitle(title).setMessage(msg)
                .setPositiveButton(android.R.string.ok, null)
                .create().show();
    }
}
