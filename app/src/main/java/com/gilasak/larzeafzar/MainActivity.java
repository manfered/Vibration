package com.gilasak.larzeafzar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.farsitel.bazaar.IUpdateCheckService;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    IUpdateCheckService service;
    UpdateServiceConnection connection;
    private static final String TAG = "UpdateCheck";

    Button buttonUpdateYes;
    Button buttonUpdateNo;
    Dialog updateDialog;

    Toolbar mainToolBar;
    DrawerLayout drawerLayout;
    TextView mainToolBarTextview;
    NavigationView navigationView;

    ImageView navigationViewHeaderImageView;

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateDialog = new Dialog(MainActivity.this);
        updateDialog.setContentView(R.layout.update_dialog_layout);

        initService();
        setContentView(R.layout.main);

        //first initilization for units and settings
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        if (!prefs.getBoolean("firstTime", false)) {
            // run your one time code
            // first initilization
            editor.putString("sensorUnit" , "MS2");

            //Code block ends
            editor.putBoolean("firstTime", true);
            editor.commit();
        }


        buttonUpdateYes = (Button) updateDialog.findViewById(R.id.buttonUpdateYes);
        buttonUpdateNo = (Button) updateDialog.findViewById(R.id.buttonUpdateNo);

        mainToolBar = (Toolbar) findViewById(R.id.mainToolBar);
        mainToolBarTextview = (TextView) findViewById(R.id.mainToolBarTextview);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);

        View headerView = getLayoutInflater().inflate(R.layout.navigationview_header_layout, navigationView, false);
        navigationViewHeaderImageView = (ImageView) headerView.findViewById(R.id.navigationViewHeaderImageView);
        navigationView.addHeaderView(headerView);

        setSupportActionBar(mainToolBar);

        Typeface tf1 = Typeface.createFromAsset(getAssets(), "fonts/A.Pixel.2.ttf");
        mainToolBarTextview.setTypeface(tf1);


        Menu m = navigationView.getMenu();
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_content, new MenuTileListFragment(), "MenuTitleList");
        fragmentTransaction.commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Integer id = item.getItemId();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                if (id == R.id.navigationViewMenuTileList) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    fragmentTransaction.replace(R.id.fragment_content, new MenuTileListFragment(), "MenuTitleList");
                    fragmentTransaction.commit();
                    //az kar andakhtan dokmeye back dar toolbar
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setDisplayShowHomeEnabled(false);
                    return true;

                } else if (id == R.id.navigationViewSensor) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    fragmentTransaction.replace(R.id.fragment_content, new SensorFragment());
                    fragmentTransaction.commit();
                    return true;

                }
                else if (id == R.id.navigationViewAboutus) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    fragmentTransaction.replace(R.id.fragment_content, new AboutFragment());
                    fragmentTransaction.commit();
                    return true;

                } else if (id == R.id.navigationViewContactus) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    fragmentTransaction.replace(R.id.fragment_content, new ContactFragment());
                    fragmentTransaction.commit();
                    return true;

                } else if (id == R.id.navigationViewExit) {
                    finish();
                }
                return false;
            }
        });

        buttonUpdateYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("bazaar://details?id=" + "com.gilasak.larzeafzar"));
                    intent.setPackage("com.farsitel.bazaar");
                    startActivity(intent);
                }
                catch (Exception ex){
                    String msg = ex.getMessage();
                    Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonUpdateNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDialog.dismiss();
            }
        });

        //testupdate
        //updateDialog.show();
        navigationViewHeaderImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://www.laserafzar.ir"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Integer id = item.getItemId();
        if (id == android.R.id.home) {
            //bazgash be fragment asli aval
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_content, new MenuTileListFragment(), "MenuTitleList");
            fragmentTransaction.commit();
            //az kar andakhtan dokmeye back dar toolbar
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayShowHomeEnabled(false);

        } else if (id == R.id.navigationMenuAction) {
            if (!drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            } else {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Mehdi.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    @Override
    public void onBackPressed() {
        MenuTileListFragment myFragment = (MenuTileListFragment) getSupportFragmentManager().findFragmentByTag("MenuTitleList");
        if (myFragment != null && myFragment.isVisible()) {
            // add your code here
            if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            } else {
                if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
                    super.onBackPressed();
                    return;
                } else {
                    Toast.makeText(getBaseContext(), "برای خروج دوبار دکمه back را فشار دهید", Toast.LENGTH_SHORT).show();
                }

                mBackPressed = System.currentTimeMillis();
            }
        } else {
            if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                drawerLayout.closeDrawer(Gravity.RIGHT);
            } else {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_content, new MenuTileListFragment(), "MenuTitleList");
                fragmentTransaction.commit();
                //az kar andakhtan dokmeye back dar toolbar dar surate vojood
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowHomeEnabled(false);
            }
        }


    }


    class UpdateServiceConnection implements ServiceConnection {
        public void onServiceConnected(ComponentName name, IBinder boundService) {
            service = IUpdateCheckService.Stub
                    .asInterface((IBinder) boundService);
            try {
                long vCode = service.getVersionCode("com.gilasak.larzeafzar");
                if (vCode != -1) {
                    //update dialog
                    updateDialog.show();
                }
                //Toast.makeText(MainActivity.this, "Version Code:" + vCode,Toast.LENGTH_LONG).show();

            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e(TAG, "onServiceConnected(): Connected");
        }

        public void onServiceDisconnected(ComponentName name) {
            service = null;
            Log.e(TAG, "onServiceDisconnected(): Disconnected");
        }
    }


    private void initService() {
        Log.i(TAG, "initService()");
        connection = new UpdateServiceConnection();
        Intent i = new Intent(
                "com.farsitel.bazaar.service.UpdateCheckService.BIND");
        i.setPackage("com.farsitel.bazaar");
        boolean ret = bindService(i, connection, Context.BIND_AUTO_CREATE);
        Log.e(TAG, "initService() bound value: " + ret);
    }


    /**
     * This is our function to un-binds this activity from our service.
     */
    private void releaseService() {
        unbindService(connection);
        connection = null;
        Log.d(TAG, "releaseService(): unbound.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseService();
    }



}
