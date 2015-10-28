package com.example.example.httpdownloader;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class DownloadDispatcher {

	private static final String TAG = DownloadRequestQueue.class.getSimpleName();

	private static final int MESSAGE_ADD_NEW_TASK = 0x000;

	/**
	 * Internal task scheduling Thread + Loop + Handler
	 */
	private Thread          mPoolThread                 = null;
	private Handler         mPoolThreadHandler          = null;
	/**
	 * Make sure Handler init
	 */
	private ExecutorService mThreadPool                 = null;
	private Semaphore       mSemaphorePoolThreadHandler = new Semaphore(0);
	private Semaphore       mSemaphoreThreadPool        = null;

	/**
	 * Queue
	 */
	private DownloadRequestQueue mDownloadRequestQueue = null;
	private DownloadDelivery     mDownloadDelivery     = null;

	public DownloadDispatcher(int threadCount) {
		mThreadPool = Executors.newFixedThreadPool(threadCount);
		mSemaphoreThreadPool = new Semaphore(threadCount);
		mDownloadRequestQueue = new DownloadRequestQueue(this);
		mDownloadDelivery = new DownloadDelivery(new Handler(Looper.getMainLooper()));
		initInternalScheduling();
	}

	public DownloadDispatcher(int threadCount, int queueMaxCount) {
		mThreadPool = Executors.newFixedThreadPool(threadCount);
		mSemaphoreThreadPool = new Semaphore(threadCount);
		mDownloadRequestQueue = new DownloadRequestQueue(this, queueMaxCount);
		mDownloadDelivery = new DownloadDelivery(new Handler(Looper.getMainLooper()));
		initInternalScheduling();
	}

	private void initInternalScheduling() {
		mPoolThread = new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				mPoolThreadHandler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						switch (msg.what) {
							case MESSAGE_ADD_NEW_TASK:
								/** get form queue */
								DownloadRequest request = mDownloadRequestQueue.dequeue();
								if (null != request) {
									/** add to downloading Set list */
									mDownloadRequestQueue.startRequest(request);
									DownloadRunnable downloadRunnable = new DownloadRunnable(request, mDownloadDelivery);
									mThreadPool.execute(downloadRunnable);
									acquireThreadPoolSemaphore();
								}
								break;
						}
					}
				};
				mSemaphorePoolThreadHandler.release();
				Looper.loop();
			}
		};
		mPoolThread.start();
	}


	public int add(DownloadRequest request) {
		if (request == null) {
			throw new IllegalArgumentException("DownloadRequest cannot be null");
		}

		/** if download id is not set, generate one */
		if (request.getDownloadId() == -1) {
			int downloadId = mDownloadRequestQueue.getSequenceNumber();
			request.setDownloadId(downloadId);
		}

		/** add download request into download request queue */
		if (mDownloadRequestQueue.enqueue(request)) {
			/** make sure Handler init */
			if (null == mPoolThreadHandler) {
				try {
					mSemaphorePoolThreadHandler.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			/** send message to internal scheduling add a new task */
			mPoolThreadHandler.sendEmptyMessage(MESSAGE_ADD_NEW_TASK);
			return request.getDownloadId();
		} else {
			return -1;
		}
	}

	public void acquireThreadPoolSemaphore() {
		if (null != mSemaphoreThreadPool) {
			try {
				mSemaphoreThreadPool.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void releaseThreadPoolSemaphore() {
		if (null != mSemaphoreThreadPool) {
			mSemaphoreThreadPool.release();
		}
	}

	public void exit() {
		if (null != mPoolThread) {
			mPoolThread.interrupt();
		}
	}
}
