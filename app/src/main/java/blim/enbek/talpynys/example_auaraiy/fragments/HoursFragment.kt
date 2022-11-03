package blim.enbek.talpynys.example_auaraiy.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import blim.enbek.talpynys.example_auaraiy.MainViewModel
import blim.enbek.talpynys.example_auaraiy.adapters.WeatherAdapter
import blim.enbek.talpynys.example_auaraiy.adapters.WeatherModel
import blim.enbek.talpynys.example_auaraiy.databinding.FragmentHoursBinding
import org.json.JSONArray
import org.json.JSONObject

class HoursFragment : Fragment() {
    private lateinit var binding: FragmentHoursBinding
    private lateinit var adapter: WeatherAdapter
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHoursBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

    }

    private fun dataHoursAtJSON(weatherItem:WeatherModel):List<WeatherModel>{
        val item = JSONArray(weatherItem.hours)
        val list = ArrayList<WeatherModel>()
        for (i in 5 until item.length() step 2){
            val item = WeatherModel(
                weatherItem.city,
                (item[i] as JSONObject).getString("time"),
                (item[i] as JSONObject).getJSONObject("condition").getString("text"),
                (item[i] as JSONObject).getString("temp_c").toFloat().toInt().toString(),
                "",
                "",
                (item[i] as JSONObject).getJSONObject("condition").getString("icon"),
                ""

            )
            list.add(item)

        }
        return list
    }

    private fun initRecyclerView() = with(binding) {

        model.liveDataCurrent.observe(viewLifecycleOwner) {
            recyclerViewInHours.layoutManager = LinearLayoutManager(activity)
            adapter = WeatherAdapter(null)
            recyclerViewInHours.adapter = adapter
            adapter.submitList(dataHoursAtJSON(it))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HoursFragment()
    }
}
