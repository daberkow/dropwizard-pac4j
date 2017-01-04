package org.pac4j.dropwizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.pac4j.core.authorization.authorizer.Authorizer;
import org.pac4j.core.authorization.generator.AuthorizationGenerator;
import org.pac4j.core.client.Client;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.http.CallbackUrlResolver;
import org.pac4j.core.matching.Matcher;
import org.pac4j.jax.rs.pac4j.JaxRsCallbackUrlResolver;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Dropwizard configuration factory to configure pac4j's {@link Config},
 * {@link Clients} as well as global JAX-RS
 * {@link org.pac4j.jax.rs.filters.SecurityFilter}s.
 * 
 * @see Pac4jConfiguration
 * @see org.pac4j.core.config.Config
 * @see org.pac4j.core.client.Clients
 * @author Evan Meagher
 * @author Victor Noel - Linagora
 * @since 1.0.0
 */
public class Pac4jFactory {

    @NotNull
    private List<FilterConfiguration> globalFilters = new ArrayList<>();

    private String clientNameParameter;

    private String callbackUrl;

    private CallbackUrlResolver callbackUrlResolver = new JaxRsCallbackUrlResolver();

    private boolean sessionEnabled = true;

    @NotNull
    private List<AuthorizationGenerator> authorizationGenerators = new ArrayList<>();

    @NotNull
    private Map<String, Matcher> matchers = new HashMap<>();

    @NotNull
    private List<Client> clients = new ArrayList<>();

    private String defaultClient = null;

    private String defaultClients = null;

    @NotNull
    private Map<String, Authorizer> authorizers = new HashMap<>();

    @JsonProperty
    public List<FilterConfiguration> getGlobalFilters() {
        return globalFilters;
    }

    @JsonProperty
    public void setGlobalFilters(List<FilterConfiguration> filters) {
        this.globalFilters = filters;
    }

    @JsonProperty
    public String getClientNameParameter() {
        return clientNameParameter;
    }

    @JsonProperty
    public void setClientNameParameter(String clientNameParameter) {
        this.clientNameParameter = clientNameParameter;
    }

    @JsonProperty
    public String getCallbackUrl() {
        return callbackUrl;
    }

    @JsonProperty
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    @JsonProperty
    public List<AuthorizationGenerator> getAuthorizationGenerators() {
        return authorizationGenerators;
    }

    @JsonProperty
    public void setAuthorizationGenerators(
            List<AuthorizationGenerator> authorizationGenerators) {
        this.authorizationGenerators = authorizationGenerators;
    }

    @JsonProperty
    public Map<String, Matcher> getMatchers() {
        return matchers;
    }

    @JsonProperty
    public void setMatchers(Map<String, Matcher> matchers) {
        this.matchers = matchers;
    }

    @JsonProperty
    public List<Client> getClients() {
        return clients;
    }

    @JsonProperty
    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    /**
     * @since 1.1.0
     */
    @JsonProperty
    public String getDefaultClient() {
        return defaultClient;
    }

    /**
     * @since 1.1.0
     */
    @JsonProperty
    public void setDefaultClient(String defaultClient) {
        this.defaultClient = defaultClient;
    }

    /**
     * @since 1.1.0
     */
    @JsonProperty
    public String getDefaultClients() {
        return defaultClients;
    }

    /**
     * @since 1.1.0
     */
    @JsonProperty
    public void setDefaultClients(String defaultClients) {
        this.defaultClients = defaultClients;
    }

    @JsonProperty
    public Map<String, Authorizer> getAuthorizers() {
        return authorizers;
    }

    @JsonProperty
    public void setAuthorizers(Map<String, Authorizer> authorizers) {
        this.authorizers = authorizers;
    }

    @JsonProperty
    public CallbackUrlResolver getCallbackUrlResolver() {
        return callbackUrlResolver;
    }

    @JsonProperty
    public void setCallbackUrlResolver(
            CallbackUrlResolver callbackUrlResolver) {
        this.callbackUrlResolver = callbackUrlResolver;
    }

    /**
     * @since 1.1.0
     */
    @JsonProperty
    public boolean getSessionEnabled() {
        return sessionEnabled;
    }

    /**
     * @param sessionEnabled
     *            if <code>true</code> session management will be enabled at the
     *            Jetty level, if <code>false</code> it won't.
     * @since 1.1.0
     */
    @JsonProperty
    public void setSessionEnabled(boolean sessionEnabled) {
        this.sessionEnabled = sessionEnabled;
    }

    public Config build() {
        Clients client = new Clients();
        Config config = new Config(client);

        if (callbackUrl != null) {
            client.setCallbackUrl(callbackUrl);
        }
        if (clientNameParameter != null) {
            client.setClientNameParameter(clientNameParameter);
        }
        client.setCallbackUrlResolver(callbackUrlResolver);
        client.setAuthorizationGenerators(authorizationGenerators);
        client.setClients(clients);

        if (defaultClient != null) {
            boolean found = false;
            for (Client c : clients) {
                if (defaultClient.equals(c.getName())) {
                    client.setDefaultClient(c);
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new IllegalArgumentException("Client '" + defaultClient
                        + "' is not one of the configured clients");
            }
        }

        config.setAuthorizers(authorizers);
        config.setMatchers(matchers);

        return config;
    }

    public static class FilterConfiguration {

        private Boolean skipResponse;

        private String clients;

        private String authorizers;

        private String matchers;

        private Boolean multiProfile;

        @JsonProperty
        public String getClients() {
            return clients;
        }

        @JsonProperty
        public String getAuthorizers() {
            return authorizers;
        }

        @JsonProperty
        public String getMatchers() {
            return matchers;
        }

        @JsonProperty
        public Boolean getMultiProfile() {
            return multiProfile;
        }

        @JsonProperty
        public Boolean getSkipResponse() {
            return skipResponse;
        }

        @JsonProperty
        public void setClients(String clients) {
            this.clients = clients;
        }

        @JsonProperty
        public void setAuthorizers(String authorizers) {
            this.authorizers = authorizers;
        }

        @JsonProperty
        public void setMatchers(String matchers) {
            this.matchers = matchers;
        }

        @JsonProperty
        public void setMultiProfile(Boolean multiProfile) {
            this.multiProfile = multiProfile;
        }

        @JsonProperty
        public void setSkipResponse(Boolean skipResponse) {
            this.skipResponse = skipResponse;
        }
    }
}
