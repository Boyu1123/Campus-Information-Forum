package com.ustb.page;

import com.ustb.school.R;
import com.ustb.video.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class Fragment_3_1 extends Fragment implements OnClickListener {
	private LinearLayout l1, l2, l3, l4;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.from(getActivity()).inflate(R.layout.fragment_3_1,
				null);
		init(view);
		return view;
	}

	private void init(View view) {
		l1 = (LinearLayout) view.findViewById(R.id.f31_new);
		l2 = (LinearLayout) view.findViewById(R.id.f31_top);
		l3 = (LinearLayout) view.findViewById(R.id.f31_end);
		l4 = (LinearLayout) view.findViewById(R.id.f31_start);
		l1.setOnClickListener(this);
		l2.setOnClickListener(this);
		l3.setOnClickListener(this);
		l4.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.f31_new:
			Intent i1 = new Intent(getActivity(), MainActivity.class);
			startActivity(i1);
			break;
		case R.id.f31_top:
			Intent i2 = new Intent(getActivity(), Activity_Top.class);
			startActivity(i2);
			break;
		case R.id.f31_end:
			Intent i3 = new Intent(getActivity(), Activity_New.class);
			startActivity(i3);
			break;
		case R.id.f31_start:
			Intent i4 = new Intent(getActivity(), Activity_Start.class);
			startActivity(i4);
			break;

		}
	}
}
