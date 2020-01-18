package com.ustb.page;

import java.util.HashMap;
import java.util.Map;

import com.ustb.application.MyApplication;
import com.ustb.callback.Callback;
import com.ustb.http.connection.HttpServer;
import com.ustb.model.BeanData;
import com.ustb.model.CommentData;
import com.ustb.parser.CommentParser;
import com.ustb.school.R;
import com.ustb.school.R.layout;
import com.ustb.school.R.menu;
import com.ustb.status.StatusCode;
import com.ustb.url.URLConstants;
import com.ustb.utils.ExampleUtil;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Other_Comment extends Activity implements OnClickListener {
	private LinearLayout layout;
	private Button btn_submit, btn_cancel;
	private EditText editText;
	private int cardid;
	private MyApplication app;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other_comment);
		init();
	}

	private void init() {
		layout = (LinearLayout) findViewById(R.id.comment_ly);
		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		btn_submit = (Button) findViewById(R.id.comment_submit);
		btn_cancel = (Button) findViewById(R.id.comment_cancel);
		btn_submit.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
		editText = (EditText) findViewById(R.id.comment_edit);

		cardid = getIntent().getIntExtra("cardid", 0);
		app = (MyApplication) getApplication();
	}

	// 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.comment_submit:
			addComment();
			break;
		case R.id.comment_cancel:
			break;

		}
		finish();

	}

	private void addComment() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "addComment");
		params.put("cardid", String.valueOf(cardid));
		params.put("mess", editText.getText().toString());
		params.put("userid", String.valueOf(app.getUser().getId()));
		HttpServer.setPostRequest(params, new CommentParser(),
				URLConstants.BASEURL + URLConstants.COMMENTURL, new Callback() {

					@Override
					public void success(BeanData beanData) {
						CommentData commentData = (CommentData) beanData;
						if (commentData.getCode() == StatusCode.Common.SUCCESS) {
							if (commentData.getFlag() == StatusCode.Dao.INSERT_SUCCESS) {
								ExampleUtil.showToast("评论成功", Other_Comment.this);
							} else {
								ExampleUtil.showToast("评论失败", Other_Comment.this);
							}
						} else {
							ExampleUtil.showToast("服务器繁忙", Other_Comment.this);
						}
					}

					@Override
					public void fail(String error) {
						ExampleUtil.showToast(error, Other_Comment.this);

					}
				});
	}

}
