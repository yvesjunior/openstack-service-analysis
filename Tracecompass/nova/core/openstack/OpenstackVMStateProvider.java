package org.eclipse.tracecompass.analysis.os.linux.core.openstack;
import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;
import java.util.TreeSet;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.common.core.NonNullUtils;
//import org.eclipse.tracecompass.common.core.NonNullUtils;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystemBuilder;
import org.eclipse.tracecompass.statesystem.core.exceptions.AttributeNotFoundException;
import org.eclipse.tracecompass.statesystem.core.exceptions.StateValueTypeException;
import org.eclipse.tracecompass.statesystem.core.statevalue.ITmfStateValue;
import org.eclipse.tracecompass.statesystem.core.statevalue.TmfStateValue;
import org.eclipse.tracecompass.tmf.core.event.ITmfEvent;
import org.eclipse.tracecompass.tmf.core.statesystem.AbstractTmfStateProvider;
import org.eclipse.tracecompass.tmf.core.statesystem.ITmfStateProvider;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.json.*;

/**
 * @since 2.0
 */
@SuppressWarnings("javadoc")
public class OpenstackVMStateProvider extends AbstractTmfStateProvider{
    public static String ID="openstack.trace.analyse.provider.id"; //$NON-NLS-1$
    public static ITmfStateValue BUILDING=TmfStateValue.newValueInt(0);
    public static ITmfStateValue ACTIVE=TmfStateValue.newValueInt(1);
    public static ITmfStateValue SUSPENDED=TmfStateValue.newValueInt(2);
    public static ITmfStateValue DELETED=TmfStateValue.newValueInt(3);
    public static ITmfStateValue PAUSED=TmfStateValue.newValueInt(4);
    public static ITmfStateValue STOPPED=TmfStateValue.newValueInt(5);
    public static ITmfStateValue MIGRATING=TmfStateValue.newValueInt(6);
    public static ITmfStateValue NOTSET = TmfStateValue.newValueInt(7);
    //-------------------------------
    public static ITmfStateValue CREATE = TmfStateValue.newValueInt(8);
    public static ITmfStateValue STOP = TmfStateValue.newValueInt(9);
    public static ITmfStateValue DELETE = TmfStateValue.newValueInt(10);
    public TreeSet<String> List_HOSTS=new TreeSet<>();
    public static String lttng_start_trace="lttng_trace:"; //$NON-NLS-1$
    /**
     * @param trace
     * @param id
     */
    public OpenstackVMStateProvider(@NonNull ITmfTrace trace, @NonNull String id) {
        super(trace, id);
        // TODO Auto-generated constructor stub
    }

    public OpenstackVMStateProvider(@NonNull ITmfTrace trace) {
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
        return new OpenstackVMStateProvider(getTrace());
    }

    @Override
    protected void eventHandle(@NonNull ITmfEvent ev) {
        ITmfEvent event=ev;
        final long ts = event.getTimestamp().getValue();
        if((event.getType().getName().equals("lttng_python:event"))){
            String msg=event.getContent().getField("msg").getValue().toString();//recupere payload
            if(msg.contains(lttng_start_trace)){
                ITmfStateSystemBuilder ss = checkNotNull(getStateSystemBuilder());
                //ss.toString();
                // System.out.println(ts);
                @SuppressWarnings("null")
                String json=msg.split(lttng_start_trace)[1];
                try {
                    System.out.println(json+"\n");
                    JSONObject jsonobj=new JSONObject(json);
                    String vmname= jsonobj.getString("vmname"); //$NON-NLS-1$
                    String state = jsonobj.getString("vm_state");
                    String pro = jsonobj.getString("project_id");
                    String vm_host=jsonobj.getString("host");
                    int Pquark=ss.getQuarkAbsoluteAndAdd("PROJECTs", pro);
                    int quark = ss.getQuarkRelativeAndAdd(Pquark,"VMs", vmname, "Status");
                    int hostquark = ss.getQuarkRelativeAndAdd(Pquark,"VMs", vmname, "Host");

                    // int user_quark = ss.getQuarkRelativeAndAdd(Pquark,"USERs", user_action, "Action");

                    //vm state value
                    ITmfStateValue value;
                    switch (NonNullUtils.checkNotNull(state)) {
                    case "building": //$NON-NLS-1$
                        value=BUILDING;
                        break;
                    case "active": //$NON-NLS-1$
                        value=ACTIVE;
                        break;
                    case "supended": //$NON-NLS-1$
                        value=SUSPENDED;
                        break;
                    case "deleted": //$NON-NLS-1$
                        value=DELETED;
                        break;
                    case "paused": //$NON-NLS-1$
                        value=PAUSED;
                        break;
                    case "stopped": //$NON-NLS-1$
                        value=STOPPED;
                        break;
                    case "migrating": //$NON-NLS-1$
                        value=MIGRATING;
                        break;
                    default:
                        value=NOTSET;
                        break;
                    }
                    ss.modifyAttribute(ts, value, quark);

                    // code for VM Host
                    ITmfStateValue vmhost = TmfStateValue.newValueString(vm_host);
                    ss.modifyAttribute(ts, vmhost,hostquark );
                    List_HOSTS.add(vm_host);
                    //

                } catch (JSONException | StateValueTypeException | AttributeNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }
        //System.out.println("YES-YES");
    }
    public TreeSet<String> getList_HOSTS(){
        return List_HOSTS;
    }

}