package com.ustb.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.onekeyshare.OnekeyShare;

import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.ustb.adapter.CommentListViewAdapter;
import com.ustb.application.MyApplication;
import com.ustb.callback.Callback;
import com.ustb.entity.E_Card;
import com.ustb.entity.E_Comment;
import com.ustb.http.connection.HttpServer;
import com.ustb.model.BeanData;
import com.ustb.model.CollectData;
import com.ustb.model.CommentData;
import com.ustb.model.FriendData;
import com.ustb.parser.CardParser;
import com.ustb.parser.CollectParser;
import com.ustb.parser.CommentParser;
import com.ustb.parser.FriendParser;
import com.ustb.school.R;
import com.ustb.school.R.layout;
import com.ustb.school.R.menu;
import com.ustb.status.StatusCode;
import com.ustb.url.URLConstants;
import com.ustb.utils.ExampleUtil;
import com.ustb.utils.ImageUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class More_Card extends Activity implements OnClickListener {
	private Button btn_back;
	private TextView t_title, t_author, t_time, t_mess, t_num, t_caretext;
	private ImageView i_collect, i_imgview;
	private LinearLayout l_comment, l_care, l_share;
	private boolean isCollect = false;
	private E_Card card;
	private ListView listView;
	private ArrayList<E_Comment> list;
	private MyApplication app;
	private int id;
	private int sid = 0;
	private boolean isdelete = false;
	private boolean isfriend = false;
	private int friendtableid = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more_card);
		init();
	}

	private void init() {
		t_caretext = (TextView) findViewById(R.id.card_caretext);
		i_imgview = (ImageView) findViewById(R.id.card_imgview);
		l_comment = (LinearLayout) findViewById(R.id.card_comment);
		l_care = (LinearLayout) findViewById(R.id.card_care);
		l_share = (LinearLayout) findViewById(R.id.card_share);
		t_title = (TextView) findViewById(R.id.card_title);
		t_author = (TextView) findViewById(R.id.card_author);
		t_time = (TextView) findViewById(R.id.card_time);
		t_mess = (TextView) findViewById(R.id.card_mess);
		t_num = (TextView) findViewById(R.id.card_num);
		i_collect = (ImageView) findViewById(R.id.card_collect);
		btn_back = (Button) findViewById(R.id.back);
		btn_back.setOnClickListener(this);
		i_collect.setOnClickListener(this);
		l_comment.setOnClickListener(this);
		l_care.setOnClickListener(this);
		l_share.setOnClickListener(this);

		card = (E_Card) getIntent().getSerializableExtra("card");
		t_title.setText(card.getCardtitle());
		t_author.setText(card.getUsername());
		t_time.setText(card.getCarddate());
		t_mess.setText(card.getCardmess());
		t_num.setText(card.getNum() + "");

		ImageUtils.getBitmapUtils(this).display(i_imgview,
				URLConstants.BASEURL + card.getCardphoto1(),
				new BitmapLoadCallBack<View>() {

					@Override
					public void onLoadCompleted(View arg0, String arg1,
							Bitmap arg2, BitmapDisplayConfig arg3,
							BitmapLoadFrom arg4) {
						i_imgview.setImageBitmap(arg2);
					}

					@Override
					public void onLoadFailed(View arg0, String arg1,
							Drawable arg2) {
						i_imgview.setImageResource(R.drawable.g1);

					}
				});

		listView = (ListView) findViewById(R.id.cardcomment_listview);
		list = new ArrayList<E_Comment>();

		app = (MyApplication) getApplication();

		sid = getIntent().getIntExtra("sid", 100);
		addNum();
		if (app.isLoginCode()) {
			getCollect();
			getFirend();
		} else {

		}
		getComment();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.back:
			if (sid != 100) {
				if (isdelete) {
					Intent intent = new Intent("UPDATE_COLLECT");
					intent.putExtra("index", sid);
					sendBroadcast(intent);
				}
			}
			finish();

			break;
		case R.id.card_collect:
			if (isCollect) {
				// 取消收藏
				deleteCollect();
			} else {
				// 收藏
				addCollect();
			}
			break;
		case R.id.card_comment:

			if (app.isLoginCode()) {
				Intent i1 = new Intent(More_Card.this, Other_Comment.class);
				i1.putExtra("cardid", card.getCardid());
				startActivity(i1);
			} else {
				ExampleUtil.showToast("请先登录", More_Card.this);
			}
			break;
		case R.id.card_care:

			if (app.isLoginCode()) {
				if (isfriend) {
					deleteFriend();
				} else {
					addFriend();
				}
			} else {
				ExampleUtil.showToast("请先登录", More_Card.this);
			}

			break;
		case R.id.card_share:
			showShare();
			break;
		}

	}

	/**
	 * 添加好友
	 */
	private void addFriend() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "addFriend");
		params.put("userid", String.valueOf(app.getUser().getId()));
		params.put("friendid", String.valueOf(card.getUserid()));
		HttpServer.setPostRequest(params, new FriendParser(),
				URLConstants.BASEURL + URLConstants.FRIENDURL, new Callback() {

					@Override
					public void success(BeanData beanData) {
						FriendData friendData = (FriendData) beanData;
						if (friendData.getCode() == StatusCode.Common.SUCCESS) {
							if (friendData.getFlag() == StatusCode.Dao.INSERT_SUCCESS) {
								friendtableid = friendData.getId();
								t_caretext.setText("已关注");
								isfriend = true;
							} else {
								ExampleUtil.showToast("添加好友失败", More_Card.this);
							}
						} else {
							ExampleUtil.showToast("服务器繁忙", More_Card.this);
						}

					}

					@Override
					public void fail(String error) {
						ExampleUtil.showToast(error, More_Card.this);

					}
				});
	}

	/**
	 * 删除好友
	 */
	private void deleteFriend() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "deleteFriend");
		params.put("id", String.valueOf(friendtableid));
		HttpServer.setPostRequest(params, new FriendParser(),
				URLConstants.BASEURL + URLConstants.FRIENDURL, new Callback() {

					@Override
					public void success(BeanData beanData) {
						FriendData friendData = (FriendData) beanData;
						if (friendData.getCode() == StatusCode.Common.SUCCESS) {
							if (friendData.getFlag() == StatusCode.Dao.DELETE_SUCCESS) {
								t_caretext.setText("关注");
								isfriend = false;
							} else {
								ExampleUtil.showToast("取消关注好友失败",
										More_Card.this);
							}
						} else {
							ExampleUtil.showToast("服务器繁忙", More_Card.this);
						}

					}

					@Override
					public void fail(String error) {
						ExampleUtil.showToast(error, More_Card.this);

					}
				});
	}

	/**
	 * 获取当前是不是好友
	 */
	private void getFirend() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "isFriend");
		params.put("userid", String.valueOf(app.getUser().getId()));
		params.put("friendid", String.valueOf(card.getUserid()));
		HttpServer.setPostRequest(params, new FriendParser(),
				URLConstants.BASEURL + URLConstants.FRIENDURL, new Callback() {

					@Override
					public void success(BeanData beanData) {
						FriendData friendData = (FriendData) beanData;
						if (friendData.getCode() == StatusCode.Common.SUCCESS) {
							if (friendData.getFlag() == StatusCode.Dao.UPDATE_SUCCESS) {
								friendtableid = friendData.getId();
								t_caretext.setText("已关注");
								isfriend = true;
							} else {
								t_caretext.setText("关注");
								isfriend = false;
							}
						} else {
							ExampleUtil.showToast("服务器繁忙", More_Card.this);
						}

					}

					@Override
					public void fail(String error) {
						ExampleUtil.showToast(error, More_Card.this);

					}
				});

	}

	/**
	 * 添加收藏
	 */

	private void addCollect() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "addCollect");
		params.put("userid", String.valueOf(app.getUser().getId()));
		params.put("cardid", String.valueOf(card.getCardid()));
		HttpServer.setPostRequest(params, new CollectParser(),
				URLConstants.BASEURL + URLConstants.COLLECTURL, new Callback() {

					@Override
					public void success(BeanData beanData) {
						CollectData collectData = (CollectData) beanData;
						if (beanData.getCode() == StatusCode.Common.SUCCESS) {
							if (beanData.getFlag() == StatusCode.Dao.INSERT_SUCCESS) {
								i_collect.setSelected(true);
								isCollect = true;
								id = collectData.getId();
								isdelete = false;
							} else {
								ExampleUtil.showToast("收藏失败", More_Card.this);
							}
						} else {
							ExampleUtil.showToast("服务器繁忙", More_Card.this);
						}

					}

					@Override
					public void fail(String error) {
						ExampleUtil.showToast(error, More_Card.this);

					}
				});
	}

	/**
	 * 取消收藏
	 */
	private void deleteCollect() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "deleteCollect");
		params.put("id", String.valueOf(id));
		HttpServer.setPostRequest(params, new CollectParser(),
				URLConstants.BASEURL + URLConstants.COLLECTURL, new Callback() {

					@Override
					public void success(BeanData beanData) {
						if (beanData.getCode() == StatusCode.Common.SUCCESS) {
							if (beanData.getFlag() == StatusCode.Dao.DELETE_SUCCESS) {
								i_collect.setSelected(false);
								isCollect = false;
								isdelete = true;
							} else {
								ExampleUtil.showToast("取消收藏失败，请重试",
										More_Card.this);
							}
						} else {
							// ExampleUtil.showToast("服务器繁忙", More_Card.this);
						}

					}

					@Override
					public void fail(String error) {
						ExampleUtil.showToast(error, More_Card.this);

					}
				});
	}

	/**
	 * 获取是否收藏
	 */
	private void getCollect() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "getCollect");
		params.put("userid", String.valueOf(app.getUser().getId()));
		params.put("cardid", String.valueOf(card.getCardid()));
		HttpServer.setPostRequest(params, new CollectParser(),
				URLConstants.BASEURL + URLConstants.COLLECTURL, new Callback() {

					@Override
					public void success(BeanData beanData) {
						CollectData collectData = (CollectData) beanData;
						if (beanData.getCode() == StatusCode.Common.SUCCESS) {
							if (beanData.getFlag() == StatusCode.Dao.SELECT_SUCCESS) {
								id = collectData.getId();
								i_collect.setSelected(true);
								isCollect = true;
							} else {
								i_collect.setSelected(false);
								isCollect = false;
							}
						} else {
							// ExampleUtil.showToast("服务器繁忙", More_Card.this);
						}

					}

					@Override
					public void fail(String error) {
						ExampleUtil.showToast(error, More_Card.this);

					}
				});

	}

	/**
	 * 获取评论
	 */
	private void getComment() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "getComment");
		params.put("cardid", String.valueOf(card.getCardid()));
		HttpServer.setPostRequest(params, new CommentParser(),
				URLConstants.BASEURL + URLConstants.COMMENTURL, new Callback() {

					@Override
					public void success(BeanData beanData) {
						CommentData commentData = (CommentData) beanData;
						if (commentData.getCode() == StatusCode.Common.SUCCESS) {
							if (commentData.getFlag() == StatusCode.Dao.SELECT_SUCCESS) {
								list = commentData.getList();
								CommentListViewAdapter adapter = new CommentListViewAdapter(
										More_Card.this, list);
								listView.setAdapter(adapter);

							} else {
								ExampleUtil.showToast("获取评论繁忙", More_Card.this);
							}
						} else {
							ExampleUtil.showToast("服务器繁忙", More_Card.this);
						}
					}

					@Override
					public void fail(String error) {
						ExampleUtil.showToast(error, More_Card.this);

					}
				});

	}

	/**
	 * 增加浏览量
	 */
	private void addNum() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "addNum");
		params.put("cardid", String.valueOf(card.getCardid()));
		HttpServer.setPostRequest(params, new CardParser(),
				URLConstants.BASEURL + URLConstants.CARDURL, new Callback() {

					@Override
					public void success(BeanData beanData) {
						// TODO Auto-generated method stub

					}

					@Override
					public void fail(String error) {
						// TODO Auto-generated method stub

					}
				});

	}

	/**
	 * 分享
	 */

	private void showShare() {
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
		oks.setTitle(t_title.getText().toString());
		// titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText(t_mess.getText().toString());
		// 分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
		oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite("ShareSDK");
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);
	}

}
