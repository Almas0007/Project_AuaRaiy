package blim.enbek.talpynys.example_auaraiy.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(frag: FragmentActivity, val fragLst : List<Fragment>):FragmentStateAdapter(frag){
    override fun getItemCount(): Int {
        return fragLst.size
    }

    override fun createFragment(position: Int): Fragment {
       return fragLst[position]
    }
}