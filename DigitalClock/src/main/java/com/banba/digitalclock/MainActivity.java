package com.banba.digitalclock;

import android.app.Notification;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == android.R.id.list) {
            getMenuInflater().inflate(R.menu.context_menu, menu);
            menu.setHeaderTitle("Choose an Option");
            menu.setHeaderIcon(R.drawable.ic_dialog_menu_generic);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
                item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.menu_edit:
                showDialog(R.id.menu_edit);
                break;

            case R.id.menu_delete:
                Intent cancel = new Intent(this, AlarmService.class);
                cancel.putExtra("notificationId", String.valueOf(info.id));
                cancel.setAction(AlarmService.CANCEL);
                startService(cancel);

                SimpleCursorAdapter adapter = (SimpleCursorAdapter) getListAdapter();
                adapter.getCursor().requery();
                adapter.notifyDataSetChanged();
                break;
        }

        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private TextView headingText;
        private Typeface font;

        public final Calendar cal = Calendar.getInstance();
        public final Date dt = new Date();


        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.main_fragment, container, false);
////            AnalogClock analogClock = new AnalogClock(getResources());
//            ImageView v = (ImageView)getActivity().findViewById(R.id.analog_appwidget);
//            v.setImageBitmap(analogClock.draw());

            getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);

            headingText = (TextView) rootView.findViewById(R.id.heading_tv);
            font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/OpenSans.ttf");
            headingText.setTypeface(font);

            registerForContextMenu(getActivity().getListView());
            return rootView;
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        private Cursor createCursor() {
            long time = cal.getTimeInMillis();
            Cursor c = DigitalClockApp.db.rawQuery("SELECT * FROM Notification WHERE time BETWEEN " +
                    time + " AND " + (time + 86400000), null);
            startManagingCursor(c);
            return c;
        }

        @Override
        public void onResume() {
            super.onResume();

            SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                    this,
                    R.layout.row,
                    createCursor(),
                    new String[]{Notification.COL_MSG, Notification.COL_DATETIME},
                    new int[]{R.id.msg_tv, R.id.time_tv});

            adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                    if (view.getId() == R.id.msg_tv)
                        return false;

                    TextView tv = (TextView) view;
                    switch (view.getId()) {
                        case R.id.time_tv:
                            dt.setTime(cursor.getLong(columnIndex));
                            tv.setText(dt.getHours() + ":" + dt.getMinutes());
                            break;
                    }
                    return true;
                }
            });
            setListAdapter(adapter);
        }
    }
}
