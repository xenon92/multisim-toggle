package com.shubhangrathore.multisimtoggle;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

    private static String sCommandToGetMultiSimProp = "getprop persist.radio.multisim.config";
    private static String sCommandToSetMultiSimPropActive = "setprop persist.radio.multisim.config dsds";
    private static String sCommandToSetMultiSimPropDeactive = "setprop persist.radio.multisim.config none";
    private static String sCommandToReboot = "reboot";
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeFloatingButtons();
        setAppVersion();
        setCurrentMultiSimStatusOnTextView();
        getCurrentMultiSimStatus();
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

            String mVersionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            TextView mVersionTextView = (TextView) findViewById(R.id.app_version);
            mVersionTextView.setText(getString(R.string.version) + mVersionName);

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Unable to set app version");
        }
    }

    private void initializeFloatingButtons() {
        Fab mFabSimToggle = (Fab) findViewById(R.id.fabbutton_sim_toggle);
        mFabSimToggle.setFabColor(getResources().getColor(R.color.material_pink));
        mFabSimToggle.setFabDrawable(getResources().getDrawable(R.drawable.ic_single_sim));
        mFabSimToggle.showFab();
        mFabSimToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (getCurrentMultiSimStatus()) {

                    new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_HOLO_LIGHT)
                            .setTitle(getString(R.string.disable_multisim))
                            .setMessage(getString(R.string.disable_multisim_warning))
                            .setPositiveButton(R.string.continue_button, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int integer) {

                                    boolean successful = Utilities.runAsRoot(sCommandToSetMultiSimPropDeactive);

                                    if (successful) {
                                        Toast.makeText(MainActivity.this, getString(R.string.multisim_disabled), Toast.LENGTH_SHORT).show();
                                        rebootDevice();
                                    } else {
                                        Toast.makeText(MainActivity.this, getString(R.string.unable_to_execute), Toast.LENGTH_SHORT).show();
                                    }

                                    setCurrentMultiSimStatusOnTextView();
                                }
                            })
                            .setIcon(R.drawable.ic_single_sim_dark)
                            .show();

                } else {

                    new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_HOLO_LIGHT)
                            .setTitle(getString(R.string.enable_multisim))
                            .setMessage(getString(R.string.enable_multisim_warning))
                            .setPositiveButton(R.string.continue_button, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int integer) {

                                    boolean successful = Utilities.runAsRoot(sCommandToSetMultiSimPropActive);

                                    if (successful) {
                                        Toast.makeText(MainActivity.this, getString(R.string.multisim_enabled), Toast.LENGTH_SHORT).show();
                                        rebootDevice();
                                    } else {
                                        Toast.makeText(MainActivity.this, getString(R.string.unable_to_execute), Toast.LENGTH_SHORT).show();
                                    }
                                    setCurrentMultiSimStatusOnTextView();
                                }
                            })
                            .setIcon(R.drawable.ic_single_sim_dark)
                            .show();
                }
            }
        });

        Fab mFabGithub = (Fab) findViewById(R.id.fabbutton_github);
        mFabGithub.setFabColor(getResources().getColor(R.color.material_green));
        mFabGithub.setFabDrawable(getResources().getDrawable(R.drawable.ic_github));
        mFabGithub.showFab();
        mFabGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), getString(R.string.github_source), Toast.LENGTH_SHORT).show();
                openLink(GITHUB_SOURCE_LINK);
            }
        });

        Fab mFabInfo = (Fab) findViewById(R.id.fabbutton_info);
        mFabInfo.setFabColor(getResources().getColor(R.color.material_amber));
        mFabInfo.setFabDrawable(getResources().getDrawable(R.drawable.ic_info));
        mFabInfo.showFab();
        mFabInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), getString(R.string.know_more), Toast.LENGTH_SHORT).show();
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

    private boolean getCurrentMultiSimStatus() {

        String mMultiSimPropValue = "dsds";

        String mCurrentMultiSimPropValue = Utilities.executeOnShell(sCommandToGetMultiSimProp);

        return mCurrentMultiSimPropValue.equals(mMultiSimPropValue);
    }

    private void setCurrentMultiSimStatusOnTextView() {

        TextView mMultiSimStatusTextView = (TextView) findViewById(R.id.multisim_status);

        if (getCurrentMultiSimStatus()) {
            mMultiSimStatusTextView.setText(R.string.enabled);
        } else {
            mMultiSimStatusTextView.setText(R.string.disabled);
        }
    }

    private void rebootDevice() {

        Log.i(TAG, "Rebooting device...");

        mProgressDialog = new ProgressDialog(this, AlertDialog.THEME_HOLO_LIGHT);
        mProgressDialog.setMessage(getString(R.string.rebooting));
        mProgressDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                // Sleep for 3 seconds
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Log.e(TAG, "Sleeping thread interrupted before reboot command execution");
                }

                // Reboot phone
                Utilities.runAsRoot(sCommandToReboot);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog.dismiss();
                    }
                });
            }
        }).start();
    }
}
