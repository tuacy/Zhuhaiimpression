package com.example.example.httpdownloader;

public class DownloadManager {

	/**
	 * custom http code invalid
	 */
	public static final int HTTP_INVALID = 1;

	/**
	 * custom http code error size
	 */
	public static final int HTTP_ERROR_SIZE = 1 << 1;

	/**
	 * custom http code error network
	 */
	public static final int HTTP_ERROR_NETWORK = 1 << 2;

	/**
	 * range not satisfiable
	 */
	public static final int HTTP_REQUESTED_RANGE_NOT_SATISFIABLE = 416;

	private static DownloadManager mInstance = null;

	private DownloadDispatcher mDownloadDispatcher = null;

	private DownloadManager(int threadPoolThreadMax) {
		mDownloadDispatcher = new DownloadDispatcher(threadPoolThreadMax);
	}

	public static DownloadManager getInstance() {
		if (null == mInstance) {
			synchronized (DownloadManager.class) {
				if (null == mInstance) {
					mInstance = new DownloadManager(DownloadRequestQueue.DEFAULT_THREAD_POOL_THREAD_COUNT);
				}
			}
		}
		return mInstance;
	}

	public static DownloadManager getInstance(final int threadPoolThreadMax) {
		if (null == mInstance) {
			synchronized (DownloadManager.class) {
				if (null == mInstance) {
					mInstance = new DownloadManager(threadPoolThreadMax);
				}
			}
		}
		return mInstance;
	}

	public int add(DownloadRequest request) {
		if (request == null) {
			throw new IllegalArgumentException("DownloadRequest cannot be null");
		}
		return mDownloadDispatcher.add(request);
	}
}
