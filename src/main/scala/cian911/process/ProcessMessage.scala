package cian911.process

import scala.util.parsing.json.JSON
import cian911.source.SensorData
import cian911._
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.util.Collector
import org.apache.flink.configuration.Configuration
import java.time.Instant

class ProcessMessage extends ProcessFunction[String, SensorData] {

  override def processElement(
      event: String,
      ctx: ProcessFunction[String, SensorData]#Context,
      out: Collector[SensorData]
  ): Unit = {
    // Convert string msg to SensorData
    val eventData: Tuple6[Double, Double, Double, Double, Double, Double] =
      parseMsg(
        event
      )

    val sensorData: SensorData = SensorData.tupled(eventData)
    out.collect(sensorData)
  }

  private def parseMsg(
      msg: String
  ): Tuple6[Double, Double, Double, Double, Double, Double] = {
    val parsed = JSON.parseFull(msg)
    parsed match {
      case Some(event) => {
        val eventValues =
          event
            .asInstanceOf[Map[String, Map[String, Double]]]
            .flatMap(_._2)
            .map(_._2)

        val eventTupleValues = eventValues match {
          case List(a, b, c, d, e) => {
            val timestamp: Long = Instant.now().getEpochSecond()
            (a, b, c, d, e, timestamp.toDouble)
          }
        }

        return eventTupleValues
      }
      case None => (0.0, 0.0, 0.0, -1.0, 0.0, -1.0)
    }
  }
}
