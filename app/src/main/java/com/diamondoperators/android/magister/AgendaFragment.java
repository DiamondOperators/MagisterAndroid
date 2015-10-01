package com.diamondoperators.android.magister;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.util.Date;

public class AgendaFragment extends Fragment implements AgendaView.OnAppointmentClickListener {

    AgendaView agendaView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View theView = inflater.inflate(R.layout.fragment_agenda, container, false);

        agendaView = (AgendaView) theView.findViewById(R.id.agendaView);
        agendaView.setOnAppointmentClickListener(this);

        return theView;
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

        new AlertDialog.Builder(getContext())
                .setTitle(title).setMessage(msg)
                .setPositiveButton(android.R.string.ok, null)
                .create().show();
    }
}
