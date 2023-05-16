package com.setonMyProjects.birthdayreminder

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.widget.Toast
import android.os.Vibrator
import android.os.VibratorManager

class VibratingToast(context: Context, text: CharSequence?, duration: Int) : Toast(context) {
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val v = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            v.defaultVibrator.vibrate(VibrationEffect.createOneShot(500,
                VibrationEffect.DEFAULT_AMPLITUDE))
        }
        else {
            val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            v.vibrate(300)
        }

        Toast.makeText(context, text, duration).show()
    }
}