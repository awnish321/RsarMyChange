package rsarapp.com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import rsarapp.com.modelClass.response.TestResponseModel;
import rsarapp.com.rsarapp.R;

public class Test1GridAdapter extends RecyclerView.Adapter<Test1GridAdapter.ViewHolder> {
    private final List<TestResponseModel.Datum> listdata;
    private final Context context;

    public Test1GridAdapter(List<TestResponseModel.Datum> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @NonNull
    @Override
    public Test1GridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.recycler_view_new,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Test1GridAdapter.ViewHolder holder, int position) {
        TestResponseModel.Datum mydata =listdata.get(position);

        holder.textView1.setText(mydata.getId().toString());
        holder.textView2.setText(mydata.getLink());
        holder.textView3.setText(mydata.getPublished());
        holder.textView4.setText(mydata.getSummary());
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView1,textView2,textView3,textView4;
        public ViewHolder(View itemView)
        {
            super(itemView);
//            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textView1 = (TextView) itemView.findViewById(R.id.txt1);
            textView2 = (TextView) itemView.findViewById(R.id.txt2);
            textView3 = (TextView) itemView.findViewById(R.id.txt3);
            textView4 = (TextView) itemView.findViewById(R.id.txt4);

        }
    }
}
