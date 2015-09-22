package com.tuacy.wuyunxing.zhuhaiimpression.tools;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.tuacy.wuyunxing.zhuhaiimpression.R;
import com.tuacy.wuyunxing.zhuhaiimpression.base.MobileBaseApplication;

/**
 * Created by Administrator on 2015/6/21.
 */
public class VolleyImageCache {

	public static void networkImageViewUse(NetworkImageView iv, String url) {
		ImageLoader imLoader = new ImageLoader(MobileBaseApplication.getInstance().getRequestQueue(), new BitmapCache());
		iv.setDefaultImageResId(R.mipmap.pic_default);
		iv.setErrorImageResId(R.mipmap.pic_error);
		iv.setImageUrl(url, imLoader);
	}
}
