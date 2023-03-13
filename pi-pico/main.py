import machine
import network
import json
import time
from time import sleep
from machine import Pin, PWM
from ssd1306 import SSD1306_I2C
from umqtt.simple import MQTTClient

# Wakeup
sleep(5)

# Scan for OLED Display
i2c2=machine.I2C(1,sda=Pin(18), scl=Pin(19), freq=400000)
oled = SSD1306_I2C(128, 64, i2c2)

sleep(2)

# Connect to home network
ssid = ""
password = ""

wlan = network.WLAN(network.STA_IF)
wlan.active(True)

# Disable power-saving mode on pico
wlan.config(pm = 0xa11140)
wlan.connect(ssid, password)
sleep(8)

# Wait for connection to be made
max_wait = 10
while max_wait > 0:
    if wlan.status() < 0 or wlan.status() >= 3:
        break
    max_wait = -1
    print('Waiting for a connection...')
    sleep(1)

# Check & Handle connection errors
if wlan.status() != 3:
    oled.text("Connection Failed..", 0, 0)
    oled.show
    raise RuntimeError('Wifi Connection Failed.')
else:
    print('connected')
    oled.text("connected", 0, 0)
    oled.show()
    status = wlan.ifconfig()

# Create MQTT Client
def connectToBroker():
    client = MQTTClient(client_id=b"YOUR_CLIENT_ID",
                        server=b"",
                        port=0,
                        user=b"",
                        password=b"",
                        keepalive=0,
                        ssl=True,
                        ssl_params={'server_hostname': ''})
    
    client.set_last_will('YOUR_MQTT_TOPIC', json.dumps('{"connection": "closed"}').encode('utf-8'),retain=False)
    client.connect(clean_session=True)
    return client

client = connectToBroker()

def publishToTopic(topic, event):
    client.publish(topic, event)

# Scan for CO2 Sensor
i2c=machine.I2C(0,sda=Pin(0), scl=Pin(1), freq=400000)

print('Scanning i2c bus')

devices = i2c.scan()

if len(devices) == 0:
     print("No i2c device !")
     oled.text("No i2c devices", 0, 0)
     oled.show()
else:
     print('i2c devices found:',len(devices))
     for device in devices:
         print("Decimal address: ",device," | Hexa address: ",hex(device))
     
lastCo2Val = 0
ipStatus = wlan.ifconfig()

if len(devices) > 0:
    sleep(1)
    
    while True:
        try:
            # Read from co2 sensor at location, and read block size of 8 bytes
            read_data = i2c.readfrom_mem(0x5E, 0x00, 8)
            co2 = read_data[0].to_bytes(1, 'big') + read_data[1].to_bytes(1, 'big')
            temperature = read_data[2].to_bytes(1, 'big') + read_data[3].to_bytes(1, 'big')
            # reserved value - useful to check that the sensor is reading out correctly
            # this should be 0x8000
            resvd = read_data[4].to_bytes(1, 'big') + read_data[5].to_bytes(1, 'big')
            pressure = read_data[6].to_bytes(1, 'big') + read_data[7].to_bytes(1, 'big')
        
            
            curCo2Val = "CO2: " + str(int.from_bytes(co2, "big")) + "ppm"
            curTempVal = "TEMP: " + str(int.from_bytes(temperature, "big") / 100) + "C"
            curIp = "IP: " + status[0]

            # Create JSON event
            event = {
                "event": {
                    "co2": int.from_bytes(co2, "big"),
                    "temperature": int.from_bytes(temperature, "big") / 100,
                    "pressure": int.from_bytes(pressure, "big") / 10,
                    "node_id": 1,
                    "timestamp": time.time()
                }
            }

            oled.text(curCo2Val, 0, 0)
            oled.text(curTempVal, 0, 16)
            oled.text(curIp, 0, 32)
            oled.show()

            if lastCo2Val != curCo2Val:
                oled.fill(0)

            publishToTopic("YOUR_MQTT_TOPIC", json.dumps(event).encode('utf-8'))

            sleep(3)
        except Exception as e:
            print("Could not read sensor data: ", e)
            sleep(5)

