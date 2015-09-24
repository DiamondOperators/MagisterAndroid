package com.diamondoperators.android.magister;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class AgendaView extends View {

    private Paint mRoundRectPaint, mNowIndicatorPaint, mTextPaint;
    private RectF mRoundRect;
    private float mPixelsPerHour = 200;
    private float mPaddingBetweenAppointments = 3;
    private float mAppointmentsPadding;
    private float mNowIndicatorWidth = 5;
    private float mTextPaddingTop, mTextPaddingLeft;
    private int mNowIndicatorColor = Color.RED;

    private List<Appointment> data = new ArrayList<>();

    private void init() {
        mRoundRectPaint = new Paint();
        mRoundRectPaint.setColor(getResources().getColor(R.color.agendaAppointmentColor));
        mRoundRectPaint.setStyle(Paint.Style.FILL);
        mNowIndicatorPaint = new Paint();
        mNowIndicatorPaint.setStrokeWidth(mNowIndicatorWidth);
        mNowIndicatorColor = getResources().getColor(R.color.colorAccent);
        mNowIndicatorPaint.setColor(mNowIndicatorColor);
        mRoundRect = new RectF();
        mTextPaint = new Paint();
        setTitleTextSize(18);
        mTextPaint.setColor(Color.WHITE);

        // 8 dp
        mAppointmentsPadding = mTextPaddingTop = mTextPaddingLeft =
                8 * getResources().getDisplayMetrics().densityDpi / 160f;

        data.add(new Appointment("Wiskunde B", 1443162600, 1443166800));
        data.add(new Appointment("Grieks", 1443167700, 1443171900));
        data.add(new Appointment("Flipperkast programmeren", 1443171900, 1443174300));
        data.add(new Appointment("Informatica", 1443176100, 1443180300));
        data.add(new Appointment("Nederlands", 1443180300, 1443184500));
        data.add(new Appointment("Natuurkunde", 1443185400, 1443189600));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        long firstAppointment = timeOfFirstAppointment();

        for (int i = 0; i < data.size(); i++) {
            float start = (data.get(i).getStartTime() - firstAppointment) / 3600f * mPixelsPerHour
                    + mAppointmentsPadding;
            float end = (data.get(i).getEndTime() - firstAppointment) / 3600f * mPixelsPerHour
                    + mAppointmentsPadding - mPaddingBetweenAppointments;
            mRoundRect.set(mAppointmentsPadding, start, getWidth() - mAppointmentsPadding, end);
            canvas.drawRoundRect(mRoundRect, 20, 20, mRoundRectPaint);

            canvas.drawText(data.get(i).getName(), mAppointmentsPadding + mTextPaddingLeft,
                    start + mTextPaint.getTextSize() + mTextPaddingTop, mTextPaint);
        }

        float nowIndicator = (/*System.currentTimeMillis() / 1000*/ 1443169780 - firstAppointment) / 3600f * mPixelsPerHour
                + mAppointmentsPadding;
        canvas.drawLine(mAppointmentsPadding, nowIndicator, getWidth(), nowIndicator, mNowIndicatorPaint);
        canvas.drawCircle(mAppointmentsPadding, nowIndicator, mNowIndicatorWidth * 3, mNowIndicatorPaint);
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
        mTextPaint.setTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, sizeSP, getResources().getDisplayMetrics()));
    }
}
