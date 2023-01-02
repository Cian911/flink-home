package cian911.process

import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction
import cian911.source.SensorData
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import java.lang
import org.apache.flink.util.Collector

class ProcessWindow extends ProcessWindowFunction[(SensorData), String, SensorData, TimeWindow] {
  override def process(key: String, ctx: Context, input: Iterable[SensorData], out: Collector[SensorData]): Unit = ???
}
