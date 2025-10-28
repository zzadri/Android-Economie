package com.example.projet1diiage.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

object VibrationUtil {
    fun vibrate(context: Context, durationMs: Long = 80) {
        val v = context.getSystemService(Vibrator::class.java) ?: return
        if (v.hasVibrator()) {
            v.vibrate(VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

}

@Composable
fun rememberVibration(): (Long) -> Unit {
    val context = LocalContext.current
    return { duration -> VibrationUtil.vibrate(context, duration) }
}
