package openstackTraceAnalyse;

import java.awt.Rectangle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystem;
import org.eclipse.tracecompass.statesystem.core.exceptions.StateValueTypeException;
import org.eclipse.tracecompass.statesystem.core.exceptions.TimeRangeException;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfStateSystemAnalysisModule;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.StateItem;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.TimeGraphPresentationProvider;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.ITimeEvent;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.ITimeGraphEntry;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.NullTimeEvent;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.TimeEvent;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.widgets.ITmfTimeGraphDrawingHelper;

import openstack.analyse.OpenstackVMStateAnalysisModule;
import openstack.analyse.StateValues;
import openstackTraceAnalyse.OpenstackVMStateEntry.Type;;
public class OpenstackVMStatePresentationProvider extends TimeGraphPresentationProvider{
	private long fLastThreadId = -1;
	private Color fColorWhite;
	private Color fColorGray;
	private Integer fAverageCharWidth;

	private enum State {
		BUILDING             (new RGB(200, 200, 200)),
		ACTIVE  (new RGB(200, 150, 100)),
		NOTSET  (new RGB(0, 0, 0));
		public final RGB rgb;
		private State(RGB rgb) {
			this.rgb = rgb;
		}
	}
	public OpenstackVMStatePresentationProvider(){
		super();
	}
	private static State[] getStateValues() {
		return State.values();
	}
	private static State getEventState(TimeEvent event) {
		if (event.hasValue()) {
			OpenstackVMStateEntry entry = (OpenstackVMStateEntry) event.getEntry();
			int value = event.getValue();

			if (entry.getType() == Type.VM) {
				if (value == StateValues.VM_STATUS_BUILDING) {
					return State.BUILDING;
				} else if (value == StateValues.VM_STATUS_ACTIVE) {
					return State.ACTIVE;
				}else{
					return State.NOTSET;
				}
			} 
		}
		return null;
	}
	@Override
	public int getStateTableIndex(ITimeEvent event) {
		State state = getEventState((TimeEvent) event);
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
		State[] states = getStateValues();
		StateItem[] stateTable = new StateItem[states.length];
		for (int i = 0; i < stateTable.length; i++) {
			State state = states[i];
			stateTable[i] = new StateItem(state.rgb, state.toString());
		}
		return stateTable;
	}
	@Override
	public String getEventName(ITimeEvent event) {
		State state = getEventState((TimeEvent) event);
		if (state != null) {
			return state.toString();
		}
		if (event instanceof NullTimeEvent) {
			return null;
		}
		return "Messages.ResourcesView_multipleStates";
	}
	public void postDrawEvent(ITimeEvent event, Rectangle bounds, GC gc){
		if (fColorGray == null) {
			fColorGray = gc.getDevice().getSystemColor(SWT.COLOR_GRAY);
		}
		if (fColorWhite == null) {
			fColorWhite = gc.getDevice().getSystemColor(SWT.COLOR_WHITE);
		}
		if (fAverageCharWidth == null) {
			fAverageCharWidth = gc.getFontMetrics().getAverageCharWidth();
		}
		ITmfTimeGraphDrawingHelper drawingHelper = getDrawingHelper();
		if (bounds.width <= fAverageCharWidth) {
			return;
		}

		if (!(event instanceof TimeEvent)) {
			return;
		}
		TimeEvent tcEvent = (TimeEvent) event;
		if (!tcEvent.hasValue()) {
			return;
		}

		OpenstackVMStateEntry entry=(OpenstackVMStateEntry) event.getEntry();
		if (!entry.getType().equals(Type.VM)) {
			return;
		}
		int status = tcEvent.getValue();
		/*if (status != StateValues.CPU_STATUS_RUN_USERMODE && status != StateValues.CPU_STATUS_RUN_SYSCALL) {
			return;
		}*/
		ITmfStateSystem ss = TmfStateSystemAnalysisModule.getStateSystem(entry.getTrace(),OpenstackVMStateAnalysisModule.ID);
		if (ss == null) {
			return;
		}
		long time=event.getTime();
		try{
			while(time<event.getTime()+event.getDuration()){
				int vmquark=entry.getQuark();
			}
		}
		catch (TimeRangeException | StateValueTypeException e) {
			e.printStackTrace();
		}
	}
	public void postDrawEntry(ITimeGraphEntry entry, Rectangle bounds, GC gc) {
		fLastThreadId = -1;
	}
}
