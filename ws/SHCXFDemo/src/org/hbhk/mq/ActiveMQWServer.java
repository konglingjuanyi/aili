package org.hbhk.mq;

import javax.xml.ws.Endpoint;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.hbhk.service.impl.UserServiceImpl;

public class ActiveMQWServer {

	// ����JMS��URI�ĸ�ʽ��jms:queue or topic:destination
	private static final String JMS_ENDPOINT_URI = "jms:queue:jaxws.jmstransport.queue?timeToLive=1000"
			+ "&jndiConnectionFactoryName=ConnectionFactory"
			+ "&jndiInitialContextFactory"
			+ "=org.apache.activemq.jndi.ActiveMQInitialContextFactory";

	private ActiveMQWServer() {
	}

	public static void main(String args[]) throws Exception {
		ActiveMQWServer server = new ActiveMQWServer();
		// ��ʼ��Broker�ķ���
		server.initBrokerService();
		// ����JAXWS��ص� ����
		server.launchJaxwsApi();

		System.out.println("Server ready... Press any key to exit");
		System.in.read();
		System.out.println("Server exiting");
		System.exit(0);
	}

	/**
	 * ����ActiveMQ��Broker
	 * 
	 * @throws Exception
	 */
	public void initBrokerService() throws Exception {
		BrokerService brokerService = new BrokerService();
		brokerService
				.addConnector(ActiveMQConnectionFactory.DEFAULT_BROKER_BIND_URL);
		brokerService.setDataDirectory("target/activemq-data");
		brokerService.start();
	}

	/**
	 * ����WebService����
	 * 
	 */
	private void launchJaxwsApi() {
		Endpoint.publish(JMS_ENDPOINT_URI, new UserServiceImpl());
	}
}
