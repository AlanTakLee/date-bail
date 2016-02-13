package atl.date_bail;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import atl.date_bail.model.DateInfo;
import atl.date_bail.model.DateReaderContract;
import atl.date_bail.model.DateReaderDbHelper;
import atl.date_bail.model.IdHolder;
import atl.date_bail.services.DelayedAlarmService;

public class FormActivity extends AppCompatActivity {

    private TextView contact1;
    private TextView contact2;
    final int CONTACT_PICKER_RESULT1 = 1001;
    final int CONTACT_PICKER_RESULT2 = 1002;
    private String[] numbers = new String[2];
    private String[] names = new String[2];
    private DateInfo currentDateInfo;
    private Boolean newInfo = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        // check if the intent came from list or fab
        currentDateInfo = new DateInfo();
        Intent intent = getIntent();
        Bundle bund = intent.getExtras();
        if (bund != null) {
            long maybeId = bund.getLong("id", -1);
            if (maybeId > -1) {
                newInfo = false;
                fetchCurrentDateInfo(maybeId);
                populateForm();
            }
        }
        // setup UI elements
        setupDeleteButton();
        setupToolbar();
        setupTimePicker();
        setupDatePicker();
        setupContactPicker();
    }

    private void setupDeleteButton() {
        final DateReaderDbHelper mDbHelper = new DateReaderDbHelper(this);
        Button button = (Button) findViewById(R.id.dateFormEventButtonDelete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                String selection = DateReaderContract.DateEntry.COLUMN_NAME_ID + " LIKE ?";
                String[] args = {String.valueOf(currentDateInfo.getId())};
                db.delete(DateReaderContract.DateEntry.TABLE_NAME, selection, args);
                finish();
            }
        });
    }

    private void populateForm() {
        EditText titleTxt = (EditText) findViewById(R.id.dateFormEventNameEditText);
        TextView dateTxt = (TextView) findViewById(R.id.dateFormEventDateText);
        TextView timeTxt = (TextView) findViewById(R.id.dateFormEventTimeText);
        EditText locaTxt = (EditText) findViewById(R.id.dateFormEventLocationEditText);
        EditText noteTxt = (EditText) findViewById(R.id.dateFormEventDescriptionEditText);
        contact1 = (TextView) findViewById(R.id.dateFormEventPrimaryText);
        contact2 = (TextView) findViewById(R.id.dateFormEventSecondaryText);

        titleTxt.setText(currentDateInfo.getName());
        dateTxt.setText(currentDateInfo.getDate());
        timeTxt.setText(currentDateInfo.getTime());
        locaTxt.setText(currentDateInfo.getLocation());
        noteTxt.setText(currentDateInfo.getNotes());

        String[] peoples = currentDateInfo.getBailouts().split("\n");
        ArrayList<String> bails = new ArrayList<>();
        for (String ppl : peoples) {
            String[] temp = ppl.split(",");
            bails.add(temp[0]);
            bails.add(temp[1]);
        }
        String c1 = bails.get(0) + "\n" + bails.get(1);
        contact1.setText(c1);
        names[0] = bails.get(0);
        numbers[0] = bails.get(1);

        String c2 = bails.get(2) + "\n" + bails.get(3);
        contact2.setText(c2);
        names[1] = bails.get(2);
        numbers[1] = bails.get(3);
    }

    private void fetchCurrentDateInfo(long id) {
        DateReaderDbHelper readerDbHelper = new DateReaderDbHelper(this);
        SQLiteDatabase db = readerDbHelper.getReadableDatabase();
        String[] args = new String[1];
        args[0] = "" + id;
        Cursor data = db.query(
            DateReaderContract.DateEntry.TABLE_NAME,  // The table to query
            null,                               // The columns to return
            DateReaderContract.DateEntry.COLUMN_NAME_ID + "=?", // The columns for the WHERE clause
            args,                            // The values for the WHERE clause
            null,                                     // don't group the rows
            null,                                     // don't filter by row groups
            null                                 // The sort order
        );

        while (data.moveToNext()) {
            DateInfo current = new DateInfo();
            for (int j = 1; j < data.getColumnCount(); j++) {
                switch (j) {
                    case 1: {
                        current.setId(data.getLong(j));
                        break;
                    }
                    case 2: {
                        current.setName(data.getString(j));
                        break;
                    }
                    case 3: {
                        current.setTime(data.getString(j));
                        break;
                    }
                    case 4: {
                        current.setDate(data.getString(j));
                        break;
                    }
                    case 5: {
                        current.setLocation(data.getString(j));
                        break;
                    }
                    case 6: {
                        current.setBailouts(data.getString(j));
                        break;
                    }
                    case 7: {
                        current.setNotes(data.getString(j));
                        break;
                    }
                }
            }
            currentDateInfo = current;
        }
        data.close();
    }

    private void setupDatePicker() {
        RelativeLayout dateLayout = (RelativeLayout) findViewById(R.id.dateFormDateLayout);
        ImageView calImg = (ImageView) findViewById(R.id.dateFormEventDateIcon);
        final TextView date = (TextView) findViewById(R.id.dateFormEventDateText);

        View.OnClickListener datePick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dateFrag = new DatePickerFragment();
                ((DatePickerFragment) dateFrag).setToEdit(date);
                dateFrag.show(getSupportFragmentManager(), "datePick");
            }
        };

        dateLayout.setOnClickListener(datePick);
        date.setOnClickListener(datePick);
        calImg.setOnClickListener(datePick);
    }

    private void setupTimePicker() {
        RelativeLayout timeLayout = (RelativeLayout) findViewById(R.id.dateFormTimeLayout);
        ImageView clockImg = (ImageView) findViewById(R.id.dateFormEventTimeIcon);
        final TextView time = (TextView) findViewById(R.id.dateFormEventTimeText);

        View.OnClickListener timePick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timeFrag = new TimePickerFragment();     //time-picker popup
                ((TimePickerFragment) timeFrag).setToEdit(time);
                timeFrag.show(getSupportFragmentManager(), "timePick");
            }
        };
        timeLayout.setOnClickListener(timePick);
        time.setOnClickListener(timePick);
        clockImg.setOnClickListener(timePick);
    }

    private void setupContactPicker() {
        contact1 = (TextView) findViewById(R.id.dateFormEventPrimaryText);
        contact2 = (TextView) findViewById(R.id.dateFormEventSecondaryText);

        contact1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactIntent, CONTACT_PICKER_RESULT1);
            }
        });
        contact2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent contactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactIntent, CONTACT_PICKER_RESULT2);
            }
        });
    }

    private void setupToolbar() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.formToolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_48dp);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button button = (Button) findViewById(R.id.formToolbarSaveBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri result = data.getData();
            String[] PROJECTION = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER,
            };
            String SELECTION = ContactsContract.Contacts.HAS_PHONE_NUMBER + "='1'";
            Cursor cursor = getContentResolver().query(result, PROJECTION, SELECTION, null, null);
            if (cursor != null) {
                cursor.moveToFirst();

                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);

                cursor.close();

                String phone = "";
                if (phones != null) {
                    phones.moveToFirst();
                    phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    phones.close();
                }
                switch (requestCode) {
                    case CONTACT_PICKER_RESULT1: {
                        String display = "" + name + "\n" + phone;
                        names[0] = name;
                        numbers[0] = phone;
                        contact1.setText(display);
                        break;
                    }
                    case CONTACT_PICKER_RESULT2: {
                        String display = "" + name + "\n" + phone;
                        names[1] = name;
                        numbers[1] = phone;
                        contact2.setText(display);
                        break;
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void saveData() {
        EditText titleTxt = (EditText) findViewById(R.id.dateFormEventNameEditText);
        EditText locationTxt = (EditText) findViewById(R.id.dateFormEventLocationEditText);
        EditText descTxt = (EditText) findViewById(R.id.dateFormEventDescriptionEditText);
        TextView dateTxt = (TextView) findViewById(R.id.dateFormEventDateText);
        TextView timeTxt = (TextView) findViewById(R.id.dateFormEventTimeText);
        ContentValues values = new ContentValues();
        StringBuilder strBuilder = new StringBuilder();

        Long idToSave = IdHolder.getInstance().getLastId();
        String nameToSave = titleTxt.getText().toString();
        String locationToSave = locationTxt.getText().toString();
        for (int i = 0; i < 2; i++) {
            String n = names[i];
            String p = numbers[i];
            if (p != null && !p.isEmpty()) {
                String clean = PhoneNumberUtils.normalizeNumber(p);
                String formattedNumber = PhoneNumberUtils.formatNumber(clean);
                strBuilder.append(n);
                strBuilder.append(", ");
                strBuilder.append(formattedNumber);
                strBuilder.append('\n');
            } else {
                strBuilder.append("");
            }
        }
        String bailers = strBuilder.toString();
        String bailersToSave = bailers.substring(0, bailers.length() - 1);
        String dateToSave = IdHolder.getInstance().getSaveDate();
        String timeToSave = IdHolder.getInstance().getSaveTime();
        String notesToSave = descTxt.getText().toString();

        currentDateInfo.setName(nameToSave);
        currentDateInfo.setLocation(locationToSave);
        currentDateInfo.setNotes(notesToSave);
        currentDateInfo.setBailouts(bailersToSave);
        currentDateInfo.setDate(IdHolder.getInstance().getSaveDate());
        currentDateInfo.setTime(IdHolder.getInstance().getSaveTime());

        if (newInfo) {
            currentDateInfo.setId(idToSave);
            values.put(DateReaderContract.DateEntry.COLUMN_NAME_ID, idToSave);
            values.put(DateReaderContract.DateEntry.COLUMN_NAME_TIME, timeToSave);
            values.put(DateReaderContract.DateEntry.COLUMN_NAME_DATE, dateToSave);
            long temp = IdHolder.getInstance().getLastId() + 1;
            IdHolder.getInstance().setLastId(temp);
        } else {
            values.put(DateReaderContract.DateEntry.COLUMN_NAME_ID, currentDateInfo.getId());
            values.put(DateReaderContract.DateEntry.COLUMN_NAME_TIME, timeTxt.getText().toString());
            values.put(DateReaderContract.DateEntry.COLUMN_NAME_DATE, dateTxt.getText().toString());

        }
        values.put(DateReaderContract.DateEntry.COLUMN_NAME_NAME, nameToSave);
        values.put(DateReaderContract.DateEntry.COLUMN_NAME_LOCATION, locationToSave);
        values.put(DateReaderContract.DateEntry.COLUMN_NAME_CONTACTS, bailersToSave);
        values.put(DateReaderContract.DateEntry.COLUMN_NAME_NOTES, notesToSave);
        // Insert the new row, returning the primary key value of the new row
        DateReaderDbHelper mDbHelper = new DateReaderDbHelper(this);
        if (newInfo) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            db.insert(
                DateReaderContract.DateEntry.TABLE_NAME,
                null,
                values);
        } else {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();
            String selection = DateReaderContract.DateEntry.COLUMN_NAME_ID + " LIKE ?";
            String[] args = {String.valueOf(currentDateInfo.getId())};
            db.update(DateReaderContract.DateEntry.TABLE_NAME,
                values, selection, args);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm aa", Locale.getDefault());
        String toParse = dateToSave + " " + timeToSave;
        try {
            Date date = sdf.parse(toParse);
            // Only trigger alarm manager if later than now
            if (date.getTime() > Calendar.getInstance().getTime().getTime()) {
                AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(getBaseContext(), DelayedAlarmService.class);
                intent.putExtra("id", idToSave);
                PendingIntent sender = PendingIntent.getService(getBaseContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                am.cancel(sender);
                am.set(AlarmManager.RTC_WAKEUP, date.getTime(), sender);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        finish();
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        private TextView toEdit;

        public void setToEdit(TextView view) {
            toEdit = view;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, false);
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar instance = Calendar.getInstance();
            instance.set(Calendar.HOUR_OF_DAY, hourOfDay);
            instance.set(Calendar.MINUTE, minute);
            instance.set(Calendar.SECOND, 0);
            SimpleDateFormat format = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
            String toDisplay = format.format(instance.getTime());
            IdHolder.getInstance().setSaveTime(toDisplay);
            toEdit.setText(toDisplay);
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        private TextView toEdit;

        public void setToEdit(TextView view) {
            toEdit = view;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar instance = Calendar.getInstance();
            instance.set(Calendar.YEAR, year);
            instance.set(Calendar.MONTH, monthOfYear);
            instance.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String toDisplay = DateFormat.getDateInstance().format(instance.getTime());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            IdHolder.getInstance().setSaveDate(format.format(instance.getTime()));
            toEdit.setText(toDisplay);
        }
    }
}
