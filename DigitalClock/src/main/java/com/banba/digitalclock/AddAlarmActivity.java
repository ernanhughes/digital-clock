package com.banba.digitalclock;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.ViewSwitcher;

import java.util.Calendar;

/**
 * Created by Ernan on 02/01/14.
 * Copyrite Banba Inc. 2013.
 */
public class AddAlarmActivity extends Activity {

    private ViewSwitcher vs;
    private RadioGroup rg;
    private RelativeLayout rl3;
    private RelativeLayout rl4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("New Reminder");
        setContentView(R.layout.add);

        vs = (ViewSwitcher) findViewById(R.id.view_switcher);
        rg = (RadioGroup) findViewById(R.id.radioGroup);
        rl3 = (RelativeLayout) findViewById(R.id.relativeLayout3);
        rl4 = (RelativeLayout) findViewById(R.id.relativeLayout4);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radio0:
                        rl3.setVisibility(View.VISIBLE);
                        rl4.setVisibility(View.GONE);
                        break;
                    case R.id.radio1:
                        rl4.setVisibility(View.VISIBLE);
                        rl3.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    final int   DIALOG_FROMDATE  = 1;
    final int   DIALOG_TODATE  = 2;
    final int   DIALOG_ATTIME  = 3;


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggleButton:
                vs.showNext();
                break;
            case R.id.fromdate_lb:
                showDialog(DIALOG_FROMDATE);
                break;

            case R.id.todate_lb:
                showDialog(DIALOG_TODATE);
                break;

            case R.id.attime_lb:
                showDialog(DIALOG_ATTIME);
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(final int id) {
        Calendar cal = Calendar.getInstance();
        switch(id) {
            case DIALOG_ATTIME:
                TimePickerDialog.OnTimeSetListener mTimeSetListener =
                        new TimePickerDialog.OnTimeSetListener() {
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // TODO capture time
                            }
                        };
                return new TimePickerDialog(this, mTimeSetListener, cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE), false);

            case DIALOG_FROMDATE:
            case DIALOG_TODATE:
                DatePickerDialog.OnDateSetListener dateListener =
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                // TODO capture date
                            }
                        };
                return new DatePickerDialog(this, dateListener, cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        }

        return super.onCreateDialog(id);
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {

        datePicker.getYear();
        datePicker.getMonth();
        datePicker.getDayOfMonth();

        timePicker.getCurrentHour();
        timePicker.getCurrentMinute();

        super.onPrepareDialog(id, dialog);

        switch(id) {
            case DIALOG_ATTIME:
                ((TimePickerDialog)dialog).updateTime(hourOfDay, minute);
                break;

            case DIALOG_FROMDATE:
                ((DatePickerDialog)dialog).updateDate(year, monthOfYear, dayOfMonth);
                break;

            case DIALOG_TODATE:
                ((DatePickerDialog)dialog).updateDate(year, monthOfYear, dayOfMonth);
                break;
        }
    }

}