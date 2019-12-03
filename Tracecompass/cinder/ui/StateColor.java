package org.eclipse.tracecompass.analysis.os.linux.openstack.cinder.ui;

import org.eclipse.swt.graphics.RGB;

public interface StateColor {
    public enum State {
        NOTSET (new RGB(186, 186, 186)),
        VOLUME_CREATE (new RGB(0, 102, 204)),
        VOLUME_UPDATE (new RGB(153,255,153)),
        VOLUME_DELETE (new RGB(153,0,0)),
        VOLUME_ATTACH(new RGB(0,102,0)),
        VOLUME_DETACH(new RGB(96,96,96)),
        VOLUME_MIGRATE(new RGB(255,103,204)),
        VOLUME_RESIZE(new RGB(204,103,255)),
        SNAPSHOT_CREATE(new RGB(0,0,255)),
        SNAPSHOT_DELETE(new RGB(153,0,0));
        public final RGB rgb;

        private State(RGB rgb) {
            this.rgb = rgb;
        }
    }

}
