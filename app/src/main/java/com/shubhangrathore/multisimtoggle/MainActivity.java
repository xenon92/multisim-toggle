package com.shubhangrathore.multisimtoggle;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends Activity {

    public static final String TAG = "MultisimToggle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setAppVersion();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_exit) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAppVersion() {
        try {

            String mVersionName = getPackageManager().getPackageInfo(getPackageName(),0).versionName;
            TextView mVersionTextView = (TextView) findViewById(R.id.app_version);
            mVersionTextView.setText(getString(R.string.version) + mVersionName);

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Unable to set app version");
        }
    }
}
