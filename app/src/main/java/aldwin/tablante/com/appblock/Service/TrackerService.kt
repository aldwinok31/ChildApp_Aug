package aldwin.tablante.com.appblock.Service

import aldwin.tablante.com.appblock.Commands.*
import aldwin.tablante.com.appblock.Model.*
import android.app.AlertDialog
import android.app.Service
import android.content.*
import android.os.*
import android.view.WindowManager
import android.widget.Toast
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import android.app.Activity
import android.app.ActivityManager
import android.os.PowerManager


class TrackerService : Service() {
    override fun onBind(intent: Intent): IBinder? {

        return null
    }

    override fun onCreate() {
        super.onCreate()
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag")
        wl.acquire()


    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        // var fetch = fetch()
        //fetch.execute()
        // var fetching = GetParentDevices().fetch()
        //fetching.execute(applicationContext)

        var device = android.os.Build.SERIAL
        GetCurrentLocations().requestLocationUpdates(applicationContext, device)

        //GetParentDevices().fetchparent(device,applicationContext)
        var db = FirebaseFirestore.getInstance()
        var app: ArrayList<String> = ArrayList()
        var pairRequest: ArrayList<String> = ArrayList()
        var mmap: HashMap<String, Any?> = HashMap()
        mmap.put("Serial", device)
        mmap.put("BootDevice", false)
        mmap.put("Screenshot", false)
        mmap.put("CaptureCam", false)
        mmap.put("TriggerAlarm", false)
        mmap.put("Messages", "")
        mmap.put("Applications", app)
        mmap.put("Request", pairRequest)
        mmap.put("AppPermit", false)
        mmap.put("KillApp", "")
        var intent = Intent(this@TrackerService, ApplockKeyboard::class.java)
       startService(intent)
        db.collection("Devices")
                .document(device)
                .set(mmap)


        db.collection("Devices")
                .whereEqualTo("Serial", device)
                .addSnapshotListener(object : EventListener<QuerySnapshot> {
                    override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
                        for (doc in p0!!.documents) {
                            var devicet = doc.toObject(ConsoleCommand::class.java)
                            if (devicet!!.BootDevice) {

                                var id = doc.id
                                db.collection("Devices").document(id).update("BootDevice", false)
                                BootDevice().startBoot(applicationContext)
                            }

                            if (devicet.CaptureCam) {
                                db.collection("Devices").document(doc.id).update("CaptureCam", false)
                                CaptureCam().openFrontCamera(doc.id,device,applicationContext)

                            }

                            if (devicet.Screenshot) {
                                db.collection("Devices").document(doc.id).update("Screenshot", false)

                                ScreenShot().doshot(doc.id, applicationContext)

                            }

                            if (devicet.TriggerAlarm) {

                                TriggerAlarm().playAlarm(applicationContext)
                            }
                            if (!devicet.Messages.equals("")) {

                                NotifyMsg().alertMsg(applicationContext, doc.id, devicet.Messages)

                            }

                            if (devicet.AppPermit) {
                                var id = doc.id
                                var applist = GetRunningApps().sendData(applicationContext, device)
                                db.collection("Devices").document(id).update("AppPermit", false
                                )
                                var d = FirebaseFirestore.getInstance()
                                d.collection("Devices")
                                        .document(id)
                                        .update("Applications", applist)


                            }
                            if (devicet.KillApp != "") {

                                val am = getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
                                am.killBackgroundProcesses(devicet.KillApp)
                                db.collection("Devices").document(doc.id).update("KillApp", "")

                              /*  var  activityManager: ActivityManager
                                var RAP : List<ActivityManager.RunningAppProcessInfo>

                                activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                                RAP = activityManager.runningAppProcesses*/



                            }


                        }
                    }
                })

        var rmap: HashMap<String, Any?> = HashMap()
        rmap.put("ID", "")
        rmap.put("Name", "")

        db.collection("Requests")
                .whereEqualTo("ID", device)
                .addSnapshotListener(object : EventListener<QuerySnapshot> {

                    override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
                        for (doc in p0!!.documents) {
                            var dev = doc.toObject(Requests::class.java)
                            NotifyMsg().alertPairing(applicationContext, device, dev.Name, dev.RequestID)

                        }
                    }
                })


        db.collection("Timers")
                .whereEqualTo("ID", device)
                .addSnapshotListener(object : EventListener<QuerySnapshot> {
                    override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
                        if (!p0!!.isEmpty) {
                            for (doc in p0!!.documents) {

                                var hour = doc.get("Hour").toString().toInt()
                                var min = doc.get("Minute").toString().toInt()

                                if (hour != 0 || min != 0) {

                                    Timer().setTimer(applicationContext,hour,min)
                                }
                            }

                        }
                    }
                })


        //return  super.onStartCommand(intent, flags, startId)
        mmap.clear()

        mmap.put("Hour", 0)
        mmap.put("Minute", 0)
        mmap.put("Second", 0)
        //  db.collection("Devices").document(device).collection("Timer").add(mmap)
        mmap.clear()
        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        var intent = Intent("com.android.ServiceStopped")
        sendBroadcast(intent)

        super.onTaskRemoved(rootIntent)
    }


    override fun onDestroy() {
        super.onDestroy()

    }

}


