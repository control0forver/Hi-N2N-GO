package lgfstudio.bestlgf.hin2ngo.template;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lgfstudio.bestlgf.hin2ngo.R;

/**
 * Created by janiszhang on 2018/4/13.
 */

public class MainTitleTemplate extends BaseTemplate {
    private Context mContext;
    /**
     * 整个页面的视图View
     */
    private LinearLayout mPageView;

    /**
     * 标题栏布局区域
     */
    private RelativeLayout mTitleLayout;

    /**
     * 内容显示区
     */
    private FrameLayout mContainerLayout;


    public MainTitleTemplate(Context context, String title) {
        super(context);

        this.mContext = context;

        mPageView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.main_template_view, null);

        mTitleLayout = (RelativeLayout) mPageView.findViewById(R.id.rl_title);
        mContainerLayout = (FrameLayout) mPageView.findViewById(R.id.fl_container);

        TextView titleText = (TextView) mPageView.findViewById(R.id.tv_title);

        titleText.setText(title);

    }

    @Override
    public void setContentView(View contentView) {
        RelativeLayout.LayoutParams contentViewLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mContainerLayout.addView(contentView, contentViewLayoutParams);
    }

    @Override
    public View getPageView() {
        return mPageView;
    }
}
