package rsarapp.com.Common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import rsarapp.com.rsarapp.R;


public class GridViewAdapterClass extends BaseAdapter {

	//Context
	Context context;

	ArrayList<String> C_ID;
	ArrayList<String> C_Name;

	private static LayoutInflater inflater=null;
	public GridViewAdapterClass(Context context, ArrayList<String> C_id,
                                ArrayList<String> C_name) {
		this.context = context;
		this.C_ID = C_id;
		this.C_Name = C_name;
		inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		System.out.println("data adapter" + "  " + C_id + "  " + C_name);
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		System.out.println("Valueeeecount" + " " + C_ID.size());

		return C_ID.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}


	public class Holder {
		TextView os_text;

	}
	@SuppressLint("ViewHolder")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=new Holder();
		View rowView;

		rowView = inflater.inflate(R.layout.gridview, null);
		holder.os_text =(TextView) rowView.findViewById(R.id.textView1);


		holder.os_text.setText(C_Name.get(position));



		return rowView;
	}





	/*@Override
	public View getView(final int position, final View convertView, final ViewGroup parent) {

		View view = convertView;

		if (view == null) {
			view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.gridview, parent, false);
		}

		final TextView text = (TextView) view.findViewById(android.R.id.text1);

		text.setText(C_Name.get(position));

		return view;
	}*/




	/*@SuppressLint("NewApi") @Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View grid;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);





		if (convertView == null) {



			grid = new View(context);
			grid = inflater.inflate(R.layout.gridview, null);
			TextView Class_Name = (TextView) grid.findViewById(R.id.textView1);

            System.out.println("Valueeee"+" "+C_Name.get(position));
			Class_Name.setText(C_Name.get(position).toString());

		} else {
			grid = (View) convertView;
		}

		return grid;


	}*/

}
