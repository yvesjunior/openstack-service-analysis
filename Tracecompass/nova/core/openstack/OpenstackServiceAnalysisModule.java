package org.eclipse.tracecompass.analysis.os.linux.core.openstack;

import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.tracecompass.analysis.graph.core.building.TmfGraphBuilderModule;
import org.eclipse.tracecompass.statesystem.core.ITmfStateSystem;
import org.eclipse.tracecompass.statesystem.core.interval.ITmfStateInterval;
import org.eclipse.tracecompass.tmf.core.statesystem.ITmfStateProvider;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfStateSystemAnalysisModule;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.core.trace.TmfTraceUtils;
/**
 * @author yves
 * @since 2.0
 *
 */
public class OpenstackServiceAnalysisModule extends TmfStateSystemAnalysisModule {
    public static String ID="org.eclipse.tracecompass.analysis.os.linux.core.openstack.service.id";
    private @Nullable TmfGraphBuilderModule fGraphModule;
    List<List<ITmfStateInterval>> fullstates=new ArrayList<>();
    @Override
    protected @NonNull ITmfStateProvider createStateProvider() {
        // TODO Auto-generated method stub
        ITmfTrace trace=checkNotNull(getTrace());
        return new ServiceProvider(trace);

    }
    @Override
    protected StateSystemBackendType getBackendType() {
        return StateSystemBackendType.FULL;
    }
    //////////--------------------test to remove
    public TmfGraphBuilderModule getGraph(){
        TmfGraphBuilderModule module = fGraphModule;
        if (module == null) {
            ITmfTrace trace = getTrace();
            if (trace == null) {
                return fGraphModule;
            }
            for (TmfGraphBuilderModule mod : TmfTraceUtils.getAnalysisModulesOfClass(trace, TmfGraphBuilderModule.class)) {
                module = mod;
                break;
            }
            if (module != null) {
                fGraphModule = module;
            }
        }

        return module;
    }

    public void findAllstate(ITmfStateSystem ss){
        ArrayList<ITmfStateInterval> arr=new ArrayList<>();
        ArrayList<ArrayList<ITmfStateInterval>> FULL=new ArrayList<>();
        try{

            ss.waitUntilBuilt();
            long start =ss.getStartTime();
            List<Integer>listQ=ss.getQuarks("SERVICEs","*","request_id");
            for(int i:listQ){
                while(start<ss.getCurrentEndTime()){
                    ITmfStateInterval si= ss.querySingleState(start, i);
                    arr.add(si);
                    start=si.getEndTime()+1;
                    System.out.println(si.getEndTime());
                }
                FULL.add(arr);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
//        System.out.println(FULL.size());
//        System.out.println(FULL.get(3).get(0).getEndTime());
    }

}
