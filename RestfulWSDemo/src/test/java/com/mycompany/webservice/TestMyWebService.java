package com.mycompany.webservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.mycompany.consumer.QueueListener;
import com.mycompany.sender.MessageSender;
import com.mycompany.soapservice.MySoapService;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ MyWebService.class, MessageSender.class, QueueListener.class, MySoapService.class})
public class TestMyWebService {

	final static Logger log = Logger.getLogger(TestMyWebService.class);
	
	/**
	 * Method to test message sender using Apace MQ.
	 * @throws Exception
	 */
	@Test
	public void testSendMessage() throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Start testSendMessage");
		}
		final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"vm://localhost?broker.persistent=false");
		final Connection connection = connectionFactory.createConnection();
		connection.start();
		final Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		final Queue queue = session.createTemporaryQueue();
		InitialContext jndi = mock(InitialContext.class);
		PowerMockito.whenNew(InitialContext.class).withNoArguments()
				.thenReturn(jndi);
		when(jndi.lookup("java:/ConnectionFactory")).thenReturn(
				connectionFactory);
		when(jndi.lookup("java:/jms/queue/messageQueue")).thenReturn(queue);
		MessageSender classUnderTest = new MessageSender();
		classUnderTest.sendMessage("11-12");

		// Create Consumer and read message.
		final MessageConsumer consumer = session.createConsumer(queue);
		final TextMessage message = (TextMessage) consumer.receiveNoWait();
		assertNotNull(message);
		assertEquals("11-12", message.getText());
		if (log.isDebugEnabled()) {
			log.debug("End testSendMessage");
		}
	}

	/*@Test
	public void testListenMessage() throws Exception {
		final ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				"vm://localhost?broker.persistent=false");
		final Connection connection = connectionFactory.createConnection();
		final Session session = connection.createSession(false,
				Session.AUTO_ACKNOWLEDGE);
		connection.start();
		// Create Queue and send a message.
		final Queue queue = session.createTemporaryQueue();
		InitialContext jndi = mock(InitialContext.class);
		PowerMockito.whenNew(InitialContext.class).withNoArguments()
				.thenReturn(jndi);
		when(jndi.lookup("java:/ConnectionFactory")).thenReturn(
				connectionFactory);
		when(jndi.lookup("java:/jms/queue/messageQueue")).thenReturn(queue);		
		MessageProducer producer = session.createProducer(queue);
		TextMessage message = session.createTextMessage("10-20");
		producer.send(message);
		QueueListener listener = new QueueListener();
		String text = listener.listenSingleMessage();
		assertEquals("10-20", text);
	}*/
	
	/**
	 * Method to test GCD calculation.
	 * @throws Exception
	 */
	@Test
	public void testGCD() throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Start testGCD");
		}
		QueueListener listener = mock(QueueListener.class);
		PowerMockito.whenNew(QueueListener.class).withNoArguments()
		.thenReturn(listener);
		when(listener.listenSingleMessage()).thenReturn("10-20");
		MySoapService classUnderTest = new MySoapService();
		int gcd = classUnderTest.gcd();
		assertEquals(10, gcd);
		if (log.isDebugEnabled()) {
			log.debug("End testGCD");
		}
	}

}
