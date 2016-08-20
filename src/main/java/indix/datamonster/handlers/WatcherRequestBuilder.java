/**
 * 
 */
package indix.datamonster.handlers;

import org.springframework.util.StringUtils;

/**
 * @author prasad
 *
 */
public class WatcherRequestBuilder {

	String left_op = "minSalePrice";
	String op = "gte";
	String right_op = "10";
	
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
			"               \"query\":{\r\n" +
			"\"filtered\":{  \r\n" + 
			"         \"filter\":{  \r\n" + 
			"            \"script\":{  \r\n" + 
			"               \"script\":\""
//			+ "doc[\\\"minSalePrice\\\"].value "
			+ "${left_op}"
//			+ "< "
			+ "${op}"
//			+ "doc[\\\"minListPrice\\\"].value"
			+ "${right_op}"
			+ "\",\r\n" + 
			"               \"params\":{  \r\n" + 
			"                  \"cutoff\":1500\r\n" + 
			"               }\r\n" + 
			"            }\r\n" + 
			"         },\r\n" + 
			"         \"query\":{  \r\n" + 
			"            \"match_all\":{  \r\n" + 
			"\r\n" + 
			"            }\r\n" + 
			"         }\r\n" + 
			"      }"+
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
	
	public static String getRangeReq(String index, String left, String right, String op,String interval){
		String request = StringUtils.replace(SCRIPT_COMP, "${index}", index);
		request = StringUtils.replace(request, "${left_op}", left);
		request = StringUtils.replace(request, "${right_op}", right);
		request = StringUtils.replace(request, "${op}", op);
		request = StringUtils.replace(request, "${interval}", interval);
		
		return request;
	}
}
