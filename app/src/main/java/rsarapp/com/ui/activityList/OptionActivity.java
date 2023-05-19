package rsarapp.com.ui.activityList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import rsarapp.com.rsarapp.ShowWebView;
import rsarapp.com.modelClass.response.BannerModel;
import rsarapp.com.rsarapp.R;
import rsarapp.com.utilities.AppConstant;

public class OptionActivity extends Activity {

    String Pref_Bg_Code, Pref_Top_Bg_Code, Pref_Button_Bg, Pref_School_UI, Pref_School_name, Pref_Restric_Id;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    LinearLayout ln_bg, ln_rsarapp, ln_pp, ln_ep, ln_trm, ln_help;
    String Book_Id, Book_Name, Class_Id, Subject_Id, Practice_Paper, Exam_Paper, TRM, Practice_Paper_Value, Exam_Paper_Value, TRM_Value, Rsar_Value, Op_Diff_Play, Str_Status, Str_Msg;

    //-----------Slider--------------------------
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
//    private ArrayList<ImageModel> imageModelArrayList;
    private ArrayList<BannerModel.BannerDatum> bannerDatumArrayList;
    private BannerModel.BannerDatum bannerDatum;
//    private int[] myImageList = new int[]{R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5};
    JSONArray Banner_Data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_option);

        preferences = getSharedPreferences("RSAR_APP", Context.MODE_PRIVATE);
        Pref_School_UI = preferences.getString("Rsar_School_UI", "");
        Pref_School_name = preferences.getString("Rsar_School_Name", "");
        Pref_Bg_Code = preferences.getString("Rsar_Bg_Code", "");
        Pref_Top_Bg_Code = preferences.getString("Rsar_Top_Bg_Code", "");
        Pref_Button_Bg = preferences.getString("Rsar_Button_Bg", "");
        Pref_Restric_Id = preferences.getString("Rsar_Restric_ID", "");

        Class_Id = getIntent().getExtras().getString("Rsar_Class_Id");
        Subject_Id = getIntent().getExtras().getString("Rsar_Subject_Id");
        Book_Id = getIntent().getExtras().getString("Rsar_Book_Id");
        Book_Name = getIntent().getExtras().getString("Rsar_Book_Name");
        Op_Diff_Play = getIntent().getExtras().getString("Rsar_Diff_Play");

        Practice_Paper = getIntent().getExtras().getString("Rsar_Practice_Paper");
        Exam_Paper = getIntent().getExtras().getString("Rsar_Exam_Paper");
        TRM = getIntent().getExtras().getString("Rsar_TRM");
        Practice_Paper_Value = getIntent().getExtras().getString("Rsar_Practice_Paper_Value");
        Exam_Paper_Value = getIntent().getExtras().getString("Rsar_Exam_Paper_Value");
        TRM_Value = getIntent().getExtras().getString("Rsar_TRM_Value");
        Rsar_Value = getIntent().getExtras().getString("Rsar_RSAR_Value");

//        imageModelArrayList = new ArrayList<>();
//        imageModelArrayList = populateList();
        callHelpBannerApi();
        ButtonsDetails();
    }

//    private ArrayList<ImageModel> populateList() {
//
//        ArrayList<ImageModel> list = new ArrayList<>();
//
//        for (int i = 0; i < 5; i++) {
//            ImageModel imageModel = new ImageModel();
//            imageModel.setImage_drawable(myImageList[i]);
//            list.add(imageModel);
//        }
//
//        return list;
//    }

    private void ButtonsDetails() {

      /*  ln_bg = (LinearLayout)findViewById(R.id.Lnr_Bg);
        ln_bg.setBackgroundColor(Color.parseColor(Pref_Bg_Code));*/
        Button btn_Rsarapp = (Button) findViewById(R.id.Btn_Rsarapp);
        Button btn_PP = (Button) findViewById(R.id.Btn_PP);
        Button btn_Ep = (Button) findViewById(R.id.Btn_Ep);
        Button btn_TRM = (Button) findViewById(R.id.Btn_TRM);
        LinearLayout LinearLayoutHelp = (LinearLayout) findViewById(R.id.llHelp);
        ln_rsarapp = (LinearLayout) findViewById(R.id.Lnr_Rsarapp);
        ln_pp = (LinearLayout) findViewById(R.id.Lnr_PP);
        ln_ep = (LinearLayout) findViewById(R.id.Lnr_EP);
        ln_trm = (LinearLayout) findViewById(R.id.Lnr_TRM);
        ln_help = (LinearLayout) findViewById(R.id.Lnr_Help);

        System.out.println("Value Check" + " " + Rsar_Value + " " + Practice_Paper_Value + " " + Exam_Paper_Value + " " + TRM_Value);

        if (Rsar_Value.equalsIgnoreCase("True")) {
            ln_rsarapp.setVisibility(View.VISIBLE);
        } else {
            ln_rsarapp.setVisibility(View.GONE);
        }

        if (Practice_Paper_Value.equalsIgnoreCase("True")) {
            ln_pp.setVisibility(View.VISIBLE);
        } else {
            ln_pp.setVisibility(View.GONE);
        }

        if (Exam_Paper_Value.equalsIgnoreCase("True")) {
            ln_ep.setVisibility(View.VISIBLE);
        } else {
            ln_ep.setVisibility(View.GONE);
        }

        if (TRM_Value.equalsIgnoreCase("True")) {
            ln_trm.setVisibility(View.VISIBLE);
        } else {
            ln_trm.setVisibility(View.GONE);
        }

        btn_Rsarapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionActivity.this, ChapterListActivity.class);
                intent.putExtra("Rsar_Book_Id", Book_Id);
                intent.putExtra("Rsar_Subject_Id", Subject_Id);
                intent.putExtra("Rsar_Class_Id", Class_Id);
                intent.putExtra("Rsar_Book_Name", Book_Name);
                intent.putExtra("Rsar_Op_Diff_Play", Op_Diff_Play);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

            }
        });
        LinearLayoutHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialogss = new Dialog(OptionActivity.this);
                dialogss.setContentView(R.layout.popup_help);
                dialogss.setCancelable(true);

                // set the custom dialog components - text, image and button
                LinearLayout ln_outline = (LinearLayout) dialogss.findViewById(R.id.dia_ln_outline);

                mPager = (ViewPager) dialogss.findViewById(R.id.pager);
                mPager.setAdapter(new HelpImageAdapter(OptionActivity.this, bannerDatumArrayList));

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
                ln_outline.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                dialogss.show();
            }
        });
        btn_PP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setClass(getApplicationContext(), ShowWebView.class);
                intent.putExtra("Linkpass", Practice_Paper);
                startActivity(intent);
            }
        });
        btn_Ep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setClass(getApplicationContext(), ShowWebView.class);
                intent.putExtra("Linkpass", Exam_Paper);
                startActivity(intent);
            }
        });
        btn_TRM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setClass(getApplicationContext(), ShowWebView.class);
                intent.putExtra("Linkpass", TRM);
                startActivity(intent);
            }
        });
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
                                    final Dialog dialogss = new Dialog(OptionActivity.this);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
