
def event_dhcp(event_type,network,msg):
    Message ="{'event_type':'"+str(event_type)+"','network':'"+str(network)+"','msg':'"+str(msg)+"'}"
    return "lttng_trace:"+Message

def event_l3(event_type,router,msg):
    Message ="{'event_type':'"+str(event_type)+	"','router':'"+str(router)+"','msg':'"+str(msg)+"'}"
    return "lttng_trace:"+Message
	
