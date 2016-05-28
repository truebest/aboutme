package shilin.aboutme.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import shilin.aboutme.R;
import shilin.aboutme.fragments.AboutMeFragment;
import shilin.aboutme.fragments.ContactFragment;
import shilin.aboutme.fragments.HomePageFragment;
import shilin.aboutme.fragments.MapFragment;
import shilin.aboutme.fragments.PortfolioPageFragment;
import shilin.aboutme.fragments.SkillsPageFragment;

/**
 * Created by beerko on 09.05.16.
 */


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, NavigationView.OnNavigationItemSelectedListener {

    private static final int REQUEST_PHONE_STATE = 0;

    private static final int REQUEST_FINE_LOCATION = 1;

    private static final int REQUEST_COURCE_LOCATION = 2;

    private static final int REQUEST_WIFI_STATE = 3;
    private int pageNum = -1;


    private void checkPermissions(Context context, Activity activity) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_PHONE_STATE)) {

            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
            }
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_COURCE_LOCATION);
            }
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_WIFI_STATE)) {

            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, REQUEST_WIFI_STATE);
            }
        }
    }

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private Toolbar toolbar;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions(this, this);


        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.home_page);

        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(5);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(4);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        toolbar.setTitle(R.string.home_page);
                        ((FloatingActionButton) findViewById(R.id.fab)).hide();
                        break;
                    case 1:
                        toolbar.setTitle(R.string.about_page);
                        ((FloatingActionButton) findViewById(R.id.fab)).show();
                        break;
                    case 2:
                        toolbar.setTitle(R.string.skils_page);
                        ((FloatingActionButton) findViewById(R.id.fab)).show();
                        break;
                    case 3:
                        toolbar.setTitle(R.string.portfolio_page);
                        ((FloatingActionButton) findViewById(R.id.fab)).hide();
                        break;
                    case 4:
                        toolbar.setTitle(R.string.map_page);
                        ((FloatingActionButton) findViewById(R.id.fab)).show();
                        break;
                    case 5:
                        toolbar.setTitle(R.string.contact_page);
                        ((FloatingActionButton) findViewById(R.id.fab)).hide();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (pageNum >= 0)
        mPager.setCurrentItem(pageNum);
    }

    @Override
    protected void onStop() {
        super.onStop();
        pageNum = mPager.getCurrentItem();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case REQUEST_PHONE_STATE:
                    break;
                case REQUEST_FINE_LOCATION:

                    break;
                case REQUEST_COURCE_LOCATION:

                    break;
                case REQUEST_WIFI_STATE:

                    break;
                default:
                    break;
            }
            checkPermissions(getApplicationContext(), getParent());

        } else {

            switch (requestCode) {
                case REQUEST_PHONE_STATE:
                    break;

                case REQUEST_FINE_LOCATION:
                    break;

                case REQUEST_COURCE_LOCATION:
                    break;

                case REQUEST_WIFI_STATE:
                    break;

                default:
                    break;
            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new HomePageFragment();
                    break;
                case 1:
                    fragment = new AboutMeFragment();
                    break;
                case 2:
                    fragment = new SkillsPageFragment();
                    break;
                case 3:
                    fragment = new PortfolioPageFragment();
                    break;
                case 4:
                    fragment = new MapFragment();
                    break;
                case 5:
                    fragment = new ContactFragment();
                default:
            }
            return fragment;
        }


        @Override
        public int getCount() {
            return 6;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home_page:
                mPager.setCurrentItem(0);
                break;
            case R.id.nav_about_page:
                mPager.setCurrentItem(1);
                break;
            case R.id.nav_skill_page:
                mPager.setCurrentItem(2);
                break;
            case R.id.nav_portfolio:
                mPager.setCurrentItem(3);
                break;
            case R.id.nav_map:
                mPager.setCurrentItem(4);
                break;
            case R.id.nav_contact:
                mPager.setCurrentItem(5);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
}
