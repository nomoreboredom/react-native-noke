
package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.LifecycleEventListener;

import com.facebook.react.modules.core.DeviceEventManagerModule;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.Context;
import android.os.IBinder;
import android.content.ComponentName;
import android.util.Log;
import android.support.annotation.Nullable;
import android.os.Bundle;

import com.noke.nokemobilelibrary.NokeDevice;
import com.noke.nokemobilelibrary.NokeServiceListener;
import com.noke.nokemobilelibrary.NokeDeviceManagerService;

public class RNNokeModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    private final ReactApplicationContext reactContext;
    private NokeDeviceManagerService mNokeService = null;
    private NokeDevice currentNoke;

    public RNNokeModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext=reactContext;
    }

    private void initiateNokeService() {
        Intent nokeServiceIntent = new Intent(getReactApplicationContext(), NokeDeviceManagerService.class);
        reactContext.bindService(nokeServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
            Log.e("Info: ", "serviceConnected");
            //Store reference to service
            mNokeService = ((NokeDeviceManagerService.LocalBinder) rawBinder).getService();
            //Uncomment to allow devices that aren't in the device array
            //mNokeService.setAllowAllDevices(true);
            //Register callback listener
            mNokeService.registerNokeListener(mNokeServiceListener);
            Log.e("Info: ", "nokeListenerRegistered");
            //Add locks to device manager
            mNokeService.addNokeDevice(new NokeDevice("NOKE", "DA:AD:B3:5A:EE:9B"));
            mNokeService.addNokeDevice(new NokeDevice("NOKE", "F6:C8:C3:E0:A7:7D"));
            Log.e("Info: ", "nokeDevicesAdded");
            //Start bluetooth scanning
            mNokeService.startScanningForNokeDevices();
            if (!mNokeService.initialize()) {
                sendEvent(reactContext,"error","Unable to initialize Bluetooth");
                Log.e("E", "Unable to initialize Bluetooth");
            }
            sendEvent(reactContext,"initializationDone","NOKE initialization is done");
        }
        public void onServiceDisconnected(ComponentName classname) {
            mNokeService = null;
        }
    };
    private NokeServiceListener mNokeServiceListener = new NokeServiceListener() {
        @Override
        public void onNokeDiscovered(NokeDevice noke) {
            Log.e("Info: ", "onNokeDiscovered");
            sendEvent(reactContext,"discovered", noke);
        }
        @Override
        public void onNokeConnecting(NokeDevice noke) {
            Log.e("Info: ", "onNokeConnecting");
        }
        @Override
        public void onNokeConnected(NokeDevice noke) {
            Log.e("Info: ", "onNokeConnected");
            sendEvent(reactContext,"connected", noke);
        }
        @Override
        public void onNokeSyncing(NokeDevice noke) {
            Log.e("Info: ", "onNokeSyncing");
        }
        @Override
        public void onNokeUnlocked(NokeDevice noke) {
            Log.e("Info: ", "onNokeUnlocked");
        }
        @Override
        public void onNokeDisconnected(NokeDevice noke) {
            Log.e("Info: ", "onNokeDisconnected");
        }
        @Override
        public void onBluetoothStatusChanged(int bluetoothStatus) {
            Log.e("Info: ", "onBluetoothStatusChanged");
        }
        @Override
        public void onError(NokeDevice noke, int error, String message) {
            Log.e("Info: ", "NOKE SERVICE ERROR " + error + ": " + message);
        }
    };


    private void sendEvent(ReactContext reactContext, String eventName, @Nullable String param) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, param);
    }

    @Override
    public void onHostResume() {
        getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("activityResume", null);
    }

    @Override
    public void onHostPause() {
        getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("activityPause", null);
    }

    @Override
    public void onHostDestroy() {
    }

    @ReactMethod
    public void sendCommand(String response, NokeDevice noke) {
       //Log.d(TAG, "UNLOCK RECEIVED: "+ response);
       try{
           JSONObject obj = new JSONObject(response);
           Boolean result = obj.getBoolean("result");
           if(result){
               JSONObject data = obj.getJSONObject("data");
               String commandString = data.getString("commands");
               currentNoke.sendCommands(commandString);
           }else{

           }
       }catch (JSONException e){
           //Log.e(TAG, e.toString());
       }
    }

    @ReactMethod
    public void init() {
        initiateNokeService();
    }

    @ReactMethod
    public void connectToNoke(NokeDevice noke) {
        mNokeService.connectToNoke(noke);
    }

    @Override
    public String getName() {
        return "RNNoke";
    }

}
