package org.eclipse.tracecompass.analysis.os.linux.openstack.neutron.ui;

import org.eclipse.swt.graphics.RGB;

public interface StateColor {
    public enum State {
        NOTSET (new RGB(186, 186, 186)),
        NETWORK_CREATE (new RGB(0, 102, 204)),
        NETWORK_DELETE (new RGB(204, 0, 0)),
        NETWORK_UPDATE (new RGB(255, 102, 102)),
        SUBNET_DELETE (new RGB(255, 51, 51)),
        SUBNET_UPDATE (new RGB(255, 102, 178)),
        PORT_DELETE (new RGB(18, 255, 145)),
        ROUTER_CREATE (new RGB(0, 255, 0)),
        ROUTER_REMOVE (new RGB(51, 51, 255)),
        ROUTER_DELETE (new RGB(18, 255, 145)),;
        public final RGB rgb;

        private State(RGB rgb) {
            this.rgb = rgb;
        }
    }

}



