package cian911

import com.typesafe.config.Config
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration

object Settings {
  implicit class RichConfig(config: Config) {

    def getScalaDuration(path: String): FiniteDuration =
      duration.Duration.fromNanos(config.getDuration(path).toNanos())
  }
}

class Settings(val config: Config) extends Serializable {
  import Settings._

  val applicationName = config.getString("application-name")

  val flinkSettings = FlinkSettings(
    config.getInt("flink.parallelism"),
    config.getScalaDuration("flink.watermark-interval"),
    config.getInt("flink.tm-slots")
  )

  val mqttSettings = MqttSettings(
    config.getString("mqtt-source.client-url"),
    config.getString("mqtt-source.username"),
    config.getString("mqtt-source.password"),
    config.getString("mqtt-source.topic")
  )
}

case class FlinkSettings(
    parallelism: Int,
    watermarkInterval: FiniteDuration,
    taskManagerSlots: Int
)

case class MqttSettings(
    clientUrl: String,
    username: String,
    password: String,
    topic: String
)
