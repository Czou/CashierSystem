package com.shengxun.customview;
import android.app.Activity;
import android.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.shengxun.cashiersystem.R;
public class LDialog {
	/**
	 * 普通Dialog
	 * @param context
	 */
	public static void openMessageDialog(String title,String content,boolean isHasCancel,final Activity mActivity)
	{
		
		final AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
		alertDialog.setCancelable(true);
		alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		alertDialog.show();
		alertDialog.getWindow().setContentView(R.layout.dialog_layout);
		OnClickListener onClickListener = new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				switch(v.getId())
				{
				case R.id.okView:
					alertDialog.dismiss();
					break;
				case R.id.cancleView:
					alertDialog.dismiss();
					break;
				}
			}
		};
		final TextView okView = (TextView) alertDialog.findViewById(R.id.okView);
		okView.setOnClickListener(onClickListener);
		final TextView dailog_title = (TextView) alertDialog.findViewById(R.id.dailog_title);
		final TextView dailog_content = (TextView) alertDialog.findViewById(R.id.dailog_content);
		dailog_content.setMovementMethod(new ScrollingMovementMethod());
		if(title!=null&&!title.equals("")){
			dailog_title.setText(""+title);
		}
		if(content!=null&&!content.equals("")){
			dailog_content.setText(""+content);
		}
		final TextView cancleView = (TextView) alertDialog.findViewById(R.id.cancleView);
		if(isHasCancel){
			cancleView.setVisibility(View.VISIBLE);
			cancleView.setOnClickListener(onClickListener);
		}else{
			cancleView.setVisibility(View.GONE);
		}
		
	}
	
	public static void openMessageDialog(String content,boolean isHasCancel,final Activity mActivity){
		openMessageDialog(null, content, isHasCancel, mActivity);
	}
}
