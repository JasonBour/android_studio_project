package me.isming.meizitu.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Administrator on 2015/8/21.
 */
public class MyFragment extends BaseFragment implements View.OnClickListener {


	private static final String ARG_SECTION_NUMBER = "section_number";

	private TextView textView1,textView2,textView3;
   private Context context ;
	private  View  view ;

	public static MyFragment newInstance(int sectionNumber) {
		MyFragment fragment = new MyFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_my, container, false);
		context = getActivity();
        initView();
		return  view;
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((AppMainActivity) activity).onSectionAttached(
				getArguments().getInt(ARG_SECTION_NUMBER));
	}


	public void initView( ){
		textView1 = (TextView)view.findViewById(R.id.text1);
		textView1.setOnClickListener(this);
		textView2 = (TextView)view.findViewById(R.id.text2);
		textView2.setOnClickListener(this);
		textView3 =(TextView)view.findViewById(R.id.text3);
		textView3.setOnClickListener(this);


	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case  R.id.text1:
				Toast.makeText(context,"you have click the first one",Toast.LENGTH_SHORT).show();
                break;
			case R.id.text2:
				Toast.makeText(context,"you have click the second one",Toast.LENGTH_SHORT).show();
                break;
			case R.id.text3:
		 		Toast.makeText(context,"you have click the third one",Toast.LENGTH_SHORT).show();
		        break;
			default: break;
		}


	}
}
