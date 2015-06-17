package com.shengxun.customview;
import android.app.Activity;
import android.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.shengxun.cashiersystem.R;
import com.shengxun.cashiersystem.SettingActivity.UpdatePsdListener;
public class LDialog {
	/**
	 * 普通Dialog
	 * @param context
	 */
	public static void openMessageDialog(String title,String content,boolean isHasCancel,boolean isHasEditText,String etHint,String donetext,final Activity mActivity,UpdatePsdListener callback)
	{
		final UpdatePsdListener mCallback = callback;
		final AlertDialog alertDialog = new AlertDialog.Builder(mActivity).create();
		alertDialog.setCancelable(true);
		alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		alertDialog.show();
		alertDialog.getWindow().setContentView(R.layout.dialog_layout);
		alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM); 
		alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		final EditText dailog_edittext = (EditText) alertDialog.findViewById(R.id.dailog_edittext);
		OnClickListener MyClick  = new OnClickListener() {
			@Override
			public void onClick(View v)
			{
				switch(v.getId())
				{
				case R.id.okView:
					if(mCallback!=null){
						mCallback.callBack(dailog_edittext.getText().toString().trim(), alertDialog);
					}else{
						alertDialog.dismiss();
					}
					break;
				case R.id.cancleView:
					alertDialog.dismiss();
					break;
				}
			}
		};
		
		final TextView okView = (TextView) alertDialog.findViewById(R.id.okView);
		okView.setOnClickListener(MyClick);
		final TextView dailog_title = (TextView) alertDialog.findViewById(R.id.dailog_title);
		final TextView dailog_content = (TextView) alertDialog.findViewById(R.id.dailog_content);
		dailog_content.setMovementMethod(new ScrollingMovementMethod());
		if(title!=null&&!title.equals("")){
			dailog_title.setText(""+title);
		}
		if(content!=null&&!content.equals("")){
			dailog_content.setText(""+content);
		}
		if(isHasEditText){
			dailog_edittext.setVisibility(View.VISIBLE);
			dailog_content.setVisibility(View.GONE);
			dailog_edittext.setHint(etHint);
			okView.setText(donetext);
		}else{
			dailog_edittext.setVisibility(View.GONE);
			dailog_content.setVisibility(View.VISIBLE);
		}
		final TextView cancleView = (TextView) alertDialog.findViewById(R.id.cancleView);
		if(isHasCancel){
			cancleView.setVisibility(View.VISIBLE);
			cancleView.setOnClickListener(MyClick);
		}else{
			cancleView.setVisibility(View.GONE);
		}
		
	}
	
	public static void openMessageDialog(String content,boolean isHasCancel,final Activity mActivity){
		openMessageDialog(null, content, isHasCancel,false,null,null, mActivity,null);
	}
}
