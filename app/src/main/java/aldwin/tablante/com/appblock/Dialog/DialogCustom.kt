package aldwin.tablante.com.appblock.Dialog

import aldwin.tablante.com.appblock.R
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v7.widget.AlertDialogLayout
import android.view.Window.FEATURE_NO_TITLE
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Camera
import android.os.Environment
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.wonderkiln.camerakit.*
import kotlinx.android.synthetic.main.camera_view.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class DialogCustom {

    fun showDialog(context: Context, msg: String) {

        val dialog: Dialog = Dialog(context.applicationContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.camera_view)

        val camview = dialog.findViewById<CameraView>(R.id.cameraView)
        camview.start()
        camview.facing = android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT
        camview.addCameraKitListener(object : CameraKitEventListener {
            override fun onError(p0: CameraKitError?) {
                null
            }

            override fun onEvent(p0: CameraKitEvent?) {
                null
            }

            override fun onImage(p0: CameraKitImage?) {
                val videoUri = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                Toast.makeText(context.applicationContext,p0!!.bitmap.toString(), Toast.LENGTH_SHORT).show()


                val f = File(videoUri, "ChildApp.png")
                f.createNewFile()
                val bos = ByteArrayOutputStream()
                p0!!.bitmap.compress(Bitmap.CompressFormat.PNG,0,bos)
                val bitmapdata = bos.toByteArray()
                val fos = FileOutputStream(f)
                fos.write(bitmapdata)
                fos.flush()
                fos.close()




             }

            override fun onVideo(p0: CameraKitVideo?) {
                null
            }
        })





        val dialogText = dialog.findViewById<TextView>(R.id.tView)
        val dialogButton = dialog.findViewById<Button>(R.id.buttonCapture)
        dialogButton.setOnClickListener {

            camview.captureImage()



            Thread.sleep(3000)
            camview.stop()
            dialog.dismiss()


        }
        dialog.window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        dialog.show()



    }

    fun getCurrentTime(): Date {

        val calendar = Calendar.getInstance()
        val mdformat = SimpleDateFormat("HH:mm:ss")
        val strDate =  calendar.time
        return strDate


    }


}



