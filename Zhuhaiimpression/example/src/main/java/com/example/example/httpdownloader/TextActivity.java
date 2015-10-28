package com.example.example.httpdownloader;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.example.R;
import com.example.example.base.MobileBaseActivity;
import com.example.example.networkstate.NetworkUtils;

public class TextActivity extends MobileBaseActivity {

	private static final String DEST_DIR_PATH = Environment.getExternalStorageDirectory().toString() + "/G-SmartRouter/download/";

	private Button mBtnTest;
	private Button mBtnStop;
	private DownloadRequest mRequest0;
	private DownloadRequest mRequest1;
	private DownloadRequest mRequest2;
	private DownloadRequest mRequest3;
	private DownloadRequest mRequest4;
	private DownloadManager mManager;

	@Override
	protected void onNetworkConnected(NetworkUtils.NetworkType type) {

	}

	@Override
	protected void onNetworkDisConnected() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		initView();
		initData();
		initEvent();
	}

	private void initView() {
		mBtnTest = (Button) findViewById(R.id.btn_test);
		mBtnStop = (Button) findViewById(R.id.btn_test_stop);
	}

	private void initData() {
		mManager = DownloadManager.getInstance(3);
	}

	private void initEvent() {
		mRequest0 = new DownloadRequest().setUrl("http://219.235.31.61/sw/189/26/MAGNUM_MR-2500HD_V1.21_26-02-2014.bin")
										 .setDestDirectory(DEST_DIR_PATH)
										 .setProgressInterval(1000);
		mRequest0.setDownloadListener(new CustomerDownloadListener());
		mRequest0.setPriority(DownloadRequest.Priority.HIGH);
		mRequest1 = new DownloadRequest().setUrl("http://219.235.31.61/sw/119/5/X10_BETA_V1.09.12203_30-05-2014.bin")
										 .setDestDirectory(DEST_DIR_PATH)
										 .setProgressInterval(1000);
		mRequest1.setDownloadListener(new CustomerDownloadListener());
		mRequest1.setPriority(DownloadRequest.Priority.HIGH);
		mRequest2 = new DownloadRequest().setUrl("http://219.235.31.61/sw/197/25/Miuibox_Champion_V1.10_23-07-2015.bin")
										 .setDestDirectory(DEST_DIR_PATH)
										 .setProgressInterval(1000);
		mRequest2.setDownloadListener(new CustomerDownloadListener());
		mRequest2.setPriority(DownloadRequest.Priority.LOW);
		mRequest3 = new DownloadRequest().setUrl("http://219.235.31.61/sw/119/5/X100_Super_V1.09.12203_30-05-2014.bin")
										 .setDestDirectory(DEST_DIR_PATH)
										 .setProgressInterval(1000);
		mRequest3.setDownloadListener(new CustomerDownloadListener());
		mRequest4 = new DownloadRequest().setUrl("http://219.235.31.61/sw/119/5/X100_Super_V1.09.12397_19-06-2014.bin")
										 .setDestDirectory(DEST_DIR_PATH)
										 .setProgressInterval(1000);
		mRequest4.setDownloadListener(new CustomerDownloadListener());
		mBtnTest.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mManager.add(mRequest0);
				mManager.add(mRequest1);
				mManager.add(mRequest2);
				mManager.add(mRequest3);
				mManager.add(mRequest4);

			}
		});
		mBtnStop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/** get download queue */

			}
		});
	}

	private class CustomerDownloadListener implements DownloadListener {

		@Override
		public void onStart(int downloadId, String url, String filePath, long totalBytes) {
			Log.d("vae_tag", "onStart url:" + url);
		}

		@Override
		public void onRetry(int downloadId, String url, String filePath) {
			Log.d("vae_tag", "onRetry url:" + url);
		}

		@Override
		public void onProgress(int downloadId, String url, String filePath, long bytesWritten, long totalBytes) {
//			Log.d("vae_tag", "onProgress url:" + url + " bytesWritten:" + bytesWritten);
		}

		@Override
		public void onSuccess(int downloadId, String url, String filePath) {
			Log.d("vae_tag", "onSuccess url:" + url);
		}

		@Override
		public void onFailure(int downloadId, String url, String filePath, int statusCode, String errMsg) {
			Log.d("vae_tag", "onFailure url:" + url);
		}

		@Override
		public void onCancel(int downloadId, String url, String filePath) {
			Log.d("vae_tag", "onCancel");
		}
	}
}
