package com.example.example.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class ImageLoader {

	private static final int                      DEFAULT_THREAD_COUNT           = 4;
	private static final Type                     DEFAULT_TYPE                   = Type.LIFO;
	private static final int                      MESSAGE_OBTAIN_BITMAP_SUCCESS  = 0x001;
	private static final int                      MESSAGE_OBTAIN_BITMAP_ERROR    = 0x002;
	private static final int                      MESSAGE_OBTAIN_TASK_FROM_QUEUE = 0x003;
	private static       ImageLoader              mInstance                      = null;
	private              LruCache<String, Bitmap> mLruCache                      = null;
	private              ExecutorService          mThreadPool                    = null;
	private              LinkedList<Runnable>     mTaskQueue                     = null;
	private              Semaphore                mSemaphoreThreadPool           = null;
	/**
	 * background thread
	 */
	private              Thread                   mPoolThread                    = null;
	private              Handler                  mPoolThreadHandler             = null;
	private              Semaphore                mSemaphorePoolThreadHandler    = new Semaphore(0);
	/**
	 * UI thread
	 */
	private              Handler                  mUiHandler                     = null;
	private              Type                     mType                          = Type.LIFO;

	public enum Type {
		FIFO,
		LIFO
	}

	protected ImageLoader(int threadPool, Type type) {
		init(threadPool, type);
	}

	private void init(int threadCount, Type type) {
		mType = type;
		/** background thread */
		mPoolThread = new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				mPoolThreadHandler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						/** get a task form task queue and add to pool thread */
						switch (msg.what) {
							case MESSAGE_OBTAIN_TASK_FROM_QUEUE:
								mThreadPool.execute(getTaskFromQueue());
								try {
									mSemaphoreThreadPool.acquire();
								} catch (InterruptedException e) {
									e.printStackTrace();
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
		long maxMemorySize = Runtime.getRuntime().maxMemory();
		long cacheSize = maxMemorySize / 8;
		mLruCache = new LruCache<String, Bitmap>((int) cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
		mThreadPool = Executors.newFixedThreadPool(threadCount);
		mTaskQueue = new LinkedList<>();
		mSemaphoreThreadPool = new Semaphore(threadCount);
	}

	public static ImageLoader getInstance() {
		if (null == mInstance) {
			synchronized (ImageLoader.class) {
				if (null == mInstance) {
					mInstance = new ImageLoader(DEFAULT_THREAD_COUNT, DEFAULT_TYPE);
				}
			}
		}
		return mInstance;
	}

	public static ImageLoader getInstance(final int threadPool, final Type type) {
		if (null == mInstance) {
			synchronized (ImageLoader.class) {
				if (null == mInstance) {
					mInstance = new ImageLoader(threadPool, type);
				}
			}
		}
		return mInstance;
	}

	public void loaderImage(String path, ImageView imageView) {
		imageView.setTag(path);
		if (null == mUiHandler) {
			mUiHandler = new RefreshImageHandler();
		}
		Bitmap bitmap = getBitmapFromLruCache(path);
		if (null != bitmap) {
			refreshImageMessage(path, imageView, bitmap);
		} else {
			addTaskToQueue(new TaskRunnable(path, imageView));
		}
	}

	private Bitmap getBitmapFromLruCache(String key) {
		if (null != mLruCache) {
			return mLruCache.get(key);
		}
		return null;
	}

	private void addBitmapToLruCache(String path, Bitmap bitmap) {
		if (null != bitmap) {
			if (null == getBitmapFromLruCache(path)) {
				mLruCache.put(path, bitmap);
			}
		}
	}

	private void refreshImageMessage(String path, ImageView imageView, Bitmap bitmap) {
		if (null != bitmap) {
			ImageBeanHolder imageBeanHolder = new ImageBeanHolder();
			imageBeanHolder.mBitmap = bitmap;
			imageBeanHolder.mImageView = imageView;
			imageBeanHolder.mKey = path;
			Message message = Message.obtain();
			message.obj = imageBeanHolder;
			message.what = MESSAGE_OBTAIN_BITMAP_SUCCESS;
			mUiHandler.sendMessage(message);
		} else {
			mUiHandler.sendEmptyMessage(MESSAGE_OBTAIN_BITMAP_ERROR);
		}
	}

	private synchronized void addTaskToQueue(Runnable runnable) {
		mTaskQueue.add(runnable);
		if (null == mPoolThreadHandler) {
			try {
				mSemaphorePoolThreadHandler.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		mPoolThreadHandler.sendEmptyMessage(MESSAGE_OBTAIN_TASK_FROM_QUEUE);
	}

	private synchronized Runnable getTaskFromQueue() {
		if (mTaskQueue.size() > 0) {
			if (Type.FIFO == mType) {
				return mTaskQueue.removeFirst();
			} else {
				return mTaskQueue.removeLast();
			}
		}
		return null;
	}

	private ImageViewSize getImageViewSize(ImageView imageView) {
		DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();
		ImageViewSize imageViewSize = new ImageViewSize();
		ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
		int width = imageView.getWidth();
		if (0 >= width) {
			width = layoutParams.width;
		}
		if (0 >= width) {
			width = getImageViewFieldValue(imageView, "mMaxWidth");//imageView.getMaxWidth();
		}
		if (0 >= width) {
			width = displayMetrics.widthPixels;
		}
		int height = imageView.getHeight();
		if (0 >= height) {
			height = layoutParams.height;
		}
		if (0 >= height) {
			height = getImageViewFieldValue(imageView, "mMaxHeight");//imageView.getMaxHeight();
		}
		if (0 >= height) {
			height = displayMetrics.heightPixels;
		}
		imageViewSize.mWidth = width;
		imageViewSize.mHeight = height;
		return imageViewSize;
	}

	private static int getImageViewFieldValue(Object object, String fieldName) {
		int value = 0;
		try {
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = field.getInt(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
				value = fieldValue;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return value;
	}

	private Bitmap decoderSampleBitmapFromPath(String path, ImageViewSize imageViewSize) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		options.inSampleSize = calculateInSampleSize(options, imageViewSize.mWidth, imageViewSize.mHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		int width = options.outWidth;
		int height = options.outHeight;
		int sampleSize = 1;
		if (reqWidth < width || reqHeight < height) {
			int widthRadio = Math.round(width * 1.0f / reqWidth);
			int heightRadio = Math.round(height * 1.0f / reqHeight);
			sampleSize = Math.max(widthRadio, heightRadio);
		}
		return sampleSize;
	}


	private class ImageViewSize {

		int mWidth;
		int mHeight;
	}


	private class ImageBeanHolder {

		ImageView mImageView;
		String    mKey;
		Bitmap    mBitmap;
	}

	private class TaskRunnable implements Runnable {

		private ImageView mImageView;
		private String    mPath;

		public TaskRunnable(final String path, final ImageView imageView) {
			mPath = path;
			mImageView = imageView;
		}

		@Override
		public void run() {
			ImageViewSize imageViewSize = getImageViewSize(mImageView);
			Bitmap bitmap = decoderSampleBitmapFromPath(mPath, imageViewSize);
			/** save in cache */
			addBitmapToLruCache(mPath, bitmap);
			refreshImageMessage(mPath, mImageView, bitmap);
			mSemaphoreThreadPool.release();
		}
	}

	private static class RefreshImageHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			/** get image and set image to ImageView */
			switch (msg.what) {
				case MESSAGE_OBTAIN_BITMAP_SUCCESS:
					ImageBeanHolder imageBeanHolder = (ImageBeanHolder) msg.obj;
					if (imageBeanHolder.mImageView.getTag().toString().equals(imageBeanHolder.mKey)) {
						imageBeanHolder.mImageView.setImageBitmap(imageBeanHolder.mBitmap);
					}
					break;
				case MESSAGE_OBTAIN_BITMAP_ERROR:
					break;
			}
		}
	}

}
