package org.eclipse.tracecompass.analysis.os.linux.core.openstack;

import org.eclipse.tracecompass.statesystem.core.statevalue.ITmfStateValue;
import org.eclipse.tracecompass.statesystem.core.statevalue.TmfStateValue;

/**
 * @since 2.0
 */
@SuppressWarnings("javadoc")
public interface StateValues {

    int VM_STATUS_BUILDING=0;
    int VM_STATUS_ACTIVE=1;
    int VM_STATUS_SUSPENDED=2;
    int VM_STATUS_DELETED=3;
    int VM_STATUS_PAUSED=4;
    int VM_STATUS_STOPPED=5;
    int VM_STATUS_MIGRATING=6;

    ITmfStateValue VM_STATUS_BUILDING_VALUE = TmfStateValue.newValueInt(VM_STATUS_BUILDING);
    ITmfStateValue VM_STATUS_ACTIVE_VALUE = TmfStateValue.newValueInt(VM_STATUS_ACTIVE);
    ITmfStateValue VM_STATUS_SUSPENDED_VALUE=TmfStateValue.newValueInt(VM_STATUS_SUSPENDED);
    ITmfStateValue VM_STATUS_DELETED_VALUE=TmfStateValue.newValueInt(VM_STATUS_DELETED);
    ITmfStateValue VM_STATUS_PAUSED_VALUE=TmfStateValue.newValueInt(VM_STATUS_PAUSED);
    ITmfStateValue VM_STATUS_STOPPED_VALUE=TmfStateValue.newValueInt(VM_STATUS_STOPPED);
    ITmfStateValue VM_STATUS_MIGRATING_VALUE=TmfStateValue.newValueInt(VM_STATUS_MIGRATING);

  //nova compute manager event 10-
    public static ITmfStateValue CREATE=TmfStateValue.newValueInt(10);
    public static ITmfStateValue PAUSE=TmfStateValue.newValueInt(11);
    public static ITmfStateValue DELETE=TmfStateValue.newValueInt(12);
    public static ITmfStateValue SHUTDOWN=TmfStateValue.newValueInt(13);
    public static ITmfStateValue LIVE_MIGRATION_PRE=TmfStateValue.newValueInt(14);
    public static ITmfStateValue LIVE_MIGRATION_POST=TmfStateValue.newValueInt(15);
    public static ITmfStateValue LIVE_MIGRATION_P_POST=TmfStateValue.newValueInt(16);
    public static ITmfStateValue REBOOT=TmfStateValue.newValueInt(17);
    public static ITmfStateValue POWER_OFF=TmfStateValue.newValueInt(18);
    //nova compute scheduler event 20-
    public static ITmfStateValue SELECT_DEST=TmfStateValue.newValueInt(20);
    public static ITmfStateValue SELECT_DEST_BY_FILTER_CHANCE=TmfStateValue.newValueInt(21);

    //nova compute conductor event 30-
    public static ITmfStateValue CONDUCTOR_LIVE_MIG=TmfStateValue.newValueInt(30);
    public static ITmfStateValue CONDUCTOR_BUILD_INSTANCE=TmfStateValue.newValueInt(31);
    public static ITmfStateValue CONDUCTOR_REBUILD_INSTANCE=TmfStateValue.newValueInt(32);
    public static ITmfStateValue CONDUCTOR_PROVIDE_FW_RULE=TmfStateValue.newValueInt(33);
    public static ITmfStateValue CONDUCTOR_OBJECT_ACTION=TmfStateValue.newValueInt(34);

    //nova network event 40-
    public static ITmfStateValue ALLOCATE_NETWORK=TmfStateValue.newValueInt(40);
    public static ITmfStateValue LEASE_FIXED_IP=TmfStateValue.newValueInt(41);
    public static ITmfStateValue DEALLOCATE_NETWORK=TmfStateValue.newValueInt(42);
    public static ITmfStateValue RELEASE_FIXED_IP=TmfStateValue.newValueInt(43);
    //--nova virt libvirt driver 50-
    public static ITmfStateValue LIVE_MIG_OPERATION=TmfStateValue.newValueInt(50);
    public static ITmfStateValue LIVE_MIG_FIND_MIN_DATA=TmfStateValue.newValueInt(51);
    public static ITmfStateValue CAN_LIVE_MIG_SRC=TmfStateValue.newValueInt(52);
    public static ITmfStateValue CAN_LIVE_MIG_DST=TmfStateValue.newValueInt(53);
    public static ITmfStateValue LIVE_MIG_MONITOR=TmfStateValue.newValueInt(54);
    public static ITmfStateValue LIVE_MIG_MEM_PRECOPY=TmfStateValue.newValueInt(55);
    public static ITmfStateValue LIVE_MIG_MEM_COPY_Iter=TmfStateValue.newValueInt(56);
    //------log level-----
    public static ITmfStateValue NOTSET=TmfStateValue.newValueInt(100);
    public static ITmfStateValue TRACE=TmfStateValue.newValueInt(105);
    public static ITmfStateValue INFO=TmfStateValue.newValueInt(110);
    public static ITmfStateValue DEBUG=TmfStateValue.newValueInt(120);
    public static ITmfStateValue WARNING=TmfStateValue.newValueInt(130);
    public static ITmfStateValue ERROR=TmfStateValue.newValueInt(140);
    public static ITmfStateValue CRITICAL=TmfStateValue.newValueInt(150);
    //----------------------
    public static ITmfStateValue VM_ACTIVE=TmfStateValue.newValueString("active");
    public static ITmfStateValue VM_STOPPED=TmfStateValue.newValueString("stopped");
    public static ITmfStateValue VM_DELETED=TmfStateValue.newValueString("deleted");
    public static ITmfStateValue VM_DEFAULT=TmfStateValue.newValueString("default");


    }