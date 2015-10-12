package com.example.example.base;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by tuacy on 2015/9/20.
 */
public abstract class MobileBaseWebView extends WebView {
    /**
     * Constructor.
     * @param context The current context.
     */
    public MobileBaseWebView(Context context) {
        super(context);

    }

    /**
     * Constructor.
     * @param context The current context.
     * @param attrs The attribute set.
     */
    public MobileBaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


}
