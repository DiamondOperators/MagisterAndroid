package com.diamondoperators.android.magister;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
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

    private List<Appointment> mData = new ArrayList<>();
    private float[] mStartYs = new float[0];
    private float[] mEndYs = new float[0];

    private OnAppointmentClickListener mClickListener;
    private GestureDetector mDetector;

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

        mData.add(new Appointment(1443162600, 1443166800, "Biologie", "MAJ", "a031", "A5vBi1", "Bi - MAJ - A5vBi1"));
        mData.add(new Appointment(1443167700, 1443171900, "Grieks", "M.L. Kopinga", "a121", "A5vGrTL1", "GrTL - KOM - A5vGrTL1"));
        mData.add(new Appointment(1443171900, 1443174300, "Flipperkast programmeren", "S. van der Staaij", "a107", "VWO Xtra", "V_v5_vxtra - STA"));
        mData.add(new Appointment(1443176100, 1443180300, "Informatica", "S. van der Staaij", "a107", "A5vIn1", "In - STA - A5vIn1"));
        mData.add(new Appointment(1443180300, 1443184500, "Nederlands", "M. Louwman", "a102", "A5v5", "NeTL - LOU - A5v5"));
        mData.add(new Appointment(1443185400, 1443189600, "Natuurkunde", "J.H.D. Dozeman", "a023", "A5vNa3", "Na - DOH - A5vNa3"));

        mDetector = new GestureDetector(getContext(), new AgendaGestureListener());
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
        mStartYs = new float[mData.size()];
        mEndYs = new float[mData.size()];

        long firstAppointment = startTimeOfFirstAppointment();

        // Draw rounded triangles
        for (int i = 0; i < mData.size(); i++) {
            mStartYs[i] = (mData.get(i).getStartTime() - firstAppointment) / 3600f * mPixelsPerHour
                    + mAppointmentsPadding;
            mEndYs[i] = (mData.get(i).getEndTime() - firstAppointment) / 3600f * mPixelsPerHour
                    + mAppointmentsPadding - mAppointmentSpacing;
            mRoundRect.set(mAppointmentsPadding, mStartYs[i], getWidth() - mAppointmentsPadding, mEndYs[i]);
            canvas.drawRoundRect(mRoundRect, 20, 20, mRoundRectPaint);
        }

        // Draw now indicator
        float nowIndicator = (/*System.currentTimeMillis() / 1000*/ 1443169400 - firstAppointment) / 3600f * mPixelsPerHour
                + mAppointmentsPadding;
        canvas.drawLine(mAppointmentsPadding, nowIndicator, getWidth(), nowIndicator, mNowIndicatorPaint);
        canvas.drawCircle(mAppointmentsPadding, nowIndicator, mNowIndicatorCircleRadius, mNowIndicatorPaint);

        // Draw text
        for (int i = 0; i < mData.size(); i++) {
            Appointment appt = mData.get(i);
            float start = mStartYs[i] + mTextPaddingTop + mTitleTextPaint.getTextSize();

            canvas.drawText(appt.getSubject(), mAppointmentsPadding + mTextPaddingLeft,
                    start, mTitleTextPaint);
            String subtitle = String.format("%s — %s", appt.getLocation(), appt.getTeacher());
            canvas.drawText(subtitle, mAppointmentsPadding + mTextPaddingLeft,
                    start + mDescriptionTextPaint.getTextSize() + mLineSpacing,
                    mDescriptionTextPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }

    private long startTimeOfFirstAppointment() {
        try {
            long earliest = mData.get(0).getStartTime();
            for (int i = 1; i < mData.size(); i++)
                if (mData.get(i).getStartTime() < earliest)
                    earliest = mData.get(i).getStartTime();
            return earliest;
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private long endTimeOfLastAppointment() {
        try {
            long last = mData.get(0).getEndTime();
            for (int i = 1; i < mData.size(); i++)
                if (mData.get(i).getEndTime() > last)
                    last = mData.get(i).getEndTime();
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

    public void setOnAppointmentClickListener(OnAppointmentClickListener listener) {
        mClickListener = listener;
    }


    public interface OnAppointmentClickListener {
        void onClickAppointment(Appointment appt);
    }

    class AgendaGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            float y = event.getY();
            for (int i = 0; i < mStartYs.length; i++) {
                if (y >= mStartYs[i] && y <= mEndYs[i]) {
                    if (mClickListener != null) {
                        mClickListener.onClickAppointment(mData.get(i));
                    }
                    return true;
                }
            }

            return false;
        }
    }
}
