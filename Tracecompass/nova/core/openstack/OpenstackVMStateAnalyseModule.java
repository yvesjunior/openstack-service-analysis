package org.eclipse.tracecompass.analysis.os.linux.core.openstack;
import static org.eclipse.tracecompass.common.core.NonNullUtils.checkNotNull;
import java.util.TreeSet;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tracecompass.common.core.NonNullUtils;
import org.eclipse.tracecompass.statesystem.core.exceptions.AttributeNotFoundException;
import org.eclipse.tracecompass.statesystem.core.exceptions.StateSystemDisposedException;
import org.eclipse.tracecompass.statesystem.core.exceptions.TimeRangeException;
import org.eclipse.tracecompass.statesystem.core.statevalue.ITmfStateValue;
import org.eclipse.tracecompass.tmf.core.statesystem.ITmfStateProvider;
import org.eclipse.tracecompass.tmf.core.statesystem.TmfStateSystemAnalysisModule;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;

/**
 * @author yves
 * @since 2.0
 *
 */
public class OpenstackVMStateAnalyseModule extends TmfStateSystemAnalysisModule{
    /**
     *
     */
    public TreeSet<String> Hostslist;
    public static final String ID = "org.eclipse.tracecompass.openstack.analyse.VM.state"; //$NON-NLS-1$
    @Override
    protected @NonNull ITmfStateProvider createStateProvider() {
        // TODO Auto-generated method stub
        ITmfTrace trace=checkNotNull(getTrace());
        //System.out.println("YES-YES-YES");
        OpenstackVMStateProvider p=new OpenstackVMStateProvider(trace);
        Hostslist=p.getList_HOSTS();
        return p;
    }
    @Override
    protected StateSystemBackendType getBackendType() {
        return StateSystemBackendType.FULL;
    }

    /**
     * @param vm
     * @param timestamp
     * @return
     */
    public int vmState(String vm,long timestamp){
        try {
            int quark=NonNullUtils.checkNotNull(getStateSystem()).getQuarkAbsolute("VMs",vm,"Status");  //$NON-NLS-1$//$NON-NLS-2$
            ITmfStateValue value = NonNullUtils.checkNotNull(getStateSystem()).querySingleState(timestamp, quark).getStateValue();
            if(value.equals(OpenstackVMStateProvider.BUILDING)){
                return 0;
            }else if (value.equals(OpenstackVMStateProvider.ACTIVE)) {
                return 1;
            }else{
                return 2;
            }

        } catch (AttributeNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch (TimeRangeException e) {
            e.printStackTrace();
        } catch (StateSystemDisposedException e) {
            e.printStackTrace();
        }
        return 2;
    }
    public TreeSet<String> getHOSTsLIST(){
        return Hostslist;
    }

}
