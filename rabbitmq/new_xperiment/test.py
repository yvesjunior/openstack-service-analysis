#!/usr/bin/env python
import amqp

def listener():
    connection = amqp.Connection(host='localhost', userid='guest',
    password='guest', virtual_host='/')
    channel = connection.channel()
    EXCHANGE = 'amq.rabbitmq.trace'
    QUEUE = 'firehose-queue'
    channel.queue_declare(queue=QUEUE, durable=False,
    auto_delete=True, exclusive=True)
    channel.queue_bind(queue=QUEUE, exchange=EXCHANGE, routing_key='deliver.#')
    channel.basic_consume(callback=handle_message, queue=QUEUE, no_ack=True)
    while channel.callbacks:
        channel.wait()
    channel.close()
    connection.close()

def handle_message(message):
    print message.routing_key, '->', message.properties, message.body
    print '--------------------------------'


if __name__ == "__main__":
    
    print ' [*] Waiting for messages. To exit press CTRL+C'
    listener()



