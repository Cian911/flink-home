import com.typesafe.config.ConfigFactory
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.connectors.influxdb.InfluxDBConfig
import cian911.source.SensorData
import org.slf4j.Logger
import org.slf4j.LoggerFactory

package object cian911 {
  val settings = new Settings(ConfigFactory.load())

  val LOG: Logger = LoggerFactory.getLogger(Job.getClass())

  implicit lazy val tiSensroData =
    TypeInformation.of(classOf[(SensorData)])
  implicit lazy val tiString =
    TypeInformation.of(classOf[(String)])
  implicit lazy val tiDouble =
    TypeInformation.of(classOf[(Double)])
  implicit lazy val influxConfig =
    TypeInformation.of(classOf[(InfluxDBConfig.Builder)])
  implicit lazy val influxConfig2 =
    TypeInformation.of(classOf[(InfluxDBConfig)])
}
