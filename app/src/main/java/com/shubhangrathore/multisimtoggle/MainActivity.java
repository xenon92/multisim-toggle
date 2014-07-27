package com.shubhangrathore.multisimtoggle;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.faizmalkani.floatingactionbutton.Fab;


public class MainActivity extends Activity {

    public static final String TAG = "MultiSimToggle";
    
    public static final String GITHUB_SOURCE_LINK = "https://github.com/xenon92/multisim-toggle";
    public static final String MORE_INFO_BLOG_LINK = "http://blog.shubhangrathore.com/multisim-toggle/index.html";

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
        mFabGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), getString(R.string.github_source) , Toast.LENGTH_SHORT).show();
                openLink(GITHUB_SOURCE_LINK);
            }
        });

        mFabInfo = (Fab) findViewById(R.id.fabbutton_info);
        mFabInfo.setFabColor(getResources().getColor(R.color.material_amber));
        mFabInfo.setFabDrawable(getResources().getDrawable(R.drawable.ic_info));
        mFabInfo.showFab();
        mFabInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), getString(R.string.know_more) , Toast.LENGTH_SHORT).show();
                openLink(MORE_INFO_BLOG_LINK);
            }
        });
    }

    private void openLink(String link) {
        Log.i(TAG, "Opening link = " + link);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(link));
        startActivity(browserIntent);
    }
}
