package com.banba.digitalclock;

import android.app.ListActivity;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ernan on 14/12/13.
 * Copyrite Banba Inc. 2013.
 */
public class AlarmActivity extends ListActivity {

    private TextView headingText;
    private Typeface font;

    public final Calendar cal = Calendar.getInstance();
    public final Date dt = new Date();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.set_alarm);

        headingText = (TextView) findViewById(R.id.heading_tv);
        font = Typeface.createFromAsset(getAssets(), "fonts/digital-7.ttf");
        headingText.setTypeface(font);

        registerForContextMenu(getListView());
    }

    private Cursor createCursor() {
//        long time = cal.getTimeInMillis();
//        Cursor c = RemindMe.db.rawQuery("SELECT * FROM Notification WHERE time BETWEEN "+
//                time+" AND "+(time+86400000), null);
//        startManagingCursor(c);
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();

//        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
//                this,
//                R.layout.row,
//                createCursor(),
//                new String[]{Notification.COL_MSG, Notification.COL_DATETIME},
//                new int[]{R.id.msg_tv, R.id.time_tv});
//
//        adapter.setViewBinder(new ViewBinder() {
//            @Override
//            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
//                if (view.getId() == R.id.msg_tv)
//                    return false;
//
//                TextView tv = (TextView)view;
//                switch(view.getId()) {
//                    case R.id.time_tv:
//                        dt.setTime(cursor.getLong(columnIndex));
//                        tv.setText(dt.getHours()+":"+dt.getMinutes());
//                        break;
//                }
//                return true;
//            }
//        });
//        setListAdapter(adapter);
    }

}