package rsarapp.com.ui.activityList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import rsarapp.com.adapter.GridViewAdapterChapterBook;
import rsarapp.com.adapter.GridViewAdapterSub;
import rsarapp.com.modelClass.SetterGetter_Sub_Chap;
import rsarapp.com.rsarapp.R;
import rsarapp.com.rsarapp.databinding.ActivityDashBoardBinding;
import rsarapp.com.ui.dialog.ProgressHUD;
import rsarapp.com.utilities.AllStaticMethod;
import rsarapp.com.utilities.AppConstant;

public class DashBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Context context;
    ActivityDashBoardBinding binding;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    JSONArray Sub_Data;
    SetterGetter_Sub_Chap vinsgetter;
    ArrayList<SetterGetter_Sub_Chap> vinsquesarrayList;
    String Pref_Bg_Code, Pref_Top_Bg_Code, Pref_Button_Bg, Pref_School_UI, Pref_School_name, Pref_Restric_Id, Pref_UserType, Pref_Email, Pref_Mob, userStatus;
    String Str_Status, Str_Msg, Details, Sub_Id, Sub_Name, Class_Id, Db_Class_Id, Str_Restrict_SD, Str_Otp_Value,userName;
    String Str_Act_Status, sVersionName;
    int sVersionCode;
    ProgressHUD progressHUD;
    PackageInfo pinfo;
    public static String PACKAGE_NAME;
    Boolean isInternetPresent = false;
    JSONArray Class_Data;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = DashBoardActivity.this;
        toolbar = findViewById(R.id.dashboardToolbar);

        navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        preferences = getSharedPreferences("RSAR_APP", Context.MODE_PRIVATE);
        Pref_School_UI = preferences.getString("Rsar_School_UI", "NTY=");
        Pref_School_name = preferences.getString("Rsar_School_Name", "");
        Pref_Bg_Code = preferences.getString("Rsar_Bg_Code", "");
        Pref_Top_Bg_Code = preferences.getString("Rsar_Top_Bg_Code", "");
        Pref_Button_Bg = preferences.getString("Rsar_Button_Bg", "");
        Pref_Restric_Id = preferences.getString("Rsar_Restric_ID", "");
        Str_Otp_Value = preferences.getString("Rsar_Otp_Value", "");
        Pref_Email = preferences.getString("Rsar_Pref_Email", "");
        Pref_Mob = preferences.getString("Rsar_Pref_Mobile", "");
        Db_Class_Id = preferences.getString("Rsar_ClassID", "");
        Pref_UserType = preferences.getString("Rsar_UserType", "");
        Str_Act_Status = preferences.getString("Rsar_Act_Status", "");
        userStatus = preferences.getString("rsar_registered_user_status", "");
        userName = preferences.getString("rsar_registered_user_name", "");

        PACKAGE_NAME = getApplicationContext().getPackageName();
        try {
            pinfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            sVersionCode = pinfo.versionCode; // 1
            sVersionName = pinfo.versionName; // 1.0
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        isInternetPresent = AllStaticMethod.isOnline(context);

        if (isInternetPresent) {
            if (userStatus.matches("True")) {
                if (Pref_UserType.equalsIgnoreCase("Teacher")) {
                    binding.dashboardToolbar.txtHeading.setText("All Class");
                    callClassUrl();
                    callProgressBar();
                } else {
                    binding.txtClassName.setVisibility(View.VISIBLE);
                    callSubjectURl(Db_Class_Id);
                    callProgressBar();
                }
            } else {
                startActivity(new Intent(context, LoginPageActivity.class));
                finish();
            }
        } else {
            AllStaticMethod.showAlertDialog(context, "No Internet Connection",
                    "You don't have internet connection.", false);
        }

//        binding.dashboardToolbar.imgLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AllStaticMethod.logout(context);
//                Intent intent = new Intent(context, LoginPageActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                overridePendingTransition(R.anim.fade_inn, R.anim.fade_outt);
//                finishAffinity();
//            }
//        });

        binding.dashboardToolbar.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "RSAR APP");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Download this Application now:- https://play.google.com/store/apps/details?id=rsarapp.com.rsarapp");
                startActivity(Intent.createChooser(shareIntent, "share via"));
            }
        });

        View headerLayout = navigationView.getHeaderView(0);
        TextView headerUserName = (TextView) headerLayout.findViewById(R.id.txtUserName);
        TextView headerUserEmail = (TextView) headerLayout.findViewById(R.id.txtUserEmail);
        if (userName.isEmpty()) {
            headerUserEmail.setText(Pref_Email);
        }
        else {
            headerUserName.setText(AllStaticMethod.capitalizeWord(userName));
            headerUserEmail.setText(Pref_Email);
        }

    }
    private void callClassUrl() {

        RequestQueue queue = Volley.newRequestQueue(context);
        String urlmanual = AppConstant.url + "class.php?";
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
                                    Class_Data = object.getJSONArray("ClassData");

                                    for (int j = 0; j < Class_Data.length(); j++) {
                                        JSONObject ObjectData;
                                        ObjectData = new JSONObject(Class_Data.getJSONObject(j).toString());

                                        String ClassName = ObjectData.getString("Class_Name");
                                        String ClassId = ObjectData.getString("Class_Id");
                                        vinsgetter = new SetterGetter_Sub_Chap();
                                        vinsgetter.setClassName(ObjectData.getString("Class_Name"));
                                        vinsgetter.setClass_Id(ObjectData.getString("Class_Id"));
                                        vinsquesarrayList.add(vinsgetter);
                                        GridViewAdapterChapterBook adapter = new GridViewAdapterChapterBook(context, vinsquesarrayList);
                                        binding.gridView.setAdapter(adapter);
                                        binding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                // TODO Auto-generated method stub

                                                try {
                                                    Details = Class_Data.get(position).toString();
                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    JSONObject myObject = new JSONObject(Details);
                                                    Class_Id = myObject.getString("Class_Id");
                                                    Intent intent = new Intent(context, SubjectActivity.class);
                                                    intent.putExtra("Rsar_Class_Id", Class_Id);
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    final Dialog dialogss = new Dialog(context);
                                    dialogss.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialogss.setContentView(R.layout.alert_dialog);
                                    dialogss.setCancelable(true);

                                    LinearLayout ln_outline = (LinearLayout) dialogss.findViewById(R.id.dia_ln_outline);
                                    View view = (View) dialogss.findViewById(R.id.dia_view);
                                    TextView Error_text = (TextView) dialogss.findViewById(R.id.dia_error_title);
                                    TextView text = (TextView) dialogss.findViewById(R.id.dia_error_msg);
                                    text.setText(Str_Msg);

                                    Button btn_yes = (Button) dialogss.findViewById(R.id.dia_b_yes);

                                    ln_outline.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                                    view.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                                    Error_text.setTextColor(Color.parseColor(Pref_Bg_Code));
                                    btn_yes.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                                    btn_yes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogss.dismiss();
                                        }
                                    });
                                    dialogss.show();
                                }
                                if (progressHUD.isShowing())
                                    progressHUD.dismiss();
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
                params.put("School_UI", Pref_School_UI);
                params.put("action", "class");
                params.put("Restrict_SD", Pref_Restric_Id);
                return params;
            }
        };
        queue.add(postRequest);
    }
    private void callSubjectURl(String userClassId) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = AppConstant.url + "subject.php?";
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
                                Str_Restrict_SD = object.get("Restrict_SD").toString();
                                String Class_Name = object.get("Class_Title").toString();
                                preferences = getApplicationContext().getSharedPreferences("RSAR_APP", Context.MODE_PRIVATE);
                                editor = preferences.edit();
                                editor.putString("rsar_class_name", Class_Name);
                                editor.commit();
                                editor.apply();
                                if (Str_Status.equalsIgnoreCase("True")) {
                                    Sub_Data = object.getJSONArray("SubjectData");
                                    binding.dashboardToolbar.txtHeading.setText(Class_Name);
                                    for (int j = 0; j < Sub_Data.length(); j++) {
                                        JSONObject ObjectData;
                                        ObjectData = new JSONObject(Sub_Data.getJSONObject(j).toString());
                                        String SubName = ObjectData.getString("Subject_Name");
                                        String SubId = ObjectData.getString("Subject_Id");
                                        vinsgetter = new SetterGetter_Sub_Chap();
                                        vinsgetter.setSubject_Id(ObjectData.getString("Subject_Id"));
                                        vinsgetter.setSubjectName(ObjectData.getString("Subject_Name"));
                                        vinsquesarrayList.add(vinsgetter);
                                        GridViewAdapterSub adapter = new GridViewAdapterSub(context, vinsquesarrayList);
                                        binding.gridView.setAdapter(adapter);
                                        binding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                // TODO Auto-generated method stub
                                                try {
                                                    Details = Sub_Data.get(position).toString();
                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    JSONObject myObject = new JSONObject(Details);
                                                    Sub_Id = myObject.getString("Subject_Id");
                                                    Sub_Name = myObject.getString("Subject_Name");
                                                    Intent intent = new Intent(context, BookListActivity.class);
                                                    intent.putExtra("Rsar_Subject_Id", Sub_Id);
                                                    intent.putExtra("Rsar_Class_Id", Db_Class_Id);
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
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
                                    ln_outline.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                                    view.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                                    Error_text.setTextColor(Color.parseColor(Pref_Bg_Code));
                                    btn_yes.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                                    btn_yes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogss.dismiss();
                                        }
                                    });
                                    dialogss.show();
                                }
                                if (progressHUD.isShowing())
                                    progressHUD.dismiss();
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
                params.put("School_UI", Pref_School_UI);
                params.put("cId", userClassId);// Second one u can change
                params.put("action", "subject");
                params.put("Restrict_SD", Pref_Restric_Id);
                return params;
            }
        };
        queue.add(postRequest);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.navProfile:

                if (userStatus != null) {
                    startActivity(new Intent(context, MyProfileActivity.class));
                    overridePendingTransition(R.anim.fade_inn, R.anim.fade_outt);
                }
                break;

            case R.id.navShare:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "RSAR APP");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Download this Application now:- https://play.google.com/store/apps/details?id=rsarapp.com.rsarapp");
                startActivity(Intent.createChooser(shareIntent, "share via"));
                break;

            case R.id.navLogout:

                if (userStatus != null) {

                    AllStaticMethod.logout(context);
                    Intent intent = new Intent(context, LoginPageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_inn, R.anim.fade_outt);
                    finishAffinity();
                }
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
    private void callProgressBar(){
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

}
