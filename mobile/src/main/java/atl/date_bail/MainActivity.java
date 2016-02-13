package atl.date_bail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
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
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerTitles = getResources().getStringArray(R.array.draweritems);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        final ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.placeholder, R.string.placeholder) {
            @Override
            public void onDrawerClosed(View drawerView) {
                this.syncState();
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                this.syncState();
                super.onDrawerOpened(drawerView);
            }
        };
        final Context context = this;

        drawerToggle.syncState();

        drawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_item_layout, R.id.drawerTitleTxt, drawerTitles));
        drawerList.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switchFragment(position);
            }
        });
        drawerList.performItemClick(drawerList.getAdapter().getView(0, null, null), 0, 0);

        drawerLayout.setDrawerListener(drawerToggle);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FormActivity.class);
                startActivity(intent);
            }
        });
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU: {
                if (!drawerLayout.isDrawerOpen(drawerList)) {
                    drawerLayout.openDrawer(drawerList);
                }
                if (drawerLayout.isDrawerOpen(drawerList)) {
                    drawerLayout.closeDrawer(drawerList);
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}