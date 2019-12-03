
def event_volume(event_type,volume,msg):
    Message ="{'event_type':'"+str(event_type)+	"','volume':'"+str(volume)+"','msg':'"+str(msg)+"'}"
    return "lttng_trace:"+Message


def event_vol_attach(event_type,volume,instance,hostname,msg):
    Message ="{'event_type':'"+str(event_type)+	"','volume':'"+str(volume)+"','instance_id':'"+str(instance)+"','hostname':'"+str(hostname)+"','msg':'"+str(msg)+"'}"
    return "lttng_trace:"+Message
	

def event_vol_mig(event_type,volume,host,msg):
    Message ="{'event_type':'"+str(event_type)+	"','volume':'"+str(volume)+"','hostname':'"+str(host)+"','msg':'"+str(msg)+"'}"
    return "lttng_trace:"+Message












