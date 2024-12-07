package co.com.bancolombia.ibmmq;

public record MqConnectionSecret (String user, String   password, String trustStorePass, String qmGroup,
                                  String InputQueueListener){
}
