package cian911

import org.apache.flink.streaming.api.scala.{
  DataStream,
  StreamExecutionEnvironment
}
import cian911.source.HiveMqttSource
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.api.datastream
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer
import cian911.source.HiveMqttSource
import cian911.source.SensorData

object Job {
  def main(args: Array[String]): Unit = {
    implicit lazy val typeInfo =
      TypeInformation.of(classOf[(SensorData)])

    val env = StreamExecutionEnvironment.createLocalEnvironment(
      settings.flinkSettings.parallelism
    )

    env.getConfig.setAutoWatermarkInterval(1000L)

    val sensorData: DataStream[SensorData] = env
      .addSource(new HiveMqttSource())
      .name("hivemq-source")
      .uid("hivemq-source")

    /** sensorData .process(EventToSensorData) .filter(InvalidEvents)
      * .name(Name) .uid(Name)
      */
    val readings: DataStream[SensorData] = sensorData
      .map(r => {
        r
      })

    readings.print()

    /** Once we have readings, we can start using time windows. Get Max CO2 &
      * Temp values in last 30m & 60m
      */

    env.execute(settings.applicationName)
  }

}
