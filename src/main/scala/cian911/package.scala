import com.typesafe.config.ConfigFactory
import org.apache.flink.api.common.typeinfo.TypeInformation
import cian911.source.SensorData

package object cian911 {
  val settings = new Settings(ConfigFactory.load())

  implicit lazy val tiSensroData =
    TypeInformation.of(classOf[(SensorData)])
  implicit lazy val tiString =
    TypeInformation.of(classOf[(String)])
  implicit lazy val tiDouble =
    TypeInformation.of(classOf[(Double)])
}
