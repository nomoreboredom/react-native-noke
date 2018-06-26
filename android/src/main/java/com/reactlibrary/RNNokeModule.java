
package com.reactlibrary;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

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
  
  private ServiceConnection mServiceConnection = new ServiceConnection() {
     public void onServiceConnected(ComponentName className, IBinder rawBinder) {
         //Log.w(TAG, "ON SERVICE CONNECTED");
         //Store reference to service
         mNokeService = ((NokeDeviceManagerService.LocalBinder) rawBinder).getService();
         //Register callback listener
         mNokeService.registerNokeListener(mNokeServiceListener);
         //Add locks to device manager
         NokeDevice noke1 = new NokeDevice("TEST LOCK", "XX:XX:XX:XX:XX:XX");
         mNokeService.addNokeDevice(noke1);
         //mNokeService.setUploadUrl("https://coreapi-sandbox.appspot.com/upload/");
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

 @ReactMethod
 public void initNoke() {
   initiateNokeService();
 }

 private void initiateNokeService(){
   Intent nokeServiceIntent = new Intent(getReactApplicationContext(), NokeDeviceManagerService.class);
   reactContext.bindService(nokeServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
 }

 private NokeServiceListener mNokeServiceListener = new NokeServiceListener() {
      @Override
      public void onNokeDiscovered(NokeDevice noke) {
          //setStatusText("NOKE DISCOVERED: " + noke.getName());
          mNokeService.connectToNoke(noke);
      }

      @Override
      public void onNokeConnecting(NokeDevice noke) {
          //setStatusText("NOKE CONNECTING: " + noke.getName());
      }

      @Override
      public void onNokeConnected(NokeDevice noke) {
          //setStatusText("NOKE CONNECTED: " + noke.getName());
          currentNoke = noke;
          mNokeService.stopScanning();
      }

      @Override
      public void onNokeSyncing(NokeDevice noke) {
          //setStatusText("NOKE SYNCING: " + noke.getName());
      }

      @Override
      public void onNokeUnlocked(NokeDevice noke) {
          //setStatusText("NOKE UNLOCKED: " + noke.getName());
      }

      @Override
      public void onNokeDisconnected(NokeDevice noke) {
          //setStatusText("NOKE DISCONNECTED: " + noke.getName());
          currentNoke = null;
          mNokeService.uploadData();
          mNokeService.startScanningForNokeDevices();
      }

      @Override
      public void onBluetoothStatusChanged(int bluetoothStatus) {

      }
      @Override
      public void onError(NokeDevice noke, int error, String message) {
          //Log.e(TAG, "NOKE SERVICE ERROR " + error + ": " + message);
       }
  };

  @ReactMethod
  public void onUnlockReceived(String response, NokeDevice noke) {
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
}
