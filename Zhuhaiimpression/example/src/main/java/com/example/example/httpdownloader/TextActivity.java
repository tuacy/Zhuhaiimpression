package com.example.example.httpdownloader;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.example.R;
import com.example.example.base.MobileBaseActivity;
import com.example.example.networkstate.NetworkUtils;

public class TextActivity extends MobileBaseActivity {

	private static final String DEST_DIR_PATH = Environment.getExternalStorageDirectory().toString() + "/G-SmartRouter/download/";

	private ProgressBar mProgressBar1;
	private Button      mBtnStart1;
	private Button      mBtnStop1;
	private ProgressBar mProgressBar2;
	private Button      mBtnStart2;
	private Button      mBtnStop2;
	private ProgressBar mProgressBar3;
	private Button      mBtnStart3;
	private Button      mBtnStop3;
	private ProgressBar mProgressBar4;
	private Button      mBtnStart4;
	private Button      mBtnStop4;
	private ProgressBar mProgressBar5;
	private Button      mBtnStart5;
	private Button      mBtnStop5;
	private Button      mBtnStartAll;
	private Button      mBtnStopAll;

	private DownloadRequest mRequest1;
	private DownloadRequest mRequest2;
	private DownloadRequest mRequest3;
	private DownloadRequest mRequest4;
	private DownloadRequest mRequest5;
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
		mProgressBar1 = (ProgressBar) findViewById(R.id.progress_download1);
		mBtnStart1 = (Button) findViewById(R.id.btn_start_download1);
		mBtnStop1 = (Button) findViewById(R.id.btn_stop_download1);
		mProgressBar2 = (ProgressBar) findViewById(R.id.progress_download2);
		mBtnStart2 = (Button) findViewById(R.id.btn_start_download2);
		mBtnStop2 = (Button) findViewById(R.id.btn_stop_download2);
		mProgressBar3 = (ProgressBar) findViewById(R.id.progress_download3);
		mBtnStart3 = (Button) findViewById(R.id.btn_start_download3);
		mBtnStop3 = (Button) findViewById(R.id.btn_stop_download3);
		mProgressBar4 = (ProgressBar) findViewById(R.id.progress_download4);
		mBtnStart4 = (Button) findViewById(R.id.btn_start_download4);
		mBtnStop4 = (Button) findViewById(R.id.btn_stop_download4);
		mProgressBar5 = (ProgressBar) findViewById(R.id.progress_download5);
		mBtnStart5 = (Button) findViewById(R.id.btn_start_download5);
		mBtnStop5 = (Button) findViewById(R.id.btn_stop_download5);

		mBtnStartAll = (Button) findViewById(R.id.btn_start_all);
		mBtnStopAll = (Button) findViewById(R.id.btn_stop_all);
	}

	private void initData() {
		mManager = DownloadManager.getInstance(3);
		mRequest1 = new DownloadRequest().setUrl("http://219.235.31.61/sw/119/5/X10_BETA_V1.09.12203_30-05-2014.bin")
										 .setDestDirectory(DEST_DIR_PATH)
										 .setProgressInterval(1000);
		mRequest1.setDownloadListener(new CustomerDownloadListener(mProgressBar1));
		mRequest1.setPriority(DownloadRequest.Priority.HIGH);
		mRequest2 = new DownloadRequest().setUrl("http://219.235.31.61/sw/197/25/Miuibox_Champion_V1.10_23-07-2015.bin")
										 .setDestDirectory(DEST_DIR_PATH)
										 .setProgressInterval(1000);
		mRequest2.setDownloadListener(new CustomerDownloadListener(mProgressBar2));
		mRequest2.setPriority(DownloadRequest.Priority.LOW);
		mRequest3 = new DownloadRequest().setUrl("http://219.235.31.61/sw/119/5/X100_Super_V1.09.12203_30-05-2014.bin")
										 .setDestDirectory(DEST_DIR_PATH)
										 .setProgressInterval(1000);
		mRequest3.setDownloadListener(new CustomerDownloadListener(mProgressBar3));
		mRequest4 = new DownloadRequest().setUrl("http://219.235.31.61/sw/119/5/X100_Super_V1.09.12397_19-06-2014.bin")
										 .setDestDirectory(DEST_DIR_PATH)
										 .setProgressInterval(1000);
		mRequest4.setDownloadListener(new CustomerDownloadListener(mProgressBar4));
		mRequest5 = new DownloadRequest().setUrl("http://219.235.31.61/sw/189/26/MAGNUM_MR-2500HD_V1.21_26-02-2014.bin")
										 .setDestDirectory(DEST_DIR_PATH)
										 .setProgressInterval(1000);
		mRequest5.setDownloadListener(new CustomerDownloadListener(mProgressBar5));
		mRequest5.setPriority(DownloadRequest.Priority.HIGH);
	}

	private void initEvent() {

		mBtnStart1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int id = mManager.add(mRequest1);
				Log.d("vae_tag", id + "   id");
			}
		});
		mBtnStop1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		mBtnStart2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mManager.add(mRequest2);
			}
		});
		mBtnStop2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		mBtnStart3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mManager.add(mRequest3);
			}
		});
		mBtnStop3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		mBtnStart4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mManager.add(mRequest4);
			}
		});
		mBtnStop4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		mBtnStart5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mManager.add(mRequest5);
			}
		});
		mBtnStop5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		mBtnStartAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mManager.add(mRequest1);
				mManager.add(mRequest2);
				mManager.add(mRequest3);
				mManager.add(mRequest4);
				mManager.add(mRequest5);

			}
		});
		mBtnStopAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
	}

	private class CustomerDownloadListener implements DownloadListener {

		public CustomerDownloadListener(ProgressBar progressBar) {
			mProgressBar = progressBar;
		}

		private ProgressBar mProgressBar;

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
			Log.d("vae_tag", " bytesWritten:" + bytesWritten + " rota:" + ((bytesWritten * 1.0f / totalBytes) * 100));
			mProgressBar.setProgress((int) ((bytesWritten * 1.0f / totalBytes) * 100));
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
