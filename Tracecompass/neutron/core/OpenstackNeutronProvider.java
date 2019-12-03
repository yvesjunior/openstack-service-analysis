package org.eclipse.tracecompass.analysis.os.linux.openstack.neutron;

import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.common.core.NonNullUtils;
//import org.eclipse.tracecompass.common.core.NonNullUtils;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystemBuilder;
import org.eclipse.tracecompass.statesystem.core.statevalue.ITmfStateValue;
import org.eclipse.tracecompass.statesystem.core.statevalue.TmfStateValue;
import org.eclipse.tracecompass.tmf.core.event.ITmfEvent;
import org.eclipse.tracecompass.tmf.core.statesystem.AbstractTmfStateProvider;
import org.eclipse.tracecompass.tmf.core.statesystem.ITmfStateProvider;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.json.*;
/**
 * @since 2.1
 */
@SuppressWarnings("javadoc")
public class OpenstackNeutronProvider extends AbstractTmfStateProvider{
    public static String ID="openstack.neutron.trace.analyse.provider.id"; //$NON-NLS-1$
    public static String lttng_start_trace="lttng_trace:"; //$NON-NLS-1$
    /**
     * @param trace
     * @param id
     */
    public OpenstackNeutronProvider(@NonNull ITmfTrace trace, @NonNull String id) {
        super(trace, id);
        // TODO Auto-generated constructor stub
    }

    public OpenstackNeutronProvider(@NonNull ITmfTrace trace) {
        super(trace, ID);
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getVersion() {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public @NonNull ITmfStateProvider getNewInstance() {
        // TODO Auto-generated method stub
        return new OpenstackNeutronProvider(getTrace());
    }

    @Override
    protected void eventHandle(@NonNull ITmfEvent ev) {
        ITmfEvent event=ev;
        final long ts = event.getTimestamp().getValue();
        if((event.getType().getName().equals("lttng_python:event"))){
            String msg=event.getContent().getField("msg").getValue().toString();
            ITmfStateSystemBuilder ss = checkNotNull(getStateSystemBuilder());
            String logger_name=event.getContent().getField("logger_name").getValue().toString();
            int quark=ss.getQuarkAbsoluteAndAdd("SERVICEs", logger_name,"Action");
            String event_type = null; //$NON-NLS-1$
            if(msg.contains(lttng_start_trace)){
                String json=msg.split("lttng_trace:")[1];
                ITmfStateValue value=TmfStateValue.nullValue();
                try {
                    JSONObject jsonobj=new JSONObject(json);
                    event_type= jsonobj.getString("event_type"); //$NON-NLS-1$

                    switch (NonNullUtils.checkNotNull(event_type)) {
                    case "network.create.start":
                        value=StateValues.NETWORK_CREATE_VALUE;
                        break;
                    case "network.create.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "network.delete.start":
                        value=StateValues.NETWORK_DELETE_VALUE;
                        break;
                    case "network.delete.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "subnet.update.start":
                        value=StateValues.SUBNET_UPDATE_VALUE;
                        break;
                    case "subnet.update.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case " subnet.delete.start":
                        value=StateValues.SUBNET_DELETE_VALUE;
                        break;
                    case " subnet.delete.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "port.delete.start":
                        value=StateValues.PORT_DELETE_VALUE;
                        break;
                    case "port.delete.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "router.create.start":
                        value=StateValues.ROUTER_CREATE_VALUE;
                        break;
                    case "router.create.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "router.remove.start":
                        value=StateValues.ROUTER_REMOVE_VALUE;
                        break;
                    case "router.remove.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "router.delete.start":
                        value=StateValues.ROUTER_DELETE_VALUE;
                        break;
                    case "router.delete.end":
                        value=TmfStateValue.nullValue();
                        break;
                    default:
                        value=TmfStateValue.nullValue();//NOTSET;
                        break;
                    }
                    ss.modifyAttribute(ts, value, quark);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
    }


}