package cian911.process

import scala.util.parsing.json.JSON
import cian911.source.SensorData
import cian911._
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.util.Collector
import org.apache.flink.configuration.Configuration

class ProcessMessage extends ProcessFunction[String, SensorData] {

  override def processElement(
      x: String,
      y: ProcessFunction[String, SensorData]#Context,
      z: Collector[SensorData]
  ): Unit = {
    // Convert string msg to SensorData
  }

  private def parseMsg(msg: String): SensorData = {
    val parsed = JSON.parseFull(msg)
    parsed match {
      case Some(event) => {
        val eventValues =
          event
            .asInstanceOf[Map[String, Map[String, Double]]]
            .flatMap(_._2)
            .map(_._2)

        val eventTupleValues = eventValues match {
          case List(a, b, c, d, e) => (a, b, c, d, e)
        }

        return SensorData.tupled(eventTupleValues)
      }
      case None => SensorData(0, 0, 0, -1, 0)
    }
  }
}
