package org.eclipse.tracecompas.analysis.os.linux.ui.views.openstack.service;
import java.util.LinkedHashMap;
import java.util.Map;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.tracecompas.analysis.os.linux.ui.views.openstack.service.ServiceEntry.Type;
import org.eclipse.tracecompass.analysis.os.linux.core.openstack.OpenstackServiceAnalysisModule;
import org.eclipse.tracecompass.internal.analysis.os.linux.ui.Activator;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystem;
import org.eclipse.tracecompass.statesystem.core.exceptions.TimeRangeException;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfStateSystemAnalysisModule;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.StateItem;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.TimeGraphPresentationProvider;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.ITimeEvent;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.NullTimeEvent;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.TimeEvent;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.widgets.Utils;
/**
 * @author yves
 *
 */
public class ServicePresentationProvider extends TimeGraphPresentationProvider{
    private Color fColorWhite;
    private Color fColorGray;
    private Integer fAverageCharWidth;
    public ITimeEvent EVENT_PUB;

    /**
     *
     */
    public ServicePresentationProvider(){
        super();
    }
    private static StateColor.State[] getStateValues() {
        return StateColor.State.values();
    }
    private static StateColor.State getEventState(TimeEvent event) {
        if (event.hasValue()) {
            ServiceEntry entry = (ServiceEntry) event.getEntry();
            int value = event.getValue();
            if (entry.getType() == Type.SERVICE) {//entry type SERVICE
                //-----compute
                if (value ==10 ) {
                    return StateColor.State.CREATE;
                }
                else if (value ==11 ) {
                    return StateColor.State.PAUSE;
                }
                else if (value == 12) {
                    return StateColor.State.DELETE;
                }
                else if (value == 13) {
                    return StateColor.State.SHUTDOWN;
                }
                else if (value == 14) {
                    return StateColor.State.LIVE_MIGRATION_PRE;
                }
                else if (value == 15) {
                    return StateColor.State.LIVE_MIGRATION_POST;
                }
                else if (value == 16) {
                    return StateColor.State.LIVE_MIGRATION_P_POST;
                }
                else if (value == 17) {
                    return StateColor.State.REBOOT;
                }
                else if (value == 18) {
                    return StateColor.State.POWER_OFF;
                }
                //----------scheduler--------------
                else if (value == 20) {
                    return StateColor.State.SHED_SELECT_DEST;
                }
                else if (value == 21) {
                    return StateColor.State.SCHED_SELECT_FILTER_CHANCE;
                }
                //-----------conductor
                else if (value == 30) {
                    return StateColor.State.CONDUCTOR_LIVE_MIG;
                }
                else if (value == 31) {
                    return StateColor.State.CONDUCTOR_BUILD_INSTANCE;
                }
                else if (value == 32) {
                    return StateColor.State.CONDUCTOR_REBUILD_INSTANCE;
                }
                else if (value == 33) {
                    return StateColor.State.CONDUCTOR_PROVIDE_FW_RULE;
                }
                else if (value == 34) {
                    return StateColor.State.CONDUCTOR_OBJECT_ACTION;
                }
                //---------network
                else if (value == 40) {
                    return StateColor.State.ALLOCATE_NETWORK;
                }
                else if (value == 41) {
                    return StateColor.State.LEASE_FIXED_IP;
                }
                else if (value == 42) {
                    return StateColor.State.DEALLOCATE_NETWORK;
                }
                else if (value == 43) {
                    return StateColor.State.RELEASE_FIXED_IP;
                }
                else if (value == 50) {
                    return StateColor.State.LIVE_MIG_OPERATION;
                }
                else if (value == 51) {
                    return StateColor.State.LIVE_MIG_FIND_MIN_DATA;
                }
                else if (value == 52) {
                    return StateColor.State.CAN_LIVE_MIG_SRC;
                }
                else if (value == 53) {
                    return StateColor.State.CAN_LIVE_MIG_DST;
                }
                else if (value == 54) {
                    return StateColor.State.LIVE_MIG_MONITOR;
                }
                else if (value == 55) {
                    return StateColor.State.LIVE_MIG_MEM_PRECOPY;
                }
                else if (value == 56) {
                    return StateColor.State.LIVE_MIG_MEM_COPY_Iter;
                }
                //log-----
                else if(value==100){
                    return StateColor.State.NOTSET;
                }
                else if(value==110){
                    return StateColor.State.log_INFO;
                }
                else if(value==105 || value==120){System.out.println("deb");
                    return StateColor.State.log_DEBUG_TR;
                }
                else if(value==130){
                    return StateColor.State.log_WARN;
                }
                else if(value==140 || value==150){
                    return StateColor.State.log_ERR_CRIT;
                }
            }else if(entry.getType() == Type.VM){//entry type VM
                if(value==0){
                    return StateColor.State.VM_STATE_BUILDING;
                }else if(value==1){
                    return StateColor.State.VM_STATE_ACTIVE;
                }else if(value==2){
                    return StateColor.State.VM_STATE_SUSPENDED;
                }
                else if(value==3){
                    return StateColor.State.VM_STATE_DELETED;
                }else if(value==4){
                    return StateColor.State.VM_STATE_PAUSED;
                }
                else if(value==5){
                    return StateColor.State.VM_STATE_STOPPED;
                }
                else if(value==6){
                    return StateColor.State.VM_STATE_MIGRATING;
                }
            }
        }
        return null;
    }
    @Override
    public int getStateTableIndex(ITimeEvent event) {
        StateColor.State state = getEventState((TimeEvent) event);
        if (state != null) {
            return state.ordinal();
        }
        if (event instanceof NullTimeEvent) {
            return INVISIBLE;
        }
        return TRANSPARENT;
    }

    @Override
    public StateItem[] getStateTable() {
        StateColor.State[] states = getStateValues();
        StateItem[] stateTable = new StateItem[states.length];
        for (int i = 0; i < stateTable.length; i++) {
            StateColor.State state = states[i];
            stateTable[i] = new StateItem(state.rgb, state.toString());
        }
        return stateTable;
    }

    @Override
    public String getEventName(ITimeEvent event) {
        StateColor.State state = getEventState((TimeEvent) event);
        if (state != null) {
            return state.toString();
        }
        if (event instanceof NullTimeEvent) {
            return null;
        }
        return ""; //$NON-NLS-1$
    }
    @Override
    public Map<String, String> getEventHoverToolTipInfo(ITimeEvent event, long hoverTime) {
        EVENT_PUB=event;

        Map<String, String> retMap = new LinkedHashMap<>();
        if (event instanceof TimeEvent && ((TimeEvent) event).hasValue()) {

            TimeEvent tcEvent = (TimeEvent) event;
            ServiceEntry entry = (ServiceEntry) event.getEntry();

            if (tcEvent.hasValue()) {
                @SuppressWarnings("null")
                ITmfStateSystem ss = TmfStateSystemAnalysisModule.getStateSystem(entry.getTrace(), OpenstackServiceAnalysisModule.ID);
                if (ss == null) {
                    return retMap;
                }
                // Check for type CPU
                if (entry.getType().equals(Type.SERVICE)) {
                    // int status = tcEvent.getValue();


                }
            }
        }

        return retMap;
    }
    @Override
    public void postDrawEvent(ITimeEvent event, Rectangle bounds, GC gc) {
        if (fAverageCharWidth == null) {
            fAverageCharWidth = gc.getFontMetrics().getAverageCharWidth();
        }
        if (bounds.width <= fAverageCharWidth) {
            return;
        }
        //        if (!(event instanceof TimeEvent)) {
        //            return;
        //        }

        ServiceEntry entry = (ServiceEntry) event.getEntry();
        @SuppressWarnings("null")
        ITmfStateSystem ss = TmfStateSystemAnalysisModule.getStateSystem(entry.getTrace(), OpenstackServiceAnalysisModule.ID);
        if (ss == null) {
            return;
        }
        try {
            //String str=ss.querySingleState(event.getTime(), entry.getQuark()+2).getStateValue().unboxStr();
            String str=event.getEntry().getName();
            if(str.equals("nullValue")) {
                str="";
            }else{str=":"+str;}
            gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_WHITE));
            String state= getEventName(event);
            Utils.drawText(gc, state+str, bounds.x, bounds.y, bounds.width, bounds.height, true, true);

        } catch (TimeRangeException e) {
            Activator.getDefault().logError("Error in ControlFlowPresentationProvider", e); //$NON-NLS-1$
        }
    }


    public Color getColorWhite() {
        return fColorWhite;
    }

    public void setColorWhite(Color colorWhite) {
        fColorWhite = colorWhite;
    }

    public Color getColorGray() {
        return fColorGray;
    }

    public void setColorGray(Color colorGray) {
        fColorGray = colorGray;
    }

    public Integer getAverageCharWidth() {
        return fAverageCharWidth;
    }

    public void setAverageCharWidth(Integer averageCharWidth) {
        fAverageCharWidth = averageCharWidth;
    }
}
