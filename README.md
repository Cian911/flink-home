# Flink Home

# Table of Contents
- [Description](#description)
- [Components](#components)
- [Setup](#setup)
    - [Assumptions](#assumptions)
    - [Pi Pico](#pi-pico)
    - [Kubernetes](#kubernetes)
       - [MQTT](#mqtt)
       - [InfluxDB](#influxdb)
       - [Apache Flink](#apache-flink)
       - [Bento](#bento)
    - [Case](#case)

### Description

Flink home is a scala streaming application run on my at home raspberry-pi cluster. The aim of flink home is to ingest data from various CO2 sensor nodes in my home (powered via the Pi Pico W), clean the data, and sink it to InfluxDB which can be used as a source in grafana. 

The data from this will then be used to automate the fan speed of my home ventilation system once the CO2 levels in a particular room reach a certain pre-defined threshold. We will do this using a custom made client - [Bento](https://github.com/Cian911/bento).

There will be an accompanying blog post for this project added here in the very near future.

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

#### Assumptions

In my particular case, I had some things running out of the box for this project already such as Grafana and and existing MQTT broker all running within my at home K8s cluster running a couple of raspberry-pis as example.

Going forward, this project assumes you can fill in the holes which I may have left out, or modify & taylor anything in this project to suite your own needs.

#### Pi Pico

Code and documentation pertaining to the setup on the Raspberry Pi Pico can be found [here](./pi-pico/README.md).

#### Kubernetes

##### MQTT

Note, I have not tested this manifest myself, as I am using a cloud broker provided by [HiveMQ](https://console.hivemq.cloud/). You can also go ahead and use this, or continue and apply the manifests as described below, just be consiouc that you may (likely) will need to make further modifications.

```bash
### Apply resources
kubectl apply -f kubernetes/influxdb/manifest.yaml
```

Code and documentation pertaining to the setup and running of an MQTT broker can be found [here](./kubernetes/README.md).

##### InfluxDB

First, make any needed modifications to the influxdb manifest, such as changing secrets to your own desired configuration, changing the namespace and anything else you deem relevant. Then apply the changes.

```bash
### Apply secrets
kubectl create secret generic influxdb-secrets --from-file=kubernetes/influxdb/manifest.yaml

### Apply resources
kubectl apply -f kubernetes/influxdb/manifest.yaml
```

Code and documentation pertaining to the setup and running of an InfluxDB can be found [here](./kubernetes/README.md).

##### Apache Flink

**N.B**: The docker image associated with this manifest is a 64bit image, which means you will need to have a 64bit OS to run it. This is important if you plan to run this on a raspberrypi.

Similarly to InfluxDB, it is best to first go over the flink manifest and make any modifications you deem necessary before continuing. For instance, I have added a custom node affinity to the manifest to ensure the pods will be scheduled only on 64bit nodes in my cluster. This may not be needed if you can ensure anywhere you plan to run this it will be on a 64bit OS.

```bash
# Flink
kubectl apply -f kubernetes/flink/manifest.yaml
```

Code and documentation pertaining to the setup and running of an Apache Flink be found [here](./kubernetes/README.md).

##### Bento

TBD

#### Case

`.STL` files can be found for the custom sensor housing and case [here](./case/README.md).
