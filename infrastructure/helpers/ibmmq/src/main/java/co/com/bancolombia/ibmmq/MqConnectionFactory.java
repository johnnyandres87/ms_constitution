package co.com.bancolombia.ibmmq;

import co.com.bancolombia.secretmanager.SecretManager;
import com.ibm.mq.jakarta.jms.MQConnectionFactory;
import com.ibm.mq.spring.boot.MQConfigurationProperties;
import com.ibm.msg.client.jakarta.jms.JmsFactoryFactory;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static com.ibm.msg.client.jakarta.jms.JmsConstants.JAKARTA_WMQ_PROVIDER;
import static com.ibm.msg.client.jakarta.jms.JmsConstants.PASSWORD;
import static com.ibm.msg.client.jakarta.wmq.common.CommonConstants.WMQ_CONNECTION_MODE;
import static com.ibm.msg.client.jakarta.wmq.common.CommonConstants.WMQ_CM_CLIENT;
import static com.ibm.msg.client.jakarta.wmq.common.CommonConstants.WMQ_CLIENT_RECONNECT;
import static com.ibm.msg.client.jakarta.wmq.common.CommonConstants.WMQ_QUEUE_MANAGER;
import static com.ibm.msg.client.jakarta.wmq.common.CommonConstants.USERID;
import static com.ibm.msg.client.jakarta.wmq.common.CommonConstants.WMQ_TEMPORARY_MODEL;




@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(MQConfigurationProperties.class)
public class MqConnectionFactory {

    public static final String JAVAX_NET_SSL_TRUST_STORE_PASSWORD = "javax.net.ssl.trustStorePassword";
    public static final String COM_IBM_MQ_CFG_USE_IBMCIPHER_MAPPINGS = "com.ibm.mq.cfg.useIBMCipherMappings";
    public static final String JAVAX_NET_SSL_TRUST_STORE = "javax.net.ssl.trustStore";

    @Value("${ibm.mq.ccdt}")
    String ccdt;
    String jks;
    @Value("${ibm.mq.secret-ibm}")
    String secretName;

    private final  SecretManager secretManager;

    @Bean
    public ConnectionFactory getMyConnectionFaactory(MQConfigurationProperties properties) throws IOException,
            JMSException {
        var ccdtUrl = new File(ccdt).toURI().toURL();
        var mqConnectionSecret = secretManager.getSecret(secretName, MqConnectionSecret.class);
        //System.setProperty(JAVAX_NET_SSL_TRUST_STORE, jks);
        System.setProperty(JAVAX_NET_SSL_TRUST_STORE_PASSWORD, mqConnectionSecret.trustStorePass());
        System.setProperty(COM_IBM_MQ_CFG_USE_IBMCIPHER_MAPPINGS, "false");
        var ff = JmsFactoryFactory.getInstance(JAKARTA_WMQ_PROVIDER);
        MQConnectionFactory mqConnectionFactory = (MQConnectionFactory) ff.createConnectionFactory();
        mqConnectionFactory.setIntProperty(WMQ_CONNECTION_MODE, WMQ_CM_CLIENT);
        mqConnectionFactory.setTransportType(WMQ_CM_CLIENT);
        mqConnectionFactory.setClientReconnectOptions(WMQ_CLIENT_RECONNECT);
        mqConnectionFactory.setStringProperty(WMQ_QUEUE_MANAGER, mqConnectionSecret.qmGroup());
        mqConnectionFactory.setStringProperty(USERID, mqConnectionSecret.user());
        mqConnectionFactory.setCCDTURL(ccdtUrl);
        if(Objects.nonNull(mqConnectionSecret.password())&&!mqConnectionSecret.password().isEmpty()){
            mqConnectionFactory.setStringProperty(PASSWORD, mqConnectionSecret.password());
        }
        if(Objects.nonNull(properties.getTempModel())){
            mqConnectionFactory.setStringProperty(WMQ_TEMPORARY_MODEL,properties.getTempModel());
        }

        return mqConnectionFactory;
    }
}
