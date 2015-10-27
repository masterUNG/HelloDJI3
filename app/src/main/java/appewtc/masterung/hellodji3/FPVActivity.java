package appewtc.masterung.hellodji3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import dji.sdk.api.DJIDrone;
import dji.sdk.api.DJIDroneTypeDef;
import dji.sdk.api.DJIError;
import dji.sdk.interfaces.DJIGerneralListener;
import dji.sdk.interfaces.DJIReceivedVideoDataCallBack;
import dji.sdk.widget.DjiGLSurfaceView;

public class FPVActivity extends AppCompatActivity {

    //Explicit
    private static final String TAG = "MyApp";
    private int DroneCode;
    private DJIReceivedVideoDataCallBack mReceivedVideoDataCallBack = null;
    private DjiGLSurfaceView mDjiGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fpv);

        //Create Thread
        new Thread(){
            public void run(){
                try{

                    DJIDrone.checkPermission(getApplicationContext(), new DJIGerneralListener() {
                        @Override
                        public void onGetPermissionResult(int result) {
                            if(result == 0) {
                                // show success
                                Log.e(TAG, "onGetPermissionResult =" + result);
                                Log.e(TAG,
                                        "onGetPermissionResultDescription="+ DJIError.getCheckPermissionErrorDescription(result));
                            } else {
                                // show errors
                                Log.e(TAG, "onGetPermissionResult ="+result);
                                Log.e(TAG,
                                        "onGetPermissionResultDescription="+DJIError.getCheckPermissionErrorDescription(result));
                            }
                        }
                    });


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }.start();


        DroneCode = 2;
        onInitSDK(DroneCode);


        DJIDrone.connectToDrone(); // Connect to the drone

        mDjiGLSurfaceView = (DjiGLSurfaceView)findViewById(R.id.DjiSurfaceView_02);
        mDjiGLSurfaceView.start();

        mReceivedVideoDataCallBack = new DJIReceivedVideoDataCallBack() {
            @Override
            public void onResult(byte[] videoBuffer, int size) {

                mDjiGLSurfaceView.setDataToDecoder(videoBuffer, size);

            }

        };
          DJIDrone.getDjiCamera().setReceivedVideoDataCallBack(mReceivedVideoDataCallBack);

    } // onCreate


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (DJIDrone.getDjiCamera() != null) {
            DJIDrone.getDjiCamera().setReceivedVideoDataCallBack(null);
        }
        mDjiGLSurfaceView.destroy();



    }

    private void onInitSDK(int type) {


        switch(type){
            case 0: {
                DJIDrone.initWithType(this.getApplicationContext(), DJIDroneTypeDef.DJIDroneType.DJIDrone_Vision);
                // The SDK initiation for Phantom 2 Vision or Vision Plus
                break;
            }
            case 1: {
                DJIDrone.initWithType(this.getApplicationContext(), DJIDroneTypeDef.DJIDroneType.DJIDrone_Inspire1);
                // The SDK initiation for Inspire 1 or Phantom 3 Professional.
                break;
            }
            case 2: {
                DJIDrone.initWithType(this.getApplicationContext(), DJIDroneTypeDef.DJIDroneType.DJIDrone_Phantom3_Advanced);
                // The SDK initiation for Phantom 3 Advanced
                break;
            }
            case 3: {
                DJIDrone.initWithType(this.getApplicationContext(), DJIDroneTypeDef.DJIDroneType.DJIDrone_M100);
                // The SDK initiation for Matrice 100.
                break;
            }
            default:{
                break;
            }
        }



    }


}   // Main Class
