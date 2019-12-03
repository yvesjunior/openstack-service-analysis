package org.eclipse.tracecompass.analysis.os.linux.openstack.neutron;

import org.eclipse.tracecompass.statesystem.core.statevalue.ITmfStateValue;
import org.eclipse.tracecompass.statesystem.core.statevalue.TmfStateValue;

/**
 * @author yves
 * @since 2.1
 *
 */
public class StateValues {

    static int NETWORK_CREATE=1;
    static int NETWORK_UPDATE=2;
    static int NETWORK_DELETE=3;
    static int SUBNET_UPDATE=4;
    static int SUBNET_DELETE=5;
    static int PORT_DELETE=6;
    static int ROUTER_CREATE=7;
    static int ROUTER_REMOVE=8;
    static int ROUTER_DELETE=9;
    static ITmfStateValue NETWORK_CREATE_VALUE = TmfStateValue.newValueInt(NETWORK_CREATE);
    static ITmfStateValue NETWORK_UPDATE_VALUE = TmfStateValue.newValueInt(NETWORK_UPDATE);
    static ITmfStateValue NETWORK_DELETE_VALUE = TmfStateValue.newValueInt(NETWORK_DELETE);
    static ITmfStateValue SUBNET_UPDATE_VALUE = TmfStateValue.newValueInt(SUBNET_UPDATE);
    static ITmfStateValue SUBNET_DELETE_VALUE = TmfStateValue.newValueInt(SUBNET_DELETE);
    static ITmfStateValue PORT_DELETE_VALUE = TmfStateValue.newValueInt(PORT_DELETE);
    static ITmfStateValue ROUTER_CREATE_VALUE = TmfStateValue.newValueInt(ROUTER_CREATE);
    static ITmfStateValue ROUTER_REMOVE_VALUE = TmfStateValue.newValueInt(ROUTER_REMOVE);
    static ITmfStateValue ROUTER_DELETE_VALUE = TmfStateValue.newValueInt(ROUTER_DELETE);

}
