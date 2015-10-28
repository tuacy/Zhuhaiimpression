package com.example.example.httpdownloader;

import android.text.TextUtils;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class DownloadRequestQueue {

	private static final String TAG = DownloadRequestQueue.class.getSimpleName();

	public static final int DEFAULT_THREAD_POOL_THREAD_COUNT = 3;

	/**
	 * The default capacity of download request queue.
	 */
	private static final int CAPACITY = 20;

	/**
	 * The queue of download request.
	 */
	private PriorityBlockingQueue<DownloadRequest> mDownloadQueue = null;

	/**
	 * The set of all downloading request
	 */
	private final Set<DownloadRequest> mCurrentRequests = new HashSet<>();

	/**
	 * Used for generating monotonically-increasing sequence numbers for requests.
	 */
	private AtomicInteger mSequenceGenerator = new AtomicInteger();

	/**
	 * Control thread pool thread count
	 */

	private DownloadDispatcher mDownloadDispatcher = null;

	/**
	 * @param dispatcher DownloadDispatcher
	 */
	public DownloadRequestQueue(DownloadDispatcher dispatcher) {
		this(dispatcher, CAPACITY);
	}

	public DownloadRequestQueue(DownloadDispatcher dispatcher, int queueMaxCount) {
		mDownloadDispatcher = dispatcher;
		mDownloadQueue = new PriorityBlockingQueue<>(queueMaxCount);
	}

	/**
	 * Add to queue
	 *
	 * @param request add request
	 */
	public synchronized boolean enqueue(DownloadRequest request) {
		/** check if url is empty */
		if (TextUtils.isEmpty(request.getUrl())) {
			Log.w(TAG, "download url cannot be empty");
			return false;
		}

		/** if the request is downloading, do nothing */
		if (isDownloading(request.getDownloadId()) || isDownloading(request.getUrl())) {
			Log.w(TAG, "the download request is in downloading");
			return false;
		}
		/** tag the request as belonging to this queue */
		request.setDownloadQueue(this);
		mDownloadQueue.add(request);
		return true;
	}

	/**
	 * Out form the queue
	 *
	 * @return the request
	 */
	public synchronized DownloadRequest dequeue() {
		try {
			return mDownloadQueue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Gets a sequence number.
	 *
	 * @return return the sequence number
	 */
	public int getSequenceNumber() {
		return mSequenceGenerator.incrementAndGet();
	}

	/**
	 * In order to guarantee the maximum number of threads in thread pool in this function we use Semaphore
	 *
	 * @param request add request
	 */

	protected void startRequest(DownloadRequest request) {
		synchronized (mCurrentRequests) {
			mCurrentRequests.add(request);
		}
	}

	/**
	 * Remember to release the semaphore
	 *
	 * @param request finish request
	 */
	protected void finishRequest(DownloadRequest request) {
		synchronized (mCurrentRequests) {
			mCurrentRequests.remove(request);
			//			mSemaphore.release();
			mDownloadDispatcher.releaseThreadPoolSemaphore();
		}
	}

	/**
	 * To check if the request is downloading according to download id.
	 *
	 * @param downloadId download id
	 * @return true if the request is downloading, otherwise return false
	 */
	protected DownloadRequest.DownloadState query(int downloadId) {
		synchronized (mCurrentRequests) {
			for (DownloadRequest request : mCurrentRequests) {
				if (request.getDownloadId() == downloadId) {
					return request.getDownloadState();
				}
			}
		}

		return DownloadRequest.DownloadState.INVALID;
	}

	/**
	 * To check if the request is downloading according to download url.
	 *
	 * @param url the url to check
	 * @return true if the request is downloading, otherwise return false
	 */
	protected DownloadRequest.DownloadState query(String url) {
		synchronized (mCurrentRequests) {
			for (DownloadRequest request : mCurrentRequests) {
				if (request.getUrl().equals(url)) {
					return request.getDownloadState();
				}
			}
		}

		return DownloadRequest.DownloadState.INVALID;
	}

	/**
	 * To check if the request is downloading according to download downloadId.
	 *
	 * @param downloadId the downloadId to check
	 * @return true if the request is downloading, otherwise return false
	 */
	protected boolean isDownloading(int downloadId) {
		DownloadRequest.DownloadState state = query(downloadId);
		return (state == DownloadRequest.DownloadState.PENDING || state == DownloadRequest.DownloadState.RUNNING);
	}

	/**
	 * To check if the request is downloading according to download url.
	 *
	 * @param url the url to check
	 * @return true if the request is downloading, otherwise return false
	 */
	protected boolean isDownloading(String url) {
		DownloadRequest.DownloadState state = query(url);
		return (state == DownloadRequest.DownloadState.PENDING || state == DownloadRequest.DownloadState.RUNNING);
	}

	/**
	 * To check if the request is downloading according to download state.
	 *
	 * @param state the state to check
	 * @return true if the request is downloading, otherwise return false
	 */
	protected boolean isDownloading(DownloadRequest.DownloadState state) {

		return (state == DownloadRequest.DownloadState.PENDING || state == DownloadRequest.DownloadState.RUNNING);
	}


}
