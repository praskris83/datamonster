/**
 * 
 */
package indix.datamonster.handlers;

import org.springframework.util.StringUtils;

import indix.datamonster.bo.Monitor;

/**
 * @author prasad
 *
 */
public class WatcherRequestBuilder {

	public static final String TEMPLATE = "{\r\n" + 
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
			"                  \"filtered\":{\r\n" + 
			"                     \"filter\":{\r\n" + 
			"                        \"script\":{\r\n" + 
			"                           \"script\":\""
			+"${query}"
			+ "\",\r\n" + 
			"                           \"params\":{\r\n" + 
			"                              \"cutoff\":1500\r\n" + 
			"                           }\r\n" + 
			"                        }\r\n" + 
			"                     },\r\n" + 
			"                     \"query\":{\r\n" + 
			"                        \"match_all\":{\r\n" + 
			"\r\n" + 
			"                        }\r\n" + 
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
			"      \"send_email\":{\r\n" + 
			"         \"email\":{\r\n" + 
			"            \"to\":[\r\n" + 
			"               \"sathishs1988@gmail.com\",\r\n" + 
			"               \"praskris83@gmail.com\"\r\n" + 
			"            ],\r\n" + 
			"            \"subject\":\""
			+ "${message}"
			+ "\",\r\n" + 
			"            \"body\":\""
			+ "Total Alerts in the last ${interval} is {{ctx.payload.hits.total}}."
			+ "Please Refer attachment for more details"
			+ "\",\r\n" + 
			"            \"attachments\":{\r\n" + 
			"               \"data_attachments\":{\r\n" + 
			"                  \"data\":{\r\n" + 
			"                     \"format\":\"json\"\r\n" + 
			"                  }\r\n" + 
			"               }\r\n" + 
			"            }\r\n" + 
			"         }\r\n" + 
			"      }\r\n" + 
			"   }\r\n" + 
			"}";
	
	/**
	 * @param index
	 * @param left
	 * @param right
	 * @param op
	 * @param interval
	 * @return
	 */
	public static String getRangeReq(String index, String left, String right, String op,String interval){
		String request = StringUtils.replace(TEMPLATE, "${index}", index);
		request = StringUtils.replace(request, "${left_op}", left);
		request = StringUtils.replace(request, "${right_op}", right);
		request = StringUtils.replace(request, "${op}", op);
		request = StringUtils.replace(request, "${interval}", interval);
		
		return request;
	}
	
	/**
	 * @param query
	 * @param monitor
	 * @return
	 */
	public static String getQueryReq(String query, Monitor monitor){
		String request = StringUtils.replace(TEMPLATE, "${index}", monitor.getEvent());
		request = StringUtils.replace(request, "${query}", query);
		request = StringUtils.replace(request, "${interval}", monitor.getInterval());
		request = StringUtils.replace(request, "${message}", monitor.getMessage());
//		request = StringUtils.replace(request, "${to}", monitor.getAlertTo());
		
		return request;
	}
}
