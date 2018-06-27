
package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.Context;
import android.os.IBinder;
import android.content.ComponentName;

import com.noke.nokemobilelibrary.NokeDevice;
import com.noke.nokemobilelibrary.NokeServiceListener;
import com.noke.nokemobilelibrary.NokeDeviceManagerService;

public class RNNokeModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public RNNokeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNNoke";
    }

    private NokeDeviceManagerService mNokeService = null;
    private NokeDevice currentNoke;

    private void initiateNokeService() {
        Intent nokeServiceIntent = new Intent(reactContext, NokeDeviceManagerService.class);
        reactContext.bindService(nokeServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            //Store reference to service
            mNokeService = ((NokeDeviceManagerService.LocalBinder) rawBinder).getService();
            //Register callback listener
            mNokeService.registerNokeListener(mNokeServiceListener);
            //Start bluetooth scanning
            mNokeService.startScanningForNokeDevices();
            if (!mNokeService.initialize()) {
                //Log.e(TAG, "Unable to initialize Bluetooth");
            }
        }
        public void onServiceDisconnected(ComponentName classname) {
            mNokeService = null;
        }
    };
    private NokeServiceListener mNokeServiceListener = new NokeServiceListener() {
        @Override
        public void onNokeDiscovered(NokeDevice noke) {

        }

        @Override
        public void onNokeConnecting(NokeDevice noke) {

        }

        @Override
        public void onNokeConnected(NokeDevice noke) {

        }

        @Override
        public void onNokeSyncing(NokeDevice noke) {

        }

        @Override
        public void onNokeUnlocked(NokeDevice noke) {

        }
        @Override
        public void onNokeDisconnected(NokeDevice noke) {

        }
        @Override
        public void onBluetoothStatusChanged(int bluetoothStatus) {

        }
        @Override
        public void onError(NokeDevice noke, int error, String message) {
            // Log.e(TAG, "NOKE SERVICE ERROR " + error + ": " + message);
        }
    };

    @ReactMethod
    public void initNoke() {
      initiateNokeService();
    }
    // @ReactMethod
    // public void onUnlockReceived(String response, NokeDevice noke) {
    //    // //Log.d(TAG, "UNLOCK RECEIVED: "+ response);
    //    // try{
    //    //     JSONObject obj = new JSONObject(response);
    //    //     Boolean result = obj.getBoolean("result");
    //    //     if(result){
    //    //         JSONObject data = obj.getJSONObject("data");
    //    //         String commandString = data.getString("commands");
    //    //         currentNoke.sendCommands(commandString);
    //    //     }else{
    //    //     }
    //    //
    //    // }catch (JSONException e){
    //    //     //Log.e(TAG, e.toString());
    //    // }
    // }
}
