/*
 * MultiSIM Toggle
 *
 * Android app to enable or disable multiSIM functionality on multiSIM supporting ROMs
 *
 * Copyright (c) 2014 Shubhang Rathore
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

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
