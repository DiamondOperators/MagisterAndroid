package com.diamondoperators.android.magister;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class AgendaView extends View {

    private Paint mRoundRectPaint, mNowIndicatorPaint, mTitleTextPaint, mDescriptionTextPaint;
    private RectF mRoundRect;
    private float mPixelsPerHour = 200;
    private float mPaddingBetweenAppointments = 3;
    private float mAppointmentsPadding;
    private float mNowIndicatorWidth = 5;
    private float mTextLineMargins = 5;
    private float mTextPaddingTop, mTextPaddingLeft;
    private int mNowIndicatorColor = Color.RED;

    private List<Appointment> data = new ArrayList<>();

    private void init() {
        mRoundRectPaint = new Paint();
        mRoundRectPaint.setColor(getResources().getColor(R.color.agendaAppointmentColor));
        mRoundRectPaint.setStyle(Paint.Style.FILL);
        mNowIndicatorPaint = new Paint();
        mNowIndicatorPaint.setStrokeWidth(mNowIndicatorWidth);
        mNowIndicatorColor = getResources().getColor(R.color.colorPrimaryDark);
        mNowIndicatorPaint.setColor(mNowIndicatorColor);
        mRoundRect = new RectF();
        mTitleTextPaint = new Paint();
        mTitleTextPaint.setColor(Color.WHITE);
        mDescriptionTextPaint = new Paint(mTitleTextPaint);
        setTitleTextSize(18);
        mTitleTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        setDescriptionTextSize(15);

        mTitleTextPaint.setAntiAlias(true);
        mDescriptionTextPaint.setAntiAlias(true);
        mRoundRectPaint.setAntiAlias(true);
        mNowIndicatorPaint.setAntiAlias(true);

        // 8 dp
        mAppointmentsPadding = mTextPaddingTop = mTextPaddingLeft =
                8 * getResources().getDisplayMetrics().densityDpi / 160f;

        data.add(new Appointment(1443162600, 1443166800, "Biologie", "MAJ", "a031", "A5vBi1", "Bi - MAJ - A5vBi1"));
        data.add(new Appointment(1443167700, 1443171900, "Grieks", "M.L. Kopinga", "a121", "A5vGrTL1", "GrTL - KOM - A5vGrTL1"));
        data.add(new Appointment(1443171900, 1443174300, "Flipperkast programmeren", "S. van der Staaij", "a107", "VWO Xtra", "V_v5_vxtra - STA"));
        data.add(new Appointment(1443176100, 1443180300, "Informatica", "S. van der Staaij", "a107", "A5vIn1", "In - STA - A5vIn1"));
        data.add(new Appointment(1443180300, 1443184500, "Nederlands", "M. Louwman", "a102", "A5v5", "NeTL - LOU - A5v5"));
        data.add(new Appointment(1443185400, 1443189600, "Natuurkunde", "J.H.D. Dozeman", "a023", "A5vNa3", "Na - DOH - A5vNa3"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        long firstAppointment = timeOfFirstAppointment();

        for (Appointment appointment : data) {
            float start = (appointment.getStartTime() - firstAppointment) / 3600f * mPixelsPerHour
                    + mAppointmentsPadding;
            float end = (appointment.getEndTime() - firstAppointment) / 3600f * mPixelsPerHour
                    + mAppointmentsPadding - mPaddingBetweenAppointments;
            mRoundRect.set(mAppointmentsPadding, start, getWidth() - mAppointmentsPadding, end);
            canvas.drawRoundRect(mRoundRect, 20, 20, mRoundRectPaint);
        }

        float nowIndicator = (/*System.currentTimeMillis() / 1000*/ 1443169400 - firstAppointment) / 3600f * mPixelsPerHour
                + mAppointmentsPadding;
        canvas.drawLine(mAppointmentsPadding, nowIndicator, getWidth(), nowIndicator, mNowIndicatorPaint);
        canvas.drawCircle(mAppointmentsPadding, nowIndicator, mNowIndicatorWidth * 3, mNowIndicatorPaint);

        for (Appointment appointment : data) {
            float start = (appointment.getStartTime() - firstAppointment) / 3600f * mPixelsPerHour
                    + mAppointmentsPadding + mTextPaddingTop + mTitleTextPaint.getTextSize();

            canvas.drawText(appointment.getSubject(), mAppointmentsPadding + mTextPaddingLeft,
                    start, mTitleTextPaint);
            String subtitle = String.format("%s — %s", appointment.getLocation(), appointment.getTeacher());
            canvas.drawText(subtitle, mAppointmentsPadding + mTextPaddingLeft,
                    start + mDescriptionTextPaint.getTextSize() + mTextLineMargins,
                    mDescriptionTextPaint);
        }
    }

    private long timeOfFirstAppointment() {
        try {
            long earliest = data.get(0).getStartTime();
            for (int i = 1; i < data.size(); i++)
                if (data.get(i).getStartTime() < earliest)
                    earliest = data.get(i).getStartTime();
            return earliest;
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public AgendaView(Context context) {
        super(context);
        init();
    }

    public AgendaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AgendaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setTitleTextSize(float sizeSP) {
        mTitleTextPaint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, sizeSP, getResources().getDisplayMetrics()));
    }

    public void setDescriptionTextSize(float sizeSP) {
        mDescriptionTextPaint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, sizeSP, getResources().getDisplayMetrics()));
    }
}
