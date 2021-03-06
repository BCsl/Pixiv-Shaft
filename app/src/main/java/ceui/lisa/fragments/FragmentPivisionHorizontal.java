package ceui.lisa.fragments;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.ybq.android.spinkit.style.DoubleBounce;

import java.util.ArrayList;
import java.util.List;

import ceui.lisa.R;
import ceui.lisa.activities.TemplateActivity;
import ceui.lisa.adapters.PivisionHorizontalAdapter;
import ceui.lisa.databinding.FragmentPivisionHorizontalBinding;
import ceui.lisa.http.NullCtrl;
import ceui.lisa.http.Retro;
import ceui.lisa.model.ListArticle;
import ceui.lisa.models.SpotlightArticlesBean;
import ceui.lisa.utils.DensityUtil;
import ceui.lisa.view.LinearItemHorizontalDecoration;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static ceui.lisa.activities.Shaft.sUserModel;

/**
 * Pivision 文章
 */
public class FragmentPivisionHorizontal extends BaseBindFragment<FragmentPivisionHorizontalBinding> {

    private List<SpotlightArticlesBean> allItems = new ArrayList<>();
    private PivisionHorizontalAdapter mAdapter;

    @Override
    void initLayout() {
        mLayoutID = R.layout.fragment_pivision_horizontal;
    }

    @Override
    void initData() {
        DoubleBounce doubleBounce = new DoubleBounce();
        doubleBounce.setColor(getResources().getColor(R.color.white));
        baseBind.progress.setIndeterminateDrawable(doubleBounce);
        FadeInLeftAnimator landingAnimator = new FadeInLeftAnimator();
        landingAnimator.setAddDuration(400L);
        landingAnimator.setRemoveDuration(400L);
        landingAnimator.setMoveDuration(400L);
        landingAnimator.setChangeDuration(400L);
        baseBind.recyclerView.setItemAnimator(landingAnimator);
        baseBind.recyclerView.addItemDecoration(new LinearItemHorizontalDecoration(DensityUtil.dp2px(8.0f)));
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        baseBind.recyclerView.setLayoutManager(manager);
        baseBind.recyclerView.setHasFixedSize(true);
        ViewGroup.LayoutParams layoutParams = baseBind.recyclerView.getLayoutParams();
        layoutParams.width = MATCH_PARENT;
        layoutParams.height = mContext.getResources()
                .getDimensionPixelSize(R.dimen.article_horizontal_height) +
                mContext.getResources()
                        .getDimensionPixelSize(R.dimen.sixteen_dp);
        baseBind.recyclerView.setLayoutParams(layoutParams);
        mAdapter = new PivisionHorizontalAdapter(allItems, mContext);
        baseBind.recyclerView.setAdapter(mAdapter);
        baseBind.seeMore.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, TemplateActivity.class);
            intent.putExtra(TemplateActivity.EXTRA_FRAGMENT, "特辑");
            intent.putExtra("hideStatusBar", false);
            startActivity(intent);
        });
        getFirstData();
    }

    @Override
    public void getFirstData() {
        Retro.getAppApi().getArticles(sUserModel.getResponse().getAccess_token(), "all")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NullCtrl<ListArticle>() {
                    @Override
                    public void success(ListArticle listArticle) {
                        allItems.clear();
                        allItems.addAll(listArticle.getList());
                        mAdapter.notifyItemRangeInserted(0, listArticle.getList().size());
                    }

                    @Override
                    public void must(boolean isSuccess) {
                        baseBind.progress.setVisibility(View.INVISIBLE);
                    }
                });
    }
}
