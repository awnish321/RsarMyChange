package rsarapp.com.ui.activityList;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import rsarapp.com.adapter.HelpImageAdapter;
import rsarapp.com.modelClass.SetterGetter_Sub_Chap;
import rsarapp.com.modelClass.request.DeviceDetailModel;
import rsarapp.com.modelClass.response.BannerModel;
import rsarapp.com.rsarapp.R;
import rsarapp.com.rsarapp.WebViewAbout;
import rsarapp.com.rsarapp.WebViewPrivacy;
import rsarapp.com.rsarapp.databinding.ActivityLoginPageBinding;
import rsarapp.com.ui.dialog.ProgressHUD;
import rsarapp.com.utilities.AllStaticMethod;
import rsarapp.com.utilities.AppConstant;
import rsarapp.com.utilities.MySharedPreferences;

public class LoginPageActivity extends AppCompatActivity {

    Context context;
    ActivityLoginPageBinding binding;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission
            .CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE};

    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    String Device_Id, Mob_Id, Mob_Product, Mob_Brand, Mob_Manufacture, Mob_Model;
    ProgressHUD dialog;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private String Str_Stu_Name, Str_Mobile, Str_Email_Id, Str_Msg, Str_UserType, Str_ClassID, Str_School_UI, Str_Status, Str_School_name, Str_Fd_School_name,Str_UserName;
    private String Str_Bg_Code, Str_Top_Bg_Code, Str_Button_Bg, Str_Act_Status, Str_UserStatus, Str_Restric_ID, Str_Sch_Name_Color, Str_Otp_value, Pre_Email_Id, Pre_Mob_No;
    Boolean isInternetPresent = false;
    ArrayList<SetterGetter_Sub_Chap> vinsquesarrayList;
    JSONArray DropClass_Data;
    SetterGetter_Sub_Chap vinsgetter;
    Spinner Spin;
    String Details, DClass_ID, RadioUserDetail;
    private ArrayList<String> DClass_Name;
    private RadioButton radioUserButton;
    PackageInfo pinfo;
    public static String PACKAGE_NAME;
    String sVersionName;
    int sVersionCode;
    Dialog forgetDetailDialog;
    ProgressHUD progressHUD;
    TextView text11;
    EditText ettext;
    JSONArray Banner_Data;
    private ArrayList<BannerModel.BannerDatum> bannerDatumArrayList;
    private BannerModel.BannerDatum bannerDatum;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    String Pref_Bg_Code;
    ImageView refresh;
    Dialog otpDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = LoginPageActivity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_register);
        setSupportActionBar(toolbar);
//        ImageView img = (ImageView) toolbar.findViewById(R.id.imgHelp);

        DeviceDetailModel deviceDetailModel = (DeviceDetailModel) MySharedPreferences.readUserDeviceDetail(context);
        Device_Id = deviceDetailModel.getDevice_Id();
        Mob_Id = deviceDetailModel.getMob_Id();
        Mob_Product = deviceDetailModel.getMob_Product();
        Mob_Brand = deviceDetailModel.getMob_Brand();
        Mob_Manufacture = deviceDetailModel.getMob_Manufacture();
        Mob_Model = deviceDetailModel.getMob_Model();

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        preferences = getSharedPreferences("RSAR_APP", Context.MODE_PRIVATE);
        Pref_Bg_Code = preferences.getString("Rsar_Bg_Code", "");
        Spin = findViewById(R.id.spinner);
        PACKAGE_NAME = getApplicationContext().getPackageName();
        try {
            pinfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            sVersionCode = pinfo.versionCode; // 1
            sVersionName = pinfo.versionName; // 1.0
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        DClass_Name = new ArrayList<String>();
        binding.txtPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, WebViewPrivacy.class);
                startActivity(i);
            }
        });
        binding.txtAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, WebViewAbout.class);
                startActivity(i);
            }
        });
        binding.txtForgets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callForgetDialog();

            }
        });
        binding.toolbarRegister.imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialogss = new Dialog(context);
                dialogss.setContentView(R.layout.popup_help);
                dialogss.setCancelable(true);

                // set the custom dialog components - text, image and button
                LinearLayout ln_outline = (LinearLayout) dialogss.findViewById(R.id.dia_ln_outline);

                mPager = (ViewPager) dialogss.findViewById(R.id.pager);
                mPager.setAdapter(new HelpImageAdapter(context, bannerDatumArrayList));
                CirclePageIndicator indicator = (CirclePageIndicator) dialogss.findViewById(R.id.indicator);
                indicator.setViewPager(mPager);
                final float density = getResources().getDisplayMetrics().density;
                indicator.setRadius(5 * density);
                NUM_PAGES = bannerDatumArrayList.size();
                // Auto start of viewpager
                final Handler handler = new Handler();
                final Runnable Update = new Runnable() {
                    public void run() {
                        if (currentPage == NUM_PAGES) {
                            currentPage = 0;
                        }
                        mPager.setCurrentItem(currentPage++, true);
                    }
                };
                indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int position) {
                        currentPage = position;

                    }

                    @Override
                    public void onPageScrolled(int pos, float arg1, int arg2) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int pos) {

                    }
                });
                dialogss.show();
            }
        });

        GetAndroidPermission();

        // FirebaseApp.initializeApp(context);
        isInternetPresent = AllStaticMethod.isOnline(context);

        if (isInternetPresent) {
            DropDownService();
            callHelpBannerApi();

            String message = "Please Wait....";
            dialog = new ProgressHUD(context, R.style.ProgressHUD);
            dialog.setTitle("");
            dialog.setContentView(R.layout.progress_hudd);
            if (message == null || message.length() == 0) {
                dialog.findViewById(R.id.message).setVisibility(View.GONE);
            } else {
                TextView txt = (TextView) dialog.findViewById(R.id.message);
                txt.setText(message);
            }
            dialog.setCancelable(false);

            dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.dimAmount = 0.2f;
            dialog.getWindow().setAttributes(lp);
            dialog.show();

        } else {
            AllStaticMethod.showAlertDialog(context, "No Internet Connection", "You don't have internet connection.", false);
        }

        binding.radioUser.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radioStudent:
                        // do operations specific to this selection
                        Spin.setEnabled(true);
                        binding.llClass.setVisibility(View.VISIBLE);
                        binding.txtClass.setVisibility(View.VISIBLE);
                        break;
                    case R.id.radioTeacher:
                        // do operations specific to this selection
                        Spin.setEnabled(false);
                        binding.llClass.setVisibility(View.GONE);
                        binding.txtClass.setVisibility(View.GONE);
                        break;
                }
            }
        });
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = binding.radioUser.getCheckedRadioButtonId();
                radioUserButton = (RadioButton) findViewById(selectedId);
                RadioUserDetail = radioUserButton.getText().toString();

                if (binding.edtName.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter the Name.", Toast.LENGTH_SHORT).show();
                }
                if (binding.edtEmailId.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter the Email Id.", Toast.LENGTH_SHORT).show();
                }
                if (binding.edtMobileNumber.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Enter the Mobile No.", Toast.LENGTH_SHORT).show();
                } else {
                    Str_Stu_Name = binding.edtName.getText().toString().trim();
                    Str_Email_Id = binding.edtEmailId.getText().toString().trim();
                    Str_Mobile = binding.edtMobileNumber.getText().toString().trim();
                    if (isInternetPresent) {
                        ActivationCodeURL();
                        String message = "Please Wait....";
                        dialog = new ProgressHUD(context, R.style.ProgressHUD);
                        dialog.setTitle("");
                        dialog.setContentView(R.layout.progress_hudd);
                        if (message == null || message.length() == 0) {
                            dialog.findViewById(R.id.message).setVisibility(View.GONE);
                        } else {
                            TextView txt = (TextView) dialog.findViewById(R.id.message);
                            txt.setText(message);
                        }
                        dialog.setCancelable(true);
                        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
                        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                        lp.dimAmount = 0.2f;
                        dialog.getWindow().setAttributes(lp);
                        dialog.show();
                    } else {
                        AllStaticMethod.showAlertDialog(context, "No Internet Connection",
                                "You don't have internet connection.", false);
                    }
                }
            }

        });

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void DropDownService() {

        // TODO Auto-generated method stub
        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = AppConstant.url + "classDropdown.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            vinsquesarrayList = new ArrayList<SetterGetter_Sub_Chap>();
                            JSONArray array;
                            array = new JSONArray(response);
                            JSONObject object = new JSONObject();
                            for (int i = 0; i < array.length(); i++) {
                                object = array.getJSONObject(i);
                                Str_Status = object.get("Status").toString();
                                Str_Msg = object.get("Message").toString();
                                System.out.println("messsssss" + "  " + Str_Msg);
                                if (Str_Status.equalsIgnoreCase("True")) {
                                    DropClass_Data = object.getJSONArray("ClassData");
                                    System.out.println("dinchiaaa" + "  " + object.get("ClassData").toString());
                                    for (int j = 0; j < DropClass_Data.length(); j++) {
                                        JSONObject ObjectData;
                                        ObjectData = new JSONObject(DropClass_Data.getJSONObject(j).toString());
                                        String ClassName = ObjectData.getString("Class_Name");
                                        String ClassId = ObjectData.getString("Class_Id");
                                        vinsgetter = new SetterGetter_Sub_Chap();
                                        DClass_Name.add(ObjectData.getString("Class_Name"));
                                        vinsgetter.setDropclassName(ObjectData.getString("Class_Name"));
                                        vinsgetter.setDropclass_Id(ObjectData.getString("Class_Id"));
                                        vinsquesarrayList.add(vinsgetter);
                                        Spin.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, DClass_Name));

                                        Spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                try {
                                                    Details = DropClass_Data.get(position).toString();
                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    JSONObject myObject = new JSONObject(Details);
                                                    DClass_ID = myObject.getString("Class_Id");

                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {
                                            }
                                        });
                                    }
                                }

                            }
                            if (dialog.isShowing())
                                dialog.dismiss();

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                        try {
                            Log.d("Error.Response", error.getMessage());

                        } catch (Exception e) {
                            // TODO: handle exception
                        }


                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("action", "classDropdown");// Second one u can changePref_Restric_Id

                return params;
            }
        };
        queue.add(postRequest);

    }

    private void ActivationCodeURL() {
        // TODO Auto-generated method stub
        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = AppConstant.url + "activation.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        try {
                            final JSONArray array;
                            array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object;
                                object = new JSONObject(array.getString(i).toString());
                                Str_Status = object.getString("Status");
                                Str_Msg = object.getString("Message");
                                System.out.println("abbb" + "    " + Str_Status);
                                if (Str_Status.equalsIgnoreCase("True")) {
                                    Str_UserType = object.getString("User_Type");
                                    Str_ClassID = object.getString("Class_Id");
                                    Str_School_UI = object.getString("School_UI");
                                    Str_School_name = object.getString("School_Name");
                                    Str_Fd_School_name = object.getString("School_Folder_Name");
                                    Str_Bg_Code = object.getString("Bg_Code");
                                    Str_Top_Bg_Code = object.getString("Top_BgCode");
                                    Str_Button_Bg = object.getString("Button_Bg_Color");
                                    Str_Act_Status = object.getString("Activation_Status");
                                    Str_Restric_ID = object.getString("Restrict_SD");
                                    Str_Sch_Name_Color = object.getString("School_Name_Color");
                                    Str_Otp_value = object.getString("OTP");
                                    Pre_Email_Id = object.getString("Email");
                                    Pre_Mob_No = object.getString("Mobile");
                                    Str_UserStatus = object.getString("rsar_registered_user_status");
                                    Str_UserName = object.getString("rsar_registered_user_name");

                                    preferences = getApplicationContext().getSharedPreferences("RSAR_APP", Context.MODE_PRIVATE);
                                    editor = preferences.edit();
                                    editor.putString("Rsar_UserType", Str_UserType);
                                    editor.putString("Rsar_ClassID", Str_ClassID);
                                    editor.putString("Rsar_School_UI", Str_School_UI);
                                    editor.putString("Rsar_School_Name", Str_School_name);
                                    editor.putString("Rsar_Fd_School_Name", Str_Fd_School_name);
                                    editor.putString("Rsar_Bg_Code", Str_Bg_Code);
                                    editor.putString("Rsar_Top_Bg_Code", Str_Top_Bg_Code);
                                    editor.putString("Rsar_Button_Bg", Str_Button_Bg);
                                    editor.putString("Rsar_Act_Status", Str_Act_Status);
                                    editor.putString("Rsar_Restric_ID", Str_Restric_ID);
                                    editor.putString("Rsar_Sch_Bgcol_Name", Str_Sch_Name_Color);
                                    editor.putString("Rsar_Otp_Value", Str_Otp_value);
                                    editor.putString("Rsar_Pref_Email", Pre_Email_Id);
                                    editor.putString("Rsar_Pref_Mobile", Pre_Mob_No);
                                    editor.putString("rsar_registered_user_status", Str_UserStatus);
                                    editor.putString("rsar_registered_user_name", Str_UserName);
                                    editor.commit();
                                    editor.apply();
                                    openOtpDialogBox(object.getString("OTP"));
                                } else {
                                    final Dialog dialoga = new Dialog(context);
                                    dialoga.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialoga.setContentView(R.layout.new_dialog);
                                    dialoga.setCancelable(true);
                                    TextView text = (TextView) dialoga.findViewById(R.id.error_msg);
                                    text.setText(Str_Msg);
                                    Button dialogButton = (Button) dialoga.findViewById(R.id.b_error_button);
                                    dialogButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialoga.dismiss();
                                        }
                                    });
                                    dialoga.show();
                                }
                            }
                            if (dialog.isShowing())
                                dialog.dismiss();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        // Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {

                HashMap<String, String> params = new HashMap<String, String>();

                params.put("classId", DClass_ID);
                params.put("userType", radioUserButton.getText().toString());
                params.put("mobile", Str_Mobile);
                params.put("email", Str_Email_Id);
                params.put("name", Str_Stu_Name);
                params.put("mid", Mob_Id);
                params.put("mproduct", Mob_Product);
                params.put("mbrand", Mob_Brand);
                params.put("mmanufacture", Mob_Manufacture);
                params.put("mmodel", Mob_Model);
                params.put("mdid", Device_Id);
                params.put("package", PACKAGE_NAME);
                params.put("version_name", sVersionName);
                params.put("version_code", Integer.toString(sVersionCode));
                params.put("Restrict_SD", "");
                params.put("action", "rsarapp");
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void GetAndroidPermission() {

        if (ActivityCompat.checkSelfPermission(context, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginPageActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginPageActivity.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginPageActivity.this, permissionsRequired[2])) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Camera and Storage permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(LoginPageActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Storage and Camera permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Storage and Camera", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(LoginPageActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
//            proceedAfterPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

//            if (allgranted) {
//                proceedAfterPermission();
//            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginPageActivity.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginPageActivity.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(LoginPageActivity.this, permissionsRequired[2])) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs Storage and Camera permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(LoginPageActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                // Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(context, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
//                proceedAfterPermission();
            }
        }
    }

    private void callForgetDialog() {
        forgetDetailDialog = new Dialog(context);
        forgetDetailDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        forgetDetailDialog.setContentView(R.layout.forget);
        forgetDetailDialog.setCancelable(false);

        LinearLayout ln_outline = (LinearLayout) forgetDetailDialog.findViewById(R.id.dia_ln_outline1);
        View view1 = (View) forgetDetailDialog.findViewById(R.id.dia_view1);
        TextView Error_text = (TextView) forgetDetailDialog.findViewById(R.id.dia_error_title1);
        text11 = forgetDetailDialog.findViewById(R.id.textwrittn1);
        ettext = (EditText) forgetDetailDialog.findViewById(R.id.dia_error_msg1);
        Button btn_yes = (Button) forgetDetailDialog.findViewById(R.id.dia_b_yes1);
        Button cencel = (Button) forgetDetailDialog.findViewById(R.id.cancels1);
        cencel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                forgetDetailDialog.dismiss();
            }
        });
        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ettext.getText().toString().trim().isEmpty()) {
                    Toast.makeText(context, "Please Enter EmailID", Toast.LENGTH_LONG).show();
                } else {

                    SendMsg(ettext.getText().toString().trim());
                    callProgressBar();
                }
            }

        });

        forgetDetailDialog.show();
    }

    private void callProgressBar() {
        String message = "Please Wait....";
        progressHUD = new ProgressHUD(context, R.style.ProgressHUD);
        progressHUD.setTitle("");
        progressHUD.setContentView(R.layout.progress_hudd);
        if (message == null || message.length() == 0) {
            progressHUD.findViewById(R.id.message).setVisibility(View.GONE);
        } else {
            TextView txt = (TextView) progressHUD.findViewById(R.id.message);
            txt.setText(message);
        }
        progressHUD.setCancelable(true);
        progressHUD.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = progressHUD.getWindow().getAttributes();
        lp.dimAmount = 0.2f;
        progressHUD.getWindow().setAttributes(lp);
        progressHUD.show();
    }

    private void SendMsg(String userEmail) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = AppConstant.url + "forgotDetails.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (progressHUD.isShowing()) {
                            progressHUD.dismiss();
                        }
                        try {
                            vinsquesarrayList = new ArrayList<SetterGetter_Sub_Chap>();
                            JSONArray array;
                            array = new JSONArray(response);
                            JSONObject object = new JSONObject();
                            for (int i = 0; i < array.length(); i++) {
                                object = array.getJSONObject(i);
                                Str_Status = object.get("Status").toString();
                                Str_Msg = object.get("Message").toString();
                                if (Str_Status.equalsIgnoreCase("True")) {
                                    preferences = getSharedPreferences("RSAR_APP", Context.MODE_PRIVATE);
                                    editor = preferences.edit();
                                    editor.putString("Rsar_Otp_Value", "False");
                                    editor.commit();
                                    text11.setText("Please Check your Email");
                                    Toast.makeText(context, "Please Check your Email", Toast.LENGTH_LONG).show();
                                } else {
                                    text11.setText("Not registered with us");
                                    Toast.makeText(context, "Not registered with us", Toast.LENGTH_LONG).show();
                                }
                                if (forgetDetailDialog.isShowing()) {
                                    forgetDetailDialog.dismiss();
                                }
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            Toast.makeText(context, " Server not responding Please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        try {
                            Toast.makeText(context, " Server not responding Please try again", Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("School_UI", "Nzk=");
                params.put("email", userEmail);
                params.put("action", "forgot");

                return params;
            }
        };
        queue.add(postRequest);
    }

    private void callHelpBannerApi() {
        // TODO Auto-generated method stub
        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = AppConstant.url + "banners.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            bannerDatumArrayList = new ArrayList<>();
                            bannerDatumArrayList.clear();
                            JSONArray array;
                            array = new JSONArray(response);
                            JSONObject object = new JSONObject();
                            for (int i = 0; i < array.length(); i++) {
                                object = array.getJSONObject(i);

                                Str_Status = object.get("Status").toString();

                                Str_Msg = object.get("Message").toString();

                                if (Str_Status.equalsIgnoreCase("True")) {
                                    Banner_Data = object.getJSONArray("Banner_Data");

                                    for (int j = 0; j < Banner_Data.length(); j++) {
                                        JSONObject ObjectData;
                                        ObjectData = new JSONObject(Banner_Data.getJSONObject(j).toString());

                                        String BannerId = ObjectData.getString("Banner_Id");
                                        String BannerURL = ObjectData.getString("Banner_URL");

                                        bannerDatum = new BannerModel.BannerDatum();
                                        bannerDatum.setBannerId(BannerId);
                                        bannerDatum.setBannerURL(BannerURL);
                                        bannerDatumArrayList.add(bannerDatum);
                                    }

                                } else {
                                    final Dialog dialogss = new Dialog(context);
                                    dialogss.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialogss.setContentView(R.layout.alert_dialog);
                                    dialogss.setCancelable(true);

                                    // set the custom dialog components - text, image and button
                                    LinearLayout ln_outline = (LinearLayout) dialogss.findViewById(R.id.dia_ln_outline);
                                    View view = (View) dialogss.findViewById(R.id.dia_view);
                                    TextView Error_text = (TextView) dialogss.findViewById(R.id.dia_error_title);
                                    TextView text = (TextView) dialogss.findViewById(R.id.dia_error_msg);
                                    text.setText(Str_Msg);
                                    Button btn_yes = (Button) dialogss.findViewById(R.id.dia_b_yes);
                                    btn_yes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogss.dismiss();
                                        }
                                    });
                                    dialogss.show();
                                }
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        // Log.d("Error.Response", error.getMessage());
                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("action", "banner");
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void openOtpDialogBox(String otpValue) {

        if (!otpValue.isEmpty()) {
            otpDialog = new Dialog(context);
            otpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            otpDialog.setContentView(R.layout.otp_dialog);
            otpDialog.setCancelable(false);

            LinearLayout ln_outline = (LinearLayout) otpDialog.findViewById(R.id.dia_ln_outline);
            View view = (View) otpDialog.findViewById(R.id.dia_view);
            TextView Error_text = (TextView) otpDialog.findViewById(R.id.dia_error_title);
            ettext = (EditText) otpDialog.findViewById(R.id.dia_error_msg);

            Button btn_yes = (Button) otpDialog.findViewById(R.id.dia_b_yes);
            Button cancel = (Button) otpDialog.findViewById(R.id.cancels);
            refresh = otpDialog.findViewById(R.id.refresh);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AllStaticMethod.logout(context);
                    otpDialog.cancel();
                }
            });
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OtpRefresh();
                    Toast.makeText(context, "Refreshing....Wait", Toast.LENGTH_SHORT).show();
                }
            });
            btn_yes.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               if (ettext.getText().toString().trim().isEmpty()) {
                                                   Toast.makeText(context, "Please Enter OTP .", Toast.LENGTH_LONG).show();
                                               } else {
                                                   OtpVerify(ettext.getText().toString().trim());
                                                   callProgressBar();
                                               }
                                           }
                                       }
            );
            otpDialog.show();
        }
    }

    private void OtpRefresh() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = AppConstant.url + "otpResend.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            vinsquesarrayList = new ArrayList<SetterGetter_Sub_Chap>();
                            JSONArray array;
                            array = new JSONArray(response);
                            JSONObject object = new JSONObject();
                            for (int i = 0; i < array.length(); i++) {
                                object = array.getJSONObject(i);
                                Str_Status = object.get("Status").toString();
                                Str_Msg = object.get("Message").toString();
                                if (Str_Status.equalsIgnoreCase("True")) {
                                    Toast.makeText(context, "Please check your Email and Mobile", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, Str_Msg, Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        try {
                            Log.d("Error.Response", error.getMessage());

                        } catch (Exception e) {
                            // TODO: handle exception
                        }
                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("School_UI", Str_School_UI);
                params.put("email", Str_Email_Id);
                params.put("mobile", Str_Mobile);
                params.put("action", "otp");
                return params;
            }
        };
        queue.add(postRequest);
    }

    private void OtpVerify(String verifyOtp) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = AppConstant.url + "checkOtp.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            vinsquesarrayList = new ArrayList<SetterGetter_Sub_Chap>();
                            JSONArray array;
                            array = new JSONArray(response);
                            JSONObject object = new JSONObject();
                            for (int i = 0; i < array.length(); i++) {
                                object = array.getJSONObject(i);
                                Str_Status = object.get("Status").toString();
                                Str_Msg = object.get("Message").toString();
                                if (Str_Status.equalsIgnoreCase("True")) {
                                    preferences = getApplicationContext().getSharedPreferences("RSAR_APP", Context.MODE_PRIVATE);
                                    editor = preferences.edit();
                                    editor.putString("rsar_registered_user_status", "True");
                                    editor.commit();
                                    editor.apply();

                                    if (Str_UserType.equalsIgnoreCase("Teacher")) {
                                        startActivity(new Intent(context, DashBoardActivity.class));
                                        finish();
                                    } else {
                                        Intent intent = new Intent(context, DashBoardActivity.class);
                                        intent.putExtra("Rsar_Class_Id", Str_ClassID);
                                        startActivity(intent);
                                        finish();
                                    }
                                    otpDialog.dismiss();
                                    overridePendingTransition(R.anim.fade_inn, R.anim.fade_outt);
                                } else {
                                    Toast.makeText(context, Str_Msg, Toast.LENGTH_LONG).show();
                                }
                                if (progressHUD.isShowing()) {
                                    progressHUD.dismiss();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("School_UI", Str_School_UI);
                params.put("otp", verifyOtp);// Second one u can changePref_Restric_Id
                params.put("mobile", Str_Mobile);
                params.put("email", Str_Email_Id);
                return params;
            }
        };
        queue.add(postRequest);
    }
}