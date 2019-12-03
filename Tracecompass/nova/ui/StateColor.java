package org.eclipse.tracecompas.analysis.os.linux.ui.views.openstack.service;

import org.eclipse.swt.graphics.RGB;

public interface StateColor {
    public enum State {
        NOTSET (new RGB(186, 186, 186)),
        //compute event color
        CREATE (new RGB(0, 102, 204)),
        DELETE (new RGB(204, 0, 0)),
        PAUSE (new RGB(255, 102, 102)),
        SHUTDOWN (new RGB(255, 51, 51)),
        LIVE_MIGRATION_PRE (new RGB(255, 102, 178)),
        LIVE_MIGRATION_POST (new RGB(255, 0, 127)),
        LIVE_MIGRATION_P_POST (new RGB(255, 102, 178)),
        POWER_OFF (new RGB(18, 255, 145)),
        POWER_ON (new RGB(0, 255, 0)),
        REBOOT (new RGB(51, 51, 255)),
        MIGRATION (new RGB(18, 255, 145)),
        //scheduler event color
        SHED_SELECT_DEST (new RGB(1, 255, 145)),
        SCHED_SELECT_FILTER_CHANCE (new RGB(18, 255, 145)),
        // conductor event color
        CONDUCTOR_LIVE_MIG (new RGB(18, 255, 145)),
        CONDUCTOR_BUILD_INSTANCE (new RGB(18, 255, 145)),
        CONDUCTOR_REBUILD_INSTANCE (new RGB(18, 255, 145)),
        CONDUCTOR_PROVIDE_FW_RULE (new RGB(18, 255, 145)),
        CONDUCTOR_OBJECT_ACTION (new RGB(18, 255, 145)),

        //netwok event color
        ALLOCATE_NETWORK (new RGB(102, 102, 0)),
        LEASE_FIXED_IP (new RGB(102, 102, 0)),
        DEALLOCATE_NETWORK (new RGB(102, 102, 0)),
        RELEASE_FIXED_IP (new RGB(102, 102, 0)),
        //logs color
        LOG (new RGB(187, 150, 145)),
        log_DEBUG_TR (new RGB(229, 204, 255)),
        log_ERR_CRIT (new RGB(255, 0, 0)),
        log_INFO (new RGB(187, 150, 145)),
        log_WARN (new RGB(255, 255, 153)),
        //nova virt libvirt driver
        LIVE_MIG_OPERATION (new RGB(100, 20, 145)),
        LIVE_MIG_MEM_PRECOPY (new RGB(100, 200, 145)),
        LIVE_MIG_MEM_COPY_Iter (new RGB(10, 240, 145)),
        LIVE_MIG_FIND_MIN_DATA(new RGB(100, 150, 0)),
        CAN_LIVE_MIG_SRC (new RGB(187, 150, 145)),
        CAN_LIVE_MIG_DST (new RGB(187, 0, 145)),
        LIVE_MIG_MONITOR (new RGB(187, 50, 120)),

        //VM state color
        VM_STATE_BUILDING (new RGB(102, 255, 255)),
        VM_STATE_ACTIVE (new RGB(0, 255, 0)),
        VM_STATE_SUSPENDED (new RGB(204, 204, 0)),
        VM_STATE_DELETED (new RGB(255, 250, 250)),
        VM_STATE_PAUSED (new RGB(204, 204, 0)),
        VM_STATE_STOPPED (new RGB(204, 204, 0)),
        VM_STATE_MIGRATING (new RGB(187, 50, 120));
        public final RGB rgb;

        private State(RGB rgb) {
            this.rgb = rgb;
        }
    }

}
