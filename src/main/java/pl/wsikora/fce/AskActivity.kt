package pl.wsikora.fce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import pl.wsikora.fce.databinding.ActivityAskBinding


class AskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityAskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.headerAsk.section = "Podsumowanie"

        binding.askHeader.text = "Pomóż ulepszyć dokładność"
        binding.fuelConsumptionInput.hint = "podaj osiągnięte spalanie"

        val refuseButton = binding.refuse
        refuseButton.text = "innym razem"
        refuseButton.setOnClickListener { startActivity(Intent(baseContext, MainActivity::class.java)) }

        val sendForm = binding.sendForm
        sendForm.text = "prześlij"
        sendForm.setOnClickListener { startActivity(Intent(baseContext, MainActivity::class.java)) }

    }


}