package com.example.administrator.biyao;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;

import com.example.administrator.biyao.Fragments.Fragment1;
import com.example.administrator.biyao.Fragments.Fragment2;
import com.example.administrator.biyao.Fragments.Fragment3;

public class MainActivity extends Activity {


public static FragmentManager fragmentManager ;
public Fragment fragment1 ,fragment2,fragment3 ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fragmentManager=getFragmentManager();
		initFragments();
		buttonLiatener();
	}




	 public void initFragments(){
		 FragmentTransaction  fragmentTransaction = fragmentManager.beginTransaction();
		 fragment1 = new Fragment1();
         fragmentTransaction.add(R.id.FragmentsContainer, fragment1, "Fragment1");
		 fragmentTransaction.addToBackStack("Fragment1");

		 fragmentTransaction.commit();

	 }

    public void buttonLiatener(){
		findViewById(R.id.Fragment1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (fragmentManager.findFragmentByTag("weiXinFragment") != null && fragmentManager.findFragmentByTag("weiXinFragment").isVisible()) {
					return;
				}
				popAllFragmentsExceptTheBottomOne();

			}
		});

		findViewById(R.id.Fragment2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popAllFragmentsExceptTheBottomOne();
				fragment2 = new Fragment2();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.hide(fragmentManager.findFragmentByTag("Fragment1"));
				fragmentTransaction.add(R.id.FragmentsContainer, fragment2, "Fragment2");
				fragmentTransaction.addToBackStack("Fragment2");
				fragmentTransaction.commit();
			}
		});
		findViewById(R.id.Fragment3).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popAllFragmentsExceptTheBottomOne();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragment3 = new Fragment3();
				fragmentTransaction.hide(fragmentManager.findFragmentByTag("Fragment1"));
				fragmentTransaction.add(R.id.FragmentsContainer, fragment3, "Fragment3");
				fragmentTransaction.addToBackStack("Fragment3");
				fragmentTransaction.commit();
			}
		});


	}

	/**
	 * 从back stack弹出所有的fragment，保留首页的那个
	 */
	public static void popAllFragmentsExceptTheBottomOne() {
		for (int i = 0, count = fragmentManager.getBackStackEntryCount() - 1; i < count; i++) {
			fragmentManager.popBackStack();
		}
	}
}
