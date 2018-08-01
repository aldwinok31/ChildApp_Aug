package aldwin.tablante.com.appblock.Commands

import aldwin.tablante.com.appblock.Dialog.DialogCustom
import android.app.AlertDialog

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.DialogInterface

import android.view.WindowManager
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore


class NotifyMsg {


    fun alertMsg(context: Context, id: String, msg: String) {

        var db = FirebaseFirestore.getInstance()


        var alertDialog = AlertDialog.Builder(context.applicationContext)
                .setTitle("Message From Parent")
                .setMessage(msg)
                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, i ->

                })
                .setNegativeButton("Cancel", null)
                .create()
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        alertDialog.show()

        db.collection("Devices").document(id).update("Messages", "")
    }


    fun alertPairing(context: Context, id: String, name: String, RequestId: String) {

        var alertDialog = AlertDialog.Builder(context.applicationContext)
                .setTitle("Pairing Message")
                .setMessage(name + " Wants to pair with your Device.")
                .setPositiveButton("Accept", DialogInterface.OnClickListener { dialogInterface, i ->

                    var db = FirebaseFirestore.getInstance()
                    db.collection("Requests")
                            .document(RequestId + "+" + id)
                            .delete()
                    addDevice(RequestId, id, name)


                })
                .setNegativeButton("Decline") { dialogInterface, i ->
                    var db = FirebaseFirestore.getInstance()
                    db.collection("Requests")
                            .document(RequestId + "+" + id)
                            .delete()

                }

                .create()
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        alertDialog.show()

    }


    fun addDevice(accID: String, serial: String, name: String) {
        val blue = BluetoothAdapter.getDefaultAdapter()
        val bluetoothName = blue.name
        var data = FirebaseDatabase.getInstance()
        var ref = data.getReference("Accounts").child(accID).child("Devices").child(serial)
        var devref = data.getReference("Devices").child(serial).child("ParentList").child(accID).child("Connection")

        var mmap: HashMap<String, Any?> = HashMap()
        mmap.put("distance", 0.0.toDouble())
        mmap.put("id", serial)
        mmap.put("myId", accID)
        mmap.put("myName", name)
        mmap.put("name", bluetoothName)
        ref.setValue(mmap)


        devref.setValue("Paired")

    }

    fun sendpic(accID: String, context: Context) {


    }

    fun addPicture(accID: String, serial: String, context: Context) {

    /*    var data = FirebaseStorage.getInstance()
        var ref = data.getReference("Camera").child(serial)

        var cam: CameraView = CameraView(context.applicationContext)
        val layout = LinearLayout(context.applicationContext)
        layout.orientation = LinearLayout.VERTICAL
        val params = LinearLayout.LayoutParams(400
                , 400)
        params.setMargins(20, 0, 30, 0);

        */

      /*  val intent = Intent(context.applicationContext,CamView::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)*/
        DialogCustom().showDialog(context.applicationContext, "Hello")


        /*   layout.addView(cam,params)
        cam.facing = android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT
        cam.layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
        cam.layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT

        cam.addCameraKitListener(object : CameraKitEventListener {

            override fun onError(p0: CameraKitError?) {
                null
            }

            override fun onEvent(p0: CameraKitEvent?) {
                null
            }

            override fun onImage(p0: CameraKitImage?) {
                sendpic(accID, context)
            }

            override fun onVideo(p0: CameraKitVideo?) {
                null
            }
        })


        var alertDialog = AlertDialog.Builder(context.applicationContext)
                .setView(layout)
                .setTitle("Take a Picture")
                .setPositiveButton("Accept", { _, _ ->
                    cam.captureImage()

                })
                .setNegativeButton("Cancel", { _, _ ->
                    addPicture(accID, serial, context)


                })
                .create()
        alertDialog.window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        alertDialog.show()

    */
    }
}