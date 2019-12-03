package org.eclipse.tracecompass.analysis.os.linux.core.openstack;
import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;
import org.eclipse.tracecompass.analysis.os.linux.core.openstack.StateValues;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.common.core.NonNullUtils;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystemBuilder;
import org.eclipse.tracecompass.statesystem.core.exceptions.AttributeNotFoundException;
import org.eclipse.tracecompass.statesystem.core.exceptions.StateValueTypeException;
import org.eclipse.tracecompass.statesystem.core.statevalue.ITmfStateValue;
import org.eclipse.tracecompass.statesystem.core.statevalue.TmfStateValue;
import org.eclipse.tracecompass.tmf.core.event.ITmfEvent;
import org.eclipse.tracecompass.tmf.core.statesystem.AbstractTmfStateProvider;
import org.eclipse.tracecompass.tmf.core.statesystem.ITmfStateProvider;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.json.JSONObject;
/**
 * @since 2.0
 */
public class ServiceProvider extends AbstractTmfStateProvider {
    public static String ID="openstack.trace.analyse.service.provider.id"; //$NON-NLS-1$
    public static String lttng_start_trace="lttng_trace"; //$NON-NLS-1$
    public static ITmfStateValue DEFAULT=TmfStateValue.newValueInt(0);
    public boolean ACTIVE_LOG_UPDATE=false;
    public int old_log_quark;
    /**
     * @param trace
     */
    public ServiceProvider(@NonNull ITmfTrace trace) {
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
        return new ServiceProvider(getTrace());
    }
    @Override
    protected void eventHandle(@NonNull ITmfEvent ev) {
        // TODO Auto-generated method stub
        ITmfEvent event=ev;
        final long ts = event.getTimestamp().getValue();
        //UPDATE_LOG_HT(ts);
        if((event.getType().getName().equals("lttng_python:event"))){
            //System.out.println(event.getContent().getField("logger_name").getValue());
            String msg=event.getContent().getField("msg").getValue().toString();//recupere payload
            ITmfStateSystemBuilder ss = checkNotNull(getStateSystemBuilder());
            String logger_name=event.getContent().getField("logger_name").getValue().toString();
            int quark=ss.getQuarkAbsoluteAndAdd("SERVICEs", logger_name,"Action");
            int quarkProject=ss.getQuarkAbsoluteAndAdd("SERVICEs", logger_name,"Request_id");
            // int quarkVM=ss.getQuarkAbsoluteAndAdd("VMs","vm-test" ,"Action");
            String event_type = null; //$NON-NLS-1$
            String vmname="";
            String request_id;
            if(msg.contains(lttng_start_trace)){
                String json=msg.split("lttng_trace:")[1];
                ITmfStateValue value=TmfStateValue.nullValue();
                String val="";
                try {
                    JSONObject jsonobj=new JSONObject(json);
                    event_type= jsonobj.getString("event_type"); //$NON-NLS-1$
                    vmname= jsonobj.getString("vmname");
                    request_id = jsonobj.getString("request_id")+"## ";
                    request_id +=vmname;
                    switch (NonNullUtils.checkNotNull(event_type)) {
                    case "compute.instance.create.start":
                        value=StateValues.CREATE;
                        val=request_id;
                        break;
                    case "compute.instance.create.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "compute.instance.power_off.start":
                        value=StateValues.POWER_OFF;
                        val=request_id;
                        break;
                    case "compute.instance.power_off.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "compute.instance.delete.start":
                        value=StateValues.DELETE;
                        val=request_id;
                        break;
                    case "compute.instance.delete.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "compute.instance.pause.start":
                        value=StateValues.PAUSE;
                        val=request_id;
                        break;
                    case "compute.instance.pause.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "compute.instance.shutdown.start":
                        value=StateValues.SHUTDOWN;
                        val=request_id;
                        break;
                    case "compute.instance.shutdown.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "compute.instance.reboot.start":
                        value=StateValues.REBOOT;
                        val=request_id;
                        break;
                    case "compute.instance.reboot.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "compute.instance.live_migration.pre.start":
                        value=StateValues.LIVE_MIGRATION_PRE;
                        val=request_id;
                        break;
                    case "compute.instance.live_migration.pre.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "compute.instance.live_migration.post.dest.start":
                        value=StateValues.LIVE_MIGRATION_POST;
                        val=request_id;
                        break;
                    case "compute.instance.live_migration.post.dest.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "compute.instance.live_migration._post.start":
                        value=StateValues.LIVE_MIGRATION_P_POST;
                        val=request_id;
                        break;
                    case "compute.instance.live_migration._post.end":
                        value=TmfStateValue.nullValue();
                        break;
                        //---------------nova scheduler----------------------------
                    case "scheduler.select_destinations.start":
                        value=StateValues.SELECT_DEST;
                        val=request_id;
                        break;
                    case "scheduler.select_destinations.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "scheduler.select_destinations.chance.start":
                        value=StateValues.SELECT_DEST_BY_FILTER_CHANCE; val=request_id;
                        break;
                    case "scheduler.select_destinations.chance.end":
                        value=TmfStateValue.nullValue();
                        break;
                        //-----------nova conductor--------------
                    case "conductor.live_migration.start":
                        value=StateValues.CONDUCTOR_LIVE_MIG; val=request_id;
                        break;
                    case "conductor.live_migration.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "conductor.build_instance.start":
                        value=StateValues.CONDUCTOR_BUILD_INSTANCE; val=request_id;
                        break;
                    case "conductor.build_instance.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "conductor.rebuild_instance.start":
                        value=StateValues.CONDUCTOR_REBUILD_INSTANCE; val=request_id;
                        break;
                    case "conductor.rebuild_instance.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "conductor.provider_fw_rule.start":
                        value=StateValues.CONDUCTOR_PROVIDE_FW_RULE; val=request_id;
                        break;
                    case "conductor.provider_fw_rule.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "conductor.object_action.start":
                        value=StateValues.CONDUCTOR_OBJECT_ACTION; val=request_id;
                        break;
                    case "conductor.object_action.end":
                        value=TmfStateValue.nullValue();
                        break;
                        //----------nova network----
                    case "compute.allocate.network.instance.start":
                        value=StateValues.ALLOCATE_NETWORK; val=request_id;
                        break;
                    case "compute.allocate.network.instance.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "compute.network.lease_fixed_ip.instance.start":
                        value=StateValues.LEASE_FIXED_IP; val=request_id;
                        break;
                    case "compute.network.lease_fixed_ip.instance.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "compute.deallocate.network.instance.start":
                        value=StateValues.DEALLOCATE_NETWORK; val=request_id;
                        break;
                    case "compute.deallocate.network.instance.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "libvirt.find.migration.min.data.start":
                        value=StateValues.LIVE_MIG_FIND_MIN_DATA; val=request_id;
                        break;
                    case "libvirt.find.migration.min.data.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "libvirt.live_migration.operation.start":
                        value=StateValues.LIVE_MIG_OPERATION; val=request_id;
                        break;
                    case "libvirt.live_migration.operation.end":
                        value=TmfStateValue.nullValue();
                        break;

                    case "libvirt.can_live_migrate_source.start":
                        value=StateValues.CAN_LIVE_MIG_SRC; val=request_id;
                        break;
                    case "libvirt.can_live_migrate_source.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "libvirt.can_live_migrate_destination.start":
                        value=StateValues.CAN_LIVE_MIG_DST; val=request_id;
                        break;
                    case "libvirt.can_live_migrate_destination.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "libvirt.live_migration_monitor.start":
                        value=StateValues.LIVE_MIG_MONITOR; val=request_id;
                        break;
                    case "libvirt.live_migration_monitor.end":
                        value=TmfStateValue.nullValue();
                        break;
                    default:
                        value=DEFAULT;
                        break;
                    }
                    //System.out.println(logger_name);
                    if(logger_name.contains("nova.compute.manager")){//si nova.compute.manger alors verifier etat de VM
                        String state=jsonobj.getString("vm_state");
                        updateVMstate(vmname, state, ss, ts);
                        //System.out.println(state);
                    }
                }catch (Exception  e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println(event_type);
                }finally {
                    try {
                        ss.modifyAttribute(ts, value, quark);
                        //request_id = jsonobj.getString("request_id")+"##";
                        if(!val.equals("")) {
                            ss.modifyAttribute(ts, TmfStateValue.newValueString(val),quarkProject );
                        } else {
                            ss.modifyAttribute(ts, TmfStateValue.nullValue(),quarkProject );
                        }
                    } catch (StateValueTypeException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (AttributeNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
//            else{
//                UPDATE_LOG_HT(ts);
//                old_log_quark=quark;
//                ACTIVE_LOG_UPDATE=true;
//                ITmfStateValue value;
//                int event_type1=Integer.parseInt(event.getContent().getField("int_loglevel").getValue().toString());
//                switch (NonNullUtils.checkNotNull(event_type1)) {
//                case 0:
//                    value=StateValues.NOTSET;
//                    break;
//                case 5:
//                    value=StateValues.TRACE;
//                    break;
//                case 10:
//                    value=StateValues.INFO;
//                    break;
//                case 20:
//                    value=StateValues.DEBUG;
//                    break;
//                case 30:
//                    value=StateValues.WARNING;
//                    break;
//                case 40:
//                    value=StateValues.ERROR;
//                    break;
//                case 50:
//                    value=StateValues.CRITICAL;
//                    break;
//                default:
//                    value=TmfStateValue.nullValue();
//                    break;
//                }
//                try {
//                    ss.modifyAttribute(ts, value, quark);
//                } catch (StateValueTypeException | AttributeNotFoundException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
        }
    }
//    public void UPDATE_LOG_HT(long ts){
//        if(ACTIVE_LOG_UPDATE){
//            try {
//                ITmfStateSystemBuilder ss = checkNotNull(getStateSystemBuilder());
//                ss.modifyAttribute(ts-1, TmfStateValue.nullValue(), old_log_quark);
//                old_log_quark=-1;
//                ACTIVE_LOG_UPDATE=false;
//            } catch (StateValueTypeException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (AttributeNotFoundException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    }

    public void updateVMstate(String vm,String state,ITmfStateSystemBuilder ss,long ts){
        if(!vm.equals("") && !state.equals("")){
            int quarkVM=ss.getQuarkAbsoluteAndAdd("VMs",vm,"Action");
            ITmfStateValue value;
            switch(state){
            case "building":
                value=StateValues.VM_STATUS_BUILDING_VALUE;
                break;
            case "active":
                value=StateValues.VM_STATUS_ACTIVE_VALUE;
                break;
            case "suspended":
                value=StateValues.VM_STATUS_SUSPENDED_VALUE;
                break;
            case "deleted":
                value=StateValues.VM_STATUS_DELETED_VALUE;
                break;
            case "paused":
                value=StateValues.VM_STATUS_PAUSED_VALUE;
                break;
            case "migrating":
                value=StateValues.VM_STATUS_MIGRATING_VALUE;
                break;
            default:
                value=StateValues.VM_DEFAULT;
                break;
            }
            try {
                ss.modifyAttribute(ts,value,quarkVM);
            } catch (StateValueTypeException | AttributeNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }
    }
}