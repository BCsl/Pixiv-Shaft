package ceui.lisa.activities

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import ceui.lisa.R
import ceui.lisa.databinding.ActivityViewPagerBinding
import ceui.lisa.fragments.FragmentSingleIllust
import ceui.lisa.models.IllustsBean
import ceui.lisa.utils.Common
import ceui.lisa.utils.DataChannel
import ceui.lisa.viewmodel.Dust
import com.ToxicBakery.viewpager.transforms.DrawerTransformer

class ViewPagerActivity : BaseActivity<ActivityViewPagerBinding>() {

    private lateinit var holder: Dust

    override fun hideStatusBar(): Boolean {
        return true
    }

    override fun initView() {
        holder = ViewModelProvider(this).get(Dust::class.java)

        holder.dust.observe(this, Observer<List<IllustsBean>> { dust: List<IllustsBean> ->
            baseBind.viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
                override fun getItem(i: Int): Fragment {
                    Common.showLog(className + "setPageTransformer " + i)
                    return FragmentSingleIllust.newInstance(i)
                }

                override fun getCount(): Int {
                    return dust.size
                }
            }
        })
        holder.index.observe(this, Observer{ index ->
            run {
                baseBind.viewPager.currentItem = index
            }
        })

        baseBind.viewPager.setPageTransformer(true, DrawerTransformer())
        baseBind.viewPager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                holder.index.value = position
            }
        })

        holder.dust.value = DataChannel.get().illustList


        val p = intent.getIntExtra("position", -1)
        if (p != -1) {
            holder.index.value = p
        }
    }

    override fun initLayout(): Int {
        return R.layout.activity_view_pager
    }

    override fun initData() {
    }
}
