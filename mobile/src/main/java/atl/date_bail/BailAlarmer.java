package atl.date_bail;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

/**
 * Created by Alan on 13/02/2016.
 */
public class BailAlarmer {
    public static final int BAIL_CODE = 1738;

    public BailAlarmer() {}
    public void setAlarm(Context context) {
        // get a Calendar object with current time
        Calendar cal = Calendar.getInstance();
        // add 5 minutes to the calendar object
//        cal.add(Calendar.MINUTE, 5);
        cal.add(Calendar.SECOND, 3);
        Intent intent = new Intent(context, BailAlarmReceiver.class);
        intent.putExtra("message", "Do you need to get bailed out?");
        // In reality, you would want to have a static variable for the request code instead of 192837
        PendingIntent sender = PendingIntent.getBroadcast(context, BAIL_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get the AlarmManager service
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
    }

}
