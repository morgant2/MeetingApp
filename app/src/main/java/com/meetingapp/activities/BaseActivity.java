package com.meetingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.meetingapp.R;


/**
 * BaseActivity will add a Navigation Drawer to our application.
 * All activities with a NavigationDrawer will extend BaseActivity.
 * The layout for this activity is a DrawerLayout with a FrameLayout and NavigationView.
 * We will add our child activity to the frame layout;
 * We will add our navigation drawer to the NavigationView
 */
public class BaseActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener{

    /** mFrameLayout : parent layout for the child activity layout.
     *  protected - so that child activity can access
     */
//    todo: determine best scope qualifier
    protected FrameLayout mFrameLayout;
    /**
     * mNavigationDrawer : NavigationView to add navigation drawer to
     */
//    todo: determine best scope qualifier
    protected NavigationView mNavigationDrawer;
    /**
     * mDrawerLayout : BaseActivity's root layout.
     */
    private DrawerLayout mDrawerLayout;
    /**
     * mDrawerToggle : listener for drawer open, close,
     */
    private ActionBarDrawerToggle mDrawerToggle;
    /**
     * mDrawerMenu : menu to populate navigation drawer
     */
    private Menu mDrawerMenu;

    //Todo: getLayoutID????
/*    // Need to override this in each activity which is opened with the nav drawer
    // provides the layoutid of the activity for setting the content view in onCreate in BaseActivity
    protected abstract @LayoutRes int getLayoutId();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("pref_dark_theme", false)) {
            setTheme(R.style.AppTheme_Dark);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar_base);
        setSupportActionBar(myToolbar);
        // enable ActionBar app icon to behave as action to toggle nav drawer
        // need to be using custom toolbar to make this work properly
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mFrameLayout = (FrameLayout) findViewById(R.id.content_frame);
        mNavigationDrawer = (NavigationView)findViewById(R.id.navigation_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, myToolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerMenu = mNavigationDrawer.getMenu();
        // Set onMenuItemClickListener for each menu item
        for(int i = 0; i < mDrawerMenu.size(); i++) {
            mDrawerMenu.getItem(i).setOnMenuItemClickListener(this);
        }

        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        mDrawerLayout.closeDrawer(mNavigationDrawer);

        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.menu_create_meeting:
                startActivity(new Intent(this, CreateMeetingActivity.class));
                break;
            case R.id.menu_scheduled_meetings:
                startActivity(new Intent(this, ScheduledMeetingsActivity.class));
                break;
            case R.id.menu_view_contacts:
                startActivity(new Intent(this, ContactsActivity.class));
                break;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
