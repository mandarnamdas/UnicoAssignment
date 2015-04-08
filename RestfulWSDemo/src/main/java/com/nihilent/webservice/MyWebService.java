package com.nihilent.webservice;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;

import com.nihilent.consumer.QueueListener;
import com.nihilent.sender.MessageSender;

/**
 * Restful web service.
 * @author mandar.namdas
 *
 */
@Path("/rest")
public class MyWebService {

	final static Logger log = Logger.getLogger(MyWebService.class);
	
	/**
	 * Method is used to get 2 integer values from user and send it to queue for futher processing. 
	 * @param i1 as integer
	 * @param i2 as integer
	 * @return Success message to User
	 * @throws Exception
	 */
	@POST
	@Path("/push")	
	@Consumes("application/x-www-form-urlencoded")
	public Response push(@Context UriInfo uriInfo, @FormParam("param1") int i1, @FormParam ("param2") int i2) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Start push");
			log.debug("Input parameter i1 :"+i1);
			log.debug("Input parameter i2 :"+i2);
		}		
		MessageSender messageSender = new MessageSender();
		messageSender.sendMessage(i1+"-"+i2);
		if (log.isDebugEnabled()) {
			log.debug("End push");
		}
		URI uri = uriInfo.getBaseUriBuilder().path("success.jsp").build();
	    return Response.seeOther(uri).build();		
	}
	
	
	/**
	 * Method is used to display listened message in json format.
	 * @return string as JSON message 
	 * @throws Exception
	 */
	@GET
	@Path("/show")	
	@Produces("application/json")
	public String showQueueData() throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Start showQueueData");
		}
		QueueListener listener = new QueueListener();
		List<String> messageList = listener.listenMessage();
		StringBuilder message = new StringBuilder();
		for (String string : messageList) {
			message.append(string);
			message.append("\n");
			
		}
		if (log.isDebugEnabled()) {
			log.debug("End showQueueData");
		}
		return message.toString();
	}
}