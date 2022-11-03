package blim.enbek.talpynys.example_auaraiy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import blim.enbek.talpynys.example_auaraiy.adapters.WeatherModel

class MainViewModel : ViewModel() {

    val liveDataCurrent = MutableLiveData<WeatherModel>()
    val liveDataList = MutableLiveData<List<WeatherModel>>()

}