package cian911.sink

import org.apache.flink.streaming.api.functions.sink.RichSinkFunction
import cian911.source.SensorData
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.SinkFunction.Context
import org.influxdb.InfluxDB
import org.apache.flink.streaming.connectors.influxdb.InfluxDBConfig
import org.influxdb.InfluxDBFactory
import org.influxdb.dto.Point
import cian911._
import java.util.concurrent.TimeUnit

class InfluxDBSink extends RichSinkFunction[SensorData] {

  private var influxDBClient: InfluxDB = null
  implicit lazy val configBuilder: InfluxDBConfig.Builder = InfluxDBConfig.builder(
    settings.influxDbSettings.clientUrl,
    settings.influxDbSettings.username,
    settings.influxDbSettings.password,
    settings.influxDbSettings.database
  )
  implicit lazy val influxDBConfig: InfluxDBConfig = configBuilder.build()

  // Open connection to InfluxDB
  override def open(config: Configuration): Unit = {
    influxDBClient = InfluxDBFactory
      .connect(
        influxDBConfig.getUrl(),
        influxDBConfig.getUsername(),
        influxDBConfig.getPassword()
      )

    if (!influxDBClient.databaseExists(influxDBConfig.getDatabase()))
      throw new Exception("Database does not exist.")

    influxDBClient.setDatabase(influxDBConfig.getDatabase())

    if (influxDBConfig.getBatchActions() > 0) {
      influxDBClient.enableBatch(
        influxDBConfig.getBatchActions(), 
        influxDBConfig.getFlushDuration(),
        influxDBConfig.getFlushDurationTimeUnit()
      )
    }

    influxDBClient.enableGzip()
  }

  // Called when data arrives at the sink
  override def invoke(data: SensorData, ctx: Context): Unit = {
    val builder: Point.Builder = Point.measurement("Co2Data")
      .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
      .tag(s"sensor${data.nodeId.toString()}", "data")
      .addField("co2", data.co2)
      .addField("temperature", data.temperature)
      .addField("pressure", data.pressure)
    val p: Point = builder.build()

    try {
      influxDBClient.write(p)
    } catch {
      case e: Exception => {
        LOG.error("Failed to sink metric to influxDB: {}", e)
      }
    }
  }

  // Closes the connection to InfluxDB
  override def close(): Unit = {
    if (influxDBClient.isBatchEnabled()) {
      influxDBClient.disableBatch()
    }

    influxDBClient.close()
  }
}
