package cian911.source

import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext
import org.eclipse.paho.client.mqttv3._
import java.nio.charset.StandardCharsets
import scala.util.Try
import java.util.Properties
import javax.net.ssl.SSLSocketFactory
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import scala.util.parsing.json.JSON
import cian911._

case class SensorData(
    co2: Double,
    pressure: Double,
    timestamp: Double,
    nodeId: Double,
    temperature: Double,
    publishedAt: Double
)

class HiveMqttSource extends RichParallelSourceFunction[String] {

  lazy val client: MqttClient = new MqttClient(
    settings.mqttSettings.clientUrl,
    MqttClient.generateClientId()
  )
  lazy val waitLock: Object = new Object()
  @volatile var running: Boolean = false

  override def run(ctx: SourceContext[String]): Unit = {
    val connectionOpts: MqttConnectOptions = new MqttConnectOptions()
    connectionOpts.setCleanSession(true)
    connectionOpts.setUserName(settings.mqttSettings.username)
    connectionOpts.setPassword(
      settings.mqttSettings.password.toCharArray()
    )
    connectionOpts.setKeepAliveInterval(0)
    connectionOpts.setSocketFactory(SSLSocketFactory.getDefault())
    connectionOpts.setAutomaticReconnect(true)

    client.connect(connectionOpts)

    client.setCallback(new MqttCallback() {
      override def messageArrived(topic: String, message: MqttMessage): Unit = {
        val msg: String =
          new String(message.getPayload(), StandardCharsets.UTF_8)
        ctx.collect(msg)
      }

      override def connectionLost(x: Throwable): Unit = {
        print("Connection Lost: " + x)
      }

      override def deliveryComplete(x: IMqttDeliveryToken): Unit = ???
    })

    client.subscribe(
      settings.mqttSettings.topic
    )

    running = true

    while (running) {
      Thread.sleep(1000L)
    }
  }

  override def cancel(): Unit = {
    running = false
    client.disconnect()
  }
}
