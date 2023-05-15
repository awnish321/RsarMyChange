package rsarapp.com.ui.ActivityList;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/*public class UnzipUtil {
  private String _zipFile;
  private String _location;
  String canonicalPath;
  File outputFile;


  public UnzipUtil(String zipFile, String location) {
    _zipFile = zipFile;
    _location = location;

    _dirChecker("");
  }

  public void unzip() {
    try  {
      FileInputStream fin = new FileInputStream(_zipFile);
      //ZipInputStream zin = new ZipInputStream(fin);
      ZipInputStream zin =new ZipInputStream(new BufferedInputStream(fin));
      ZipEntry ze = null;
      while((ze = zin.getNextEntry()) != null) {
        outputFile = new File(_zipFile, ze.getName());
        canonicalPath = outputFile.getCanonicalPath();
        if (!canonicalPath.startsWith(_zipFile)) {
          _dirChecker(ze.getName());
        } else {
          FileOutputStream fout = new FileOutputStream(_location + ze.getName());
          //   for (int c = zin.read(); c != -1; c = zin.read()) {
          //   fout.write(c);


          byte[] buffer = new byte[8192];
          int len;
          while ((len = zin.read(buffer)) != -1) {
            fout.write(buffer, 0, len);
          }
          fout.close();

          //  }

          zin.closeEntry();
          // fout.close();



        }

      }
      zin.close();
    } catch(Exception e) {
      Log.e("Decompress", "unzip", e);
    }

  }

  private void _dirChecker(String dir) {
    File f = new File(_location + dir);

    if (!f.isDirectory()) {
      f.mkdirs();


    }
  }
}*/
public class UnzipUtil {
  private String _zipFile; 
  private String _location;
 
  public UnzipUtil(String zipFile, String location) { 
    _zipFile = zipFile; 
    _location = location; 
 
    _dirChecker(""); 
  } 
 
  public void unzip() { 
    try  {
      FileInputStream fin = new FileInputStream(_zipFile);
      ZipInputStream zin = new ZipInputStream(fin);
      ZipEntry ze = null;
      while ((ze = zin.getNextEntry()) != null) {
        Log.v("Decompress", "Unzipping " + ze.getName());

        if(ze.isDirectory()) {
          _dirChecker(ze.getName());
        } else {
          FileOutputStream fout = new FileOutputStream(_location + ze.getName());
       //   for (int c = zin.read(); c != -1; c = zin.read()) {
         //   fout.write(c);


            byte[] buffer = new byte[8192];
	         int len;
	         while ((len = zin.read(buffer)) != -1) {
	        	 fout.write(buffer, 0, len);
	         }
	         fout.close();

        //  }

          zin.closeEntry();
         // fout.close();



        }

      }
      zin.close();
    } catch(Exception e) {
      Log.e("Decompress", "unzip", e);
    }

  }
 
  private void _dirChecker(String dir) { 
    File f = new File(_location + dir); 
 
    if(!f.isDirectory()) { 
      f.mkdirs(); 
    }
  } 
}