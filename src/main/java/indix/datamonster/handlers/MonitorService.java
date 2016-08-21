/**
 * 
 */
package indix.datamonster.handlers;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import indix.datamonster.bo.Monitor;

/**
 * @author prasad
 *
 */
@RestController
@RequestMapping("/monitors")
public class MonitorService {

	private List<Monitor> allMonitorts = new LinkedList<Monitor>();
	ObjectMapper mapper = new ObjectMapper();

	@Autowired
	private Watcher watcher;
	
//	@PostConstruct
	public void init() {
		loadMonitors();
	}

	private void loadMonitors() {
		try {
			allMonitorts = mapper.readValue(MonitorService.class.getClassLoader().getResourceAsStream("monitor.json"),
					new TypeReference<List<Monitor>>() {
					});
			System.out.println("Watchers Count == " +allMonitorts.size());
			watcher.register(allMonitorts);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public List<Monitor> list() {
		return allMonitorts;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public List<Monitor> add(@RequestBody Monitor monitor) {
		allMonitorts.add(monitor);
		watcher.registerWatcher(monitor);
		return allMonitorts;
	}
}
