package cian911.process

import cian911.source.SensorData
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import java.time.Instant


class ProcessWindowEvents extends ProcessWindowFunction[SensorData, SensorData, String, TimeWindow] {

  def process(key: String, context: Context, input: Iterable[SensorData], out: Collector[SensorData]): Unit = {
    var avgCo2Data: Double = 0
    var avgTempData: Double = 0
    var avgPressureData: Double = 0

    val dataLen = input.size
    val nodeId = input.toList.head.nodeId

    avgCo2Data = input.map(s => {
      s.co2
    }).sum / dataLen
    
    avgTempData = input.map(s => {
      s.temperature
    }).sum / dataLen
    
    avgPressureData = input.map(s => {
      s.pressure
    }).sum / dataLen

    val publishedAt: Long = Instant.now().getEpochSecond()
    
    out.collect(new SensorData(avgCo2Data, avgPressureData, publishedAt, nodeId, avgTempData, publishedAt))
  }
}
