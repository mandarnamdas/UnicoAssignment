package com.nihilent.consumer;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import org.apache.log4j.Logger;

/**
 * Queue listener class is used to listen messages from queue.
 * 
 * @author mandar.namdas
 * 
 */
public class QueueListener {

	final static Logger log = Logger.getLogger(QueueListener.class);
	private ConnectionFactory cf = null;
	private Connection connection = null;
	private Session session = null;
	private Queue queue = null;

	/**
	 * Method is used to listen message from queue.
	 * 
	 * @return message as String
	 * @throws Exception
	 */
	public List<String> listenMessage() throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Start listenMessage");
		}

		String destinationName = "java:/jms/queue/messageQueue";

		try {
			if (connection == null || session == null) {
				InitialContext jndi = new InitialContext();
				cf = (ConnectionFactory) jndi.lookup("java:/ConnectionFactory");
				queue = (Queue) jndi.lookup(destinationName);
				connection = cf.createConnection();
				session = connection.createSession(false,
						Session.AUTO_ACKNOWLEDGE);
				connection.start();
			}

			QueueBrowser queueBrowser = session.createBrowser(queue);
			Enumeration e = queueBrowser.getEnumeration();

			List<String> messageString = new ArrayList<String>();
			TextMessage message = null;
			String aNewString = null;
			while (e.hasMoreElements()) {
				message = (TextMessage) e.nextElement();
				System.out.println(" \n\n\t\t queueBrowser : "
						+ message.getText() + " \n\n");
				aNewString = message.getText().toString();
				messageString.add(aNewString);
			}
			if (log.isDebugEnabled()) {
				log.debug("End listenMessage");
			}
			return messageString;

		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (JMSException e) {
					e.printStackTrace();
					;
				}
			}
			if (connection != null) {
				connection.close();
			}
		}
	}

	/**
	 * Method to pop first element from queue.
	 * @return single message from queue as String
	 * @throws Exception
	 */
	public String listenSingleMessage() throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Start listenSingleMessage");
		}

		String destinationName = "java:/jms/queue/messageQueue";

		try {
			if (connection == null || session == null) {
				InitialContext jndi = new InitialContext();
				cf = (ConnectionFactory) jndi.lookup("java:/ConnectionFactory");
				queue = (Queue) jndi.lookup(destinationName);
				connection = cf.createConnection();
				session = connection.createSession(false,
						Session.AUTO_ACKNOWLEDGE);
				connection.start();
			}
			MessageConsumer consumer = session.createConsumer(queue);
			String messageString;
			TextMessage message = null;
			Message m = consumer.receive();
			message = (TextMessage) m;
			messageString = message.getText();
			if (log.isDebugEnabled()) {
				log.debug("End listenSingleMessage");
			}
			return messageString;

		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (JMSException e) {
					e.printStackTrace();
					;
				}
			}
			if (connection != null) {
				connection.close();
			}
		}
	}
}
