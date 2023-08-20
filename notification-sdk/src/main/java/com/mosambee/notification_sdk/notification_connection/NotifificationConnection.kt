package com.mosambee.notification_sdk.notification_connection

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.mosambee.notification_sdk.callback_listener.NotificationListener
import com.mosambee.notification_sdk.enumeration.ResultAction
import com.mosambee.notification_sdk.model.DeviceRegistrationResponse
import com.mosambee.notification_sdk.utils.TRACE
import info.mqtt.android.service.Ack
import info.mqtt.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.json.JSONObject

class NotifificationConnection : Service() {


    companion object {
        @SuppressLint("StaticFieldLeak")
        private var notifificationConnection: NotifificationConnection? = null
        fun getInstance(): NotifificationConnection? {
            if (notifificationConnection == null) {
                synchronized(NotifificationConnection::class.java) {
                    if (notifificationConnection == null) notifificationConnection =
                        NotifificationConnection()
                }
            }
            return notifificationConnection
        }

        /*Data Initialization For MqttClient */
        private var topic: String? = null
        private var mqttQos: String? = null
        private var cleanSession: String? = null
        private var mqttUsername: String? = null
        private var mqttClientId: String? = null

        @SuppressLint("StaticFieldLeak")
        private var mqttAndroidClient: MqttAndroidClient? = null
        private var serverUri: String? = null

        @SuppressLint("StaticFieldLeak")
        private var context: Context? = null
        var responseListener: NotificationListener? = null

        @Synchronized
        fun buildMqttClient(
            mcontext: Context?,
            notificationResponseListener: NotificationListener?,
            requestData: DeviceRegistrationResponse
        ) {
            this.context = mcontext
            this.responseListener = notificationResponseListener
            serverUri = requestData.getEnvNotificationConnect(requestData.getNotificationURL()!!)
            topic = requestData.getNotificationDetails()!!.mqttSubscribeTopic
            mqttQos = requestData.getNotificationDetails()!!.mqttQos
            cleanSession = requestData.getNotificationDetails()!!.mqttCleanSession
            mqttUsername = requestData.getNotificationDetails()!!.mqttUserName
            mqttClientId = requestData.getNotificationDetails()!!.mqttClientId
        }
    }


    /*MQTT Subscribe topic*/
    private fun subscribeToTopic(client: MqttAndroidClient?, topic: String?, mqttQos: String) {
        try {
            TRACE.d("SUBSCRIBE_TOPIC:::::: $topic")
            val subToken = client!!.subscribe(topic!!, mqttQos.toInt())
            subToken.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    responseListener!!.onSuccess("Notification Connected Successfully!")
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    TRACE.d("MQTT_SUBSCRIBE:::onFailure::: " + exception.message)
                    responseListener!!.onFailed(
                        ResultAction.SUBSCRIBE_TO_TOPIC_FAILED,
                        "Failed to subscribe to the topic"
                    )
                }
            }
        } catch (exception: Exception) {
            TRACE.d("MQTT_SUBSCRIBE:::Exception::: " + exception.message)
            responseListener!!.onFailed(
                ResultAction.SOMETHING_WENT_WRONG,
                "Failed to subscribe to the topic"
            )
        }
    }

    private fun unSubscribeToTopic(client: MqttAndroidClient?, topic: String?) {
        try {
            TRACE.d("UNSUBSCRIBE_TOPIC:::::: $topic")
            val subToken = client!!.unsubscribe(topic!!)
            subToken.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    responseListener!!.onSuccess("Notification Connected Successfully!")
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    TRACE.d("MQTT_SUBSCRIBE:::onFailure::: " + exception.message)
                    responseListener!!.onFailed(
                        ResultAction.SUBSCRIBE_TO_TOPIC_FAILED,
                        "Failed to subscribe to the topic"
                    )
                }
            }
        } catch (exception: Exception) {
            TRACE.d("MQTT_SUBSCRIBE:::Exception::: " + exception.message)
            responseListener!!.onFailed(
                ResultAction.SOMETHING_WENT_WRONG,
                "Failed to subscribe to the topic"
            )
        }
    }
    private fun disconnect(client: MqttAndroidClient?, topic: String?) {
        try {
            TRACE.d("DISCONNECT:::::: $topic")
            val subToken = client!!.disconnect(500)
            subToken.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    responseListener!!.onSuccess("Disconnected Successfully!")
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    TRACE.d("MQTT_SUBSCRIBE:::onFailure::: " + exception.message)
                    responseListener!!.onFailed(
                        ResultAction.SUBSCRIBE_TO_TOPIC_FAILED,
                        "Failed to subscribe to the topic"
                    )
                }
            }
        } catch (exception: Exception) {
            TRACE.d("MQTT_SUBSCRIBE:::Exception::: " + exception.message)
        }
    }

    /*Build MQTT user passowrd basis host pre-defined pattern*/
    private fun getPassword(deviceSrNo: String): String {
        val revDeviceSrNo = StringBuilder().append(deviceSrNo).reverse()
        return "Mq@$revDeviceSrNo#Mos"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        TRACE.d("onStartCommand methode called")
        val r = Runnable { initNotification() }
        val backgroundThread = Thread(r)
        backgroundThread.start()
        return START_STICKY
    }

    private fun initNotification() {
        try {

            if (mqttAndroidClient != null) {
                mqttAndroidClient!!.close()
            }
            if (mqttAndroidClient == null) {
                val mqttConnectOptions = MqttConnectOptions()
                mqttConnectOptions.mqttVersion = MqttConnectOptions.MQTT_VERSION_3_1_1
                mqttConnectOptions.isCleanSession =
                    cleanSession.equals("1", ignoreCase = true)
                mqttConnectOptions.maxInflight = 100
                mqttConnectOptions.isAutomaticReconnect = true
                mqttConnectOptions.keepAliveInterval = 60
                mqttConnectOptions.connectionTimeout = 30
                mqttConnectOptions.userName = mqttUsername
                mqttConnectOptions.password = getPassword(mqttClientId.toString()).toCharArray()
                val client = MqttAndroidClient(
                    context!!.applicationContext,
                    serverUri!!, mqttClientId.toString(), Ack.AUTO_ACK
                )
                client.setCallback(object : MqttCallbackExtended {
                    override fun connectComplete(reconnect: Boolean, serverURI: String) {
                        TRACE.d("MQTT::::::CALLBACK:::CONNECTION_COMPLETE::: " + reconnect + " :: " + mqttAndroidClient.toString())
                        subscribeToTopic(mqttAndroidClient, topic, mqttQos.toString())
                    }

                    override fun connectionLost(cause: Throwable) {
                        TRACE.d("connectionLost::: " + cause.message)
                        responseListener!!.onFailed(ResultAction.CONNECTION_LOST, "Connection lost")
                    }

                    @Throws(Exception::class)
                    override fun messageArrived(topic: String, message: MqttMessage) {
                        try {
                            val jsonObject = JSONObject(message.toString())
                            responseListener!!.onMessageArrived(jsonObject)
                            TRACE.d("messageArrived:::$message")
                        } catch (exception: Exception) {
                            responseListener!!.onFailed(
                                ResultAction.SOMETHING_WENT_WRONG,
                                "Message arrived failed"
                            )
                        }
                    }

                    override fun deliveryComplete(token: IMqttDeliveryToken) {
                        TRACE.d("MQTT_CALLBACK::::::DELIVERY:::$token")
                    }
                })

                client.connect(mqttConnectOptions)

                TRACE.d("AFTER_MQTT_CONNECT_METHOD_CALL::::::::::")
                mqttAndroidClient = client
                TRACE.d("MQTT_CONNECTION_OBJECT_CREATED:::::::::" + mqttAndroidClient.toString())
            } else {
                TRACE.d("isConnected:::::::::" + mqttAndroidClient!!.isConnected)
                if (mqttAndroidClient!!.isConnected) {

                }
                responseListener!!.onSuccess("Notification Already Connected!!")
                TRACE.d("MQTT_CONNECTION_OBJECT_ALREADY_EXIST:::::::" + mqttAndroidClient.toString())
            }
        } catch (e: Exception) {
            responseListener!!.onFailed(
                ResultAction.NOTIFICATION_CONNECTION_FAILED,
                "Notification connection failed!"
            )
            TRACE.d("EXCEPTION_IN_MQTT_CONNECTION::::::::::" + e.message)
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
        //return null
    }

    override fun onDestroy() {
        TRACE.d("onDestroy methode called from service")
    }


}

private fun MqttAndroidClient.disconnect(quiesceTimeout: IMqttActionListener) {

}



