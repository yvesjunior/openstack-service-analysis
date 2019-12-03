package org.eclipse.tracecompass.analysis.os.linux.openstack.neutron.ui;

import java.util.Iterator;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.ITimeEvent;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.ITimeGraphEntry;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.TimeGraphEntry;

/**
 * @author yves
 *
 */
public class OPS_NEUTRON_ENTRY extends TimeGraphEntry implements Comparable<ITimeGraphEntry>{
    public static enum Type {
        /** Null resources (filler rows, etc.) */
        NULL,
        /** Entries for Services */
        SERVICE,
        VM
    }
    private final String fId;
    private final @NonNull ITmfTrace fTrace;
    private final Type fType;
    private final int fQuark;

    /**
     * @param quark
     * @param trace
     * @param name
     * @param startTime
     * @param endTime
     * @param type
     * @param id
     */
    public OPS_NEUTRON_ENTRY(int quark, @NonNull ITmfTrace trace, String name,
            long startTime, long endTime, Type type, String id) {
        super(name, startTime, endTime);
        fId = id;
        fTrace = trace;
        fType = type;
        fQuark = quark;

    }

    /**
     * Constructor
     *
     * @param trace
     *            The trace on which we are working
     * @param name
     *            The exec_name of this entry
     * @param startTime
     *            The start time of this entry lifetime
     * @param endTime
     *            The end time of this entry
     * @param id
     *            The id of this entry
     */
    public OPS_NEUTRON_ENTRY(@NonNull ITmfTrace trace, String name,
            long startTime, long endTime, String id) {
        this(-1, trace, name, startTime, endTime, Type.NULL, id);
    }

    /**
     * Constructor
     *
     * @param quark
     *            The attribute quark matching the entry
     * @param trace
     *            The trace on which we are working
     * @param startTime
     *            The start time of this entry lifetime
     * @param endTime
     *            The end time of this entry
     * @param type
     *            The type of this entry
     * @param id
     *            The id of this entry
     */
    public OPS_NEUTRON_ENTRY(int quark, @NonNull ITmfTrace trace,
            long startTime, long endTime, Type type, String id) {
        this(quark, trace, computeEntryName(type, id), startTime, endTime, type, id);
    }

    /**
     * @param type
     */
    private static String computeEntryName(Type type, String id) {
        //return type.toString() + ' ' + id;
        return id;
    }

    /**
     * Get the entry's id
     *
     * @return the entry's id
     */
    public String getId() {
        return fId;
    }

    /**
     * Get the entry's trace
     *
     * @return the entry's trace
     */
    public @NonNull ITmfTrace getTrace() {
        return fTrace;
    }

    /**
     * Get the entry Type of this entry. Uses the inner Type enum.
     *
     * @return The entry type
     */
    public Type getType() {
        return fType;
    }

    /**
     * Retrieve the attribute quark that's represented by this entry.
     *
     * @return The integer quark The attribute quark matching the entry
     */
    public int getQuark() {
        return fQuark;
    }

    @Override
    public boolean hasTimeEvents() {
        if (fType == Type.NULL) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(ITimeGraphEntry other) {
        if (!(other instanceof OPS_NEUTRON_ENTRY)) {
            /*
             * Should not happen, but if it does, put those entries at the end
             */
            return -1;
        }
        OPS_NEUTRON_ENTRY o = (OPS_NEUTRON_ENTRY) other;

        int ret = this.getType().compareTo(o.getType());
        if (ret != 0) {
            return ret;
        }
        //return Integer.compare(this.getId(), o.getId());
        return this.getId().compareTo(o.getId());
    }

    @Override
    public Iterator<@NonNull ITimeEvent> getTimeEventsIterator() {
        return super.getTimeEventsIterator();
    }
}
