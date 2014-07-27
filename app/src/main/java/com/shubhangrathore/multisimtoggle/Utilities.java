package com.shubhangrathore.multisimtoggle;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

    protected static String runAndReturnOutputAsRoot(String commandToExecute) {

        byte[] mBuffer = new byte[4096];
        int mRead;
        Process mProcess;

        try {

            mProcess = Runtime.getRuntime().exec("su");
            DataOutputStream mDataOutputStream = new DataOutputStream(mProcess.getOutputStream());

            mDataOutputStream.writeBytes(commandToExecute);
            mDataOutputStream.writeBytes("\n");
            InputStream mInputStream = mProcess.getInputStream();

            String mOutput = new String();

            while (true) {
                mRead = mInputStream.read(mBuffer);
                Log.d(TAG, "Value of mRead = " + mRead);

                if (mRead > 0) {

                    mOutput += new String(mBuffer, 0, mRead);
                    if (mRead < 4069) {
                        // we have read everything
                        break;
                    }

                } else {
                    return null;
                }
            }

            mOutput = mOutput.replaceAll("\\n", "");

            Log.i(TAG, "Utilities: Output from runAndReturnOutputAsRoot = " + mOutput);
            return mOutput;

        } catch (IOException e) {
            Log.e(TAG, "Utilities: Unable to execute command and return output as superuser!");
            return null;
        }
    }
}
