package com.vshal9921.realtimescan

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.vshal9921.realtimescan.org.eclipse.paho.android.service.MqttAndroidClient
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
    var serverUri = "tcp://192.168.71.191:1883"
    var subscriptionTopic = "server/msg"
    val LOG = "mqtt"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val textView = findViewById<AppCompatTextView>(R.id.textView)

        mqttAndroidClient = MqttAndroidClient(this, serverUri, clientId)
        mqttAndroidClient.setCallback(object : MqttCallback{

            override fun connectionLost(cause: Throwable?) {
                Log.d(LOG, "connection lost =" + cause.toString())
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Connection Lost. Reconnecting ....", Toast.LENGTH_LONG).show()
                }
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                if (message != null) {
                    Log.d(LOG, "message received =" + String(message.payload))
                    textView.text = String(message.payload)
                }
                mqttAndroidClient.publish("client/ack", MqttMessage("Received".toByteArray()))
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {

            }

        })

        var mqttConnectOptions = MqttConnectOptions()
        mqttConnectOptions.isAutomaticReconnect = true
        mqttConnectOptions.isCleanSession = false

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

        // Front camera for QR Scan

        var scanOptions = ScanOptions()
        scanOptions.setDesiredBarcodeFormats(ScanOptions.ONE_D_CODE_TYPES)
        scanOptions.setPrompt("Scan a bar code")
        scanOptions.setCameraId(1)
        scanOptions.setBeepEnabled(false)
        scanOptions.setBarcodeImageEnabled(true)
        barcodeLauncher.launch(scanOptions)
    }

    private val barcodeLauncher = registerForActivityResult<ScanOptions, ScanIntentResult>(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
        }
    }
}