package rsarapp.com.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.FileUtils;

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
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import rsarapp.com.ui.activityList.ChapterVideoPlayBackActivity;
import rsarapp.com.modelClass.ChapterModel;
import rsarapp.database.RecordDatabase;
import rsarapp.com.dynamics.VideoPlayer;
import rsarapp.com.rsarapp.R;
import rsarapp.com.utilities.AllStaticMethod;
import rsarapp.com.utilities.UnzipUtil;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {
    Context context;
    ArrayList<ChapterModel> models;
    SharedPreferences preferences;
    Boolean isInternetPresent = false;
    private ProgressDialog mProgressDialog;
    String unzipLocation ;
    String zipFile ;
    public static String Value;
    String Str_Zip_Name,Str_Chap_Name,Str_Class_Name,Str_Sub_Name,Str_Book_Name,Pref_School_Fb_Name;
    boolean deleted_zip;
    private RecordDatabase database;
    private String Save_Chapter_Id;
    File dirDeleteFolder;
    public ChapterAdapter(Context context, ArrayList<ChapterModel> models) {
        this.context=context;
        this.models=models;
        database = new RecordDatabase(context);
        preferences = context.getSharedPreferences("RSAR_APP", Context.MODE_PRIVATE);
        Pref_School_Fb_Name= preferences.getString("Rsar_Fd_School_Name", "");
    }
    @Override
    public int getItemCount() {
        return models.size();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.chapter_list_adapter, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position)
    {
        ChapterModel chapterModel=models.get(position);
        if(chapterModel.getDownload_Status().contains("1")) {
            ChapterVideoPlayBackActivity.Download_Btn.setVisibility(View.GONE);
            ChapterVideoPlayBackActivity.Scan_Btn.setVisibility(View.VISIBLE);
        }
        if(chapterModel.getDownload_Status().equalsIgnoreCase("0")) {
            viewHolder.linearLayout.setVisibility(View.GONE);
            viewHolder.btnDownload.setVisibility(View.VISIBLE);
        }
        else {
            viewHolder.linearLayout.setVisibility(View.VISIBLE);
            viewHolder.btnDownload.setVisibility(View.GONE);
            isInternetPresent = AllStaticMethod.isOnline(context);
            if (isInternetPresent) {
                viewHolder.btnDelete.setVisibility(View.VISIBLE);
            }else {
                viewHolder.btnDelete.setVisibility(View.GONE);
            }
        }

        viewHolder.txtChapterName.setText(chapterModel.getChapter_Name());
        viewHolder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChapterModel model=models.get(position);

                Str_Zip_Name= model.getZip_Name();
                Str_Chap_Name= model.getChapter_Name();
                Str_Class_Name  = model.getClass_Name();
                Str_Sub_Name = model.getSubject_Name();
                Str_Book_Name = model.getBook_Name();
                Intent intent = new Intent(context, VideoPlayer.class);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    intent.putExtra("VideoUrl",context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                            +"/.rsarapp"+"/"+Pref_School_Fb_Name+"/"+Str_Class_Name+"/"+Str_Sub_Name+"/"+Str_Book_Name+"/"+model.getZip_Name()
                            +"/videos"+"/"+model.getVideo_Name()+".mp4");
                    context.startActivity(intent);
                }else {
                    intent.putExtra("VideoUrl", Environment.getExternalStorageDirectory()
                            + "/.rsarapp" + "/" + Pref_School_Fb_Name + "/" + Str_Class_Name + "/" + Str_Sub_Name + "/" + Str_Book_Name + "/" + model.getZip_Name()
                            + "/videos" + "/" + model.getVideo_Name() + ".mp4");
                    context.startActivity(intent);
                }
            }
        });
        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final ChapterModel model = models.get(position);
                Str_Chap_Name = model.getChapter_Name();
                Str_Class_Name = model.getClass_Name();
                Str_Sub_Name = model.getSubject_Name();
                Str_Book_Name = model.getBook_Name();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    dirDeleteFolder= new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+"/.rsarapp"+"/"+Pref_School_Fb_Name+"/"+Str_Class_Name+"/"+Str_Sub_Name+"/"+Str_Book_Name+"/"+model.getZip_Name()+"/");
                } else{
                    dirDeleteFolder= new File(Environment.getExternalStorageDirectory()+"/.rsarapp"+"/"+Pref_School_Fb_Name+"/"+Str_Class_Name+"/"+Str_Sub_Name+"/"+Str_Book_Name+"/"+model.getZip_Name()+"/");
                }
                if (dirDeleteFolder.isDirectory())
                {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Confirm Delete...");
                    alertDialog.setMessage("Are you sure you want to delete ?");
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                            try {
                                FileUtils.deleteDirectory(dirDeleteFolder);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                                System.out.println("Getinternal"+"  "+dirDeleteFolder);
                                database.updateDownloadStatus(model.getBook_Name(), model.getChapter_Id(), "0");
                                model.setDownload_Status("0");
                                notifyDataSetChanged();
                                Toast.makeText(context, "Deleted.. ", Toast.LENGTH_SHORT).show();
                            }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
//                            Toast.makeText(context, "You clicked on NO", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();
                }
                else{
                    Toast.makeText(context, "No folder available.... ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewHolder.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ChapterModel model=models.get(position);
                String DwlndLink=model.getDownload_Link();/*(Integer)v.getTag();*/
                Str_Zip_Name=model.getZip_Name();
                Str_Chap_Name= model.getChapter_Name();
                Str_Class_Name  = model.getClass_Name();
                Str_Sub_Name = model.getSubject_Name();
                Str_Book_Name = model.getBook_Name();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    unzipLocation =  context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                            +"/.rsarapp"+"/"+Pref_School_Fb_Name+"/"+Str_Class_Name+"/"+Str_Sub_Name+"/"+Str_Book_Name+"/";
                    Log.d("vtvt",""+unzipLocation);

                    System.out.println("dinaadddaaa" + "  "+ DwlndLink+" "+Str_Zip_Name+" "+Str_Book_Name+" "+unzipLocation);
                }else {
                    unzipLocation =  Environment.getExternalStorageDirectory()
                            +"/.rsarapp"+"/"+Pref_School_Fb_Name+"/"+Str_Class_Name+"/"+Str_Sub_Name+"/"+Str_Book_Name+"/";
                    Log.d("vtvt1",""+unzipLocation);
                    System.out.println("dinaadddaaa" + "  "+ DwlndLink+" "+Str_Zip_Name+" "+Str_Book_Name+" "+unzipLocation);
                }
                Value=null;
                isInternetPresent = AllStaticMethod.isOnline(context);
                if (isInternetPresent)
                {
                    Save_Chapter_Id = model.getChapter_Id();
                    DownloadMapAsync mew = new DownloadMapAsync(viewHolder, model);
                    mew.execute(DwlndLink);
                    Value=Str_Zip_Name+".zip";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        zipFile = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)+"/"+Str_Zip_Name+".zip";
                    }else{
                        zipFile = Environment.getExternalStorageDirectory()+"/"+Str_Zip_Name+".zip";
                    }
                } else
                {
                    AllStaticMethod.showAlertDialog(context, "No Internet Connection", "You don't have internet connection.", false);
                }
            }
        }
        );
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        public TextView txtChapterName;
        public Button btnDownload;
        public Button btnPlay;
        public Button btnDelete;
        public ViewHolder(View itemView) {
            super(itemView);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.linearLayout);
            txtChapterName = (TextView) itemView.findViewById(R.id.txtChapterName);
            btnDownload = (Button) itemView.findViewById(R.id.btnDownload);
            btnPlay = (Button) itemView.findViewById(R.id.btnPlay);
//            btnAssessment = (Button) itemView.findViewById(R.id.btnAssessment);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }
    public class DownloadMapAsync extends AsyncTask<String, String, String> {
        String result ="";
        ViewHolder holder;
        ChapterModel chapterModel;

        public DownloadMapAsync(ViewHolder viewHolder, ChapterModel model)
        {
            this.holder=viewHolder;
            this.chapterModel = model;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Downloading File..");
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
                System.out.println("tgtgtgttg"+" "+lenghtOfFile);
                if(lenghtOfFile == 0)
                {
                    AllStaticMethod.showAlertDialog(context, "Error In Internet Connection",
                            "You don't have proper internet connection.", false);
                }
                byte data[] = new byte[1024];
                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;

                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    System.out.println("fgfffff"+" "+(int)((total*100)/lenghtOfFile));
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
            Log.d("ANDRO_ASYNC",progress[0]);
            mProgressDialog.setProgress(Integer.parseInt(progress[0]));
        }
        @Override
        protected void onPostExecute(String unused) {
            mProgressDialog.dismiss();
            if(result.equalsIgnoreCase("true")){
                try {
                    unzip(holder, chapterModel);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else{

            }
        }
    }
    public void unzip(final ViewHolder holder, ChapterModel model) throws IOException {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Please Wait...");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        new UnZipTask(holder, model).execute(zipFile, unzipLocation);
    }
    private class UnZipTask extends AsyncTask<String, Void, Boolean> {
        public ViewHolder holder;
        private ChapterModel chapterModel;
        public UnZipTask(ViewHolder viewHolder, ChapterModel model)
        {
            this.holder=viewHolder;
            this.chapterModel=model;
        }
        @SuppressWarnings("rawtypes")
        @Override
        protected Boolean doInBackground(String... params) {
            String filePath = params[0];
            String destinationPath = params[1];

            File archive = new File(filePath);
            try {
                ZipFile zipfile = new ZipFile(archive);
                for (Enumeration e = zipfile.entries(); e.hasMoreElements();) {
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
            Toast.makeText(context, "Downloading completed...", Toast.LENGTH_LONG).show();
            File filev;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                filev = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), Str_Zip_Name+".zip");
            }else{
                filev = new File(Environment.getExternalStorageDirectory(), Str_Zip_Name+".zip");
            }

            deleted_zip = filev.delete();
            database.updateDownloadStatus(chapterModel.getBook_Name(), Save_Chapter_Id, "1");
            chapterModel.setDownload_Status("1");

            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.btnDownload.setVisibility(View.GONE);
            ChapterVideoPlayBackActivity.Scan_Btn.setVisibility(View.VISIBLE);
            ChapterVideoPlayBackActivity.Download_Btn.setVisibility(View.GONE);
        }
        private void unzipEntry(ZipFile zipfile, ZipEntry entry,
                                String outputDir) throws IOException {

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

}
