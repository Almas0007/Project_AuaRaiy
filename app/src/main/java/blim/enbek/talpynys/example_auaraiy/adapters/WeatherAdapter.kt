package blim.enbek.talpynys.example_auaraiy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import blim.enbek.talpynys.example_auaraiy.R
import blim.enbek.talpynys.example_auaraiy.databinding.ListItemBinding
import com.squareup.picasso.Picasso

class WeatherAdapter(val listener:Listener?) : ListAdapter<WeatherModel, WeatherAdapter.Holder>(Comparator()) {



    class Holder(view: View, val listener: Listener?) : RecyclerView.ViewHolder(view) {
        val binding = ListItemBinding.bind(view)
        var itemTemp:WeatherModel? = null
        init {
            itemView.setOnClickListener {
                itemTemp?.let { it1 -> listener?.onClick(it1) }
            }
        }
        fun bind(item: WeatherModel) = with(binding) {
            itemTemp = item
            val temp=item.currentTemp.ifEmpty { "${item.minTemp}°C / ${item.maxTemp}" }+"°C"
            textData.text = item.time
            textCondition.text = item.condition
            textTempInItemList.text =temp
            if(textTempInItemList.text.equals("${item.minTemp}°C / ${item.maxTemp}°C" )){
                textTempInItemList.textSize=20F
            }


            Picasso.get().load("https:"+item.imageUrl).into(imageForHours)

        }

    }

    class Comparator : DiffUtil.ItemCallback<WeatherModel>() {
        override fun areItemsTheSame(oldItem: WeatherModel, newItem: WeatherModel): Boolean {

            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WeatherModel, newItem: WeatherModel): Boolean {
            return oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        /**В паренте есть контекс */
        return Holder(view,listener)
    }
    /**Создаем через bind фунцию, указывая позицию айтема, загружая на holder*/
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    interface Listener{
        fun onClick(item: WeatherModel)
    }

}