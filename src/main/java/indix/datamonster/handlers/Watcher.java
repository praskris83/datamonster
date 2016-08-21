/**
 * 
 */
package indix.datamonster.handlers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import indix.datamonster.bo.Monitor;

/**
 * @author prasad
 *
 */
@Service
public class Watcher {

	@Value("${datamonitor.es.clustername}")
	String clusterName = "82a5c9dc304ca43e36e011674d860e61";

	@Value("${datamonitor.es.host}")
	String clusterUrl = "82a5c9dc304ca43e36e011674d860e61.us-east-1.aws.found.io";

	static RestClient restClient = null;

//	@PostConstruct
	public void init() {
		System.out.println(clusterName + " -- " + clusterUrl);
		Header[] defaultHeaders = new BasicHeader[1];
		defaultHeaders[0] = new BasicHeader("Authorization", "Basic YWRtaW46YWRtaW4=");
		restClient = RestClient.builder(new HttpHost(clusterUrl, 9200, "http")).setDefaultHeaders(defaultHeaders)
				.build();
	}

	public static void register(List<Monitor> allMonitorts) {
		for (Monitor monitor : allMonitorts) {
			registerWatcher(monitor);
		}
	}

	public static void registerWatcher(Monitor monitor) {
		System.out.println("Adding Monitor - " + monitor);
		String rule = monitor.getRule();
		Map<String, String> ruleMap = buildRuleMap(rule);
		if(ruleMap != null){
			System.out.println("Rules Map " + ruleMap);
//				if(ruleMap.get("type") != null && ruleMap.get("type").equals("RANGE")){
				String req = WatcherRequestBuilder.getRangeReq(monitor.getEvent(), ruleMap.get("left")
						, ruleMap.get("right"), ruleMap.get("OP"), monitor.getInterval());
				System.out.println("Adding Watcher == " + req);
				addWatcher(req,monitor.getName());
//					break;
//				}
		}
	}

	private static void addWatcher(String req,String name) {
		Response response;
		try {
			response = restClient.performRequest("POST", "/_watcher/watch/"+name, Collections.singletonMap("pretty", "true"),
					new StringEntity(req));
			System.out.println("Result -- " + EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static Map<String, String> buildRuleMap(String rule) {
		Map<String, String> ruleMap = new HashMap<String, String>();
		String[] rules = rule.split("::");
		System.out.println(Arrays.asList(rules));
		if (rules.length == 3) {
			System.out.println("Adding Watcher ");
			ruleMap.put("type", "RANGE");
			ruleMap.put("left", getMappedOP(rules[0]));
			ruleMap.put("right", getMappedOP(rules[2]));
			ruleMap.put("OP", getMappedOPR(rules[1]));
//			if (! (rules[2]).matches("\\d+")) {
//				ruleMap.put("type", "SCRIPT");		
//			}else{
//				ruleMap.put("OP", (rules[1]));
//			}
			return ruleMap;
		}
		return null;
	}

	private static String getMappedOP(String rule) {
		if(! (rule).matches("\\d+")){
			return "doc[\\\""
					+ rule
					+ "\\\"].value";
		}
		
		return rule;
	}

	private static String getMappedOPR(String opr) {
		if(opr.equalsIgnoreCase("GTE")){
			return ">=";
		}else if(opr.equalsIgnoreCase("LTE")){
			return "<=";
		}else if(opr.equalsIgnoreCase("GT")){
			return ">";
		}else if(opr.equalsIgnoreCase("LT")){
			return "<";
		}else if(opr.equalsIgnoreCase("EQ")){
			return "==";
		}else if(opr.equalsIgnoreCase("NE")){
			return "!=";
		}
		return opr;
	}

}
