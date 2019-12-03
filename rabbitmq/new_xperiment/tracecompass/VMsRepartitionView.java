package org.eclipse.tracecompass.sample;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.tracecompass.tmf.core.event.ITmfEvent;
import org.eclipse.tracecompass.tmf.core.event.ITmfEventField;
import org.eclipse.tracecompass.tmf.core.event.TmfEvent;
import org.eclipse.tracecompass.tmf.core.request.ITmfEventRequest;
import org.eclipse.tracecompass.tmf.core.request.TmfEventRequest;
import org.eclipse.tracecompass.tmf.core.signal.TmfSignalHandler;
import org.eclipse.tracecompass.tmf.core.signal.TmfTraceSelectedSignal;
import org.eclipse.tracecompass.tmf.core.timestamp.TmfTimeRange;
import org.eclipse.tracecompass.tmf.core.trace.ITmfTrace;
import org.eclipse.tracecompass.tmf.ui.views.TmfView;
import org.swtchart.Chart;
import org.swtchart.IAxis;
import org.swtchart.IAxisSet;
import org.swtchart.IBarSeries;
import org.swtchart.ISeries.SeriesType;

class HOSTS {

	private String name;
	private ArrayList<String >VMs=new ArrayList<String>();

	public HOSTS(String n,ArrayList<String> a){
		VMs=a;
		name=n;
	}
	public HOSTS(String n,String a){
		VMs.add(a);
		name=n;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<String> getVMs() {
		return VMs;
	}
	public void setVMs(ArrayList<String> vMs) {
		VMs = vMs;
	}
	public void addVM(String v){
		VMs.add(v);
	}

}

public class SampleView extends TmfView {
	private static final String SERIES_NAME = "Series";
	private static final String Y_AXIS_TITLE = "% of VM";
	private static final String X_AXIS_TITLE = "Host-";
	private static final String FIELD = "event_type"; // The name of the field that we want to display on the Y axis
	private static final String VIEW_ID = "org.eclipse.tracecompass.sample";
	private Chart chart;
	private ITmfTrace currentTrace;
	ArrayList<HOSTS> hosts=new ArrayList<HOSTS>();
	int vm_num =0;
	String []x_axis;
	double[] y_axis;
	public SampleView() {
		super(VIEW_ID);
	}

	@Override
	public void createPartControl(Composite parent) {
		System.out.println("parent control");
		chart = new Chart(parent, SWT.BORDER);
		chart.getTitle().setText("VMs REPARTITION");
		chart.getAxisSet().getXAxis(0).getTitle().setText(X_AXIS_TITLE);
		chart.getAxisSet().getYAxis(0).getTitle().setText(Y_AXIS_TITLE);
		IAxisSet axisSet = chart.getAxisSet();
		IAxis xAxis = axisSet.getXAxis(0);
		xAxis.enableCategory(true);
		
	}

	@Override
	public void setFocus() {
		chart.setFocus();
	}
	@TmfSignalHandler
	public void traceSelected(final TmfTraceSelectedSignal signal) {
		// Don't populate the view again if we're already showing this trace
		if (currentTrace == signal.getTrace()) {
			return;
		}
		currentTrace = signal.getTrace();

		// Create the request to get data from the trace
		TmfEventRequest req = new TmfEventRequest(TmfEvent.class,
				TmfTimeRange.ETERNITY, 0, ITmfEventRequest.ALL_DATA,
				ITmfEventRequest.ExecutionType.BACKGROUND) {
			@Override
			public void handleData(ITmfEvent data) {
				// Called for each event
				super.handleData(data);
				System.out.println("handle data");
				ITmfEventField field = data.getContent().getField(FIELD);//get event_type
				System.out.println(data.getContent().getField(FIELD).getValue());
				if((data.getContent().getField(FIELD).getValue()).equals("compute.instance.create.end")){
					if(data.getContent().getField("host")!=null){
						vm_num+=1;//vm number incr.
						System.out.println(vm_num);
						int i;
						for(i=0;i<hosts.size();i++){
							if(hosts.get(i).getName().equals(data.getContent().getField("host").getValue())){
								hosts.get(i).addVM((String)data.getContent().getField("vm_name").getValue());
								break;
							}
						}
						if(i==hosts.size()){
							HOSTS h=new HOSTS((String)data.getContent().getField("host").getValue(),(String)data.getContent().getField("vm_name").getValue());
							hosts.add(h);
						}
					}
				}

			}
			public String[] getArrayX(ArrayList<HOSTS> hosts){
				String []x1=new String[hosts.size()];
				for(int i=0;i<hosts.size();i++){
					x1[i]=hosts.get(i).getName();
				}
				return x1;		
			}
			public double[] getArrayY(ArrayList<HOSTS> hosts){
				double []y1=new double[vm_num];
				for(int i=0;i<hosts.size();i++){
					y1[i]=(double)hosts.get(i).getVMs().size()/(double)vm_num;
				}
				return y1;
			}
			@Override
			public void handleSuccess() {
				// Request successful, not more data available
				super.handleSuccess();
				System.out.println("handle success");
				if(!hosts.isEmpty()){
					System.out.println("event host not empty");
					x_axis =new String[hosts.size()];
					x_axis=getArrayX(hosts);
					y_axis=new double[vm_num];
					y_axis =getArrayY(hosts);
				}
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						IAxisSet axisSet = chart.getAxisSet();
						IAxis xAxis = axisSet.getXAxis(0);
						xAxis.setCategorySeries(x_axis);

						IBarSeries series = (IBarSeries) chart.getSeriesSet().createSeries(
								SeriesType.BAR, "line series");
						//series.setBarColor(parent.getDisplay().getSystemColor(SWT.COLOR_BLUE));
						Color c=new Color(null, 0,0,255);
						series.setBarColor(c);
						chart.getAxisSet().getXAxis(0).adjustRange();
						series.setYSeries(y_axis);
						chart.redraw();
					}
				});
			}

			@Override
			public void handleFailure() {
				// Request failed, not more data available
				super.handleFailure();
				System.out.println("handle failure");
			}
		};
		ITmfTrace trace = signal.getTrace();
		trace.sendRequest(req);
	}
}
