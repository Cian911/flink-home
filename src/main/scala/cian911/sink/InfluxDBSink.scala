package cian911.sink

import org.apache.flink.streaming.api.functions.sink.RichSinkFunction
import cian911.source.SensorData
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.SinkFunction.Context

class InfluxDBSink extends RichSinkFunction[SensorData] {
  override def open(x: Configuration): Unit = ???

  override def invoke(x: SensorData, y: Context): Unit = ???

  override def close(): Unit = ???
}
