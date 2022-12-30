package cian911.sink

import org.apache.flink.streaming.api.functions.sink.RichSinkFunction
import cian911.source.SensorData
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.SinkFunction.Context
import org.influxdb.InfluxDB
import org.apache.flink.streaming.connectors.influxdb.InfluxDBConfig
import org.influxdb.InfluxDBFactory

class InfluxDBSink extends RichSinkFunction[SensorData] {

  private var influxDBClient: InfluxDB = null
  private var influxDBConfig: InfluxDBConfig = null

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
  override def invoke(data: SensorData, ctx: Context): Unit = ???

  // Closes the connection to InfluxDB
  override def close(): Unit = ???
}
