package rsarapp.com.ui.activityList;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
import rsarapp.com.rsarapp.databinding.ActivityBookListBinding;
import rsarapp.com.ui.dialog.ProgressHUD;
import rsarapp.com.modelClass.SetterGetter_Sub_Chap;
import rsarapp.com.rsarapp.R;
import rsarapp.com.utilities.AllStaticMethod;
import rsarapp.com.utilities.AppConstant;

public class BookListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityBookListBinding binding;
    Boolean isInternetPresent = false;
    SetterGetter_Sub_Chap vinsgetter;
    ArrayList<SetterGetter_Sub_Chap> vinsquesarrayList;
    String Pref_Bg_Code, Pref_School_UI, Pref_Restric_Id,userName,Pref_Email;
    String Str_Status, Str_Msg, Details, Book_Id, Book_Name, Class_Id, Subject_Id, Str_Restrict_SD, Practice_Paper, Exam_Paper, Trm, Practice_Paper_Value, Exam_Paper_Value, Trm_Value, Rsar_Value, Diff_Play;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    JSONArray Book_Data;
    Context context;
    ProgressHUD progressHUD;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        binding=ActivityBookListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = BookListActivity.this;

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
        Pref_Restric_Id = preferences.getString("Rsar_Restric_ID", "");
        Class_Id = getIntent().getExtras().getString("Rsar_Class_Id");
        Subject_Id = getIntent().getExtras().getString("Rsar_Subject_Id");
        userName = preferences.getString("rsar_registered_user_name", "");
        Pref_Email = preferences.getString("Rsar_Pref_Email", "");

        isInternetPresent = AllStaticMethod.isOnline(context);
        if (isInternetPresent) {
            callProgressBar();
            GetBook_NameURl();;
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
        } else {
            headerUserName.setText(AllStaticMethod.capitalizeWord(userName));
            headerUserEmail.setText(Pref_Email);
        }

    }
    private void GetBook_NameURl() {

        RequestQueue queue = Volley.newRequestQueue(this);

        String urlmanual = AppConstant.url + "book.php?";
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
                                String Subject_Name = object.get("Subject_Name").toString();
                                Str_Restrict_SD = object.get("Restrict_SD").toString();
                                if (Str_Status.equalsIgnoreCase("True"))
                                {
                                    binding.dashboardToolbar.txtHeading.setText(Subject_Name);
                                    Book_Data = object.getJSONArray("BooksData");
                                    for (int j = 0; j < Book_Data.length(); j++)
                                    {
                                        JSONObject ObjectData;
                                        ObjectData = new JSONObject(Book_Data.getJSONObject(j).toString());
                                        String BookName = ObjectData.getString("Book_Name");
                                        String BookId = ObjectData.getString("Book_Id");

                                        vinsgetter = new SetterGetter_Sub_Chap();
                                        vinsgetter.setBook_Id(ObjectData.getString("Book_Id"));
                                        vinsgetter.setBookName(ObjectData.getString("Book_Name"));
                                        vinsquesarrayList.add(vinsgetter);
                                        GridViewAdapterChapterBook adapter = new GridViewAdapterChapterBook(BookListActivity.this, vinsquesarrayList);
                                        binding.gridView.setAdapter(adapter);
                                        binding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                try {
                                                    Details = Book_Data.get(position).toString();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                try {
                                                    JSONObject myObject = new JSONObject(Details);
                                                    Book_Id = myObject.getString("Book_Id");
                                                    Book_Name = myObject.getString("Book_Name");
                                                    Diff_Play = myObject.getString("Different_Play");
                                                    Practice_Paper = myObject.getString("Practice_Paper");
                                                    Exam_Paper = myObject.getString("Examination_Paper");
                                                    Trm = myObject.getString("Teacher_Resource_Manual");
                                                    Practice_Paper_Value = myObject.getString("Practice_Paper_Value");
                                                    Exam_Paper_Value = myObject.getString("Examination_Paper_Value");
                                                    Trm_Value = myObject.getString("Teacher_Resource_Manual_Value");
                                                    Rsar_Value = myObject.getString("RSAR_Value");
                                                    preferences = getApplicationContext().getSharedPreferences("RSAR_APP", Context.MODE_PRIVATE);
                                                    editor = preferences.edit();
                                                    editor.putString("Pref_Diff_Play", Diff_Play);
                                                    editor.commit();
                                                    editor.apply();
                                                    Intent intent = new Intent(BookListActivity.this, OptionActivity.class);
                                                    intent.putExtra("Rsar_Book_Id", Book_Id);
                                                    intent.putExtra("Rsar_Subject_Id", Subject_Id);
                                                    intent.putExtra("Rsar_Class_Id", Class_Id);
                                                    intent.putExtra("Rsar_Book_Name", Book_Name);
                                                    intent.putExtra("Rsar_Practice_Paper", Practice_Paper);
                                                    intent.putExtra("Rsar_Exam_Paper", Exam_Paper);
                                                    intent.putExtra("Rsar_TRM", Trm);
                                                    intent.putExtra("Rsar_Diff_Play", Diff_Play);
                                                    intent.putExtra("Rsar_Practice_Paper_Value", Practice_Paper_Value);
                                                    intent.putExtra("Rsar_Exam_Paper_Value", Exam_Paper_Value);
                                                    intent.putExtra("Rsar_TRM_Value", Trm_Value);
                                                    intent.putExtra("Rsar_RSAR_Value", Rsar_Value);
                                                    startActivity(intent);
                                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    final Dialog dialogss = new Dialog(BookListActivity.this);
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
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                        try {

                        } catch (Exception e) {
                        }
                    }
                }
        )
        {
            @Override
            protected HashMap<String, String> getParams() {

                HashMap<String, String> params = new HashMap<String, String>();
                params.put("School_UI", Pref_School_UI);
                params.put("cId", Class_Id);
                params.put("sId", Subject_Id);// Second one u can change
                params.put("action", "book");
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
