package io.github.adapter.example.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.Arrays;
import java.util.List;

import io.github.adapter.example.R;
import io.github.adapter.example.bean.StaggeredBean;
import io.github.wong1988.adapter.SimpleListAdapter;
import io.github.wong1988.adapter.attr.LoadState;
import io.github.wong1988.adapter.holder.RecyclerViewHolder;


public class StaggeredVerticalAdapter extends SimpleListAdapter<StaggeredBean> {

    private static List<StaggeredBean> list;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            addData(Arrays.asList(
                    new StaggeredBean("2品牌66W SuperCharge多协议双向超级快充移动电源（白色）", "双向66W超级快充，强劲续航体验", "￥339", "限时特价"),
                    new StaggeredBean("2HUAXXX P50 Pro", "直降50元| 6期免息", "￥5398", "分期免息"),
                    new StaggeredBean("2品牌儿童手表4 Pro", "拼团省200元", "￥898", "赠送积分"),
                    new StaggeredBean("2品牌5A/6A数据线USB-C转USB-C", "限时直降10元", "￥59", "赠送积分"),
                    new StaggeredBean("2HUAXXX WATCH 3 Pro", "限时优惠100", "￥2898", "限时特价"),
                    new StaggeredBean("2HUAXXX MatePad 11", "", "￥2998", "分期免息"),
                    new StaggeredBean("2品牌超薄有线键盘", "", "￥269", "限时特价"),
                    new StaggeredBean("2智选智能插座", "统计电量，定时开关", "￥69", ""),
                    new StaggeredBean("2HUAXXX MateDock", "", "￥399", "赠送积分"),
                    new StaggeredBean("2HUAXXX MateStationB515台式机", "HUAWEI MateStationB515台式机", "￥3799", "赠送积分"),
                    new StaggeredBean("2品牌智选智能快充插排", "负载预警提醒，安全防护", "￥149", ""),
                    new StaggeredBean("2智选品牌智能声波牙刷2冰山款", "高频净白一刷闪耀五重大礼", "￥239", "限时特价"),
                    new StaggeredBean("2HUAXXX Mate 40 Pro智能视窗保护套", "", "￥299", "赠送积分")));
            setLoadState(LoadState.LOAD_END);
        }
    };

    static {

        list = Arrays.asList(
                new StaggeredBean("品牌66W SuperCharge多协议双向超级快充移动电源（白色）", "双向66W超级快充，强劲续航体验", "￥339", "限时特价"),
                new StaggeredBean("HUAXXX P50 Pro", "直降50元| 6期免息", "￥5398", "分期免息"),
                new StaggeredBean("品牌儿童手表4 Pro", "拼团省200元", "￥898", "赠送积分"),
                new StaggeredBean("品牌5A/6A数据线USB-C转USB-C", "限时直降10元", "￥59", "赠送积分"),
                new StaggeredBean("HUAXXX WATCH 3 Pro", "限时优惠100", "￥2898", "限时特价"),
                new StaggeredBean("HUAXXX MatePad 11", "", "￥2998", "分期免息"),
                new StaggeredBean("品牌超薄有线键盘", "", "￥269", "限时特价"),
                new StaggeredBean("智选智能插座", "统计电量，定时开关", "￥69", ""),
                new StaggeredBean("HUAXXX MateDock", "", "￥399", "赠送积分"),
                new StaggeredBean("HUAXXX MateStationB515台式机", "HUAWEI MateStationB515台式机", "￥3799", "赠送积分"),
                new StaggeredBean("品牌智选智能快充插排", "负载预警提醒，安全防护", "￥149", ""),
                new StaggeredBean("智选品牌智能声波牙刷2冰山款", "高频净白一刷闪耀五重大礼", "￥239", "限时特价"),
                new StaggeredBean("HUAXXX Mate 40 Pro智能视窗保护套", "", "￥299", "赠送积分")
        );
    }


    public StaggeredVerticalAdapter(Context context) {
        super(context, R.layout.item, true, list);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onBindViewHolders(RecyclerViewHolder holder, int viewType, StaggeredBean staggeredBean, int position) {


        holder.getTextView(R.id.tv).setText(staggeredBean.getTitle());
        holder.getTextView(R.id.tv1).setText(staggeredBean.getDescribe());

        holder.setVisibility(R.id.tv2, TextUtils.isEmpty(staggeredBean.getDescribe()) ? View.GONE : View.VISIBLE);
        holder.setVisibility(R.id.tv3, TextUtils.isEmpty(staggeredBean.getDiscount()) ? View.GONE : View.VISIBLE);

        holder.getTextView(R.id.tv2).setText(staggeredBean.getPrice());
        holder.getTextView(R.id.tv3).setText(staggeredBean.getDiscount());
    }

    public void addHeader() {
        TextView header = new TextView(getAttachContext());
        header.setBackgroundColor(getAttachContext().getResources().getColor(android.R.color.holo_red_dark));
        header.setText("头布局" + System.currentTimeMillis());
        addHeaderView(header, LinearLayout.VERTICAL);
    }

    public void addFooter() {
        TextView footer = new TextView(getAttachContext());
        footer.setText("脚布局" + System.currentTimeMillis());
        footer.setBackgroundColor(getAttachContext().getResources().getColor(android.R.color.holo_red_dark));
        addFooterView(footer);
    }

    public void appendData() {
        setLoadState(LoadState.LOADING);
        handler.sendEmptyMessageDelayed(1, 1000);
    }

    @Override
    public void onViewAttachedToWindow2(@NonNull RecyclerViewHolder holder, StaggeredBean staggeredBean, int position) {
        super.onViewAttachedToWindow2(holder, staggeredBean, position);

        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        if (params instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) params;
            layoutParams.setFullSpan(position == 4);
        }
    }

}
