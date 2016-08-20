package indix.datamonster.bo;

/**
 * @author prasad
 *
 */
public class Monitor {

	private String name;
	private String event;
	private String rule;
	private String interval;
	private String alert;

	public Monitor(String name) {
		super();
		this.name = name;
	}

	public Monitor() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	public String getAlert() {
		return alert;
	}

	public void setAlert(String alert) {
		this.alert = alert;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getInterval() {
		return interval;
	}

	public void setInterval(String interval) {
		this.interval = interval;
	}

	@Override
	public String toString() {
		return "Monitor [name=" + name + ", event=" + event + ", rule=" + rule + ", alert=" + alert + "]";
	}

}
