package openstackTraceAnalyse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystem;
import org.eclipse.tracecompass.statesystem.core.exceptions.AttributeNotFoundException;
import org.eclipse.tracecompass.statesystem.core.interval.ITmfStateInterval;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfStateSystemAnalysisModule;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.ui.views.timegraph.AbstractStateSystemTimeGraphView;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.TimeGraphPresentationProvider;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.ITimeEvent;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.ITimeGraphEntry;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.NullTimeEvent;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.TimeEvent;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.TimeGraphEntry;

import openstack.analyse.OpenstackVMStateAnalysisModule;
import openstackTraceAnalyse.OpenstackVMStateEntry.Type;


public class OpenstackVMStateView extends AbstractStateSystemTimeGraphView{
	/** View ID. */
	public static final String ID = "org.eclipse.tracecompass.analysis.os.linux.views.resources.openstack"; //$NON-NLS-1$
	// Timeout between updates in the build thread in ms
	private static final long BUILD_UPDATE_TIMEOUT = 500;
	private static final String[] FILTER_COLUMN_NAMES = new String[] {
			"Messages.ResourcesView_stateTypeName"
	};



	public OpenstackVMStateView(String id, TimeGraphPresentationProvider pres) {
		super(id, pres);
		// TODO Auto-generated constructor stub
	}
	public OpenstackVMStateView() {
		super(ID, new OpenstackVMStatePresentationProvider());
		setFilterColumns(FILTER_COLUMN_NAMES);
		setFilterLabelProvider(new ResourcesFilterLabelProvider());
	}

	private static class ResourcesFilterLabelProvider extends TreeLabelProvider {
		@Override
		public String getColumnText(Object element, int columnIndex) {
			OpenstackVMStateEntry entry = (OpenstackVMStateEntry) element;
			if (columnIndex == 0) {
				return entry.getName();
			}
			return ""; //$NON-NLS-1$
		}

	}


	protected @Nullable List<ITimeEvent> getEventList(@NonNull TimeGraphEntry entry, ITmfStateSystem ssq,
            @NonNull List<List<ITmfStateInterval>> fullStates, @Nullable List<ITmfStateInterval> prevFullState, @NonNull IProgressMonitor monitor) {
		OpenstackVMStateEntry resourcesEntry = (OpenstackVMStateEntry) entry;
        List<ITimeEvent> eventList = null;
        int quark = resourcesEntry.getQuark();

        if (resourcesEntry.getType().equals(Type.VM)) {
            int statusQuark;
            try {
                statusQuark = ssq.getQuarkRelative(quark,"Status");
            } catch (AttributeNotFoundException e) {
                /*
                 * The sub-attribute "status" is not available. May happen
                 * if the trace does not have sched_switch events enabled.
                 */
                return null;
            }
            eventList = new ArrayList<>(fullStates.size());
            ITmfStateInterval lastInterval = prevFullState == null || statusQuark >= prevFullState.size() ? null : prevFullState.get(statusQuark);
            long lastStartTime = lastInterval == null ? -1 : lastInterval.getStartTime();
            long lastEndTime = lastInterval == null ? -1 : lastInterval.getEndTime() + 1;
            for (List<ITmfStateInterval> fullState : fullStates) {
                if (monitor.isCanceled()) {
                    return null;
                }
                if (statusQuark >= fullState.size()) {
                    /* No information on this CPU (yet?), skip it for now */
                    continue;
                }
                ITmfStateInterval statusInterval = fullState.get(statusQuark);
                int status = statusInterval.getStateValue().unboxInt();
                long time = statusInterval.getStartTime();
                long duration = statusInterval.getEndTime() - time + 1;
                if (time == lastStartTime) {
                    continue;
                }
                if (!statusInterval.getStateValue().isNull()) {
                    if (lastEndTime != time && lastEndTime != -1) {
                        eventList.add(new TimeEvent(entry, lastEndTime, time - lastEndTime));
                    }
                    eventList.add(new TimeEvent(entry, time, duration, status));
                } else {
                    eventList.add(new NullTimeEvent(entry, time, duration));
                }
                lastStartTime = time;
                lastEndTime = time + duration;
            }
        } 

        return eventList;
    }

	@Override
	 protected void buildEventList(ITmfTrace trace, ITmfTrace parentTrace, final IProgressMonitor monitor) {
        final ITmfStateSystem ssq = TmfStateSystemAnalysisModule.getStateSystem(trace, OpenstackVMStateAnalysisModule.ID);
        if (ssq == null) {
            return;
        }
        Comparator<ITimeGraphEntry> comparator = new Comparator<ITimeGraphEntry>() {
            @Override
            public int compare(ITimeGraphEntry o1, ITimeGraphEntry o2) {
                return ((OpenstackVMStateEntry) o1).compareTo(o2);
            }
        };

        Map<Integer, OpenstackVMStateEntry> entryMap = new HashMap<>();
        TimeGraphEntry traceEntry = null;

        long startTime = ssq.getStartTime();
        long start = startTime;
        setStartTime(Math.min(getStartTime(), startTime));
        boolean complete = false;
        while (!complete) {
            if (monitor.isCanceled()) {
                return;
            }
            complete = ssq.waitUntilBuilt(BUILD_UPDATE_TIMEOUT);
            if (ssq.isCancelled()) {
                return;
            }
            long end = ssq.getCurrentEndTime();
            if (start == end && !complete) { // when complete execute one last time regardless of end time
                continue;
            }
            long endTime = end + 1;
            setEndTime(Math.max(getEndTime(), endTime));

            if (traceEntry == null) {
                traceEntry = new OpenstackVMStateEntry(trace, trace.getName(), startTime, endTime, 0);
                traceEntry.sortChildren(comparator);
                List<TimeGraphEntry> entryList = Collections.singletonList(traceEntry);
                addToEntryList(parentTrace, ssq, entryList);
            } else {
                traceEntry.updateEndTime(endTime);
            }

            List<Integer> vmQuarks = ssq.getQuarks("VMs", "*"); //$NON-NLS-1$
            for (Integer vmQuark : vmQuarks) {
                int vm = Integer.parseInt(ssq.getAttributeName(vmQuark));
                OpenstackVMStateEntry entry = entryMap.get(vmQuark);
                if (entry == null) {
                    entry = new OpenstackVMStateEntry(vmQuark, trace, startTime, endTime, Type.VM, vm);
                    entryMap.put(vmQuark, entry);
                    traceEntry.addChild(entry);
                } else {
                    entry.updateEndTime(endTime);
                }
            }
            
            

            if (parentTrace.equals(getTrace())) {
                refresh();
            }
            final List<? extends ITimeGraphEntry> traceEntryChildren = traceEntry.getChildren();
            final long resolution = Math.max(1, (endTime - ssq.getStartTime()) / getDisplayWidth());
            final long qStart = start;
            final long qEnd = end;
            queryFullStates(ssq, qStart, qEnd, resolution, monitor, new IQueryHandler() {
                @Override
                public void handle(List<List<ITmfStateInterval>> fullStates, List<ITmfStateInterval> prevFullState) {
                    for (ITimeGraphEntry child : traceEntryChildren) {
                        if (monitor.isCanceled()) {
                            return;
                        }
                        if (child instanceof TimeGraphEntry) {
                            TimeGraphEntry entry = (TimeGraphEntry) child;
                            List<ITimeEvent> eventList = getEventList(entry, ssq, fullStates, prevFullState, monitor);
                            if (eventList != null) {
                                for (ITimeEvent event : eventList) {
                                    entry.addEvent(event);
                                }
                            }
                        }
                    }
                }
            });

            start = end;
        }
    }



}
