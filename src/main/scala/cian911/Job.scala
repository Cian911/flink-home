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
import cian911.process.ProcessMessage
import cian911.sink.InfluxDBSink

object Job {
  def main(args: Array[String]): Unit = {
    /*val env = StreamExecutionEnvironment.createLocalEnvironment(*/
      /*settings.flinkSettings.parallelism*/
    /*)*/
   val env = StreamExecutionEnvironment.getExecutionEnvironment

    env.getConfig.setAutoWatermarkInterval(1000L)

    val sensorData: DataStream[String] = env
      .addSource(new HiveMqttSource())
      .name("hivemq-source")
      .uid("hivemq-source")

    val readings: DataStream[SensorData] = sensorData
      .process(new ProcessMessage())
      .keyBy(_.nodeId)
      .filter(_.co2 != 0)
      .map(r => {
        r
      })

    readings.print()

    readings
      .addSink(new InfluxDBSink())
      .name("InfluxDB-Sink")
      .uid("InfluxDB-Sink")

    /** Once we have readings, we can start using time windows. Get Max CO2 &
      * Temp values in last 30m & 60m
      */

    env.execute(settings.applicationName)
  }

}
