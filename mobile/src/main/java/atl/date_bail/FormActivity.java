package atl.date_bail;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import atl.date_bail.model.DateInfo;
import atl.date_bail.model.IdHolder;

public class FormActivity extends AppCompatActivity {

    private TextView contact1;
    private TextView contact2;
    final int CONTACT_PICKER_RESULT1 = 1001;
    final int CONTACT_PICKER_RESULT2 = 1002;
    private String[] numbers = new String[2];
    private DateInfo currentDateInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        currentDateInfo = new DateInfo();
        setupToolbar();
        setupTimePicker();
        setupDatePicker();
        setupContactPicker();
        //TODO: on create, check is message was passed in
    }

    private void setupDatePicker() {
        RelativeLayout datelayout = (RelativeLayout) findViewById(R.id.dateFormDateLayout);
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

        datelayout.setOnClickListener(datePick);
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
                //todo: save form data
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
                        numbers[0] = phone;
                        contact1.setText(display);
                        break;
                    }
                    case CONTACT_PICKER_RESULT2: {
                        String display = "" + name + "\n" + phone;
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

        currentDateInfo.setName(titleTxt.getText().toString());
        currentDateInfo.setLocation(locationTxt.getText().toString());
        currentDateInfo.setNotes(descTxt.getText().toString());

        StringBuilder strBuilder = new StringBuilder();
        for (String s: numbers){
            strBuilder.append(s);
        }
        currentDateInfo.setBailouts(strBuilder.toString());

        Log.i("debug", strBuilder.toString());
        currentDateInfo.setDate(IdHolder.getInstance().getSaveDate());
        currentDateInfo.setTime(IdHolder.getInstance().getSaveTime());
        currentDateInfo.setId(IdHolder.getInstance().getLastId());
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

            SimpleDateFormat format1 = new SimpleDateFormat("hh:mm:ss aa", Locale.getDefault());
            IdHolder.getInstance().setSaveTime(format1.format(instance.getTime()));
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
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
            IdHolder.getInstance().setSaveDate(format.format(instance.getTime()));
            toEdit.setText(toDisplay);
        }
    }
}
