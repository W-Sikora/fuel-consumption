package pl.wsikora.fce

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.wsikora.fce.databinding.ActivityMainBinding
import android.provider.Settings.Secure
import android.util.Log
import android.widget.Button
import androidx.core.view.get

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.hide()
        setContentView(binding.root)

        initializeSection()
        initializeInfo()
        initializeEngines()

        val id = getAndroidId()
        val button = initializeButton()


        button.setOnClickListener {
            val checkedEngine = getEngineType()
            Log.d("--------", "$checkedEngine")
            startActivity(Intent(baseContext, DashboardActivity::class.java))
        }

    }



    @SuppressLint("HardwareIds")
    private fun getAndroidId(): String {
        return Secure.getString(applicationContext.contentResolver,
                Secure.ANDROID_ID)
    }

    private fun initializeSection() {
        binding.infoHeader.section = "Start"
    }

    private fun initializeInfo() {
        binding.headerInfo.text = "O aplikacji"
        binding.textInfo.text = "Proin nec tristique erat, dapibus fringilla urna. Integer et diam turpis. Curabitur eu maximus risus, non ullamcorper lacus. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Aliquam tincidunt ante ornare eros laoreet viverra ut vitae augue. Sed posuere felis ac nunc viverra laoreet. Ut at nunc et turpis scelerisque ornare et a massa."
    }

    private fun initializeEngines() {
        binding.enginesHeader.text = "Wybierz pojemność silnika"
        binding.engine1.text = "do 1.4 l"
        binding.engine2.text = "od 1.4 do 2.0 l"
        binding.engine3.text = "powyżej 2.0 l"

    }

    private fun initializeButton(): Button {
        val button = binding.infoFooter.button
        button.text = "dalej"
        return button
    }

    private fun getEngineType(): Int {
        val checkedEngineId = binding.radioGroup.checkedRadioButtonId
        val enginesNb = binding.radioGroup.childCount
        for (i in 0..enginesNb) {
            if (binding.radioGroup[i].id == checkedEngineId) {
                return i
            }
        }
        return -1
    }

}
