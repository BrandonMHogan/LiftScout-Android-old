package com.brandonhogan.liftscout.core.controls;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.brandonhogan.liftscout.AppController;
import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.utils.BhDate;

import java.util.Calendar;
import java.util.Date;

public class BhDatePicker extends FrameLayout {


    //Interface

    public interface DatePickerCallback {
        void onBhDatePickerDismiss ();
    }


    // Private Properties

    private TextInputLayout inputLayout;
    private EditText editTextView;
    private Date date;
    private Date minDate;
    private Date maxDate;
    private DatePickerDialog dialog;
    private DatePickerCallback callback;


    // Public Properties

    public Date getDate() {
        return date;
    }

    public void setDate (Date date) {
        this.date = date;
        if (date != null) {
            editTextView.setText(BhDate.toStringDate(date));
        }
    }
    public Date getMinDate() { return minDate; }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;

        if(date == null || (minDate != null && minDate.after(date)))
            setDate(minDate);
    }

    public Date getMaxDate() { return maxDate; }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;

        if(date == null || (minDate != null && date.after(maxDate)))
            setDate(maxDate);
    }

    public void setHint(String hint) {
        inputLayout.setHint(hint);
    }

    public void setCallback(DatePickerCallback callback) {
        this.callback = callback;
    }


    // Constructors

    public BhDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initControl();
    }

    public BhDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl();
    }

    public BhDatePicker(Context context) {
        super(context);
        initControl();
    }


    // Private Functions

    private void initControl() {
        final View view = inflate(AppController.getInstance(), R.layout.con_date_picker, this);

        inputLayout = (TextInputLayout) findViewById(R.id.input_layout);

        editTextView = (EditText) findViewById(R.id.edit_text);
        editTextView.setKeyListener(null);

        editTextView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.hasFocus() && dialog == null) {
                    showDialog();
                    InputMethodManager imm = (InputMethodManager) AppController.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return false;
            }
        });

        editTextView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b && dialog == null) {
                    showDialog();
                    InputMethodManager imm = (InputMethodManager)AppController.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });


    }

    private void showDialog () {
        updateDialog();
        dialog.show();
    }

    private void updateDialog () {


        //use default time for initial values
        final Calendar calendar = Calendar.getInstance();

        if (date != null)
            calendar.setTime(date);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);

                setDate(calendar.getTime());
            }
        };

        dialog = new DatePickerDialog(AppController.getInstance(), listener, year, month, day);

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                datePickerDismissListener();
            }
        });

        if (minDate != null) {

            // ISSUE: changing min date will not work if the year is the same as the
            // previous min date. To make it work, we set the date to something from a
            // different year, then set it to the correct min date.
            Calendar cal = Calendar.getInstance();
            cal.setTime(minDate);
            cal.add(Calendar.YEAR, -10);

            dialog.getDatePicker().setMinDate(cal.getTime().getTime());
            dialog.getDatePicker().setMinDate(minDate.getTime());
        }

        if (maxDate != null) {

            // ISSUE: changing max date will not work if the year is the same as the
            // previous max date. To make it work, we set the date to something from a
            // different year, then set it to the correct max date.
            Calendar cal = Calendar.getInstance();
            cal.setTime(maxDate);
            cal.add(Calendar.YEAR, -10);

            dialog.getDatePicker().setMaxDate(cal.getTime().getTime());
            dialog.getDatePicker().setMaxDate(maxDate.getTime());
        }
    }

    private void datePickerDismissListener () {
        dialog = null;
        if (callback != null)
            callback.onBhDatePickerDismiss();
    }
}

