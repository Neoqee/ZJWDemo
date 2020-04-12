package com.neoqee.oomtest;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.neoqee.oomtest.json.BaseData;
import com.neoqee.oomtest.json.GsonUtil;

import java.io.IOException;
import java.io.Serializable;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BGABanner.Adapter<ImageView,String> {

    private BGABanner convenientBanner;
    private MallDetailBean mallDetailBean;
    private List<String> netImage;
    private TextView tvGoodsName;
    private TextView tvNeedIntegral;
    private TextView tvNeedMoney;
    private TextView tvNumberCount;
    private TextView tvEvaluateCount;
    private WebView webView;
    private PopupWindow popupWindow = null;
    private int num=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();

    }

    private void initView() {
        convenientBanner = findViewById(R.id.convenientBanner);
        tvGoodsName = findViewById(R.id.tv_goods_name);
        tvNeedIntegral = findViewById(R.id.tv_need_integrall);
        tvNeedMoney = findViewById(R.id.tv_need_money);
        tvNumberCount = findViewById(R.id.tv_number_count);
        tvEvaluateCount = findViewById(R.id.tv_evaluate_count);
        webView = findViewById(R.id.wv_detail);

        Button addShopCarBtn = findViewById(R.id.bt_add_shopping_car);
        addShopCarBtn.setOnClickListener(this);
    }

    private void initData() {
        String goodsId = "296510885285990400";
        getGoodsInfo(goodsId);
    }

    private void getGoodsInfo(String goodsId){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.mokehome.com").build();
        ApiService apiService = retrofit.create(ApiService.class);
        apiService.getGoodsDetail(goodsId)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String data = response.body().string();
                            BaseData baseData = GsonUtil.fromJson(data, BaseData.class);
                            MallDetailBean mallDetailBean = GsonUtil.fromObject(baseData.getData(), MallDetailBean.class);
                            getMallDetail(mallDetailBean);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
    }

    public void getMallDetail(MallDetailBean mallDetailBean) {
        String[] images = mallDetailBean.getPhoto().split(",");
        this.mallDetailBean = mallDetailBean;
        initWebView(mallDetailBean);
        netImage = Arrays.asList(images);
        convenientBanner.setAdapter(this);
        convenientBanner.setData(netImage, null);
        tvGoodsName.setText(mallDetailBean.getGoodsName());
        tvNeedIntegral.setText(mallDetailBean.getIntegral());
        float integral = Float.valueOf(mallDetailBean.getIntegral());
        tvNeedMoney.setText("￥" + integral / 10);
        tvNumberCount.setText("1");
        tvEvaluateCount.setText(mallDetailBean.getCommentNum());
        SharePreferenceHelper sharePreferencesHelper = new SharePreferenceHelper(this, "USER_INFO");
        sharePreferencesHelper.put(mallDetailBean.getGoodsName(), 1);
    }

    private void initWebView(MallDetailBean mallDetailBean) {
        String htmlStr = mallDetailBean.getIntroduce().trim();
        htmlStr = htmlStr.replaceAll("&amp;", "");
        htmlStr = htmlStr.replaceAll("&quot;", "\"");
        htmlStr = htmlStr.replaceAll("&lt;", "<");
        htmlStr = htmlStr.replaceAll("&gt;", ">");
        webView.loadData(htmlStr, "text/html", "UTF-8");
        webView.loadDataWithBaseURL(null, htmlStr, "text/html", "UTF-8", null);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webView.setWebViewClient(new MyWebViewClient());
    }

    @Override
    public void onClick(View v) {
        showShopCartPop();
    }

    private void showShopCartPop() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_join_shopcart, null);
        if (popupWindow == null) {
            popupWindow = new PopupWindow(contentView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        }
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
        setPopParams(contentView, mallDetailBean);
    }

    private void setPopParams(View contentView, MallDetailBean mallDetailBean) {
        ImageView iv_goods = contentView.findViewById(R.id.ivShow_popShopCart);
        TextView iv_close = contentView.findViewById(R.id.tv_close);
        TextView tv_need_integra = contentView.findViewById(R.id.tv_need_integra);
        TextView tv_need_money = contentView.findViewById(R.id.tv_need_money);
        TextView tv_texture = contentView.findViewById(R.id.tv_texture);
        TextView tv_num = contentView.findViewById(R.id.tv_num);
        Button bt_subtract = contentView.findViewById(R.id.bt_subtract);
        Button bt_add = contentView.findViewById(R.id.bt_add);
        Button bt_finish = contentView.findViewById(R.id.bt_finish);

        String[] images = mallDetailBean.getPhoto().split(",");
        String photoUrl = images[0];
        XImageLoader.getInstance(this).showImage(photoUrl, iv_goods, R.mipmap.ic_launcher);

        tv_need_integra.setText(mallDetailBean.getIntegral());
        float integral = Float.valueOf(mallDetailBean.getIntegral());
        tv_need_money.setText("￥" + integral / 10);
        tv_texture.setText(mallDetailBean.getTexture());
        tv_num.setText(String.valueOf(num));
        tvNumberCount.setText(String.valueOf(num));
        iv_close.setOnClickListener(v -> popupWindow.dismiss());
        bt_subtract.setOnClickListener(v -> {
            num = num - 1;
            if (num < 1) num = 1;
            tv_num.setText(String.valueOf(num));
            tvNumberCount.setText(String.valueOf(num));
        });
        bt_add.setOnClickListener(v -> {
            num = num + 1;
            tv_num.setText(String.valueOf(num));
            tvNumberCount.setText(String.valueOf(num));
        });
        bt_finish.setOnClickListener(v -> {
            ShopCartLocalBean carBean = new ShopCartLocalBean();
            carBean.setGoodsID(mallDetailBean.getId());
            carBean.setGoodsIntegral(mallDetailBean.getIntegral());
            carBean.setGoodsPrice(mallDetailBean.getPrice());
            carBean.setGoodsName(mallDetailBean.getGoodsName());
            carBean.setGoodsNumber(String.valueOf(num));
            carBean.setGoodsPhoto(mallDetailBean.getPhoto());
            carBean.setGoodsTexture(mallDetailBean.getTexture());
            popupWindow.dismiss();
        });
    }

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
        Glide.with(MainActivity.this)
                .load(model)
                .apply(new RequestOptions().dontAnimate().centerCrop())
                .into(itemView);
    }
}
