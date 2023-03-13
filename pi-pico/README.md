### Pi Pico 

<p align="center">
  <img style="float: right;width:600px;height:600px;" src="../images/flink-homev1.jpg" alt="Flink Home v0.0.1"/>
</p>


This folder contains all the code necessary to run the CO2 monitor and send data to an MQTT broker.

#### Components

The components I'm using in this project are as follows. All of these can be found quite easily online for purchase.

- [RPI SHD CO2 Sensor (Raspberry Pi CO2 Sensor Breakout board)](https://www.reichelt.com/pl/en/raspberry-pi-shield-co2-sensor-breakout-board-rpi-shd-co2-sens-p311516.html?r=1)
- [I2C OLED Display](https://randomnerdtutorials.com/guide-for-oled-display-with-arduino/)
- [Double Sided PCB Boards](https://www.amazon.co.uk/gp/product/B073ZHVKC1/ref=ppx_yo_dt_b_asin_title_o05_s00?ie=UTF8&psc=1)

#### Packages & Dependencies

**NB**: This assumes you are using the same sensor & OLED display as I am in this project. If you are using somethign different, you will have to modify the code accordingly.

To connect the pico and test this project, I used [Thonny](https://thonny.org/). Some of the below packages you may be able to install directly from Thonny, however if not you should be able to install them via the python REPL.

- `ssd1306` - Micropython Package
- `upip` - Micropython Package
- `micropython-umqtt.simple` - Simple MQTT Client
- `micropython-umqtt.robust` - Robust MQTT Client

From the Python REPL, enable and connect to wifi, and install the following package.

```python
import network
wlan = network.WLAN(network.STA_IF)
wlan.active(True)
wlan.connect(ssid, password)

import upip
upip.install('micropython-umqtt.simple', '/lib')
upip.install('micropython-umqtt.robust', '/lib')
```

After installing these you can copy the contents of `main.py` onto your own pico and run it.
