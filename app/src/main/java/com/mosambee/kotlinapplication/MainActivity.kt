package com.mosambee.kotlinapplication

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.mosambee.notification_sdk.callback_listener.NotificationListener
import com.mosambee.notification_sdk.device_registration.StartDeviceRegistration
import com.mosambee.notification_sdk.enumeration.ResultAction
import com.mosambee.notification_sdk.model.MqttRequestData
import com.mosambee.notification_sdk.utils.TRACE
import org.json.JSONObject

class MainActivity : AppCompatActivity(), NotificationListener {

    lateinit var btnClick: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnClick = findViewById(R.id.btnClick) as Button;
        btnClick.setOnClickListener {
            /*Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@MainActivity, MainActivity2::class.java)
            intent.putExtra("test","Working!...")
            startActivity(intent)*/
            //String jsonStr = "{\"deviceSerialNo\":\"141901255\",\"deviceCategory\":\"0\",\"deviceRegistrationMode\":\"0\",\"serverApiVersion\":0,\"deviceMake\":\"Morefun\",\"deviceModel\":\"MF919\",\"devicePlatform\":\"android\",\"deviceAppVersion\":\"1.1.1.141\",\"locationDetail\":{\"detailCategory\":\"0\",\"latlongDetail\":{\"latitude\":\"0.00000000\",\"longitude\":\"0.00000000\"}}}";

            /*        try {
            String decryptedData = TransactionEncUtil.decrypt("58196A648DF4C2C5F0450EE72F2AF3002EC1B02EAEE0FCA25BEF2DB710FA3D4CCB5CBA3CED6A82FC6C94E9B185C0D77A57EAC73AC5D37860D4F0077B49FB6AF8E4087F066F6C044F901F468A7235C2D00184F3C1FD3EC08A7769F7BCD0716F3396756A7D93FE983B2FF9873FEF3217118745AFD7D6FAA2BDCBC1470AE6900EC8",
                    "93CB3CC00112740DB40A668C1810300B05623697200F68D70AEC748FC1B6F919",
                "1CB58A09039ED75B30721C032F621AF8");
            Log.e("decriptedData",decryptedData);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }*/
           val mqttRequestData = MqttRequestData()
            mqttRequestData.setDeviceId("14139451") //14139451 141261887
            mqttRequestData.setEnvironment("test")
            mqttRequestData.setLatitude("19.0000000")
            mqttRequestData.setLongitude("72.0000000")
            mqttRequestData.setDeviceRegistrationMode(0.toString())
            StartDeviceRegistration.getInstance(this@MainActivity)
                ?.registerNotificationService(this, mqttRequestData)

            // Created a Work Request
            //val uploadWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<WorkerClass>().build()

            // Submit the WorkRequest to the system
           // WorkManager.getInstance(this).enqueue(uploadWorkRequest)
        }

    }

    override fun onMessageArrived(response: JSONObject?) {
        TRACE.d("onMessageArrived:::::::::" + response)
    }

    override fun onFailed(resultAction: ResultAction?, message: String?) {
        TRACE.d("onFailed:::::::::" + resultAction + "\nMessage:::::::::" + message)
    }

    override fun onSuccess(message: String?) {
        TRACE.d("onSuccess:::::::::" + message)
    }

    override fun onSoundPayingStatus(isComplete: Boolean) {
        TODO("Not yet implemented")
    }

}