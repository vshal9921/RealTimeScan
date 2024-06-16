# Real Time Scan

> An android app that communicates with a local server using a reliable, low-latency communication protocol (MQTT).

> The application keeps the front camera of the device active for scanning QR/barcodes in parallel with receiving messages.

> Every 2 seconds, the server will sends a message to the app, which the app acknowledges after receiving.  <br />

## Libraries used

1) MQTT - Eclipse Paho MQTT Android<br/>
   Link - https://github.com/eclipse/paho.mqtt.android<br/>
   
2) Barcode Scanner - journeypp/zxing-android-embedded<br/>
   Link - https://github.com/journeyapps/zxing-android-embedded<br/>

## Server setup:

MQTT Aedes Broker using Node.js

File link - [https://drive.google.com/file/d/1k2KCNPM-hCv5JIj1dW2q7_wrX_rKcFu9/view?usp=sharing](https://github.com/vshal9921/RealTimeScan/blob/master/aedesServer.js)


## App logo

![app logo scann](https://github.com/vshal9921/RealTimeScan/assets/46436486/7c27faee-a393-4de2-af8f-72b624ce6198)

