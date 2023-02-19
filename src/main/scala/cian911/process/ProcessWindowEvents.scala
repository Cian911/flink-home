package cian911.process

import cian911.source.SensorData
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction


class ProcessWindowEvents extends ProcessWindowFunction[SensorData, SensorData, String, TimeWindow] {

  def process(key: String, context: Context, input: Iterable[SensorData], out: Collector[SensorData]): Unit = {
    var count: Int = 0
    for (in <- input) {
      count = count + 1
    }
    out.collect(new SensorData(1,1,1,1,count,1))
  }

  private def predictReading(): Unit  = {}
}
