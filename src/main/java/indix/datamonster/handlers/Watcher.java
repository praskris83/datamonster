/**
 * 
 */
package indix.datamonster.handlers;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import indix.datamonster.bo.Monitor;
import indix.datamonster.bo.Rule;

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
		List<Rule> rules = monitor.getRules();
		String query = buildRuleMap(rules);
		String req = WatcherRequestBuilder.getQueryReq(query, monitor);
		System.out.println("Adding Watcher == " + req);
//		addWatcher(req, monitor.getName());

//		Map<String, String> ruleMap = buildRuleMap(rules);
//		if (ruleMap != null) {
//			System.out.println("Rules Map " + ruleMap);
//			// if(ruleMap.get("type") != null &&
//			// ruleMap.get("type").equals("RANGE")){
//			String req = WatcherRequestBuilder.getRangeReq(monitor.getEvent(), ruleMap.get("left"),
//					ruleMap.get("right"), ruleMap.get("OP"), monitor.getInterval());
//			System.out.println("Adding Watcher == " + req);
//			addWatcher(req, monitor.getName());
//			// break;
//			// }
//		}
	}

	private static String buildRuleMap(List<Rule> allrules) {
		StringBuilder query = new StringBuilder();
		for (Rule rule : allrules) {
			String[] rules = rule.getRule().split("::");
			query.append(" && ");
			boolean isString = false;
			isString = getMappedRule(rules[0], isString, query,true);
			isString = getMappedRule(rules[2], isString, query,false);
			getMappedRuleOpr(rules[1], isString, query);
			getMappedRule(rules[2], isString, query,true);
		}
		return query.substring(4);
	}

	private static void getMappedRuleOpr(String opr, boolean isString, StringBuilder query) {
		if (isString) {
			query.append(".equalsIgnoreCase");
		}else{
			query.append(getMappedOPR(opr));
		}
	}

	private static boolean getMappedRule(String opr, boolean isString, StringBuilder query, boolean canAppend) {
		if(!canAppend){
			query = new StringBuilder();
		}
		boolean isAttr = false;
		if (opr.contains(".")) {
			System.out.println("Parsing Atr " + opr);
			String[] split = opr.split("\\.");
			opr = split[1];
			isString = false;
			isAttr = true;
		}
		if (!(opr).matches("\\d+")) {
			if (isAttr) {
				query.append("doc[\\\"");
				query.append(opr);
				query.append("\\\"].value");
			} else {
				query.append("('");
				query.append(opr);
				query.append("')");
				isString = true;
			}
		} else {
			query.append(opr);
			isString = false;
		}

		return isString;
	}

	private static void addWatcher(String req, String name) {
		Response response;
		try {
			response = restClient.performRequest("POST", "/_watcher/watch/" + name,
					Collections.singletonMap("pretty", "true"), new StringEntity(req));
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
			// if (! (rules[2]).matches("\\d+")) {
			// ruleMap.put("type", "SCRIPT");
			// }else{
			// ruleMap.put("OP", (rules[1]));
			// }
			return ruleMap;
		}
		return null;
	}

	private static String getMappedOP(String rule) {
		if (!(rule).matches("\\d+")) {
			return "doc[\\\"" + rule + "\\\"].value";
		}

		return rule;
	}

	private static String getMappedOPR(String opr) {
		if (opr.equalsIgnoreCase("GTE")) {
			return ">=";
		} else if (opr.equalsIgnoreCase("LTE")) {
			return "<=";
		} else if (opr.equalsIgnoreCase("GT")) {
			return ">";
		} else if (opr.equalsIgnoreCase("LT")) {
			return "<";
		} else if (opr.equalsIgnoreCase("EQ")) {
			return "==";
		} else if (opr.equalsIgnoreCase("NE")) {
			return "!=";
		}
		return opr;
	}

}
