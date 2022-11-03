package blim.enbek.talpynys.example_auaraiy.fragments

import android.Manifest
import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import blim.enbek.talpynys.example_auaraiy.MainViewModel
import blim.enbek.talpynys.example_auaraiy.adapters.ViewPagerAdapter
import blim.enbek.talpynys.example_auaraiy.adapters.WeatherModel
import blim.enbek.talpynys.example_auaraiy.databinding.FragmentMain1Binding
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import org.json.JSONObject


class Main1_Fragment : Fragment() {
    final val API_KEY = "fa11ed2c08bc4b25b99165055222510"
    private val model:MainViewModel by activityViewModels()

    val fragList = listOf(
        HoursFragment.newInstance(),
        DaysFragment.newInstance()
    )
    val fragTavList = listOf(
        "HOURS",
        "DAYS"
    )

    lateinit var binding: FragmentMain1Binding
    lateinit var pLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMain1Binding.inflate(inflater, container, false)
        return binding.root
    }
    /**---------------------------------------------------------------------- */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initFrag()
        checkPermission()
        updateDataViewModel()
        giveDataAtAPI("Almaty")

    }
    /**---------------------------------------------------------------------- */
    private fun initFrag() = with(binding) {
        val adapter = ViewPagerAdapter(activity as FragmentActivity, fragList)
        viewPagerHoursDays.adapter = adapter

        TabLayoutMediator(tabLayout, viewPagerHoursDays)
        { tab, pos ->
            tab.text = fragTavList[pos]
        }.attach()

    }



    private fun updateDataViewModel()= with(binding){
        model.liveDataCurrent.observe(viewLifecycleOwner){
            val maxminTemp = "${it.minTemp}°C/${it.maxTemp}"
            textCurrentTime.text = it.time
            textCity.text=it.city
            textTemp.text= it.currentTemp.ifEmpty { maxminTemp }+"°C"

            textTempMaxMin.text = if(it.currentTemp.isEmpty())"" else {maxminTemp +"°C"}
            Picasso.get().load("https:"+it.imageUrl).into(imageWeather)
        }

    }
    /** Провереят на разрешение если не дано до просить дать разрешение на гео*/
    private fun checkPermission() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            listenerPermission()
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    /** Проверяет доступ который был дано и если дано вызывает ТOAST*/
    private fun listenerPermission() {
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            Toast.makeText(activity, "The permission is $it", Toast.LENGTH_LONG).show()
        }
    }

    companion object {

        fun newInstance() = Main1_Fragment()
    }

    /**Создаю через Volley запрос для получение Json обьекта данных*/
    private fun giveDataAtAPI(city: String) {
        val url = "https://api.weatherapi.com/v1/forecast.json?key=" +
                API_KEY +
                "&q=" +
                city +
                "&days=" +
                "8" +
                "&aqi=no&alerts=no"
        val queue = Volley.newRequestQueue(context)

        val stringRequest = StringRequest(Request.Method.GET, url, {
            result->parseObjectInJSON(result)


        },
            { error -> Log.d("TestLog","error API: $error")
            }
        )

        queue.add(stringRequest)
    }

    /**Создаю переменную по передаче данных Json обьекта в виде String */
    private fun parseObjectInJSON(result:String){
        val parentObject = JSONObject(result)
        val list = objectsArrayJSONDays(parentObject)
        currentDayWeather(parentObject,list[0])

    }
    private fun currentDayWeather(parentObject:JSONObject, weatherItem:WeatherModel){
        val dataAtJSON = WeatherModel(
            parentObject.getJSONObject("location").getString("name"),
            parentObject.getJSONObject("current").getString("last_updated"),
            parentObject.getJSONObject("current").getJSONObject("condition").getString("text"),
            parentObject.getJSONObject("current").getString("temp_c"),
            weatherItem.maxTemp,
            weatherItem.minTemp,
            parentObject.getJSONObject("current").getJSONObject("condition").getString("icon"),
            weatherItem.hours
        )

        model.liveDataCurrent.value= dataAtJSON

        Log.d("TestLog", dataAtJSON.city)
        Log.d("TestLog",dataAtJSON.condition)
        Log.d("TestLog",dataAtJSON.time)
        Log.d("TestLog",dataAtJSON.currentTemp)
        Log.d("TestLog",dataAtJSON.maxTemp)
        Log.d("TestLog",dataAtJSON.minTemp)
        Log.d("TestLog",dataAtJSON.hours)
    }

    private fun objectsArrayJSONDays(parentObject:JSONObject):List<WeatherModel>{
        val list = ArrayList<WeatherModel>()
        val arrayForDay = parentObject.getJSONObject("forecast").getJSONArray("forecastday")
        val city = parentObject.getJSONObject("location").getString("name")
        for (i in 0 until arrayForDay.length()){
               val day =  arrayForDay[i] as JSONObject
                val item = WeatherModel(
                    city,
                    day.getString("date"),
                    day.getJSONObject("day").getJSONObject("condition").getString("text"),
                    "",
                    day.getJSONObject("day").getString("maxtemp_c").toFloat().toInt().toString(),
                    day.getJSONObject("day").getString("mintemp_c").toFloat().toInt().toString(),
                    day.getJSONObject("day").getJSONObject("condition").getString("icon"),
                    day.getJSONArray("hour").toString()
                )

            list.add(item)
        }
        /** Передаю лист с помощю ViewModel со списком liveDataList, в дэйсФрагмент возьмут через обсерв*/
        model.liveDataList.value = list
        return list
    }

}