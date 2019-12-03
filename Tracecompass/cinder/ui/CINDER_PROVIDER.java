package org.eclipse.tracecompass.analysis.os.linux.openstack.cinder.ui;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.tracecompass.analysis.os.linux.openstack.cinder.OPS_CINDER_MODULE;
import org.eclipse.tracecompass.analysis.os.linux.openstack.cinder.StateValues;
import org.eclipse.tracecompass.analysis.os.linux.openstack.cinder.ui.OPS_CINDER_ENTRY.Type;
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
public class CINDER_PROVIDER extends TimeGraphPresentationProvider{
    private Color fColorWhite;
    private Color fColorGray;
    private Integer fAverageCharWidth;
    public ITimeEvent EVENT_PUB;

    /**
     *
     */
    public CINDER_PROVIDER(){
        super();
    }
    private static StateColor.State[] getStateValues() {
        return StateColor.State.values();
    }
    private static StateColor.State getEventState(TimeEvent event) {
        if (event.hasValue()) {
            OPS_CINDER_ENTRY entry = (OPS_CINDER_ENTRY) event.getEntry();
            int value = event.getValue();
            if (entry.getType() == Type.SERVICE) {//entry type SERVICE
                if (value == StateValues.VOLUME_CREATE) {
                    return StateColor.State.VOLUME_CREATE;
                }
                else if(value == StateValues.VOLUME_UPDATE){
                    return StateColor.State.VOLUME_UPDATE;
                }else if(value == StateValues.VOLUME_DETACH){
                    return StateColor.State.VOLUME_DETACH;
                }else if(value == StateValues.VOLUME_DELETE){
                    return StateColor.State.VOLUME_DELETE;
                }else if(value == StateValues.VOLUME_ATTACH){
                    return StateColor.State.VOLUME_ATTACH;
                }else if(value == StateValues.VOLUME_RESIZE){
                    return StateColor.State.VOLUME_RESIZE;
                }else if(value == StateValues.VOLUME_MIGRATE){
                    return StateColor.State.VOLUME_MIGRATE;
                }else if(value == StateValues.SNAPSHOT_CREATE){
                    return StateColor.State.SNAPSHOT_CREATE;
                }else if(value == StateValues.SNAPSHOT_DELETE){
                    return StateColor.State.SNAPSHOT_DELETE;
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
            OPS_CINDER_ENTRY entry = (OPS_CINDER_ENTRY) event.getEntry();

            if (tcEvent.hasValue()) {
                @SuppressWarnings("null")
                ITmfStateSystem ss = TmfStateSystemAnalysisModule.getStateSystem(entry.getTrace(),OPS_CINDER_MODULE.ID);
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
        OPS_CINDER_ENTRY entry = (OPS_CINDER_ENTRY) event.getEntry();
        @SuppressWarnings("null")
        ITmfStateSystem ss = TmfStateSystemAnalysisModule.getStateSystem(entry.getTrace(), OPS_CINDER_MODULE.ID);
        if (ss == null) {
            return;
        }
        try {
            gc.setForeground(gc.getDevice().getSystemColor(SWT.COLOR_WHITE));
            String state= getEventName(event);
            Utils.drawText(gc, state, bounds.x, bounds.y, bounds.width, bounds.height, true, true);

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
