package atl.date_bail.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.telephony.SmsManager;

import atl.date_bail.model.DateInfo;
import atl.date_bail.model.DateReaderContract;
import atl.date_bail.model.DateReaderDbHelper;

public class BailingService extends IntentService {

    public BailingService() {
        super("");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BailingService(String name) {
        super(name);
    }

    private DateInfo loadData(long maybeId) {
        DateReaderDbHelper readerDbHelper = new DateReaderDbHelper(this);
        SQLiteDatabase db = readerDbHelper.getReadableDatabase();
        String[] args = new String[1];
        args[0] = "" + maybeId;
        Cursor data = db.query(
            DateReaderContract.DateEntry.TABLE_NAME,  // The table to query
            null,                               // The columns to return
            DateReaderContract.DateEntry.COLUMN_NAME_ID + "=?", // The columns for the WHERE clause
            args,                            // The values for the WHERE clause
            null,                                     // don't group the rows
            null,                                     // don't filter by row groups
            null                                 // The sort order
        );

        DateInfo current = new DateInfo();
        while (data.moveToNext()) {
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
        }
        data.close();
        return current;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // dismiss notification if it makes it here
        NotificationManager mNotificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(BailAlarmReceiver.BAIL_NOTI_ID);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            long maybeId = bundle.getLong("id", -1);
            if (maybeId > -1) {
                SmsManager smsManager = SmsManager.getDefault();
                DateInfo currentData = loadData(maybeId);
                String[] peoples = currentData.getBailouts().split("\n");
                String[] person1 = peoples[0].split(",");
                String[] person2 = peoples[1].split(",");
                smsManager.sendTextMessage(person1[1], null, "Hey " + person1[0].split(" ")[0] + ", " +
                    "I need you to bail me out of this bad date. Give me a call?", null, null);
                smsManager.sendTextMessage(person2[1], null, "Hey " + person2[0].split(" ")[0] + ", " +
                    "I need you to bail me out of this bad date. Give me a call?", null, null);
            }
        }
    }
}
