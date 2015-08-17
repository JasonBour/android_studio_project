package com.example.administrator.biyao.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.biyao.R;

/**
 * Created by Administrator on 2015/8/16.
 */
public class Fragment2  extends Fragment {
  public  View view ;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return  view  = inflater.inflate(R.layout.fragment2, container, false);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
      TextView textView =(TextView)view.findViewById(R.id.text);


	}

}
