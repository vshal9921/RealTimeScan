package com.vshal9921.realtimescan

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage

class MainActivity : AppCompatActivity() {

    lateinit var mqttAndroidClient : MqttAndroidClient
    val clientId = MqttClient.generateClientId();
    var serverUri = "tcp://192.168.1.41:1883"
    var subscriptionTopic = "server/msg"
    val LOG = "mqtt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        mqttAndroidClient = MqttAndroidClient(this, serverUri, clientId)
        mqttAndroidClient.setCallback(object : MqttCallback{

            override fun connectionLost(cause: Throwable?) {
                Log.d(LOG, "connection lost =" + cause.toString())

            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                if (message != null) {
                    Log.d(LOG, "message received =" + String(message.payload))
                }

            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {

            }

        })

        var mqttConnectOptions = MqttConnectOptions()
        mqttConnectOptions.isAutomaticReconnect = true
        mqttConnectOptions.isCleanSession = true

        try {
            mqttAndroidClient.connect(mqttConnectOptions, null, object : IMqttActionListener{

                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(LOG, "subscription successful")
                    mqttAndroidClient.subscribe(subscriptionTopic, 2)

                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(LOG, "connection failed = ")
                    exception?.printStackTrace()
                }

            })
        }
        catch (e : Exception){
            Log.d(LOG, e.printStackTrace().toString());

        }
    }
}