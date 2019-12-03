package org.eclipse.tracecompas.analysis.os.linux.ui.views.openstack.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tracecompas.analysis.os.linux.ui.views.openstack.service.ServiceEntry.Type;
import org.eclipse.tracecompass.analysis.os.linux.core.openstack.OpenstackServiceAnalysisModule;
import org.eclipse.tracecompass.common.core.NonNullUtils;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystem;
import org.eclipse.tracecompass.statesystem.core.exceptions.AttributeNotFoundException;
import org.eclipse.tracecompass.statesystem.core.interval.ITmfStateInterval;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfStateSystemAnalysisModule;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.ui.views.timegraph.AbstractStateSystemTimeGraphView;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.ITimeEvent;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.ITimeGraphEntry;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.NullTimeEvent;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.TimeEvent;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.TimeGraphEntry;
/**
 * @author yves
 *
 */
public class OpenstackServiceView extends AbstractStateSystemTimeGraphView {
    private static final long BUILD_UPDATE_TIMEOUT = 500;
    /**
     *
     */
    boolean bool=false;
    ArrayList<String> SEARCH=new ArrayList<>();
    // List<ILinkEvent> LIST = new ArrayList<>();
    //Map<String,List<ITmfStateInterval>> LIST_MAP=new HashMap<>();
    //Map<String,Integer> LIST_MAP=new HashMap<>();
    public static final String ID = "org.eclipse.tracecompass.analysis.os.linux.views.openstack.service"; //$NON-NLS-1$
    private static final String[] COLUMN_NAMES = new String[] {
            "LOGER_NAME"
    };
    private static final String[] FILTER_COLUMN_NAMES = new String[] {
            "LOGER_NAME"
    };
    static ServicePresentationProvider sp=new ServicePresentationProvider();
    /**
     *
     */
    public OpenstackServiceView() {
        super(ID,sp);
        // TODO Auto-generated constructor stub
        setTreeColumns(COLUMN_NAMES);
        setFilterColumns(FILTER_COLUMN_NAMES);
        setFilterLabelProvider(new ServiceFilterLabelProvider());
        setTreeLabelProvider(new ServiceTreeLabelProvider());

    }
    private static class ServiceFilterLabelProvider extends TreeLabelProvider {
        @Override
        public String getColumnText(Object element, int columnIndex) {
            ServiceEntry entry = (ServiceEntry) element;
            if (columnIndex == 0) {
                return entry.getName();
            }
            return "test";
        }
    }
    /**
     * @author yves
     *
     */
    protected static class ServiceTreeLabelProvider extends TreeLabelProvider {

        @Override
        public String getColumnText(Object element, int columnIndex) {
            ServiceEntry entry = (ServiceEntry) element;
            if (columnIndex==0) {
                return entry.getName();
            }
            return "yy";
        }
    }
    @Override
    protected @Nullable List<ITimeEvent> getEventList(@NonNull TimeGraphEntry entry, ITmfStateSystem ssq, @NonNull List<List<ITmfStateInterval>> fullStates, @Nullable List<ITmfStateInterval> prevFullState, @NonNull IProgressMonitor monitor) {
        List<ITimeEvent> eventList = null;
        ServiceEntry resourcesEntry = (ServiceEntry) entry;
        int quark = resourcesEntry.getQuark();
        if (resourcesEntry.getType().equals(Type.SERVICE)) {
            int statusQuark;
            try {
                statusQuark = ssq.getQuarkRelative(quark,"Action"); //$NON-NLS-1$
            } catch (AttributeNotFoundException e) {
                e.printStackTrace();
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
                    // No information on this serice (yet?), skip it for now
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

    @SuppressWarnings("null")
    @Override
    protected void buildEventList(@NonNull ITmfTrace trace, @NonNull ITmfTrace parentTrace, @NonNull IProgressMonitor monitor) {
        // TODO Auto-generated method stub
        final ITmfStateSystem ssq = TmfStateSystemAnalysisModule.getStateSystem(trace, OpenstackServiceAnalysisModule.ID);
        if (ssq == null) {
            return;
        }

        Comparator<ITimeGraphEntry> comparator = new Comparator<ITimeGraphEntry>() {
            @Override
            public int compare(ITimeGraphEntry o1, ITimeGraphEntry o2) {
                return ((ServiceEntry) o1).compareTo(o2);
            }
        };

        //Map<Integer, ServiceEntry> entryMap = new HashMap<>();
        TimeGraphEntry traceEntry = null;
        TimeGraphEntry traceEntry1 = null;
        Map<String,TimeGraphEntry> mapservice=new HashMap();
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
                traceEntry = new ServiceEntry(trace, trace.getName(), startTime, endTime, "SERVICEs");
                traceEntry.sortChildren(comparator);
                List<TimeGraphEntry> entryList = Collections.singletonList(traceEntry);
                addToEntryList(parentTrace, ssq, entryList);
            } else {
                traceEntry.updateEndTime(endTime);
            }//---------
            if(traceEntry1 == null) {
                traceEntry1 = new ServiceEntry(trace, trace.getName(), startTime, endTime, "SERVICEs");
            } else {
                traceEntry1.updateEndTime(endTime);
            }

            List<Integer> servicesQuarks = ssq.getQuarks("SERVICEs", "*"); //$NON-NLS-1$
            for (Integer serviceQuark : servicesQuarks) {
                String serv = ssq.getAttributeName(serviceQuark).toString();
                if (serv.contains(".")) {
                    String service =serv.split("\\.")[0];//find service name
                    String subservice=serv.replace(service+".", "");//get sub service name
                    TimeGraphEntry subServiceEntry = new ServiceEntry(serviceQuark, trace, startTime, endTime, Type.SERVICE,subservice );
                    if (mapservice.containsKey(service)){
                        NonNullUtils.checkNotNull(mapservice.get(service)).addChild(subServiceEntry);
                    }
                    else{
                        TimeGraphEntry  ServiceEntry = new ServiceEntry(serviceQuark, trace, startTime, endTime, Type.SERVICE,service );
                        mapservice.put(service, ServiceEntry);
                        ServiceEntry.addChild(subServiceEntry);
                        traceEntry.addChild(ServiceEntry);
                    }

//                    @Nullable ServiceEntry entry = entryMap.get(serviceQuark);
//                    if (entry == null) {
//                        entry = new ServiceEntry(serviceQuark, trace, startTime, endTime, Type.SERVICE,subservice);
//                        entryMap.put(serviceQuark, entry);
//                        traceEntry1.addChild(entry);
//                    } else {
//                        entry.updateEndTime(endTime);
//                    }
                }
            }

            if (parentTrace.equals(getTrace())) {
                refresh();
            }
            final List<? extends ITimeGraphEntry> traceEntryChildren = traceEntry1.getChildren();
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
    /**
     * @return
     */
    public static String[] getFilterColumnNames() {
        return FILTER_COLUMN_NAMES;
    }
 }
