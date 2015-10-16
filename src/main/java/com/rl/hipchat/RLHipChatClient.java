package com.rl.hipchat;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.wink.client.ClientConfig;
import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;


import io.evanwong.oss.hipchat.v2.HipChatClient;
import io.evanwong.oss.hipchat.v2.commons.NoContent;
import io.evanwong.oss.hipchat.v2.rooms.GetAllRoomsRequestBuilder;
import io.evanwong.oss.hipchat.v2.rooms.MessageColor;
import io.evanwong.oss.hipchat.v2.rooms.Room;
import io.evanwong.oss.hipchat.v2.rooms.RoomItem;
import io.evanwong.oss.hipchat.v2.rooms.Rooms;
import io.evanwong.oss.hipchat.v2.rooms.SendRoomNotificationRequestBuilder;


@Path("/hipChat")
public class RLHipChatClient {
	
	@GET
	@Path("/notify/{msg}")
	public Response hipChatNotifier(@PathParam("msg") String msg){		
		String defaultAccessToken = "m7oOuxsaptDDjaJGVOOnMa90zNzJmu3Lu7S3JzLI";
		
		String res = "success";
		
		NoContent noContent = null;
		
	   try {	
		
		   HipChatClient client = new HipChatClient(defaultAccessToken);
		   SendRoomNotificationRequestBuilder builder = client.prepareSendRoomNotificationRequestBuilder("Rlpochipchat", msg);
		   Future<NoContent> future = builder.setColor(MessageColor.RED).setNotify(true).build().execute();
		
		   noContent = future.get();
		  
		} catch (InterruptedException e) {
			e.printStackTrace();
			res="failure";
		} catch (ExecutionException e) {
			e.printStackTrace();
			res="failure";
		}
		
		if(noContent==null){
			res="failure";
		}
		System.out.println(noContent);
		
		return Response.status(200).entity(res).build();
	}
	
	@GET
	@Path("/{param}")
	public Response getMsg(@PathParam("param") String msg) {
		String output = "Jersey say : " + msg;
		return Response.status(200).entity(output).build();
	}
	
	public void getAllrooms(){
		
		String defaultAccessToken = "m7oOuxsaptDDjaJGVOOnMa90zNzJmu3Lu7S3JzLI";
		
		String roomNotificationToken = "uecQZ7kjcruBUv4OhZ1buOwAK3tikuzMACyfHJn1";
		
		HipChatClient client = new HipChatClient(defaultAccessToken);
	    GetAllRoomsRequestBuilder builder =	client.prepareGetAllRoomsRequestBuilder();
	    Future<Rooms> future = builder.build().execute();
	    
	    Rooms rooms = null;
	    try {
	    	rooms = future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	 
		List<RoomItem> roomItems = rooms.getItems();
		
		for(RoomItem roomItem:roomItems){
			
			System.out.println(roomItem.getName());
		}

	}

	
	@GET
	@Path("jenkins/logs")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getJenkinsLogs(){
		
		String response = getLogs();
		return Response.ok(new JenkinsResponseModel(response)).build();
		
	}
	
	public String getLogs(){
		
		String auth_url = "http://52.27.46.167:8080/j_acegi_security_check";
		
		ClientConfig config = new ClientConfig();
		config.followRedirects(false);
		
		RestClient restClient = new RestClient(config);
		
		Resource resource = restClient.resource(auth_url);
		//resource.header("Connection", "keep-alive");
		//resource.contentType("application/x-www-form-urlencoded");
		//resource.header("Content-Length", "41");
		
		String formdata = "j_username=admin&j_password=admin@RL123";
		
		System.out.println("Authenticating against jenkins");
		
		ClientResponse response = resource.post(formdata);
		
		String serviceResponse = response.getEntity(String.class);
		System.out.println("Response code:"+response.getStatusCode());
		System.out.println("Headers:"+response.getHeaders());
		
		String cookie = response.getHeaders().get("set-cookie").get(0)
		.split(";")[0].split("=")[1];
		
		System.out.println("Cookie:" +cookie);
		
		String log_url = "http://52.27.46.167:8080/job/Catalyst_Get/339/logText/progressiveText?start=0";
		
		resource = restClient.resource(log_url);
		//resource.cookie("connect.sid" + "=" + cookie);
		//resource.header("Connection", "keep-alive");
		
		response = resource.contentType("application/json").get();
		serviceResponse = response.getEntity(String.class);
		
		System.out.println("Logs Response:"+serviceResponse);
		
		return serviceResponse;
	}
	
	
	public static void main(String[] args) {
		
		 //new RLHipChatClient().hipChatNotifier();
		
		RLHipChatClient hipClient = new RLHipChatClient();
		
		hipClient.getLogs();
	}
}
