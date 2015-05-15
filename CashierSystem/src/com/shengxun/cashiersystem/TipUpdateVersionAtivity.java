package com.shengxun.cashiersystem;
import java.io.File;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.shengxun.constant.C;
import com.shengxun.util.CacheM;
import com.zvezda.android.utils.AppManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.http.utils.HttpConst;
import com.zvezda.http.utils.RequestFileRunnable;
import com.zvezda.http.utils.RequestHttpListener;
/**
 * 模块描述：提示有版本更新
 * 2014-9-20 下午4:21:15
 * Write by LILIN
 */
public class TipUpdateVersionAtivity extends BaseActivity
{
	/**
	 * 下载的APP路径
	 */
	private String appPath = null;
	/**
	 * 现在的路径
	 */
	private String downloadUrl = null;
	/**
	 * 下载进度
	 */
	private TextView progressView = null;
	/**
	 * 更新提示
	 */
	private TextView tipView = null;
	
	/**
	 * 是否必须更新
	 */
	private String must_update="0";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		setContentView(R.layout.tip_update_version_layout);
		TextView okView = (TextView)findViewById(R.id.okView);
		okView.setOnClickListener(onClickListener);
		TextView cancleView = (TextView)findViewById(R.id.cancleView);
		cancleView.setOnClickListener(onClickListener);
		downloadUrl = getIntent().getStringExtra("download_url");
		must_update = getIntent().getStringExtra("must_update");
		tipView = (TextView)findViewById(R.id.tipView);
		tipView.setText(Html.fromHtml(getIntent().getStringExtra("function")));
		tipView.setMovementMethod(LinkMovementMethod.getInstance());
		appPath = CacheM.getDownloadApkCachePath(mActivity);
	}
	/**
	 * 点击事件
	 */
	private OnClickListener onClickListener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			switch(v.getId())
			{
			case R.id.okView:
				doDownload(downloadUrl);
				break;
			case R.id.cancleView:
				if(must_update.equals("0")){
					finish();
				}else{
					AppManager.getAppManager().AppExit(mActivity);
				}
				break;
				default:
					break;
			}
		}
	};
	/**
	 * 下载文件的接口
	 */
	private RequestFileRunnable requestFileRunnable = null;
	
	
	/**
	 * 开始下载
	 * @param url
	 */
	private void doDownload(String url)
	{
		String apkPath = BaseUtils.getFilePath(appPath, url);
		if(new File(apkPath).exists())
		{
			installAPK(apkPath);
		}
		else
		{
			C.openProgressDialog(this, onKeyListener, R.string.downloading);
			requestFileRunnable = new RequestFileRunnable(requestHttpListener, url, apkPath);
			new Thread(requestFileRunnable).start();
			
		}
	}
	/**
	 * 等待框的点击事件
	 */
	private OnKeyListener onKeyListener = new OnKeyListener()
	{
		
		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
		{
			if(keyCode == KeyEvent.KEYCODE_BACK)
			{
				if(requestFileRunnable != null)
				{
					requestFileRunnable.setRequestHttpListener(null);
				}
			}
			return false;
		}
	};
	/**
	 * 等待框
	 */
//	private AlertDialog customProgressDialog = null;
//	/**
//	 * 打开等待框
//	 * @param context
//	 * @param onKeyListener
//	 * @param object
//	 */
//	private void openProgressDialog(Context context, OnKeyListener onKeyListener, Object object)
//	{
//		if (customProgressDialog != null && customProgressDialog.isShowing())
//		{
//			return;
//		}
//		customProgressDialog = new AlertDialog.Builder(context).create();
//		customProgressDialog.setCancelable(false);
//		customProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		customProgressDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		customProgressDialog.show();
//		customProgressDialog.getWindow().setContentView(R.layout.download_pragress_dialog_layout);
//		customProgressDialog.setCancelable(true);
//		if (onKeyListener != null)
//		{
//			customProgressDialog.setOnKeyListener(onKeyListener);
//		}
//		final TextView infoView = (TextView) customProgressDialog.findViewById(R.id.dialogInfo);
//		if (object instanceof Integer)
//		{
//			infoView.setText((Integer) object);
//		} 
//		else if (object instanceof String)
//		{
//			infoView.setText((String) object);
//		}
//		progressView = (TextView)customProgressDialog.findViewById(R.id.progressView);
//	}
//
//	/**
//	 * 关闭等待框
//	 */
//	private void closeProgressDialog()
//	{
//		if (customProgressDialog != null)
//		{
//			customProgressDialog.cancel();
//			customProgressDialog = null;
//		}
//	}
	/**
	 * 数据请求监听
	 */
	private RequestHttpListener requestHttpListener = new RequestHttpListener()
	{
		
		@Override
		public void requestHttp(Message msg)
		{
			handler.sendMessage(msg);
		}
	};
	private void installAPK(String apkPath)
	{
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + apkPath), "application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			int result = bundle.getInt(HttpConst.ACTION_RESULT, HttpConst.ACTION_FAIL);
			switch(result)
			{
			case HttpConst.ACTION_TRANSFER:
				long downloadLength = bundle.getLong(HttpConst.CURRENT_LENGTH, 0);
				long totalLength = bundle.getLong(HttpConst.TOTAL_LENGTH, 1);
				float progress = (float)downloadLength/totalLength;
				if(progress >= 1.0f)
				{
					progress = 1.0f;
				}
				progressView.setText((int)(progress*100)+"%");
				break;
			case HttpConst.ACTION_SUCCESS:
				C.closeProgressDialog();
				String apkPath =  bundle.getString(HttpConst.FILE_PATH);
				installAPK(apkPath);
				finish();
				break;
			case HttpConst.ACTION_FAIL:
				C.closeProgressDialog();
				C.showShort(resources.getString(R.string.downloadFail) ,mActivity);
				break;
				default:
					break;
			}
		}
	};
}
