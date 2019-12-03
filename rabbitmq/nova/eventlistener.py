#!/usr/bin/env python
import sys
import os
import json
import logging as log
from kombu import BrokerConnection
from kombu import Exchange
from kombu import Queue
from kombu.mixins import ConsumerMixin
import thread
#-------
def json2xml(json_obj, line_padding=""):
    result_list = list()
    json_obj_type = type(json_obj)

    if json_obj_type is list:
        for sub_elem in json_obj:
            result_list.append(json2xml(sub_elem, line_padding))

        return "\n".join(result_list)

    if json_obj_type is dict:
        for tag_name in json_obj:
            sub_obj = json_obj[tag_name]
            result_list.append("%s<%s>" % (line_padding, tag_name))
            result_list.append(json2xml(sub_obj, "\t" + line_padding))
            result_list.append("%s</%s>" % (line_padding, tag_name))

        return "\n".join(result_list)

    return "%s%s" % (line_padding, json_obj)
#------------
def event_to_lttng(event):
	fichier=open("/home/yves/openstack_work/trace/event_trace.xml","a+")
	fichier.write("\n\n"+event)
	fichier.close()

EXCHANGE_NAME="nova"
#ROUTING_KEY="notifications.info"
ROUTING_KEY="#"
QUEUE_NAME="nova_dump_queue"

BROKER_URI="amqp://guest:guest@localhost:5672//"

log.basicConfig(stream=sys.stdout, level=log.DEBUG)

class NotificationsDump(ConsumerMixin):

    def __init__(self, connection):
        self.connection = connection
        return

    def get_consumers(self, consumer, channel):
        exchange = Exchange(EXCHANGE_NAME, type="topic", durable=False)
        queue = Queue(QUEUE_NAME, exchange, routing_key = ROUTING_KEY, durable=False, auto_delete=True, no_ack=True)
        return [ consumer(queue, callbacks = [ self.on_message ]) ]

    def on_message(self, body, message):
	try:# if event is send with messagingv2 , (...we will have oslo.messge in the event body)
		msg=body['oslo.message']
		js=json.loads(msg)#load message 
		xml="<event>"+json2xml(js)+"</event>" #pass to xml format        
		thread.start_new_thread( event_to_lttng,(xml,))
		#print xml
	except:# if the event is sended by messagingv1, (... old way, we don't have oslo.message specified)
		msg=body		
		js=json.dumps(msg)# pass to json format :without u'...
		js=json.loads(js)#load messagge
		xml="<event>"+json2xml(js)+"</event>" #pass to xml format        
		thread.start_new_thread( event_to_lttng,(xml,))
		#print xml

if __name__ == "__main__":
    #log.info("Connecting to broker {}".format(BROKER_URI))
    with BrokerConnection(BROKER_URI) as connection:
        NotificationsDump(connection).run()
