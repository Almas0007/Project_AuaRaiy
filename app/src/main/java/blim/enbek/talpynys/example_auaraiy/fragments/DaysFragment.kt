package blim.enbek.talpynys.example_auaraiy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import blim.enbek.talpynys.example_auaraiy.MainViewModel
import blim.enbek.talpynys.example_auaraiy.R
import blim.enbek.talpynys.example_auaraiy.adapters.WeatherAdapter
import blim.enbek.talpynys.example_auaraiy.adapters.WeatherModel
import blim.enbek.talpynys.example_auaraiy.databinding.FragmentDaysBinding


class DaysFragment: Fragment(),WeatherAdapter.Listener {

    lateinit var binding: FragmentDaysBinding
    private val model:MainViewModel by activityViewModels()
    lateinit var adapter: WeatherAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDaysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        model.liveDataList.observe(viewLifecycleOwner){
            adapter.submitList(it.subList(1,it.size))
        }
    }
    fun init() = with(binding){
        adapter = WeatherAdapter(this@DaysFragment)
        rcViewInDays.layoutManager =LinearLayoutManager(activity)
        rcViewInDays.adapter=adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = DaysFragment()

    }

    override fun onClick(item: WeatherModel) {
        model.liveDataCurrent.value = item
    }
}