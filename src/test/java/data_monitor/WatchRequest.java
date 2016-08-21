package data_monitor;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import indix.datamonster.bo.Monitor;
import indix.datamonster.handlers.MonitorService;
import indix.datamonster.handlers.Watcher;
import indix.datamonster.handlers.WatcherRequestBuilder;

public class WatchRequest {

	public static final String QUERY = "\"query\":{  \r\n" + 
			"       \"filtered\":{  \r\n" + 
			"         \"filter\":{  \r\n" + 
			"            \"script\":{  \r\n" + 
			"               \"script\":\""
//			+ "doc[\\\"storeId\\\"].value == 5077 && doc[\\\"availability\\\"].value.equalsIgnoreCase('IN_STOCK')"
			+"${query}"
			+ "\"\r\n" + 
			"            }\r\n" + 
			"            }\r\n" + 
			"         },\r\n" + 
			"         \"query\":{  \r\n" + 
			"            \"match_all\":{  \r\n" + 
			"\r\n" + 
			"            }\r\n" + 
			"         }\r\n" + 
			"      }\r\n" + 
			"   }";
	public static final String SCRIPT_COMP = "{\r\n" + 
			"   \"trigger\":{\r\n" + 
			"      \"schedule\":{\r\n" + 
			"         \"interval\":\""
			+ "${interval}"
			+ "\"\r\n" + 
			"      }\r\n" + 
			"   },\r\n" + 
			"   \"input\":{\r\n" + 
			"      \"search\":{\r\n" + 
			"         \"request\":{\r\n" + 
			"            \"indices\":[\r\n" + 
			"               \""
			+ "${index}"
			+ "\"\r\n" + 
			"            ],\r\n" + 
			"            \"types\":[\r\n" + 
			"               \"catalog\"\r\n" + 
			"            ],\r\n" + 
			"            \"body\":{\r\n" + 
//			"               \"query\":{\r\n" +
			QUERY +
//			"\"filtered\":{  \r\n" + 
//			"         \"filter\":{  \r\n" + 
//			"            \"script\":{  \r\n" + 
//			"               \"script\":\""
////			+ "doc[\\\"minSalePrice\\\"].value "
//			+ "${left_op}"
////			+ "< "
//			+ "${op}"
////			+ "doc[\\\"minListPrice\\\"].value"
//			+ "${right_op}"
//			+ "\",\r\n" + 
//			"               \"params\":{  \r\n" + 
//			"                  \"cutoff\":1500\r\n" + 
//			"               }\r\n" + 
//			"            }\r\n" + 
//			"         },\r\n" + 
//			"         \"query\":{  \r\n" + 
//			"            \"match_all\":{  \r\n" + 
//			"\r\n" + 
//			"            }\r\n" + 
//			"         }\r\n" + 
//			"      }"+
//			"                  \"range\":{\r\n" + 
//			"                     \""
//			+ "${left_op}"
//			+ "\":{\r\n" + 
//			"                        \""
//			+ "${op}"
//			+ "\":"
//			+ "${right_op}"
//			+ "\r\n" + 
//			"                     }\r\n" + 
//			"                  }\r\n" + 
			"               }\r\n" + 
			"            }\r\n" + 
			"         }\r\n" + 
			"      }\r\n" + 
			"   },\r\n" + 
			"   \"condition\":{\r\n" + 
			"      \"compare\":{\r\n" + 
			"         \"ctx.payload.hits.total\":{\r\n" + 
			"            \"gt\":0\r\n" + 
			"         }\r\n" + 
			"      }\r\n" + 
			"   },\r\n" + 
			"   \"actions\":{\r\n"+
			"\"send_email\":{  \r\n" + 
			"   \"email\":{  \r\n" + 
			"      \"to\":[  \r\n" + 
			"         \"sathishs1988@gmail.com\",\r\n" + 
			"         \"praskris83@gmail.com\"\r\n" + 
			"      ],\r\n" + 
			"      \"subject\":\"Watcher Notification - Cluster has been RED for the last 60 seconds\",\r\n" + 
			"      \"body\":\"{{ctx.payload.hits.total}} watches took more than 2.5 seconds to execute.\",\r\n" + 
			"      \"attachments\":{  \r\n" + 
			"         \"data_attachments\":{  \r\n" + 
			"            \"data\":{  \r\n" + 
			"               \"format\":\"json\"\r\n" + 
			"            }\r\n" + 
			"         }\r\n" + 
			"      }\r\n" + 
			"   }\r\n" + 
			"}"+
//			"      \"log_error\":{\r\n" + 
//			"         \"logging\":{\r\n" + 
//			"            \"text\":\"Found {{ctx.payload.hits.total}} errors in the logs\"\r\n" + 
//			"         }\r\n" + 
//			"      }\r\n" + 
			"   }\r\n" + 
			"}";
	
	public static final String RANGE_COMP = "{\r\n" + 
			"   \"trigger\":{\r\n" + 
			"      \"schedule\":{\r\n" + 
			"         \"interval\":\""
			+ "${interval}"
			+ "\"\r\n" + 
			"      }\r\n" + 
			"   },\r\n" + 
			"   \"input\":{\r\n" + 
			"      \"search\":{\r\n" + 
			"         \"request\":{\r\n" + 
			"            \"indices\":[\r\n" + 
			"               \""
			+ "${index}"
			+ "\"\r\n" + 
			"            ],\r\n" + 
			"            \"types\":[\r\n" + 
			"               \"catalog\"\r\n" + 
			"            ],\r\n" + 
			"            \"body\":{\r\n" + 
			"               \"query\":{\r\n" + 
			"                  \"range\":{\r\n" + 
			"                     \""
			+ "${left_op}"
			+ "\":{\r\n" + 
			"                        \""
			+ "${op}"
			+ "\":"
			+ "${right_op}"
			+ "\r\n" + 
			"                     }\r\n" + 
			"                  }\r\n" + 
			"               }\r\n" + 
			"            }\r\n" + 
			"         }\r\n" + 
			"      }\r\n" + 
			"   },\r\n" + 
			"   \"condition\":{\r\n" + 
			"      \"compare\":{\r\n" + 
			"         \"ctx.payload.hits.total\":{\r\n" + 
			"            \"gt\":0\r\n" + 
			"         }\r\n" + 
			"      }\r\n" + 
			"   },\r\n" + 
			"   \"actions\":{\r\n"+
			"\"send_email\":{  \r\n" + 
			"   \"email\":{  \r\n" + 
			"      \"to\":[  \r\n" + 
			"         \"sathishs1988@gmail.com\",\r\n" + 
			"         \"praskris83@gmail.com\"\r\n" + 
			"      ],\r\n" + 
			"      \"subject\":\"Watcher Notification - Cluster has been RED for the last 60 seconds\",\r\n" + 
			"      \"body\":\"{{ctx.payload.hits.total}} watches took more than 2.5 seconds to execute.\",\r\n" + 
			"      \"attachments\":{  \r\n" + 
			"         \"data_attachments\":{  \r\n" + 
			"            \"data\":{  \r\n" + 
			"               \"format\":\"json\"\r\n" + 
			"            }\r\n" + 
			"         }\r\n" + 
			"      }\r\n" + 
			"   }\r\n" + 
			"}"+
//			"      \"log_error\":{\r\n" + 
//			"         \"logging\":{\r\n" + 
//			"            \"text\":\"Found {{ctx.payload.hits.total}} errors in the logs\"\r\n" + 
//			"         }\r\n" + 
//			"      }\r\n" + 
			"   }\r\n" + 
			"}";
	
	private List<Monitor> allMonitorts = new LinkedList<Monitor>();
	ObjectMapper mapper = new ObjectMapper();
	
	@Test
	public void test() {
		String index = "products";
		String left_op = "minSalePrice";
		String op = "gte";
		String right_op = "10";
		
		String inout = "{\r\n" + 
				"    \"trigger\": {\r\n" + 
				"        \"schedule\": {\r\n" + 
				"            \"interval\": \"60s\"\r\n" + 
				"        }\r\n" + 
				"    },\r\n" + 
				"    \"input\": {\r\n" + 
				"        \"search\": {\r\n" + 
				"            \"request\": {\r\n" + 
				"                \"indices\": [\r\n" + 
				"                    \"event\"\r\n" + 
				"                ],\r\n" + 
				"                \"body\": {\r\n" + 
				"                    \"query\": {\r\n" + 
				"                        \"match\": {\r\n" + 
				"                            \"eventCategory\": \"CRITICAL\"\r\n" + 
				"                        }\r\n" + 
				"                    }\r\n" + 
				"                }\r\n" + 
				"            }\r\n" + 
				"        }\r\n" + 
				"    },\r\n" + 
				"    \"condition\": {\r\n" + 
				"        \"compare\": {\r\n" + 
				"            \"ctx.payload.hits.total\": {\r\n" + 
				"                \"gt\": 0\r\n" + 
				"            }\r\n" + 
				"        }\r\n" + 
				"    },\r\n" + 
				"    \"actions\": {\r\n" + 
				"        \"email_admin\": {\r\n" + 
				"            \"email\": {\r\n" + 
				"                \"to\": \"'Tanmay Deshpande < tanmay.avinash.deshpande@gmail.com >'\",\r\n" + 
				"                \"subject\": \"{{ctx.watch_id}} executed\",\r\n" + 
				"                \"body\": \"{{ctx.watch_id}} executed with {{ctx.payload.hits.total}} hits\"\r\n" + 
				"            }\r\n" + 
				"        }\r\n" + 
				"    }\r\n" + 
				"}";
		String range = "{\r\n" + 
				"   \"trigger\":{\r\n" + 
				"      \"schedule\":{\r\n" + 
				"         \"interval\":\"60s\"\r\n" + 
				"      }\r\n" + 
				"   },\r\n" + 
				"   \"input\":{\r\n" + 
				"      \"search\":{\r\n" + 
				"         \"request\":{\r\n" + 
				"            \"indices\":[\r\n" + 
				"               \""
				+ "${index}"
				+ "\"\r\n" + 
				"            ],\r\n" + 
				"            \"types\":[\r\n" + 
				"               \"catalog\"\r\n" + 
				"            ],\r\n" + 
				"            \"body\":{\r\n" + 
				"               \"query\":{\r\n" + 
				"                  \"range\":{\r\n" + 
				"                     \""
				+ "${left_op}"
				+ "\":{\r\n" + 
				"                        \""
				+ "${op}"
				+ "\":"
				+ "${right_op}"
				+ "\r\n" + 
				"                     }\r\n" + 
				"                  }\r\n" + 
				"               }\r\n" + 
				"            }\r\n" + 
				"         }\r\n" + 
				"      }\r\n" + 
				"   },\r\n" + 
				"   \"condition\":{\r\n" + 
				"      \"compare\":{\r\n" + 
				"         \"ctx.payload.hits.total\":{\r\n" + 
				"            \"gt\":0\r\n" + 
				"         }\r\n" + 
				"      }\r\n" + 
				"   },\r\n" + 
				"   \"actions\":{\r\n" + 
				"      \"log_error\":{\r\n" + 
				"         \"logging\":{\r\n" + 
				"            \"text\":\"Found {{ctx.payload.hits.total}} errors in the logs\"\r\n" + 
				"         }\r\n" + 
				"      }\r\n" + 
				"   }\r\n" + 
				"}";
		
		String interval="60s";
		System.out.println(WatcherRequestBuilder.getRangeReq(index, left_op, right_op,op, interval));
	}

	@Test
	public void test1(){
		String rules = "minSalePrice::LT::0";
		System.out.println(Arrays.asList(rules.split("::")));
		
		System.out.println("\\d+".matches("pr"));
		System.out.println("0".matches("\\d+"));
	}
	
	@Test
	public void test2() throws JsonParseException, JsonMappingException, IOException {
		String index = "products";
		String left_op = "minSalePrice";
		String op = "lt";
		String right_op = "minListPrice";
		String interval="60s";
		
		allMonitorts = mapper.readValue(MonitorService.class.getClassLoader().getResourceAsStream("monitor.json"),
				new TypeReference<List<Monitor>>() {
				});
		Watcher.register(allMonitorts);
//		System.out.println(WatcherRequestBuilder.getRangeReq(index, left_op, right_op,op, interval));
	}
}
