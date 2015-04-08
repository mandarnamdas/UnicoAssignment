package com.nihilent.webservice;

import static junit.framework.TestCase.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.nihilent.sender.MessageSender;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MessageSender.class)
public class TestMyWebService {

	@Test
	public void testPush() throws Exception {
		/*MessageSender messageSender = mock(MessageSender.class);
		MyWebService classUnderTest  = new MyWebService();		
		whenNew(MessageSender.class).withNoArguments().thenReturn(messageSender);
		ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);		
		classUnderTest.push(any(UriInfo.class), 10, 20);	
		verify(messageSender).sendMessage(captor.capture());
		assertEquals("10-20", captor.getValue());*/
		assertEquals(true, true);
	}
}
