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
from random import randint
import time
#from lttng_ops_conf import *#import configuration file
import argparse
import pprint
    
#parse json object to xml format
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
def event_to_lttng(files,event):
    fichier=open(files,"a+")
    fichier.write("\n\n"+event)
    fichier.close()
#-------
#Xchange=dict()
#ROUTING_KEY=dict()

#log.basicConfig(stream=sys.stdout, level=log.DEBUG)

'''
m1 = {"k1": "v1"}
m2 = {"k1": "v2"}
m3 = {"k3": "v3"}

keys = {"k1": 2, "k3": 1}
vals = { "k1": ["v1","v2"], "k3": ["v3"] }

determiner les types des valeurs (int, str, json, time)

'''

class NotificationsDump(ConsumerMixin):

    def __init__(self, connection,exchanges_list):
        self.connection = connection
        return

    def get_consumers(self, consumer, channel):
        list_of_queues=list()
        for ex in exchanges_list:#for each exchange
            Xchange=ex['exchange']
            exchange = Exchange(Xchange['name'], type=Xchange['type'], durable=Xchange['durable'])#create exchange 
            rts= ex["exchange"]['routing_keys']
            for ROUTING_KEY in rts:#for each routing key
                queue_name=ROUTING_KEY['routing_key_name']+'_lttng'#give a name ending by lttng
                queue = Queue(queue_name, exchange, routing_key =ROUTING_KEY['routing_key_name'] , durable=ROUTING_KEY['durable'], auto_delete=True, no_ack=True)
                list_of_queues.append(queue)#collect created queue
        return [ consumer(list_of_queues, callbacks = [ self.on_message ]) ]

    def on_message(self, body, message):
	#if 'oslo.message' in body:
		
	#else:
	

	#print("message:")
	#pprint.pprint(message)

        try:# if event is send with messagingv2 , (...we will have oslo.messge in the event body)
            msg=body['oslo.message']
            exchange= message.delivery_info['exchange']
            rk=message.delivery_info['routing_key']
            js=json.loads(msg)#load message 
            xml="<event>"+json2xml(js)+"</event>" #pass to xml format
            if not os.path.exists(trace_source+'/'+exchange):#verify if trace source folder exist, ~/openstack_work/trace/exchange/...
                os.makedirs(trace_source+'/'+exchange)  
            thread.start_new_thread( event_to_lttng,(trace_source+'/'+exchange+'/'+rk+'.xml',xml,))
            #print xml
        except:# if the event is sended by messagingv1, (... old way, we don't have oslo.message specified)
            msg=body  
            exchange= message.delivery_info['exchange']
            rk=message.delivery_info['routing_key']
            js=json.dumps(msg)# pass to json format :without u'...
            js=json.loads(js)#load messagge
            xml="<event>"+json2xml(js)+"</event>" #pass to xml format 
            if not os.path.exists(trace_source+'/'+exchange):#verify if trace source folder exist, ~/openstack_work/trace/exchange/...
                os.makedirs(trace_source+'/'+exchange)  
            thread.start_new_thread( event_to_lttng,(trace_source+'/'+exchange+'/'+rk+'.xml',xml,))
            

if __name__ == "__main__":
    print "starting trace listener on rabbitmq"
    try:
        parser = argparse.ArgumentParser(description='')
        parser.add_argument('-c','--conf', help='Input file name',required=True)
        #parser.add_argument('-o','--output',help='Output file name', required=True)
        args = parser.parse_args()
        _temp = __import__(args.conf, globals(), locals(), ['object'], -1) 
        BROKER_URI= _temp.BROKER_URI
        exchanges_list=_temp.exchanges_list["exchanges"] 
        trace_source=_temp.trace_source                                                                             
        NotificationsDump(BrokerConnection(BROKER_URI),exchanges_list).run()
    except KeyboardInterrupt:          
        print "\n\n trace file in: "+trace_source
        sys.exit(0)
    




