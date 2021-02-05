package com.fajaradisetyawan.githubuser.view.activity

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.fajaradisetyawan.githubuser.R
import com.fajaradisetyawan.githubuser.notification.AlarmReceiver
import kotlinx.android.synthetic.main.activity_setting.*
import www.sanju.motiontoast.MotionToast

class SettingActivity : AppCompatActivity(), View.OnClickListener {
    private val alarmReceiver = AlarmReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        //change language
        btn_changelanguage.setOnClickListener{
            //show AlertDialog to setting language
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }

        sc_daily.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (sc_daily.isChecked){
            this.let { alarmReceiver.setRepeatingAlarm(it) }
            MotionToast.createToast(this,
                    "Remainder ️",
                    "Remainder Trun On",
                    MotionToast.TOAST_SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this,R.font.lato))
        }else{
            this.let { alarmReceiver.cancelAlarm(it) }
            MotionToast.createToast(this,
                    "Remainder️",
                    "Remainder Turn Off",
                    MotionToast.TOAST_INFO,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this,R.font.lato))
        }
    }
}