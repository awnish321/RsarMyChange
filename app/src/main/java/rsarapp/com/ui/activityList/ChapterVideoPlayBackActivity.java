package rsarapp.com.ui.activityList;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import rsarapp.com.adapter.ChapterAdapter;
import rsarapp.com.modelClass.ChapterModel;
import rsarapp.com.rsarapp.R;
import rsarapp.com.ui.dialog.ProgressHUD;
import rsarapp.com.utilities.AllStaticMethod;
import rsarapp.com.utilities.AppConstant;
import rsarapp.com.utilities.UnzipUtil;
import rsarapp.database.RecordDatabase;

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class ChapterVideoPlayBackActivity extends AppCompatActivity {

    String[] permissionsRequired = new String[]{ Manifest.permission.READ_MEDIA_VIDEO,Manifest.permission.CAMERA};
    private boolean sentToSettings = false;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private RecyclerView recyclerView;
    private ArrayList<ChapterModel> chapterModels;
    private ChapterAdapter chapterAdapter;
    public static Button Scan_Btn, Download_Btn;
    private static String mClassToLaunch, mClassToLaunchPackage;
    private ProgressDialog mProgressDialog;
    public static String Value;
    private ArrayList<ChapterModel> models;
    private SharedPreferences permissionStatus;
    ProgressHUD dialog;
    Boolean isInternetPresent = false;
    String Pref_Bg_Code, Pref_Top_Bg_Code, Pref_Button_Bg, Pref_School_UI, Pref_School_name, Pref_School_Fb_Name,
            Pref_Restric_Id, Pref_Download_Show;
    SharedPreferences preferences;
    String Str_Msg, Book_Id, Str_Book_Name, Str_DB_Book_Name, Class_Id, Subject_Id, DataSet_Name, Asses_Value, Cl_Diff_Play,
            Practice_Paper, Exam_Paper, TRM, Practice_Paper_Value, Exam_Paper_Value, TRM_Value, Rsar_Value, unzipLocation, zipFile;
    boolean deleted_zip;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_chapter_video_play_back);

        context = ChapterVideoPlayBackActivity.this;
        preferences = getSharedPreferences("RSAR_APP", Context.MODE_PRIVATE);

        Pref_Download_Show = preferences.getString("Rsar_Show_Download", "");
        Pref_School_UI = preferences.getString("Rsar_School_UI", "");
        Pref_School_name = preferences.getString("Rsar_School_Name", "");
        Pref_Bg_Code = preferences.getString("Rsar_Bg_Code", "");
        Pref_Top_Bg_Code = preferences.getString("Rsar_Top_Bg_Code", "");
        Pref_Button_Bg = preferences.getString("Rsar_Button_Bg", "");
        Pref_School_Fb_Name = preferences.getString("Rsar_Fd_School_Name", "");
        Pref_Restric_Id = preferences.getString("Rsar_Restric_ID", "");

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);

        Practice_Paper = getIntent().getExtras().getString("Rsar_Practice_Paper");
        Exam_Paper = getIntent().getExtras().getString("Rsar_Exam_Paper");
        TRM = getIntent().getExtras().getString("Rsar_TRM");
        Practice_Paper_Value = getIntent().getExtras().getString("Rsar_Practice_Paper_Value");
        Exam_Paper_Value = getIntent().getExtras().getString("Rsar_Exam_Paper_Value");
        TRM_Value = getIntent().getExtras().getString("Rsar_TRM_Value");
        Rsar_Value = getIntent().getExtras().getString("Rsar_RSAR_Value");

        Class_Id = getIntent().getExtras().getString("Rsar_Class_Id");
        Subject_Id = getIntent().getExtras().getString("Rsar_Subject_Id");
        Book_Id = getIntent().getExtras().getString("Rsar_Book_Id");
        Str_Book_Name = getIntent().getExtras().getString("Rsar_Book_Name");
        Cl_Diff_Play = getIntent().getExtras().getString("Rsar_Diff_Play");

        TextView Sub_Title = (TextView) findViewById(R.id.Title_Sub_Chap);
        Sub_Title.setText(Str_Book_Name);
        Scan_Btn = (Button) findViewById(R.id.button_start);
        Download_Btn = (Button) findViewById(R.id.button_Download);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        Scan_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionBeforeScan();
            }
        });
    }
    @Override
    protected void onResume() {
        checkAndroidPermission();
        super.onResume();
    }

    private void loadChapterList() {
        isInternetPresent = AllStaticMethod.isOnline(context);
        if (isInternetPresent) {
            GetChapterURl();
            String message = "Please Wait....";
            dialog = new ProgressHUD(ChapterVideoPlayBackActivity.this, R.style.ProgressHUD);
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
            // Toast.makeText(getApplicationContext(), "available", Toast.LENGTH_LONG).show();
        } else {
            AllStaticMethod.showAlertDialog(context, "No Internet Connection", "You don't have internet connection.", false);
        }
    }

    private void startARActivity() {
        try {
            mClassToLaunchPackage = getPackageName();
            mClassToLaunch = "rsarapp.com.app.VideoPlayback.VideoPlayback";
            Intent i = new Intent();
            i.putExtra("chapterModels", chapterModels);
            i.putExtra("Rsar_Cl_Diff_Play", Cl_Diff_Play);
            i.setClassName(mClassToLaunchPackage, mClassToLaunch);
            ChapterVideoPlayBackActivity.this.startActivity(i);
            Log.d("linksww", mClassToLaunch);
            Log.d("linksww1", mClassToLaunchPackage);
        } catch (Exception e) {
            System.out.println("gfgfgf" + "    " + e);
        }
    }

    private void setAdapterValue() {
        chapterAdapter = new ChapterAdapter(ChapterVideoPlayBackActivity.this, chapterModels);
        recyclerView.setAdapter(chapterAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chapterAdapter.notifyDataSetChanged();
    }

    private void GetChapterURl() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String urlmanual = AppConstant.url + "chapter.php";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlmanual,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            RecordDatabase database = new RecordDatabase(ChapterVideoPlayBackActivity.this);
                            String downloadStatus = "0";
                            chapterModels = new ArrayList<>();
                            models = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String Status = jsonObject.getString("Status");
                            Str_Msg = jsonObject.getString("Message");
                            if (Status.equalsIgnoreCase("true")) {
                                String className = jsonObject.getString("Class_Name");
                                String Subject_Name = jsonObject.getString("Subject_Name");
                                //  String Subject_Name="book"+Subject_Name_A;
                                String Book_Name = jsonObject.getString("Book_Name");
                                String School_UI = jsonObject.getString("School_UI");
                                String Restrict_SD = jsonObject.getString("Restrict_SD");
                                String Class_ID = jsonObject.getString("Class_ID");
                                String Message = jsonObject.getString("Message");
                                String ChapterData = jsonObject.getString("ChapterData");
                                DataSet_Name = jsonObject.getString("Dataset_Name");
                                String DataSet_Link = jsonObject.getString("Dataset_Link");
                                File file, dir;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    unzipLocation = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                                            + "/.rsarapp" + "/" + Pref_School_Fb_Name + "/" + className + "/" + jsonObject.getString("Subject_Name") + "/" + Book_Name + "/" + "DataSet/";
                                    //System.out.println("dinaadddaaa" + "  "+ DwlndLink+" "+zip_Name+" "+Str_Book_Name);
                                    Value = null;
                                    dir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
                                    file = new File(dir, "/.rsarapp" + "/" + Pref_School_Fb_Name + "/" + className + "/" + jsonObject.getString("Subject_Name") + "/" + Book_Name + "/" + "DataSet/" + DataSet_Name/*+".xml"*/);
                                    System.out.println("FILEEEss" + "  " + file + "  " + DataSet_Name + "  " + unzipLocation);
                                } else {
                                    unzipLocation = Environment.getExternalStorageDirectory()
                                            + "/.rsarapp" + "/" + Pref_School_Fb_Name + "/" + className + "/" + jsonObject.getString("Subject_Name") + "/" + Book_Name + "/" + "DataSet/";
                                    //System.out.println("dinaadddaaa" + "  "+ DwlndLink+" "+zip_Name+" "+Str_Book_Name);
                                    Value = null;
                                    dir = Environment.getExternalStorageDirectory();
                                    file = new File(dir, "/.rsarapp" + "/" + Pref_School_Fb_Name + "/" + className + "/" + jsonObject.getString("Subject_Name") + "/" + Book_Name + "/" + "DataSet/" + DataSet_Name/*+".xml"*/);
                                    System.out.println("FILEEEss" + "  " + file + "  " + DataSet_Name + "  " + unzipLocation);
                                }
                                if (file.exists()) {
                                    System.out.println("FILEEE EXIST" + "  " + file + " " + "True");
                                } else {
                                    System.out.println("FILEEE EXIST" + "  " + file + "  " + "False");
                                    DownloadMapAsync mew = new DownloadMapAsync();
                                    mew.execute(DataSet_Link);
                                }
                                Value = DataSet_Name + ".zip";
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    zipFile = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/" + DataSet_Name + ".zip";
                                } else {
                                    zipFile = Environment.getExternalStorageDirectory() + "/" + DataSet_Name + ".zip";
                                }
                                JSONArray chapterArray = new JSONArray(ChapterData);
                                for (int i = 0; i < chapterArray.length(); i++) {
                                    JSONObject chapterObject = chapterArray.getJSONObject(i);
                                    String ZipName = chapterObject.getString("Zip_Name");
                                    Asses_Value = chapterObject.getString("Assessment_Value");
                                    System.out.println("Detailaaaa" + " " + Subject_Name + " " + Book_Name + "  " + ZipName);
                                    if (database.getRecord(Book_Name, chapterObject.getString("Chapter_Id")).equals("1")) {
                                        downloadStatus = "1";
                                    } else {
                                        downloadStatus = "0";
                                    }
                                    chapterModels.add(new ChapterModel(className, Subject_Name, Book_Name, School_UI, Restrict_SD, Class_ID, Message,
                                            chapterObject.getString("Chapter_Id"), chapterObject.getString("Chapter_Name"),
                                            chapterObject.getString("Assessment_Name"), chapterObject.getString("Video_Name"),
                                            chapterObject.getString("DB_Name"),
                                            chapterObject.getString("Download_Link"), downloadStatus, ZipName, Asses_Value, DataSet_Name));

                                    insertData(Class_ID, className, Subject_Name, Book_Name, chapterObject.getString("Chapter_Id"),
                                            chapterObject.getString("Chapter_Name"), chapterObject.getString("Video_Name"),
                                            chapterObject.getString("Download_Link"), downloadStatus, ZipName, Asses_Value, DataSet_Name);
                                }
                                if (chapterModels.size() != 0) {
                                    setAdapterValue();
                                } else {
                                    final Dialog dialogss = new Dialog(ChapterVideoPlayBackActivity.this);
                                    dialogss.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialogss.setContentView(R.layout.alert_dialog);
                                    dialogss.setCancelable(true);

                                    LinearLayout ln_outline = (LinearLayout) dialogss.findViewById(R.id.dia_ln_outline);
                                    View view = (View) dialogss.findViewById(R.id.dia_view);
                                    TextView Error_text = (TextView) dialogss.findViewById(R.id.dia_error_title);
                                    TextView text = (TextView) dialogss.findViewById(R.id.dia_error_msg);
                                    text.setText("Please Start your internet to load data.");

                                    Button btn_yes = (Button) dialogss.findViewById(R.id.dia_b_yes);

                                    ln_outline.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                                    view.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                                    Error_text.setTextColor(Color.parseColor(Pref_Bg_Code));
                                    btn_yes.setBackgroundColor(Color.parseColor(Pref_Bg_Code));
                                    btn_yes.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogss.dismiss();
                                            finish();
                                        }
                                    });

                                    dialogss.show();
                                }
                            } else {
                                final Dialog dialogss = new Dialog(ChapterVideoPlayBackActivity.this);
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
                            if (dialog.isShowing())
                                dialog.dismiss();
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
                params.put("School_UI", Pref_School_UI);
                params.put("cId", Class_Id);
                params.put("sId", Subject_Id);// Second one u can change
                params.put("bId", Book_Id);
                params.put("action", "chapter");
                params.put("Restrict_SD", Pref_Restric_Id);

                return params;
            }
        };
        queue.add(postRequest);
    }

    private void insertData(String classID, String className, String subjectName, String book_Name,
                            String chapterId, String Chapter_Name, String videoName, String downloadLink,
                            String status, String zipName, String assesValue, String dataSetName) {
        RecordDatabase database = new RecordDatabase(ChapterVideoPlayBackActivity.this);
        int i = database.getProfilesCount();
        if (database.CheckIsDataAlreadyInDBorNot(book_Name, chapterId)) {
            Log.e("PARAMS", "No NEED TO INSERT " + i);
        } else {
            Log.e("PARAMS", "INSERT " + i);
            ContentValues contentValue = new ContentValues();
            contentValue.put(RecordDatabase.Save_Class_ID, classID);
            contentValue.put(RecordDatabase.Save_Class_Name, className);
            contentValue.put(RecordDatabase.Save_Subject_Name, subjectName);
            contentValue.put(RecordDatabase.Save_Book_Name, book_Name);
            contentValue.put(RecordDatabase.Save_Chapter_Id, chapterId);
            contentValue.put(RecordDatabase.Save_Chapter_Name, Chapter_Name);
            contentValue.put(RecordDatabase.Save_Video_Name, videoName);
            contentValue.put(RecordDatabase.Save_Download_Link, downloadLink);
            contentValue.put(RecordDatabase.Save_Download_Status, status);
            contentValue.put(RecordDatabase.Save_Zip_Name, zipName);
            contentValue.put(RecordDatabase.Save_Asses_Value, assesValue);
            contentValue.put(RecordDatabase.Save_DataSet_Name, dataSetName);
            database.insertRecord(contentValue);
        }
    }

    public class DownloadMapAsync extends AsyncTask<String, String, String> {
        String result = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ChapterVideoPlayBackActivity.this);
            mProgressDialog.setMessage("Loading..");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;
            try {
                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                int lenghtOfFile = conexion.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(zipFile);
                if (lenghtOfFile == 0) {
                    showAlertDialog(ChapterVideoPlayBackActivity.this, "Error In Internet Connection", "You don't have proper internet connection.", false);
                }
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.close();
                input.close();
                result = "true";
            } catch (Exception e) {
                result = "false";
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String unused) {
            mProgressDialog.dismiss();

            if (result.equalsIgnoreCase("true")) {
                try {
                    unzip();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {

            }
        }
    }

    public void unzip() throws IOException {
        mProgressDialog = new ProgressDialog(ChapterVideoPlayBackActivity.this);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        new UnZipTask().execute(zipFile, unzipLocation);
    }

    private class UnZipTask extends AsyncTask<String, Void, Boolean> {

        @SuppressWarnings("rawtypes")
        @Override
        protected Boolean doInBackground(String... params) {
            String filePath = params[0];
            String destinationPath = params[1];
            File archive = new File(filePath);
            try {

                ZipFile zipfile = new ZipFile(archive);
                for (Enumeration e = zipfile.entries(); e.hasMoreElements(); ) {
                    ZipEntry entry = (ZipEntry) e.nextElement();
                    unzipEntry(zipfile, entry, destinationPath);
                }

                UnzipUtil d = new UnzipUtil(zipFile, unzipLocation);
                d.unzip();

            } catch (Exception e) {

                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mProgressDialog.dismiss();
            ///button hide and show
            File filev;
            //  Toast.makeText(ChapterList.this, "Downloading completed...", Toast.LENGTH_LONG).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                filev = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), DataSet_Name + ".zip");
            } else {
                filev = new File(Environment.getExternalStorageDirectory(), DataSet_Name + ".zip");
            }
            deleted_zip = filev.delete();

            System.out.println("dfdfdfdfdddddd" + "   " + filev);
        }

        private void unzipEntry(ZipFile zipfile, ZipEntry entry, String outputDir) throws IOException {

            if (entry.isDirectory()) {
                createDir(new File(outputDir, entry.getName()));
                return;
            }

            File outputFile = new File(outputDir, entry.getName());
            if (!outputFile.getParentFile().exists()) {
                createDir(outputFile.getParentFile());
            }
            // Log.v("", "Extracting: " + entry);
            BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

            try {

            } finally {
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }
        }

        private void createDir(File dir) {
            if (dir.exists()) {
                return;
            }
            if (!dir.mkdirs()) {
                throw new RuntimeException("Can not create dir " + dir);
            }
        }
    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        // Setting Dialog Title
        alertDialog.setTitle(title);
        // Setting Dialog Message
        alertDialog.setMessage(message);
        // Setting alert dialog icon
        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);
        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

//    private void GetAndroidPermission() {
//
//        int version = Build.VERSION.SDK_INT;
//        if (version <= 32) {
//            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                    && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//
//                if (ActivityCompat.shouldShowRequestPermissionRationale(ChapterVideoPlayBackActivity.this, permissionsRequired[0])
//                        || ActivityCompat.shouldShowRequestPermissionRationale(ChapterVideoPlayBackActivity.this, permissionsRequired[1]))
//                {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setTitle("Need Multiple Permissions");
//                    builder.setMessage("This app needs Camera and Storage permissions.");
//                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                            ActivityCompat.requestPermissions(ChapterVideoPlayBackActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
//                        }
//                    });
//                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                            finish();
//                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                            Toast.makeText(getBaseContext(), "Grant Permissions of Storage and Camera", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                    builder.show();
//                } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
//                    //Previously Permission Request was cancelled with 'Dont Ask Again',
//                    // Redirect to Settings after showing Information about why you need the permission
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setTitle("Need Multiple Permissions");
//                    builder.setMessage("This app needs Camera permissions.");
//                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                            sentToSettings = true;
//                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                            Uri uri = Uri.fromParts("package", getPackageName(), null);
//                            intent.setData(uri);
//                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
//                        }
//                    });
//                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                            finish();
//                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                            Toast.makeText(getBaseContext(), "Go to setting and Grant Permissions of Storage", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                    builder.show();
//                }
//                else if (permissionStatus.getBoolean(permissionsRequired[1], false)) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setTitle("Need Multiple Permissions");
//                    builder.setMessage("This app needs Storage permissions.");
//                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                            sentToSettings = true;
//                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                            Uri uri = Uri.fromParts("package", getPackageName(), null);
//                            intent.setData(uri);
//                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
//                        }
//                    });
//                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                            finish();
//                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                            Toast.makeText(getBaseContext(), "Go to setting and Grant Permissions of Camera", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                    builder.show();
//                }
//                else {
//                    ActivityCompat.requestPermissions(ChapterVideoPlayBackActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
//                }
//            } else {
//                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    SharedPreferences.Editor editor = permissionStatus.edit();
//                    editor.putBoolean(permissionsRequired[0], true);
//                    editor.apply();
//                }
//                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                    SharedPreferences.Editor editor = permissionStatus.edit();
//                    editor.putBoolean(permissionsRequired[1], true);
//                    editor.apply();
//                }
//            }
//        } else {
//            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED
//                    && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                SharedPreferences.Editor editor = permissionStatus.edit();
//                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
//                    editor.putBoolean(permissionsRequired[0], false);
//                    editor.apply();
//                }
//                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                    editor.putBoolean(permissionsRequired[1], false);
//                    editor.apply();
//                }
//
//                if (ActivityCompat.shouldShowRequestPermissionRationale(ChapterVideoPlayBackActivity.this, permissionsRequired[0])
//                        || ActivityCompat.shouldShowRequestPermissionRationale(ChapterVideoPlayBackActivity.this, permissionsRequired[1]))
//                {
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setTitle("Need Multiple Permissions");
//                    builder.setMessage("This app needs Camera and Storage permissions.");
//                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                            ActivityCompat.requestPermissions(ChapterVideoPlayBackActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
//                        }
//                    });
//                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                            finish();
//                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                            Toast.makeText(getBaseContext(), "Grant Permissions of Storage and Camera", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                    builder.show();
//                } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
//                    //Previously Permission Request was cancelled with 'Dont Ask Again',
//                    // Redirect to Settings after showing Information about why you need the permission
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setTitle("Need Multiple Permissions");
//                    builder.setMessage("This app needs Camera permissions.");
//                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                            sentToSettings = true;
//                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                            Uri uri = Uri.fromParts("package", getPackageName(), null);
//                            intent.setData(uri);
//                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
//                        }
//                    });
//                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                            finish();
//                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                            Toast.makeText(getBaseContext(), "Go to setting and Grant Permissions of Storage", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                    builder.show();
//                }
//                else if (permissionStatus.getBoolean(permissionsRequired[1], false)) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setTitle("Need Multiple Permissions");
//                    builder.setMessage("This app needs Storage permissions.");
//                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                            sentToSettings = true;
//                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                            Uri uri = Uri.fromParts("package", getPackageName(), null);
//                            intent.setData(uri);
//                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
//                        }
//                    });
//                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                            finish();
//                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                            Toast.makeText(getBaseContext(), "Go to setting and Grant Permissions of Camera", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                    builder.show();
//                }
//                else {
//                    //just request the permission
//                    ActivityCompat.requestPermissions(ChapterVideoPlayBackActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
//                }
//            } else {
//                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED) {
//                    SharedPreferences.Editor editor = permissionStatus.edit();
//                    editor.putBoolean(permissionsRequired[0], true);
//                    editor.apply();
//                }
//                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//                    SharedPreferences.Editor editor = permissionStatus.edit();
//                    editor.putBoolean(permissionsRequired[1], true);
//                    editor.apply();
//                }
//            }
//        }
//
////        if (ActivityCompat.checkSelfPermission(context, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
////                || ActivityCompat.checkSelfPermission(context, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED)
////        {
////
////            SharedPreferences.Editor editor = permissionStatus.edit();
////            if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED){
////                editor.putBoolean(permissionsRequired[1], false);
////                editor.apply();
////            }
////            if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
////                editor.putBoolean(permissionsRequired[0], false);
////                editor.apply();
////            }
////
////            if (ActivityCompat.shouldShowRequestPermissionRationale(ChapterVideoPlayBackActivity.this, permissionsRequired[0])
////                    || ActivityCompat.shouldShowRequestPermissionRationale(ChapterVideoPlayBackActivity.this, permissionsRequired[1])) {
////
////                //Show Information about why you need the permission
////                AlertDialog.Builder builder = new AlertDialog.Builder(context);
////                builder.setTitle("Need Multiple Permissions");
////                builder.setMessage("This app needs Camera and Storage permissions.");
////                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        dialog.cancel();
////                        ActivityCompat.requestPermissions(ChapterVideoPlayBackActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
////                    }
////                });
////                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        dialog.cancel();
////                        finish();
////                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
////                        Toast.makeText(getBaseContext(), "Grant Permissions of Storage and Camera", Toast.LENGTH_LONG).show();
////                    }
////                });
////                builder.show();
////            }
////            else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
////                //Previously Permission Request was cancelled with 'Dont Ask Again',
////                // Redirect to Settings after showing Information about why you need the permission
////                AlertDialog.Builder builder = new AlertDialog.Builder(context);
////                builder.setTitle("Need Multiple Permissions");
////                builder.setMessage("This app needs Camera permissions.");
////                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        dialog.cancel();
////                        sentToSettings = true;
////                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
////                        Uri uri = Uri.fromParts("package", getPackageName(), null);
////                        intent.setData(uri);
////                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
////                    }
////                });
////                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        dialog.cancel();
////                        finish();
////                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
////                        Toast.makeText(getBaseContext(), "Go to setting and Grant Permissions of Camera", Toast.LENGTH_LONG).show();
////                    }
////                });
////                builder.show();
////            }
////            else if (permissionStatus.getBoolean(permissionsRequired[1], false)) {
////                //Previously Permission Request was cancelled with 'Dont Ask Again',
////                // Redirect to Settings after showing Information about why you need the permission
////                AlertDialog.Builder builder = new AlertDialog.Builder(context);
////                builder.setTitle("Need Multiple Permissions");
////                builder.setMessage("This app needs Storage permissions.");
////                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        dialog.cancel();
////                        sentToSettings = true;
////                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
////                        Uri uri = Uri.fromParts("package", getPackageName(), null);
////                        intent.setData(uri);
////                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
////                    }
////                });
////                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        dialog.cancel();
////                        finish();
////                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
////                        Toast.makeText(getBaseContext(), "Go to setting and Grant Permissions of Storage", Toast.LENGTH_LONG).show();
////                    }
////                });
////                builder.show();
////            }
////            else {
////                //just request the permission
////                ActivityCompat.requestPermissions(ChapterVideoPlayBackActivity.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
////            }
////            editor.putBoolean(permissionsRequired[0], true);
////            editor.apply();
////            if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED){
////                editor.putBoolean(permissionsRequired[1], true);
////                editor.apply();
////            }if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
////                editor.putBoolean(permissionsRequired[0], true);
////                editor.apply();
////            }
////        }
////        else {
////            if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED){
////                SharedPreferences.Editor editor = permissionStatus.edit();
////                editor.putBoolean(permissionsRequired[1], true);
////                editor.apply();
////            }
////            if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
////                SharedPreferences.Editor editor = permissionStatus.edit();
////                editor.putBoolean(permissionsRequired[0], true);
////                editor.apply();
////            }
////        }
//    }

    private void checkAndroidPermission() {

        int version = Build.VERSION.SDK_INT;
        if( version <= 32 ) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),  Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("Please allow Camera And Storage permissions");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                });
                builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.cancel();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        Toast.makeText(getBaseContext(), "This app needs Camera And Storage permissions", Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            }else if (ContextCompat.checkSelfPermission(getApplicationContext(),  Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("Please allow Camera And Storage permissions");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                });
                builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.cancel();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        Toast.makeText(getBaseContext(), "This app needs Camera And Storage permissions.", Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            }
            else {
                SharedPreferences.Editor editor = permissionStatus.edit();
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    editor.putBoolean(permissionsRequired[0], true);
                    editor.apply();
                }
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    editor.putBoolean(permissionsRequired[1], true);
                    editor.apply();
                }
                loadChapterList();
            }
        } else
        {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),  Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("Please allow Camera And Storage permissions");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                });
                builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        Toast.makeText(getBaseContext(), "This app needs Camera And Storage permissions", Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            }else if (ContextCompat.checkSelfPermission(getApplicationContext(),  Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("Please allow Camera And Storage permissions");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                });
                builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        Toast.makeText(getBaseContext(), "This app needs Camera And Storage permissions.", Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            }else {
                SharedPreferences.Editor editor = permissionStatus.edit();
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED)
                {
                    editor.putBoolean(permissionsRequired[0], true);
                    editor.apply();
                }
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    editor.putBoolean(permissionsRequired[1], true);
                    editor.apply();
                }
                loadChapterList();
            }
        }
    }
    private void checkPermissionBeforeScan() {

        int version = Build.VERSION.SDK_INT;
        if( version <= 32 ) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),  Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("Please allow Camera And Storage permissions");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                });
                builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        Toast.makeText(getBaseContext(), "This app needs Camera And Storage permissions", Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            }
            else if (ContextCompat.checkSelfPermission(getApplicationContext(),  Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("Please allow Camera And Storage permissions");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                });
                builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        Toast.makeText(getBaseContext(), "This app needs Camera And Storage permissions.", Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            }
            else {
                SharedPreferences.Editor editor = permissionStatus.edit();
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    editor.putBoolean(permissionsRequired[0], true);
                    editor.apply();
                }
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    editor.putBoolean(permissionsRequired[1], true);
                    editor.apply();
                }
                startARActivity();
            }
        } else
        {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),  Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("Please allow Camera And Storage permissions");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                });
                builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        Toast.makeText(getBaseContext(), "This app needs Camera And Storage permissions", Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            }
            else if (ContextCompat.checkSelfPermission(getApplicationContext(),  Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("Please allow Camera And Storage permissions");
                builder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    }
                });
                builder.setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        Toast.makeText(getBaseContext(), "This app needs Camera And Storage permissions.", Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
            }
            else {
                SharedPreferences.Editor editor = permissionStatus.edit();
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED)
                {
                    editor.putBoolean(permissionsRequired[0], true);
                    editor.apply();
                }
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    editor.putBoolean(permissionsRequired[1], true);
                    editor.apply();
                }
                startARActivity();
            }
        }
    }

}


