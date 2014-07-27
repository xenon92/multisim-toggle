package com.shubhangrathore.multisimtoggle;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.faizmalkani.floatingactionbutton.Fab;


public class MainActivity extends Activity {

    public static final String TAG = "MultisimToggle";

    private Fab mFabSimToggle;
    private Fab mFabGithub;
    private Fab mFabInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeFloatingButtons();
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

    private void initializeFloatingButtons() {
        mFabSimToggle = (Fab) findViewById(R.id.fabbutton_sim_toggle);
        mFabSimToggle.setFabColor(getResources().getColor(R.color.material_pink));
        mFabSimToggle.setFabDrawable(getResources().getDrawable(R.drawable.ic_single_sim));
        mFabSimToggle.showFab();

        mFabGithub = (Fab) findViewById(R.id.fabbutton_github);
        mFabGithub.setFabColor(getResources().getColor(R.color.material_green));
        mFabGithub.setFabDrawable(getResources().getDrawable(R.drawable.ic_github));
        mFabGithub.showFab();

        mFabInfo = (Fab) findViewById(R.id.fabbutton_info);
        mFabInfo.setFabColor(getResources().getColor(R.color.material_amber));
        mFabInfo.setFabDrawable(getResources().getDrawable(R.drawable.ic_info));
        mFabInfo.showFab();
    }
}
