# Flink Home

# Talbe of Contents
1. [Description](#description)
2. [Components](#components)
3. [Setup](#setup)
    1. [Assumptions](#assumptions)
    2. [Pi Pico](#pi-pico)
    3. [Kubernetes](#kubernetes)
       1. [MQTT](#mqtt)
       2. [InfluxDB](#influxdb)
       3. [Apache Flink](#apache-flink)
       4. [Bento](#bento)

### Description

Flink home is a scala streaming application run on my at home raspberry-pi cluster. The aim of flink home is to ingest data from various CO2 sensor nodes in my home (powered via the Pi Pico W), clean the data, and sink it to InfluxDB which can be used as a source in grafana. 

The data from this will then be used to automate the fan speed of my home ventilation system once the CO2 levels in a particular room reach a certain pre-defined threshold. We will do this using a custom made client - [Bento](https://github.com/Cian911/bento).

<p align="center">
  <img style="float: right;width:600px;height:600px;" src="./images/flink-homev1.jpg" alt="Flink Home v0.0.1"/>
</p>

![Grafana Dashboard](./images/grafana-smooth-co2.png)

### Components

The components I'm using in this project are as follows. All of these can be found quite easily online for purchase. I've also added optionally the single room heat recovery unti I used for ventilation in my home. While this is not necessary, I myself use data from the CO2 sensor to automate & control the fan speed in any given room in my home.

- [RPI SHD CO2 Sensor (Raspberry Pi CO2 Sensor Breakout board)](https://www.reichelt.com/pl/en/raspberry-pi-shield-co2-sensor-breakout-board-rpi-shd-co2-sens-p311516.html?r=1)
- [I2C OLED Display](https://randomnerdtutorials.com/guide-for-oled-display-with-arduino/)
- [Double Sided PCB Boards](https://www.amazon.co.uk/gp/product/B073ZHVKC1/ref=ppx_yo_dt_b_asin_title_o05_s00?ie=UTF8&psc=1)
- [Single Room Heat Recovery Unit](https://www.blauberg.co.uk/en/blauberg-mini-air-decentralised-heat-recovery-unit-single-wall-mounted-d-mvhr-ventilator-smart-wifi-control)

### Setup
Setup Instructions

#### Assumptions
Some assumnptions that will be made

#### Pi Pico
Pi Pico setup etc

#### Kubernetes
All things K8s

##### MQTT
MQTT Stuff

##### InfluxDB
InfluxDB stuff

##### Apache Flink
Flink stuff

##### Bento
Vento bento hento stuff for vents
