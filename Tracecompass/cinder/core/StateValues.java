package org.eclipse.tracecompass.analysis.os.linux.openstack.cinder;

import org.eclipse.tracecompass.statesystem.core.statevalue.ITmfStateValue;
import org.eclipse.tracecompass.statesystem.core.statevalue.TmfStateValue;

/**
 * @author yves
 * @since 2.1
 *
 */
public class StateValues {
    public static int VOLUME_CREATE=1;
    public static int VOLUME_DELETE=2;
    public static int VOLUME_ATTACH=3;
    public static int VOLUME_DETACH=4;
    public static int VOLUME_MIGRATE=5;
    public static int VOLUME_RESIZE=6;
    public static int SNAPSHOT_CREATE=7;
    public static int SNAPSHOT_DELETE=8;
    public static int VOLUME_UPDATE=9;

    public static ITmfStateValue VOLUME_CREATE_VALUE = TmfStateValue.newValueInt(VOLUME_CREATE);
    public static ITmfStateValue VOLUME_DELETE_VALUE = TmfStateValue.newValueInt(VOLUME_DELETE);
    public static ITmfStateValue VOLUME_ATTACH_VALUE = TmfStateValue.newValueInt(VOLUME_ATTACH);
    public static ITmfStateValue VOLUME_DETACH_VALUE = TmfStateValue.newValueInt(VOLUME_DETACH);
    public static ITmfStateValue VOLUME_MIGRATE_VALUE = TmfStateValue.newValueInt(VOLUME_MIGRATE);
    public static ITmfStateValue VOLUME_RESIZE_VALUE = TmfStateValue.newValueInt(VOLUME_RESIZE);
    public static ITmfStateValue SNAPSHOT_CREATE_VALUE = TmfStateValue.newValueInt(SNAPSHOT_CREATE);
    public static ITmfStateValue SNAPSHOT_DELETE_VALUE = TmfStateValue.newValueInt(SNAPSHOT_DELETE);
    public static ITmfStateValue VOLUME_UPDATE_VALUE = TmfStateValue.newValueInt(VOLUME_UPDATE);

    public static ITmfStateValue NOTSET=TmfStateValue.newValueInt(100);
    public static ITmfStateValue TRACE=TmfStateValue.newValueInt(105);
    public static ITmfStateValue INFO=TmfStateValue.newValueInt(110);
    public static ITmfStateValue DEBUG=TmfStateValue.newValueInt(120);
    public static ITmfStateValue WARNING=TmfStateValue.newValueInt(130);
    public static ITmfStateValue ERROR=TmfStateValue.newValueInt(140);
    public static ITmfStateValue CRITICAL=TmfStateValue.newValueInt(150);
}
