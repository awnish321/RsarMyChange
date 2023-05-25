package rsarapp.com.ui.activityList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import rsarapp.com.rsarapp.R;
import rsarapp.com.rsarapp.databinding.ActivityMyProfileBinding;
import rsarapp.com.utilities.AllStaticMethod;

public class MyProfileActivity extends AppCompatActivity {

    ActivityMyProfileBinding binding;
    SharedPreferences preferences;
    Context context;
    String userClass, userName, userType, userEmail, userMobile, userStatus;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = MyProfileActivity.this;
        toolbar = findViewById(R.id.toolbarMyProfile);
        binding.toolbarMyProfile.txtTittle.setText("My Profile");
        binding.toolbarMyProfile.imgHelp.setVisibility(View.GONE);
        binding.toolbarMyProfile.txtSpace.setVisibility(View.VISIBLE);


        preferences = getSharedPreferences("RSAR_APP", Context.MODE_PRIVATE);
        userEmail = preferences.getString("Rsar_Pref_Email", "");
        userMobile = preferences.getString("Rsar_Pref_Mobile", "");
        userType = preferences.getString("Rsar_UserType", "");
        userClass = preferences.getString("rsar_class_name", "");
        userName = preferences.getString("rsar_registered_user_name", "");
        binding.llLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllStaticMethod.logout(context);
                Intent intent = new Intent(context, LoginPageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_inn, R.anim.fade_outt);
                finishAffinity();
            }
        });
        binding.llShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "RSAR APP");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Download this Application now:- https://play.google.com/store/apps/details?id=rsarapp.com.rsarapp");
                startActivity(Intent.createChooser(shareIntent, "share via"));
            }
        });

       if (userName.isEmpty()){
           AllStaticMethod.logout(context);
           Intent intent = new Intent(context, LoginPageActivity.class);
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
           intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
           startActivity(intent);
           overridePendingTransition(R.anim.fade_inn, R.anim.fade_outt);
           finishAffinity();
       } else {
           setProfileData();
       }
    }
    private void setProfileData() {
        binding.txtUserName.setText(AllStaticMethod.capitalizeWord(userName));
        binding.txtUserEmail.setText(userEmail);
        binding.txtUserMobile.setText(userMobile);
        binding.txtUserType.setText(userType);
        if (!userType.matches("Teacher")){
            binding.llClass.setVisibility(View.VISIBLE);
            binding.view.setVisibility(View.VISIBLE);
            binding.txtUserClass.setText(userClass);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}