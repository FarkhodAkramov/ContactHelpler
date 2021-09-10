package com.example.contacthelpler

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.contacthelpler.adapter.ContactAdapter
import com.example.contacthelpler.databinding.ActivityMainBinding
import com.example.contacthelpler.databinding.ActivitySecondBinding
import com.example.contacthelpler.models.Contact

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val contact = intent.extras?.getSerializable("contact") as Contact


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(getResources().getColor(R.color.whitee, resources.newTheme()))
        }
        binding.nameTv.text = contact.name
        binding.numberTv.text = contact.number
        binding.sendBtn.setOnClickListener {
            if (binding.messageEt.text.toString().isNotEmpty() && binding.numberTv.text.toString().isNotEmpty()) {
                sendingSms(contact.number.toString(), binding.messageEt.text.toString())
            }
            else{
                Toast.makeText(this, "Message or Number is empty!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

    }

    fun sendingSms(number:String,message:String){

        val pendIntent = PendingIntent.getActivity(this,
        0, Intent("SMS sent"), 0)
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(number, null, message, pendIntent, null)
        Toast.makeText(this, "Message sent!", Toast.LENGTH_SHORT).show()
        var result = "MESSAGE: $message\nSENT TO NUMBER: $number"
        binding.resultEt.text = result

        binding.messageEt.text.clear()
    }
}