package rsarapp.com.utilities;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;

import rsarapp.com.modelClass.request.DeviceDetailModel;
import rsarapp.com.modelClass.response.UserDetailResponseModel;

public class MySharedPreferences {
    static ArrayList<String> loginDetail = new ArrayList<String>();
    private static  String RSAR_USER_DATA ="USER_APP_DATA";
    private static  String RSAR_USER_DEVICE_DATA ="USER_APP_DEVICE_DATA";
    public static void saveUserStatusToSharedPreferences(Context context,String Rsar_User_status) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(RSAR_USER_DATA,MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString("Rsar_User_status", Rsar_User_status);
//        myEdit.putInt("age", Integer.parseInt(age));
        myEdit.commit();
    }
    public static void saveUserDeviceDetailToSharedPreferences(Context context, DeviceDetailModel deviceDetailModel) {

        SharedPreferences sharedPreferences =  context.getSharedPreferences(RSAR_USER_DEVICE_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(deviceDetailModel);
        prefsEditor.putString("saveUserDevice",json);
        prefsEditor.apply();
    }
    public static void saveUserDetailToSharedPreferences(Context context, UserDetailResponseModel userDetailResponseModel) {

        SharedPreferences sharedPreferences =  context.getSharedPreferences(RSAR_USER_DATA, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(userDetailResponseModel);
        prefsEditor.putString("saveUserDetail",json);
        prefsEditor.apply();
    }
    public static String readUserStatusToSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(RSAR_USER_DATA,MODE_PRIVATE);
        String name=sharedPreferences.getString("Rsar_User_status",null);
        return name;
    }
    public static DeviceDetailModel readUserDeviceDetail(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(RSAR_USER_DEVICE_DATA, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("saveUserDevice", "");
        DeviceDetailModel deviceDetailModel = gson.fromJson(json, DeviceDetailModel.class);
        return deviceDetailModel;
    }
    public static UserDetailResponseModel  readUserDetailToSharedPreferences(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(RSAR_USER_DEVICE_DATA, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("saveUserDetail", "");
        UserDetailResponseModel userDetailResponseModel = gson.fromJson(json, UserDetailResponseModel.class);
        return userDetailResponseModel;
    }

}
