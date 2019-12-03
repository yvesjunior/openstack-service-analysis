#!/usr/bin/env python
import amqp
import sys
from os.path import expanduser
import json
import os
import thread
import argparse


trace_source=expanduser("~")+"/openstack_work/trace"
EXCHANGE="ceilometer"
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
def listener():	
	connection = amqp.Connection(host='localhost', userid='guest',
	password='guest', virtual_host='/')
	channel = connection.channel()
	EXCHANGE = 'amq.rabbitmq.trace'
	QUEUE = 'firehose-queue'
	channel.queue_declare(queue=QUEUE, durable=False,
	auto_delete=True, exclusive=True)
	channel.queue_bind(queue=QUEUE, exchange=EXCHANGE, routing_key='publish.#')
	channel.basic_consume(callback=handle_message, queue=QUEUE, no_ack=True)
	while channel.callbacks:
        	channel.wait()
	channel.close()
	connection.close()

def handle_message(message):
    #print message.routing_key, '->', message.properties
    n= message.properties['application_headers']
    ex= n['exchange_name']
    #print ex
    rk= n['routing_keys'][0]
    if ex in str(EXCHANGE):
        pass
    else:
        body= json.loads(message.body)	
        try:
            msg=body['oslo.message']
            exchange= message.delivery_info['exchange']
            js=json.loads(msg)#load message 
            xml="<event>"+json2xml(js)+"</event>" #pass to xml format
            if not os.path.exists(trace_source+'/'+exchange):#verify if trace source folder exist, ~/openstack_work/trace/exchange/...
                os.makedirs(trace_source+'/'+exchange)  
            thread.start_new_thread( event_to_lttng,(trace_source+'/'+exchange+'/'+rk+'.xml',xml,))
	    thread.start_new_thread( event_to_lttng,(trace_source+'/'+exchange+'/all.xml',xml,))

	    print xml
        except KeyError:
            msg=body
            exchange= message.delivery_info['exchange']
            js=msg
            xml="<event>"+json2xml(js)+"</event>" #pass to xml format
            if not os.path.exists(trace_source+'/'+exchange):#verify if trace source folder exist, ~/openstack_work/trace/exchange/...
                os.makedirs(trace_source+'/'+exchange)  
            thread.start_new_thread( event_to_lttng,(trace_source+'/'+exchange+'/'+rk+'.xml',xml,))
            thread.start_new_thread( event_to_lttng,(trace_source+'/'+exchange+'/all.xml',xml,))

            print xml



if __name__ == "__main__":
	try:    
		parser = argparse.ArgumentParser(description='')
		parser.add_argument('-c','--conf', help='rabbitmq config param file ',required=False)
		parser.add_argument('-H','--hostname', help='rabbitmq-server hostname or ip adress ',required=False)
		parser.add_argument('-u','--username', help='rabbitmq-server user name ',required=False)
       		parser.add_argument('-t','--trace',help='trace output directory location', required=False)
       		args = parser.parse_args()
		print ' [*] Waiting for messages. To exit press CTRL+C'
		listener()
	except KeyboardInterrupt:          
        	print "\n\n trace file in: "+trace_source
        	sys.exit(0)
    
