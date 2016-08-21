package indix.datamonster.bo;

import java.util.LinkedList;
import java.util.List;

/**
 * @author prasad
 *
 */
public class Monitor {

	private String name;
	private String event;
	private List<Rule> rules = new LinkedList<Rule>();
	private String interval;
	private String alert;
	private String alertTo;
	private String message;

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
		return "Monitor [name=" + name + ", event=" + event + ", rules=" + rules + ", interval=" + interval + ", alert="
				+ alert + "]";
	}

	public List<Rule> getRules() {
		return rules;
	}

	public void setRules(List<Rule> rules) {
		this.rules = rules;
	}

	public String getAlertTo() {
		return alertTo;
	}

	public void setAlertTo(String alertTo) {
		this.alertTo = alertTo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Monitor other = (Monitor) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
