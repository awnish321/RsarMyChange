package rsarapp.com.ui.activityList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rsarapp.com.adapter.TestGridAdapter;
import rsarapp.com.api.RetrofitAPIService;
import rsarapp.com.modelClass.response.TestResponseModel;
import rsarapp.com.rsarapp.databinding.ActivityTestBinding;
import rsarapp.com.utilities.AllStaticField;

public class TestActivity extends AppCompatActivity {

    String one, two, three, four;
    Context context;
    ActivityTestBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context=TestActivity.this;


        one = getIntent().getStringExtra("1");
        two = getIntent().getStringExtra("2");
        three = getIntent().getStringExtra("3");
        four = getIntent().getStringExtra("4");

        binding.txt1.setText(one);
        binding.txt2.setText(two);
        binding.txt3.setText(three);
        binding.txt4.setText(four);

        callRetrofit();



        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Mission successfully ", Toast.LENGTH_SHORT).show();
            }
        });

        TestGridAdapter adapter = new TestGridAdapter(AllStaticField.testModel, context);
        binding.recyclerViewNew.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewNew.setAdapter(adapter);
    }

    private void callRetrofit() {
                RetrofitAPIService.getTestApiClient().test().enqueue(new Callback<TestResponseModel>() {
            @Override
            public void onResponse(Call<TestResponseModel> call, Response<TestResponseModel> response)
            {
                    try {
                        if (response.isSuccessful())
                        {
                            AllStaticField.testModel.clear();
                            AllStaticField.testModel.addAll(response.body().getData());
                            Toast.makeText(context, response.body().getAccess(), Toast.LENGTH_LONG).show();
//                            startActivity(new Intent(context,LoginPageActivity.class));
//                            finish();

                        }else {
                            Toast.makeText(context, "response is  failed", Toast.LENGTH_LONG).show();

                        }
                    }catch (Exception e){
                        Toast.makeText(context, "data not found1", Toast.LENGTH_LONG).show();

                    }
            }
            @Override
            public void onFailure(Call<TestResponseModel> call, Throwable t) {
                Toast.makeText(context, t.toString()+"error", Toast.LENGTH_SHORT).show();

            }
        });

    }
}