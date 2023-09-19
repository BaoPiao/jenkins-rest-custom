package com.baopiao;

import com.cdancy.jenkins.rest.JenkinsAuthentication;
import com.cdancy.jenkins.rest.JenkinsUtils;
import com.cdancy.jenkins.rest.auth.AuthenticationType;
import com.cdancy.jenkins.rest.config.JenkinsAuthenticationModule;
import com.cdancy.jenkins.rest.shaded.com.google.inject.Module;
import com.cdancy.jenkins.rest.shaded.javax.annotation.Nullable;
import com.cdancy.jenkins.rest.shaded.org.jclouds.ContextBuilder;
import com.google.common.collect.Lists;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class MyJenkinsClient implements Closeable {
    private final String endPoint;
    private final JenkinsAuthentication credentials;
    private final MyJenkinsApi jenkinsApi;
    private final Properties overrides;

    public MyJenkinsClient() {
        this((String) null, (JenkinsAuthentication) null, (Properties) null, (List) null);
    }

    public MyJenkinsClient(@Nullable String endPoint, @Nullable JenkinsAuthentication authentication, @Nullable Properties overrides, @Nullable List<Module> modules) {
        this.endPoint = endPoint != null ? endPoint : JenkinsUtils.inferEndpoint();
        this.credentials = authentication != null ? authentication : JenkinsUtils.inferAuthentication();
        this.overrides = this.mergeOverrides(overrides);
        this.jenkinsApi = this.createApi(this.endPoint, this.credentials, this.overrides, modules);
    }

    private MyJenkinsApi createApi(String endPoint, JenkinsAuthentication authentication, Properties overrides, List<Module> modules) {
        List<Module> allModules = Lists.newArrayList(new Module[]{ new JenkinsAuthenticationModule(authentication)});
        if (modules != null) {
            allModules.addAll(modules);
        }

        ContextBuilder contextBuilder = ContextBuilder.newBuilder((new MyJenkinsApiMetaData.Builder()).build());
        ContextBuilder overrides1 = contextBuilder.endpoint(endPoint).modules(allModules).overrides(overrides);
        return (MyJenkinsApi) overrides1.buildApi(MyJenkinsApi.class);
    }

    private Properties mergeOverrides(Properties possibleOverrides) {
        Properties inferOverrides = JenkinsUtils.inferOverrides();
        if (possibleOverrides != null) {
            inferOverrides.putAll(possibleOverrides);
        }

        return inferOverrides;
    }

    public String endPoint() {
        return this.endPoint;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public String credentials() {
        return this.authValue();
    }

    public Properties overrides() {
        return this.overrides;
    }

    public String authValue() {
        return this.credentials.authValue();
    }

    public AuthenticationType authType() {
        return this.credentials.authType();
    }

    public MyJenkinsApi api() {
        return this.jenkinsApi;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void close() throws IOException {
        if (this.api() != null) {
            this.api().close();
        }

    }

    public static class Builder {
        private String endPoint;
        private JenkinsAuthentication.Builder authBuilder;
        private Properties overrides;
        private List<Module> modules = Lists.newArrayList();

        public Builder() {
        }

        public Builder endPoint(String endPoint) {
            this.endPoint = endPoint;
            return this;
        }

        public Builder credentials(String optionallyBase64EncodedCredentials) {
            this.authBuilder = JenkinsAuthentication.builder().credentials(optionallyBase64EncodedCredentials);
            return this;
        }

        public Builder apiToken(String apiToken) {
            this.authBuilder = JenkinsAuthentication.builder();
            return this;
        }

        public Builder overrides(Properties overrides) {
            this.overrides = overrides;
            return this;
        }

        public Builder modules(Module... modules) {
            this.modules.addAll(Arrays.asList(modules));
            return this;
        }

        public MyJenkinsClient build() {
            JenkinsAuthentication authentication = this.authBuilder != null ? this.authBuilder.build() : null;
            return new MyJenkinsClient(this.endPoint, authentication, this.overrides, this.modules);
        }
    }
}
