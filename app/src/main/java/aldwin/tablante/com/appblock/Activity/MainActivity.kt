package aldwin.tablante.com.appblock.Activity

import aldwin.tablante.com.appblock.R
import aldwin.tablante.com.appblock.Service.TrackerService
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.storage.StorageMetadata
import android.provider.MediaStore




class MainActivity : Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var intent = Intent(this@MainActivity, TrackerService::class.java)
                .setAction("enable_capture")


        startService(intent)


      finish()

    }
}
