### Pi Pico 

This folder contains all the code necessary to run the CO2 monitor and send data to an MQTT broker.

#### Packages & Dependencies

**NB**: This assumes you are using the same sensor & OLED display as I am in this project. If you are using somethign different, you will have to modify the code accordingly.

To connect the pico and test this project, I used [Thonny](https://thonny.org/).

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
