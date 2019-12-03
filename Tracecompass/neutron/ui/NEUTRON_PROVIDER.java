package org.eclipse.tracecompass.analysis.os.linux.openstack.neutron.ui;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.tracecompass.analysis.os.linux.openstack.neutron.OPS_NEUTRON_MODULE;
import org.eclipse.tracecompass.analysis.os.linux.openstack.neutron.ui.OPS_NEUTRON_ENTRY.Type;
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
public class NEUTRON_PROVIDER extends TimeGraphPresentationProvider{
    private Color fColorWhite;
    private Color fColorGray;
    private Integer fAverageCharWidth;
    public ITimeEvent EVENT_PUB;

    /**
     *
     */
    public NEUTRON_PROVIDER(){
        super();
    }
    private static StateColor.State[] getStateValues() {
        return StateColor.State.values();
    }
    private static StateColor.State getEventState(TimeEvent event) {
        if (event.hasValue()) {
            OPS_NEUTRON_ENTRY entry = (OPS_NEUTRON_ENTRY) event.getEntry();
            int value = event.getValue();
            if (entry.getType() == Type.SERVICE) {//entry type SERVICE
                if (value == 1) {
                    return StateColor.State.NETWORK_CREATE;
                }
                else if (value ==2 ) {
                    return StateColor.State.NETWORK_UPDATE;
                }
                else if (value ==3 ) {
                    return StateColor.State.NETWORK_DELETE;
                }
                else if (value ==4) {
                    return StateColor.State.PORT_DELETE;
                }
                else if (value ==5) {
                    return StateColor.State.ROUTER_CREATE;
                }
                else if (value ==6) {
                    return StateColor.State.ROUTER_DELETE;
                }
                else if (value ==7) {
                    return StateColor.State.ROUTER_REMOVE;
                }
                else if (value ==8) {
                    return StateColor.State.SUBNET_DELETE;
                }
                else if (value ==9) {
                    return StateColor.State.SUBNET_UPDATE;
                }
                else{
                    return StateColor.State.NOTSET;
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
            OPS_NEUTRON_ENTRY entry = (OPS_NEUTRON_ENTRY) event.getEntry();

            if (tcEvent.hasValue()) {
                @SuppressWarnings("null")
                ITmfStateSystem ss = TmfStateSystemAnalysisModule.getStateSystem(entry.getTrace(),OPS_NEUTRON_MODULE.ID);
                if (ss == null) {
                    return retMap;
                }
                if (entry.getType().equals(Type.SERVICE)) {
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
        OPS_NEUTRON_ENTRY entry = (OPS_NEUTRON_ENTRY) event.getEntry();
        @SuppressWarnings("null")
        ITmfStateSystem ss = TmfStateSystemAnalysisModule.getStateSystem(entry.getTrace(), OPS_NEUTRON_MODULE.ID);
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
