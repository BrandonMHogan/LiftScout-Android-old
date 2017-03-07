package com.brandonhogan.liftscout.core.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.brandonhogan.liftscout.R;
import com.brandonhogan.liftscout.core.controls.filters.DecimalDigitsInputFilter;

public class NumberPicker extends RelativeLayout {

    // Private Static Properties
    private static final int AUTO_DELAY_TIME_DEFAULT = 200;
    private static final int AUTO_DELAY_TIME_HOLD_CHANGE_DEFAULT = 8;
    private static final int AUTO_DELAY_TIME_MIN = 10;

    // Private Properties

    private float textSize;
    private int textColor;
    private int backgroundColor;
    private int maxValue;
    private boolean allowDecimal;
    private float increment;

    private EditText editText;
    private Button addButton;
    private Button subButton;
    private Handler repeatUpdateHandler;
    private boolean autoIncrement = false;
    private boolean autoDecrement = false;
    private int autoDelayTime = AUTO_DELAY_TIME_DEFAULT;


    // Constructors

    public NumberPicker(Context context) {
        this(context, null);
    }

    public NumberPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initControl(attrs);
    }


    // Private Functions

    private void initControl(AttributeSet attrs)
    {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.NumberPicker);

        if (typedArray != null) {
            increment = typedArray.getFloat(R.styleable.NumberPicker_increment, 0);
            maxValue = typedArray.getInt(R.styleable.NumberPicker_maxValue, 999);
            textSize = typedArray.getDimension(R.styleable.NumberPicker_textSize, 13);
            allowDecimal = typedArray.getBoolean(R.styleable.NumberPicker_allowDecimal, false);

            backgroundColor = typedArray.getColor(
                    R.styleable.NumberPicker_backGroundColor, getResources().getColor(R.color.colorPrimary));
            textColor = typedArray.getColor(
                    R.styleable.NumberPicker_textColor, getResources().getColor(R.color.text_color));
        }

        inflate(getContext(), R.layout.con_number_picker, this);

        subButton = (Button) findViewById(R.id.subtract_btn);
        addButton = (Button) findViewById(R.id.add_btn);
        editText = (EditText) findViewById(R.id.number_counter);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout);

        subButton.setTextColor(textColor);
        addButton.setTextColor(textColor);
        editText.setTextColor(textColor);

        subButton.setTextSize(textSize);
        addButton.setTextSize(textSize);
        editText.setTextSize(textSize);

        if (allowDecimal) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            editText.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(4,2)});
        }
        else {
            editText.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(3,2)});
        }

        editText.setText("0");

        Drawable drawable = getResources().getDrawable(R.drawable.background_round_corner);
        drawable.setColorFilter(new PorterDuffColorFilter(backgroundColor, PorterDuff.Mode.SRC));
        if(Build.VERSION.SDK_INT > 16)
            layout.setBackground(drawable);
        else
            layout.setBackgroundDrawable(drawable);

        setOnClick();
        typedArray.recycle();
    }

    private void changeValue(boolean isPositive) {
        String value = editText.getText().toString();
        float floatValue = Float.parseFloat(value);

        if (isPositive)
            floatValue += increment;
        else
            floatValue -= increment;

        if (floatValue < 0)
            floatValue = 0;
        else if (floatValue > maxValue)
            floatValue = maxValue;


        if (allowDecimal) {
            value = String.valueOf(floatValue);
        }
        else {
            int integerValue = (int)floatValue;
            value = String.valueOf(integerValue);
        }

        editText.setText(value);
    }

    private void setOnClick() {

        repeatUpdateHandler = new Handler();

        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                changeValue(false);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View mView) {
                changeValue(true);
            }
        });

        addButton.setOnLongClickListener(new View.OnLongClickListener(){
                                             public boolean onLongClick(View arg0) {
                                                 autoIncrement = true;
                                                 repeatUpdateHandler.post( new RptUpdater() );
                                                 return false;
                                             }
                                         }
        );

        addButton.setOnTouchListener( new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if( (event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL)
                        && autoIncrement ){
                    autoIncrement = false;
                    autoDelayTime = AUTO_DELAY_TIME_DEFAULT;
                }
                return false;
            }
        });

        subButton.setOnLongClickListener(new View.OnLongClickListener(){
                                             public boolean onLongClick(View arg0) {
                                                 autoDecrement = true;
                                                 repeatUpdateHandler.post( new RptUpdater() );
                                                 return false;
                                             }
                                         }
        );

        subButton.setOnTouchListener( new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if( (event.getAction()==MotionEvent.ACTION_UP || event.getAction()==MotionEvent.ACTION_CANCEL)
                        && autoDecrement ){
                    autoDecrement = false;
                    autoDelayTime = AUTO_DELAY_TIME_DEFAULT;
                }
                return false;
            }
        });


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {

                    if (allowDecimal) {
                        if (Float.valueOf(editable.toString()) > maxValue) {
                            editText.setText(maxValue);
                        }
                    }
                    else {
                        if (Integer.valueOf(editable.toString()) > maxValue)
                            editText.setText(Integer.toString(maxValue));
                    }
                }
            }
        });
    }


    // Public Functions

    public String getNumber()
    {
        return editText.getText().toString();
    }

    public int getNumberAsInt() {
        return Integer.valueOf(editText.getText().toString());
    }

    public void setNumber(int number)
    {
        editText.setText(String.valueOf(number));
    }

    public void setNumber(float number)
    {
        editText.setText(String.valueOf(number));
    }


    class RptUpdater implements Runnable {
        public void run() {
            if( autoIncrement ){
                repeater(true);

            } else if( autoDecrement ){
                repeater(false);
            }
        }

        private void repeater(boolean increment) {
            changeValue(increment);
            if (autoDelayTime > AUTO_DELAY_TIME_MIN)
                autoDelayTime -= AUTO_DELAY_TIME_HOLD_CHANGE_DEFAULT;

            repeatUpdateHandler.postDelayed( new RptUpdater(), autoDelayTime );
        }
    }
}