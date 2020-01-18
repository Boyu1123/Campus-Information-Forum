package com.ustb.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ustb.adapter.FriendListViewAdapter;
import com.ustb.application.MyApplication;
import com.ustb.callback.Callback;
import com.ustb.entity.E_Friend;
import com.ustb.entity.E_User;
import com.ustb.http.connection.HttpServer;
import com.ustb.model.BeanData;
import com.ustb.model.FriendData;
import com.ustb.parser.FriendParser;
import com.ustb.school.R;
import com.ustb.status.StatusCode;
import com.ustb.url.URLConstants;
import com.ustb.utils.ExampleUtil;
import com.ustb.voice_rcd.NewChatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class Fragment_3_2 extends Fragment {
	private ListView listView;
	private MyApplication app;
	private ArrayList<E_Friend> list;
	private FriendListViewAdapter adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.from(getActivity()).inflate(R.layout.fragment_3_2,
				null);
		init(view);
		return view;
	}

	private void init(View view) {
		listView = (ListView) view.findViewById(R.id.friend_listview);
		list = new ArrayList<E_Friend>();

		app = (MyApplication) getActivity().getApplication();
		if (app.isLoginCode()) {
			getFriend();
			listView.setOnItemLongClickListener(itemLongClickListener);
			listView.setOnItemClickListener(listener);
		} else {
			E_Friend friend = new E_Friend();
			friend.setFriendhead(null);
			friend.setFriendname("请先登录");
			friend.setFriendinfo("请先登录");
			list.add(friend);
			FriendListViewAdapter adapter = new FriendListViewAdapter(
					getActivity(), list);
			listView.setAdapter(adapter);
		}
	}

	private void getFriend() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "getFriend");
		params.put("userid", String.valueOf(app.getUser().getId()));
		HttpServer.setPostRequest(params, new FriendParser(),
				URLConstants.BASEURL + URLConstants.FRIENDURL, new Callback() {

					@Override
					public void success(BeanData beanData) {
						FriendData data = (FriendData) beanData;
						if (data.getCode() == StatusCode.Common.SUCCESS) {
							if (data.getFlag() == StatusCode.Dao.SELECT_SUCCESS) {
								list = data.getList();
								adapter = new FriendListViewAdapter(
										getActivity(), list);
								listView.setAdapter(adapter);
								
							} else {
								ExampleUtil
										.showToast("获取好友列表失败", getActivity());
							}
						} else {
							ExampleUtil.showToast("服务器繁忙", getActivity());
						}

					}

					@Override
					public void fail(String error) {
						ExampleUtil.showToast(error, getActivity());
						
					}
				});
	}

	private OnItemLongClickListener itemLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			final E_Friend e_Friend = list.get(arg2);
			final String[] array = new String[] { "删除好友" };

			Dialog alertDialog = new AlertDialog.Builder(getActivity())

			.setItems(array, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which) {
					case 0:
						deleteFriend(e_Friend.getId(), which);
						break;

					default:
						break;
					}
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
				}
			}).create();
			alertDialog.show();
			return false;
		}
	};

	private OnItemClickListener listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			E_Friend e_Friend = list.get(arg2);
			Intent intent = new Intent(getActivity(), NewChatActivity.class);
			intent.putExtra("friend", e_Friend);
			startActivity(intent);
		}
	};

	/**
	 * 删除好友
	 */
	private void deleteFriend(int id, final int index) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "deleteFriend");
		params.put("id", String.valueOf(id));
		HttpServer.setPostRequest(params, new FriendParser(),
				URLConstants.BASEURL + URLConstants.FRIENDURL, new Callback() {

					@Override
					public void success(BeanData beanData) {
						FriendData friendData = (FriendData) beanData;
						if (friendData.getCode() == StatusCode.Common.SUCCESS) {
							if (friendData.getFlag() == StatusCode.Dao.DELETE_SUCCESS) {
								list.remove(index);
								adapter.notifyDataSetChanged();
							} else {
								ExampleUtil
										.showToast("删除好友失败", getActivity());
							}
						} else {
							ExampleUtil.showToast("服务器繁忙", getActivity());
						}

					}

					@Override
					public void fail(String error) {
						ExampleUtil.showToast(error, getActivity());

					}
				});
	}
}
