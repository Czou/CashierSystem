package com.shengxun.cashiersystem;

import com.shengxun.constant.C;
import com.shengxun.util.AndroidAdjustResizeUtil;
import com.zvezda.android.utils.AppManager;
import com.zvezda.android.utils.BaseUtils;
import com.zvezda.android.utils.LG;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LockActivity extends BaseActivity implements OnClickListener {
	// 解锁密码输入框
	private EditText unlock_psd;
	// 解锁按钮与重置按钮
	private TextView btn_unlock, btn_reset;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		setContentView(R.layout.lock_activity);
		AndroidAdjustResizeUtil.assistActivity(mActivity);
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		unlock_psd = (EditText) findViewById(R.id.lock_activity_unlock_psd);
		btn_reset = (TextView) findViewById(R.id.lock_activity_reset);
		btn_unlock = (TextView) findViewById(R.id.lock_activity_unlock);

		btn_reset.setOnClickListener(this);
		btn_unlock.setOnClickListener(this);

	}

	/**
	 * 解锁
	 * @auth shouwei
	 */
	private void unlock() {
		String psd = unlock_psd.getText().toString().trim();
		LG.i(getClass(), "=================psd ============"+psd+"===C.CURRENT_PSD"+C.CURRENT_LOCK_PSD);
		if (BaseUtils.IsNotEmpty(psd)) {
			if (!BaseUtils.IsNotEmpty(C.CURRENT_LOCK_PSD)) {
			}
			// 匹配密码
			if (psd.equals(C.CURRENT_LOCK_PSD)) {
				setResult(RESULT_OK);
				AppManager.getAppManager().finishActivity(mActivity);
			} else {
				C.showDialogAlert("密码错误", mActivity);
			}
		} else {
			C.showDialogAlert("解锁密码不能为空！", mActivity);
		}
	}
	/**
	 * 屏蔽back键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_HOME){
			C.showLong("按下HOME键", mActivity);
		}else if(keyCode == KeyEvent.KEYCODE_BACK){
			C.showLong("按下BACK键", mActivity);
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lock_activity_reset:
			unlock_psd.setText("");
			break;
		case R.id.lock_activity_unlock:
			unlock();
			break;
		default:
			break;
		}
	}

}
