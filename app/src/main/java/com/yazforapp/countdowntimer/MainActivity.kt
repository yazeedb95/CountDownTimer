package com.yazforapp.countdowntimer

import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.util.Log
import android.widget.*
import androidx.core.view.get
import com.yazforapp.countdowntimer.databinding.ActivityMainBinding

var isRuning = false
var timer: CountDownTimer? = null
var initealTime:Long = 25 * 1000 * 60
var timeUpdat:Long = initealTime

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.numberPicker.apply {
            minValue = 0
            maxValue = 60
            setOnValueChangedListener { picker, oldVal, newVal ->
                val min = value
                initealTime = value.toLong().times(1000).times(60)
                binding.timerText.text = min.toString()
            }
        }


        binding.apply {
            btnStart.setOnClickListener {
                if (!isRuning) {
                    startTimer(initealTime)
                    textTakePromo.text = resources.getText(R.string.keepGoing)
                }
            }

            resetText.setOnClickListener {
                resetTimeer()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("timeSaved", timeUpdat)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val value = savedInstanceState.getLong("timeSaved")
        if (value != initealTime){
            startTimer(value)
        }
    }

    private fun startTimer(startTime:Long){
        timer = object :CountDownTimer(startTime, 1000){
            override fun onTick(timeLift: Long) {
                timeUpdat = timeLift
                updateTimeer()
                binding.progressBar.progress = timeUpdat.div(initealTime.toDouble()).times(100).toInt()
            }

            override fun onFinish() {
                Toast.makeText(this@MainActivity,"time is over", Toast.LENGTH_SHORT).show()
                isRuning = false
            }

        }.start()
        isRuning = true
    }

    private fun resetTimeer() {
        timer?.cancel()
        timeUpdat = initealTime
        updateTimeer()
        binding.textTakePromo.text = resources.getText(R.string.takePromodoro)
        binding.progressBar.progress = timeUpdat.toInt()
        isRuning = false

    }

    private fun updateTimeer(){
        val menuts = timeUpdat.div(1000).div(60)
        val secund = timeUpdat.div(1000) % 60
        val timeFormating = String.format("%02d:%02d", menuts, secund)
        binding.timerText.text = timeFormating
    }

}