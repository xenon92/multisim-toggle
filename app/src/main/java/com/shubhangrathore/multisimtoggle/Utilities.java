package com.shubhangrathore.multisimtoggle;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Shubhang on 27/7/2014.
 */
public class Utilities {

    public final static String TAG = "MultiSimToggle";

    protected static boolean runAsRoot(String commandToExecute) {

        try {

            Process mProcess = Runtime.getRuntime().exec("su");
            DataOutputStream mDataOutputStream = new DataOutputStream(mProcess.getOutputStream());

            mDataOutputStream.writeBytes(commandToExecute + "\n");
            mDataOutputStream.writeBytes("exit\n");
            mDataOutputStream.flush();

            // We wait for the command to be completed completely
            // before moving forward. This ensures that the method
            // returns only after all the commands' execution is complete.
            mProcess.waitFor();

            if (mProcess.exitValue() == 1) {

                // If control is here, that means the sub process has returned
                // an unsuccessful exit code.
                // Most probably, SuperUser permission was denied
                Log.e(TAG, "Utilities: SuperUser permission denied. Abnormal termination of runAsRoot.");
                return false;

            } else {

                // SuperUser permission granted
                Log.i(TAG, "Utilities: SuperUser permission granted. Normal termination of runAsRoot.");
                return true;
            }

        } catch (IOException e) {

            Log.e(TAG, "Utilities: Unable to execute command as superuser!");
            return false;

        } catch (InterruptedException e) {

            Log.e(TAG, "Utilities: runAsRoot method interrupted");
            return false;

        }
    }

    protected static String executeOnShell(String command) {

        String mOutput = null;
        try {
            Process mProcess = Runtime.getRuntime().exec(command);
            mProcess.waitFor();
            BufferedReader mReader = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
            mOutput = mReader.readLine();
            Log.i(TAG, "Output from executeOnShell = " + mOutput);
        } catch (IOException e) {
            Log.e(TAG, "Utilities: Unable to execute command at shell without root - I/O Exception");
        } catch (InterruptedException e) {
            Log.e(TAG, "Utilities: Unable to execute command at shell without root - Process interrupted");
        }

        return mOutput;
    }
}
