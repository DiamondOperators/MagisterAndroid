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
    private float mNowIndicatorCircleRadius;
    private float mPixelsPerHour;
    private float mAppointmentSpacing;
    private float mAppointmentsPadding;
    private float mLineSpacing;
    private float mTextPaddingTop, mTextPaddingLeft;

    private List<Appointment> data = new ArrayList<>();

    private void init() {
        mRoundRect = new RectF();
        mRoundRectPaint = new Paint();
        mNowIndicatorPaint = new Paint();
        mTitleTextPaint = new Paint();
        mDescriptionTextPaint = new Paint();
        mRoundRectPaint.setStyle(Paint.Style.FILL);
        mTitleTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        // Set default values
        setAppointmentColor(getResources().getColor(R.color.agendaAppointmentColor));
        setNowIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
        setNowIndicatorHeight(1.5f);
        setNowIndicatorCircleRadius(4.5f);
        setTitleTextColor(Color.WHITE);
        setDescriptionTextColor(Color.WHITE);
        setTitleTextSize(18);
        setDescriptionTextSize(15);
        setAppointmentsPadding(8);
        setTextPaddingTop(8);
        setTextPaddingLeft(8);
        setLineSpacing(TypedValue.COMPLEX_UNIT_SP, 3);
        setHourHeight(100);
        setAppointmentSpacing(1.3f);

        mTitleTextPaint.setAntiAlias(true);
        mDescriptionTextPaint.setAntiAlias(true);
        mRoundRectPaint.setAntiAlias(true);
        mNowIndicatorPaint.setAntiAlias(true);

        data.add(new Appointment(1443162600, 1443166800, "Biologie", "MAJ", "a031", "A5vBi1", "Bi - MAJ - A5vBi1"));
        data.add(new Appointment(1443167700, 1443171900, "Grieks", "M.L. Kopinga", "a121", "A5vGrTL1", "GrTL - KOM - A5vGrTL1"));
        data.add(new Appointment(1443171900, 1443174300, "Flipperkast programmeren", "S. van der Staaij", "a107", "VWO Xtra", "V_v5_vxtra - STA"));
        data.add(new Appointment(1443176100, 1443180300, "Informatica", "S. van der Staaij", "a107", "A5vIn1", "In - STA - A5vIn1"));
        data.add(new Appointment(1443180300, 1443184500, "Nederlands", "M. Louwman", "a102", "A5v5", "NeTL - LOU - A5v5"));
        data.add(new Appointment(1443185400, 1443189600, "Natuurkunde", "J.H.D. Dozeman", "a023", "A5vNa3", "Na - DOH - A5vNa3"));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int hSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        if (hSpecMode == MeasureSpec.AT_MOST || hSpecMode == MeasureSpec.UNSPECIFIED) {
            hSpecSize = getWrapContentHeight();
        }

        setMeasuredDimension(wSpecSize, hSpecSize);
    }

    private int getWrapContentHeight() {
        long firstAppointment = startTimeOfFirstAppointment();
        long lastAppointment = endTimeOfLastAppointment();
        return (int) ((lastAppointment - firstAppointment) / 3600f * mPixelsPerHour + 2 * mAppointmentsPadding);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        long firstAppointment = startTimeOfFirstAppointment();

        for (Appointment appointment : data) {
            float start = (appointment.getStartTime() - firstAppointment) / 3600f * mPixelsPerHour
                    + mAppointmentsPadding;
            float end = (appointment.getEndTime() - firstAppointment) / 3600f * mPixelsPerHour
                    + mAppointmentsPadding - mAppointmentSpacing;
            mRoundRect.set(mAppointmentsPadding, start, getWidth() - mAppointmentsPadding, end);
            canvas.drawRoundRect(mRoundRect, 20, 20, mRoundRectPaint);
        }

        float nowIndicator = (/*System.currentTimeMillis() / 1000*/ 1443169400 - firstAppointment) / 3600f * mPixelsPerHour
                + mAppointmentsPadding;
        canvas.drawLine(mAppointmentsPadding, nowIndicator, getWidth(), nowIndicator, mNowIndicatorPaint);
        canvas.drawCircle(mAppointmentsPadding, nowIndicator, mNowIndicatorCircleRadius, mNowIndicatorPaint);

        for (Appointment appointment : data) {
            float start = (appointment.getStartTime() - firstAppointment) / 3600f * mPixelsPerHour
                    + mAppointmentsPadding + mTextPaddingTop + mTitleTextPaint.getTextSize();

            canvas.drawText(appointment.getSubject(), mAppointmentsPadding + mTextPaddingLeft,
                    start, mTitleTextPaint);
            String subtitle = String.format("%s — %s", appointment.getLocation(), appointment.getTeacher());
            canvas.drawText(subtitle, mAppointmentsPadding + mTextPaddingLeft,
                    start + mDescriptionTextPaint.getTextSize() + mLineSpacing,
                    mDescriptionTextPaint);
        }
    }

    private long startTimeOfFirstAppointment() {
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

    private long endTimeOfLastAppointment() {
        try {
            long last = data.get(0).getEndTime();
            for (int i = 1; i < data.size(); i++)
                if (data.get(i).getEndTime() > last)
                    last = data.get(i).getEndTime();
            return last;
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


    public void setTitleTextSize(float size) {
        setTitleTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setTitleTextSize(int unit, float size) {
        mTitleTextPaint.setTextSize(toPx(unit, size));
        invalidate();
        requestLayout();
    }

    public void setTitleTextColor(int color) {
        mTitleTextPaint.setColor(color);
        invalidate();
        requestLayout();
    }

    public void setDescriptionTextColor(int color) {
        mDescriptionTextPaint.setColor(color);
        invalidate();
        requestLayout();
    }

    public void setDescriptionTextSize(float size) {
        setDescriptionTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setDescriptionTextSize(int unit, float size) {
        mDescriptionTextPaint.setTextSize(toPx(unit, size));
        invalidate();
        requestLayout();
    }

    /**
     * Set the color for the circle and line which represent the
     * current time.
     *
     * @param color The color.
     */
    public void setNowIndicatorColor(int color) {
        mNowIndicatorPaint.setColor(color);
        invalidate();
    }

    /**
     * Set the color for the round rectangles which
     * represent the individual appointments.
     *
     * @param color The color.
     */
    public void setAppointmentColor(int color) {
        mRoundRectPaint.setColor(color);
        invalidate();
    }

    /**
     * @param height The height for the now indicator in
     *               density independent pixels (DIP)
     */
    public void setNowIndicatorHeight(float height) {
        setNowIndicatorHeight(TypedValue.COMPLEX_UNIT_DIP, height);
    }

    /**
     * @param unit   The unit for the height: TypedValue.COMPLEX_UNIT_
     * @param height The height given in the specified unit.
     */
    public void setNowIndicatorHeight(int unit, float height) {
        mNowIndicatorPaint.setStrokeWidth(toPx(unit, height));
        invalidate();
        requestLayout();
    }

    public void setAppointmentsPadding(float size) {
        setAppointmentsPadding(TypedValue.COMPLEX_UNIT_DIP, size);
    }

    public void setAppointmentsPadding(int unit, float size) {
        mAppointmentsPadding = toPx(unit, size);
        invalidate();
        requestLayout();
    }

    public void setTextPaddingTop(float padding) {
        setTextPaddingTop(TypedValue.COMPLEX_UNIT_DIP, padding);
    }

    public void setTextPaddingTop(int unit, float padding) {
        mTextPaddingTop = toPx(unit, padding);
        invalidate();
        requestLayout();
    }

    public void setTextPaddingLeft(float padding) {
        setTextPaddingLeft(TypedValue.COMPLEX_UNIT_DIP, padding);
    }

    public void setTextPaddingLeft(int unit, float padding) {
        mTextPaddingLeft = toPx(unit, padding);
        invalidate();
        requestLayout();
    }

    public void setNowIndicatorCircleRadius(float radius) {
        setNowIndicatorCircleRadius(TypedValue.COMPLEX_UNIT_DIP, radius);
    }

    public void setNowIndicatorCircleRadius(int unit, float radius) {
        mNowIndicatorCircleRadius = toPx(unit, radius);
        invalidate();
        requestLayout();
    }

    public void setLineSpacing(int unit, float size) {
        mLineSpacing = toPx(unit, size);
        invalidate();
        requestLayout();
    }

    public void setHourHeight(float size) {
        setHourHeight(TypedValue.COMPLEX_UNIT_DIP, size);
    }

    public void setHourHeight(int unit, float size) {
        mPixelsPerHour = toPx(unit, size);
        invalidate();
        requestLayout();
    }

    public void setAppointmentSpacing(float size) {
        setAppointmentSpacing(TypedValue.COMPLEX_UNIT_DIP, size);
    }

    public void setAppointmentSpacing(int unit, float size) {
        mAppointmentSpacing = toPx(unit, size);
        invalidate();
        requestLayout();
    }

    private float toPx(int unit, float size) {
        return TypedValue.applyDimension(
                unit, size, getResources().getDisplayMetrics());
    }
}
