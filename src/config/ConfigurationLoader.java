package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationLoader {

    public ProxyPropertyConfig load(){

        Properties properties = new Properties();

        try(InputStream stream = getClass().getClassLoader().getResourceAsStream("resources/proxy.properties")){

            if(stream == null){
                throw new IllegalStateException("proxy.properties not found");
            }

            properties.load(stream);

        }catch (IOException exception){
            throw new RuntimeException(
                    "Failed to load proxy.properties",
                    exception
            );
        }

        ProxyPropertyConfig config = new ProxyPropertyConfig();

        config.setProxyPort(
                Integer.parseInt(properties.getProperty("proxy.port"))
        );

        config.setConnectTimeout(
                Integer.parseInt(properties.getProperty("proxy.connect.timeout"))
        );

        config.setRequestTimeout(
                Integer.parseInt(
                        properties.getProperty("proxy.request.timeout")
                )
        );

        config.setHealthInterval(
                Integer.parseInt(
                        properties.getProperty("health.interval")
                )
        );

        config.setHealthTimeout(
                Integer.parseInt(
                        properties.getProperty("health.timeout")
                )
        );

        loadBackend(properties,config);

        return config;
    }

    private void loadBackend(Properties properties, ProxyPropertyConfig config) {
        int index = 1;

        while(true){

            String host = properties.getProperty("backend."+index+".host");

            if(host == null){
                break;
            }

            Integer port = Integer.parseInt(properties.getProperty("backend."+index+".port"));

            config.getBackends().add(new BackendConfiguration(host,port));

            index++;
        }

    }

}
