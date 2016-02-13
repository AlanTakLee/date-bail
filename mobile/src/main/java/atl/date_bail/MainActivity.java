package atl.date_bail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import atl.date_bail.model.DateInfo;

public class MainActivity extends AppCompatActivity implements DateFragment.DateFragmentInteractionListener {
    private String[] drawerTitles;
    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Context context = this;

        drawerTitles = getResources().getStringArray(R.array.draweritems);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        drawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_item_layout, R.id.drawerTitleTxt, drawerTitles));
        drawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switchFragment(position);
            }
        });
        drawerList.performItemClick(drawerList.getAdapter().getView(0, null, null), 0, 0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FormActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.i("bug", "tapped");
            BailAlarmer ba = new BailAlarmer();
            ba.setAlarm(this, 1);
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchFragment(int position) {
        currentPosition = position;
        Fragment fragment = null;
        switch (position) {
            case 0: {
                fragment = new DateFragment();
                break;
            }
            case 1: {
                fragment = new AboutFragment();
                break;
            }
            default: {

            }
        }
        if (fragment != null) {
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
        drawerList.setItemChecked(position, true);
        setTitle(drawerTitles[position]);
        drawerLayout.closeDrawer(drawerList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        switchFragment(currentPosition);
    }

    @Override
    public void onListClick(DateInfo item) {
        Intent intent = new Intent(this, FormActivity.class);
        Bundle extras = new Bundle();
        extras.putLong("id", item.getId());
        intent.putExtras(extras);
        startActivity(intent);
    }
}