package config;

public class ConfigurationValidator {

    public void validate(ProxyPropertyConfig config) {

        if (config.getProxyPort() <= 0 ||
                config.getProxyPort() > 65535) {

            throw new IllegalArgumentException(
                    "Invalid proxy port."
            );
        }

        if (config.getBackends().isEmpty()) {

            throw new IllegalArgumentException(
                    "At least one backend must be configured."
            );
        }

        if (config.getConnectTimeout() <= 0) {

            throw new IllegalArgumentException(
                    "Invalid connect timeout."
            );
        }

        if (config.getRequestTimeout() <= 0) {

            throw new IllegalArgumentException(
                    "Invalid request timeout."
            );
        }

    }

}