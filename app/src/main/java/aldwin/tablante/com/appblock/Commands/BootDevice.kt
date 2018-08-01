package aldwin.tablante.com.appblock.Commands

import android.Manifest.permission.REBOOT
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.os.SystemClock
import android.content.Context.POWER_SERVICE
import android.os.PowerManager
import android.support.constraint.solver.SolverVariable
import android.widget.Toast
import com.chrisplus.rootmanager.RootManager
import java.security.Permission

import java.io.DataOutputStream


class BootDevice {

    fun startBoot(context: Context){
        var  p = Runtime.getRuntime().exec("fastboot device")
        p.waitFor()



        if(RootManager.getInstance().hasRooted()) {



        }
        else{
            val manager =context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
            manager.lockNow()
            Toast.makeText(context.applicationContext,"No ROOT",Toast.LENGTH_SHORT).show()

        }
        try {
            var  powerManager:PowerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

          powerManager.reboot("No Reason")


       }
       catch (e:SecurityException){
           Toast.makeText(context.applicationContext,"Unable to Shutdown",Toast.LENGTH_SHORT).show()

       }
    }
}