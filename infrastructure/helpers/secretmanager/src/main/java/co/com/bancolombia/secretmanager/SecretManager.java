package co.com.bancolombia.secretmanager;

public interface SecretManager  {
    <T> T getSecret(String secretName, Class<T> clazz);
}

