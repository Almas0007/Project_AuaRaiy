package blim.enbek.talpynys.example_auaraiy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import blim.enbek.talpynys.example_auaraiy.databinding.ActivityMainBinding
import blim.enbek.talpynys.example_auaraiy.fragments.Main1_Fragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportFragmentManager.beginTransaction()
            .replace(R.id.mainActivityConstrait, Main1_Fragment.newInstance())
            .commit()
    }


}