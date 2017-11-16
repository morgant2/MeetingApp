package com.meetingapp.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.meetingapp.R;

abstract public class BaseActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener{
    private FrameLayout mFrameLayout;               //Frame layout which holds the activity
    private NavigationView mDrawer;         //Navigation drawer
    private DrawerLayout mDrawerLayout;             // Base activity root laout
    private ActionBarDrawerToggle mDrawerToggle;
    private Menu drawerMenu;                        //menu to populate drawer

    // Need to override this in each activity which is opened with the nav drawer
    // provides the layoutid of the activity for setting the content view in onCreate in BaseActivity
    protected abstract @LayoutRes int getLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content view to the base layout which contains the nav drawer
        setContentView(R.layout.activity_base);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        mDrawer = (NavigationView)findViewById(R.id.navigation_drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                0, 0);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerMenu = mDrawer.getMenu();
        for(int i = 0; i < drawerMenu.size(); i++) {
            drawerMenu.getItem(i).setOnMenuItemClickListener(this);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_view_profile:
                // todo: start activity
                break;
            case R.id.menu_create_meeting:
                //todo: start activity
                break;
            case R.id.menu_scheduled_meetings:
                // todo: start activity
                break;
            case R.id.menu_view_contacts:
                // todo: start activity
                break;
        }
        return false;
    }
}
