package rsarapp.com.ui.activityList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import rsarapp.com.adapter.GridViewAdapterSub;
import rsarapp.com.adapter.HelpImageAdapter;
import rsarapp.com.modelClass.SetterGetter_Sub_Chap;
import rsarapp.com.modelClass.response.BannerModel;
import rsarapp.com.rsarapp.R;
import rsarapp.com.rsarapp.databinding.ActivitySubjectBinding;
import rsarapp.com.ui.dialog.ProgressHUD;
import rsarapp.com.utilities.AllStaticMethod;
import rsarapp.com.utilities.AppConstant;

public class SubjectActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivitySubjectBinding binding;
    Boolean isInternetPresent = false;
    ArrayList<SetterGetter_Sub_Chap> vinsquesarrayList;
    String Pref_Bg_Code, Pref_School_UI, Pref_Restric_Id;
    String Str_Status, Str_Msg, Details, Sub_Id, Sub_Name, Str_Restrict_SD, userClassId,userName,Pref_Email;
    JSONArray Sub_Data;
    Context context;
    SetterGetter_Sub_Chap vinsgetter;
    ProgressHUD progressHUD;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedPreferences preferences;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<BannerModel.BannerDatum> bannerDatumArrayList;
    private BannerModel.BannerDatum bannerDatum;
    JSONArray Banner_Data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding = ActivitySubjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = SubjectActivity.this;
        userClassId = getIntent().getExtras().getString("Rsar_Class_Id");
        preferences = getSharedPreferences("RSAR_APP", Context.MODE_PRIVATE);
        Pref_School_UI = preferences.getString("Rsar_School_UI", "");
        Pref_Restric_Id = preferences.getString("Rsar_Restric_ID", "");
        userName = preferences.getString("rsar_registered_user_name", "");
        Pref_Email = preferences.getString("Rsar_Pref_Email", "");

        callHelpBannerApi();
        toolbar = findViewById(R.id.dashboardToolbar);

        navigationView = findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        isInternetPresent = AllStaticMethod.isOnline(context);
        if (isInternetPresent) {
            callSubjectURl();
            callProgressBar();
        } else {
            AllStaticMethod.showAlertDialog(context, "No Internet Connection", "You don't have internet connection.", false);
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

//        binding.dashboardToolbar.imgShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("text/plain");
//                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "RSAR APP");
//                shareIntent.putExtra(Intent.EXTRA_TEXT, "Download this Application now:- https://play.google.com/store/apps/details?id=rsarapp.com.rsarapp");
//                startActivity(Intent.createChooser(shareIntent, "share via"));
//            }
//        });

        binding.dashboardToolbar.imgHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        View headerLayout = navigationView.getHeaderView(0);
        TextView headerUserName = (TextView) headerLayout.findViewById(R.id.txtUserName);
        TextView headerUserEmail = (TextView) headerLayout.findViewById(R.id.txtUserEmail);

        if (userName.isEmpty()) {
            headerUserEmail.setText(Pref_Email);
        } else {
            headerUserName.setText(AllStaticMethod.capitalizeWord(userName));
            headerUserEmail.setText(Pref_Email);
        }
    }

    private void callSubjectURl() {

        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = AppConstant.url + "subject.php?";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
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
                                                    intent.putExtra("Rsar_Class_Id", userClassId);
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
                startActivity(new Intent(context, MyProfileActivity.class));
                overridePendingTransition(R.anim.fade_inn, R.anim.fade_outt);
                break;

            case R.id.navShare:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "RSAR APP");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Download this Application now:- https://play.google.com/store/apps/details?id=rsarapp.com.rsarapp");
                startActivity(Intent.createChooser(shareIntent, "share via"));
                break;
            case R.id.navContact:
                startActivity(new Intent(context,ContactUsActivity.class));
                overridePendingTransition(R.anim.fade_inn, R.anim.fade_outt);
                break;
            case R.id.navLogout:
                AllStaticMethod.logout(context);
                Intent intent = new Intent(context, LoginPageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_inn, R.anim.fade_outt);
                finishAffinity();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
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
                                    final Dialog dialogss = new Dialog(SubjectActivity.this);
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

}
