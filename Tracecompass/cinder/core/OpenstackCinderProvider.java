package org.eclipse.tracecompass.analysis.os.linux.openstack.cinder;

import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.common.core.NonNullUtils;
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
public class OpenstackCinderProvider extends AbstractTmfStateProvider{
    public static String ID="openstack.neutron.trace.analyse.provider.id"; //$NON-NLS-1$
    public static String lttng_start_trace="lttng_trace:"; //$NON-NLS-1$
    public static ITmfStateValue DEFAULT=TmfStateValue.newValueInt(0);
    //public boolean ACTIVE_LOG_UPDATE=false;
    //public int old_log_quark;
    //public long old_ts=0;
    /**
     * @param trace
     * @param id
     */
    public OpenstackCinderProvider(@NonNull ITmfTrace trace, @NonNull String id) {
        super(trace, id);
        // TODO Auto-generated constructor stub
    }

    public OpenstackCinderProvider(@NonNull ITmfTrace trace) {
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
        return new OpenstackCinderProvider(getTrace());
    }

    @Override
    protected void eventHandle(@NonNull ITmfEvent ev) {
        ITmfEvent event=ev;
        final long ts = event.getTimestamp().getValue();
        //UPDATE_LOG_HT(ts,old_ts);
        if((event.getType().getName().equals("lttng_python:event"))){
            String msg=event.getContent().getField("msg").getValue().toString();
            ITmfStateSystemBuilder ss = checkNotNull(getStateSystemBuilder());
            String logger_name=event.getContent().getField("logger_name").getValue().toString();
            int quark=ss.getQuarkAbsoluteAndAdd("SERVICEs", logger_name,"Action");
            String event_type = null; //$NON-NLS-1$
            if(msg.contains(lttng_start_trace)){
                //System.out.println(logger_name);

                String json=msg.split("lttng_trace:")[1];
                ITmfStateValue value=TmfStateValue.nullValue();
                try {
                    JSONObject jsonobj=new JSONObject(json);
                    event_type= jsonobj.getString("event_type"); //$NON-NLS-1$
                    //System.out.println(event_type);
                    switch (NonNullUtils.checkNotNull(event_type)) {
                    case "volume.create.start":
                        value=StateValues.VOLUME_CREATE_VALUE;
                        break;
                    case "volume.create.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "volume.delete.start":
                        value=StateValues.VOLUME_DELETE_VALUE;
                        break;
                    case "volume.delete.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "volume.attach.start":
                        value=StateValues.VOLUME_ATTACH_VALUE;
                        break;
                    case "volume.attach.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "volume.detach.start":
                        value=StateValues.VOLUME_DETACH_VALUE;
                        break;
                    case "volume.detach.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "volume.migrate.start":
                        value=StateValues.VOLUME_MIGRATE_VALUE;
                        break;
                    case "volume.migrate.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "volume.resize.start":
                        value=StateValues.VOLUME_RESIZE_VALUE;
                        break;
                    case "volume.resize.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "volume.update.start":
                        value=StateValues.VOLUME_UPDATE_VALUE;
                        break;
                    case "volume.update.end":
                        value=TmfStateValue.nullValue();
                        break;

                    case "snapshot.create.start":
                        value=StateValues.SNAPSHOT_CREATE_VALUE;
                        break;
                    case "snapshot.create.end":
                        value=TmfStateValue.nullValue();
                        break;
                    case "snapshot.delete.start":
                        value=StateValues.SNAPSHOT_DELETE_VALUE;
                        break;
                    case "snapshot.delete.end":
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
//                else{
//                UPDATE_LOG_HT(ts,old_log_quark);
//                old_log_quark=quark;
//                old_ts=ts;
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
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
        }
    }
//    public void UPDATE_LOG_HT(long ts,long old_ts1){
//        if(ACTIVE_LOG_UPDATE){
//            try {System.out.println(old_ts1);
//                ITmfStateSystemBuilder ss = checkNotNull(getStateSystemBuilder());
//                //if((ts-old_ts1)>10) {
//                    ss.modifyAttribute(ts-10, TmfStateValue.nullValue(), old_log_quark);
//                //} else {
//                //    ss.modifyAttribute((ts+old_ts1)/2, TmfStateValue.nullValue(), old_log_quark);
//                //}
//
//                old_log_quark=-1;
//                ACTIVE_LOG_UPDATE=false;
//            } catch (StateValueTypeException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            } catch (Exception e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    }

}
