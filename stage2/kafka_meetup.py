from kafka import KafkaClient, SimpleProducer
import requests
import json
import logging
import time

if __name__ == "__main__":

    logging.basicConfig(
        format='%(asctime)s.%(msecs)s:%(name)s:%(thread)d:%(levelname)s:%(process)d:%(message)s',
        level=logging.DEBUG
    )

    kafka = KafkaClient("ec2-54-229-140-56.eu-west-1.compute.amazonaws.com:9092")
    producer = SimpleProducer(kafka, async=True)

    r = requests.get('http://stream.meetup.com/2/rsvps', stream=True)

    for line in r.iter_lines():
        if line:
            jsonData = json.loads(line)
            msg = jsonData[u"member"][u"member_name"] + "|" + jsonData[u"response"] + "|" + jsonData[u"group"][u"group_name"]
            msg = str(bytearray(msg.encode('utf-8')))
            print msg
            producer.send_messages(b'meetup', bytes(msg))
            time.sleep(0.1)
    
