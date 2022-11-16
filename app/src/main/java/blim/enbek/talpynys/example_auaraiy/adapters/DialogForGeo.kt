package blim.enbek.talpynys.example_auaraiy.adapters

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText

object DialogForGeo {
    fun onLocationFunction(context: Context,listener:Listener){
        val builder = AlertDialog.Builder(context)
        val dialog = builder.create()
        dialog.setTitle("GEO location is not ON")
        dialog.setMessage("Please tap OK, if you want correctly working app")
        dialog.setButton(AlertDialog.BUTTON_POSITIVE,"OK"){_,_->
            listener.onClick(null)
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE,"NO"){_,_->

            dialog.dismiss()
        }
        dialog.show()
    }

    fun enterCity(context: Context,listener:Listener){
        val builder = AlertDialog.Builder(context)
        val cityName =EditText(context)
        builder.setView(cityName)
        EditText(context).hint = "For example: Uralsk, Almaty, Astana"
        EditText(context).gravity
        val dialog = builder.create()
        dialog.setTitle("Enter your city:")
        dialog.setMessage("Please enter in search, only English names.  ")

        dialog.setButton(AlertDialog.BUTTON_POSITIVE,"Enter"){_,_->
            listener.onClick(cityName.text.toString())
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE,"Close"){_,_->

            dialog.dismiss()
        }
        dialog.show()
    }
    interface Listener{
        fun onClick(city: String?)
    }
}