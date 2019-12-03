package org.eclipse.tracecompass.analysis.os.linux.openstack.neutron;

import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.statesystem.core.interval.ITmfStateInterval;
import org.eclipse.tracecompass.tmf.core.statesystem.ITmfStateProvider;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfStateSystemAnalysisModule;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
/**
 * @author yves
 * @since 2.1
 *
 */
public class OPS_NEUTRON_MODULE extends TmfStateSystemAnalysisModule {
    public static String ID="org.eclipse.tracecompass.analysis.os.linux.neutron.core.module1";
    List<List<ITmfStateInterval>> fullstates=new ArrayList<>();
    @Override
    protected @NonNull ITmfStateProvider createStateProvider() {
        // TODO Auto-generated method stub
        ITmfTrace trace=checkNotNull(getTrace());
        return new OpenstackNeutronProvider(trace);
    }
    @Override
    protected StateSystemBackendType getBackendType() {
        return StateSystemBackendType.FULL;
    }
}
