# Flink Home

### Description

Flink home is a scala streaming application run on my at home raspberry-pi cluster. The aim of flink home is to ingest data from various CO2 sensor nodes in my home (powered via the Pi Pico W), aggregate it, and sink it to InfluxDB which can be used as a source in grafana.

<p align="center">
  <img style="float: right;width:400px;height:400px;" src="images/pi-pico-co2-readings.png" alt="Raspberry Pi Pico W Co2 Readings"/>
</p>

![Grafana Dashboard](./images/grafana.png)
