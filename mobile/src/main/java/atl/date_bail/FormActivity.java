package atl.date_bail;

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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import atl.date_bail.model.DateInfo;

public class FormActivity extends AppCompatActivity {

    private TextView contact1;
    private TextView contact2;
    final int CONTACT_PICKER_RESULT1 = 1001;
    final int CONTACT_PICKER_RESULT2 = 1002;
    private String[] numbers = new String[2];
    private DateInfo currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        currentDate = new DateInfo();
        setupToolbar();
        setupTimePicker();
        setupContactPicker();
        //TODO: on create, check is message was passed in
    }

    private void setupTimePicker() {
        RelativeLayout timeLayout = (RelativeLayout) findViewById(R.id.dateFormTimeLayout);
        final TextView time = (TextView) findViewById(R.id.dateFormEventTimeText);
        timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Test", "Captured!");
                DialogFragment timeFrag = new TimePickerFragment();
                ((TimePickerFragment) timeFrag).setToEdit(time);
                timeFrag.show(getSupportFragmentManager(), "timePick");
            }
        });
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
            SimpleDateFormat format = new SimpleDateFormat("hh:mm:aa", Locale.getDefault());
            String toDisplay = format.format(instance.getTime());
            toEdit.setText(toDisplay);
        }
    }
}
