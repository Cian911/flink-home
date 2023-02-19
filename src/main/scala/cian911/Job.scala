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
import cian911.source._
import cian911.process._
import cian911.sink.InfluxDBSink
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.slf4j.Logger
import org.slf4j.LoggerFactory

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
      .filter(_.co2 != -1)
      .name("process-readings")
      .uid("process-readings")

    /*val smoothReadings: DataStream[SensorData] = readings*/
      /*.keyBy(_.nodeId.toString())*/
      /*.window(SlidingProcessingTimeWindows.of(Time.minutes(5), Time.seconds(60)))*/
      /*.process(new ProcessWindowEvents())*/

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
