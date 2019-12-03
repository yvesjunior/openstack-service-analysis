package openstackTraceAnalyse;
import java.util.Iterator;
import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.ITimeEvent;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.ITimeGraphEntry;
import org.eclipse.tracecompass.tmf.ui.widgets.timegraph.model.TimeGraphEntry;


public class OpenstackVMStateEntry extends TimeGraphEntry implements Comparable<ITimeGraphEntry>{


	public static enum Type{
		NULL,
		VM
	}
	private final int fId;
	private final @NonNull ITmfTrace fTrace;
	private final Type fType;
	private final int fQuark;

	 public OpenstackVMStateEntry(int quark, @NonNull ITmfTrace trace, String name,
	            long startTime, long endTime, Type type, int id) {
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
	    public OpenstackVMStateEntry(@NonNull ITmfTrace trace, String name,
	            long startTime, long endTime, int id) {
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
	    public OpenstackVMStateEntry(int quark, @NonNull ITmfTrace trace,
	            long startTime, long endTime, Type type, int id) {
	        this(quark, trace, computeEntryName(type, id), startTime, endTime, type, id);
	    }

	    private static String computeEntryName(Type type, int id) {
	        
	        return type.toString() + ' ' + id;
	    }

	    /**
	     * Get the entry's id
	     *
	     * @return the entry's id
	     */
	    public int getId() {
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
	        if (!(other instanceof OpenstackVMStateEntry)) {
	            /*
	             * Should not happen, but if it does, put those entries at the end
	             */
	            return -1;
	        }
	        OpenstackVMStateEntry o = (OpenstackVMStateEntry) other;

	        /*
	         * Resources entry names should all be of type "ABC 123"
	         *
	         * We want to filter on the Type first (the "ABC" part), then on the ID
	         * ("123") in numerical order (so we get 1,2,10 and not 1,10,2).
	         */
	        int ret = this.getType().compareTo(o.getType());
	        if (ret != 0) {
	            return ret;
	        }
	        return Integer.compare(this.getId(), o.getId());
	    }

	    @Override
	    public Iterator< ITimeEvent> getTimeEventsIterator() {
	        return super.getTimeEventsIterator();
	    }

}
