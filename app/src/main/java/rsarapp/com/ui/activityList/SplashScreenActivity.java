package rsarapp.com.ui.activityList;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import java.util.ArrayList;

import rsarapp.com.modelClass.request.DeviceDetailModel;
import rsarapp.com.rsarapp.R;
import rsarapp.com.utilities.AllStaticMethod;
import rsarapp.com.utilities.MySharedPreferences;
public class SplashScreenActivity extends AppCompatActivity {
    Boolean isInternetPresent = false;
    SharedPreferences preferences;
    Context context;
    String Pref_UserType,Pref_UserClassID,userStatus;
    String Device_Id,Mob_Id,Mob_Product,Mob_Brand,Mob_Manufacture,Mob_Model;
    private static final int TIME = 4000;// 4 seconds
    PackageInfo pinfo;
    public static String PACKAGE_NAME;
    String sVersionName;
    int sVersionCode;
    ArrayList deviceDetail=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalash_screen);
        context= SplashScreenActivity.this;
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
        isInternetPresent =AllStaticMethod.isOnline(context);
        saveDeviceDetailToSharedReference();

        PACKAGE_NAME = getApplicationContext().getPackageName();
        preferences = getSharedPreferences("RSAR_APP", Context.MODE_PRIVATE);
        Pref_UserType = preferences.getString("Rsar_UserType", "");
        Pref_UserClassID = preferences.getString("Rsar_ClassID", "");
        userStatus = preferences.getString("rsar_registered_user_status","");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                if (isInternetPresent)
                {
                    if (userStatus.matches("True"))
                    {
                        Intent intent;
                        intent = new Intent(context, DashBoardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                     else
                    {
                        startActivity(new Intent(context, LoginPageActivity.class));
                        finish();
                    }
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }else
                {
                    AllStaticMethod.showAlertDialog(context, "No Internet Connection",
                            "You don't have internet connection.", false);                }
            }

        }, TIME);
    }

    private void saveDeviceDetailToSharedReference() {
        Device_Id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        Mob_Id = android.os.Build.ID;
        Mob_Product= android.os.Build.PRODUCT;
        Mob_Brand= android.os.Build.BRAND;
        Mob_Manufacture= android.os.Build.MANUFACTURER;
        Mob_Model = android.os.Build.MODEL;

        DeviceDetailModel deviceDetailModel= new DeviceDetailModel(Device_Id,Mob_Id,Mob_Product,Mob_Brand,Mob_Manufacture,Mob_Model);
        MySharedPreferences.saveUserDeviceDetailToSharedPreferences(context,deviceDetailModel);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}