package lgfstudio.bestlgf.hin2ngo.activity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import lgfstudio.bestlgf.hin2ngo.R;
import lgfstudio.bestlgf.hin2ngo.template.BaseTemplate;
import lgfstudio.bestlgf.hin2ngo.template.CommonTitleTemplate;

import static android.view.KeyEvent.KEYCODE_BACK;

/**
 * Created by janiszhang on 2018/6/25.
 */

public class WebViewActivity extends BaseActivity {

    public static final String WEB_VIEW_TYPE = "web_view_type";

    public static final int TYPE_WEB_VIEW_ABOUT = 0;
    public static final int TYPE_WEB_VIEW_FEEDBACK = 1;
    public static final int TYPE_WEB_VIEW_SHARE = 2;
    public static final int TYPE_WEB_VIEW_CONTACT = 3;

    public static final String ABOUT_URL = "https://mail.bestlgf.pro/N2NGO/";
    public static final String SHARE_URL = "https://mail.bestlgf.pro/N2NGO/Download";
    public static final String CONTACT_URL = "https://mail.bestlgf.pro/N2NGO/Support";
    public static final String FEEDBACK_URL = "https://github.com/control0forver/Hi-N2N-GO/issues";

    private WebView mWebView;
    private AVLoadingIndicatorView mLoadingView;
    private CommonTitleTemplate mCommonTitleTemplate;

    @Override
    protected BaseTemplate createTemplate() {
        mCommonTitleTemplate = new CommonTitleTemplate(mContext, "Web");

        mCommonTitleTemplate.mLeftAction.setVisibility(View.VISIBLE);
        mCommonTitleTemplate.mLeftAction.setImageResource(R.drawable.titlebar_icon_close);
        mCommonTitleTemplate.mLeftAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mCommonTitleTemplate.mRightAction.setVisibility(View.VISIBLE);
        mCommonTitleTemplate.mRightAction.setImageResource((R.drawable.titlebar_icon_forward));
        mCommonTitleTemplate.mRightAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWebView.canGoForward()) {
                    mWebView.goForward();
                } else {
                    Toast.makeText(mContext, R.string.no_further_pages_in_the_browsing_history, Toast.LENGTH_LONG).show();
                }
            }
        });
        return mCommonTitleTemplate;
    }

    @Override
    protected void doOnCreate(Bundle savedInstanceState) {

        mLoadingView = (AVLoadingIndicatorView) findViewById(R.id.loading_view);

        mWebView = (WebView) findViewById(R.id.web_view);

        WebSettings webSettings = mWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDefaultTextEncodingName("utf-8");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mLoadingView.setVisibility(View.VISIBLE);

                if (mWebView != null) {
                    mWebView.setVisibility(View.GONE);

                }

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                mLoadingView.setVisibility(View.GONE);
                if (mWebView != null) {
                    mWebView.setVisibility(View.VISIBLE);
                    mCommonTitleTemplate.setTitleText(view.getTitle());
                }

            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        int webViewType = getIntent().getIntExtra(WEB_VIEW_TYPE, -1);

        switch (webViewType) {
            case TYPE_WEB_VIEW_ABOUT:
                mCommonTitleTemplate.setTitleText(R.string.about);
                mWebView.loadUrl(ABOUT_URL);
                break;
            case TYPE_WEB_VIEW_FEEDBACK:
                mCommonTitleTemplate.setTitleText(R.string.feedback);
                mWebView.loadUrl(FEEDBACK_URL);
                break;
            case TYPE_WEB_VIEW_SHARE:
                mCommonTitleTemplate.setTitleText(R.string.share);
                mWebView.loadUrl(SHARE_URL);
                break;
            case TYPE_WEB_VIEW_CONTACT:
                mCommonTitleTemplate.setTitleText(R.string.contact_us);
                mWebView.loadUrl(CONTACT_URL);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_empty_template;
    }
}
