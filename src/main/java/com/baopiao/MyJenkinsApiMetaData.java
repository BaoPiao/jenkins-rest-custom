package com.baopiao;

import com.cdancy.jenkins.rest.shaded.com.google.auto.service.AutoService;
import com.cdancy.jenkins.rest.shaded.org.jclouds.apis.ApiMetadata;
import com.cdancy.jenkins.rest.shaded.org.jclouds.rest.internal.BaseHttpApiMetadata;
import com.google.common.collect.ImmutableSet;

import java.net.URI;
import java.util.Properties;

@AutoService(ApiMetadata.class)
public class MyJenkinsApiMetaData extends BaseHttpApiMetadata<MyJenkinsApi> {

    public static final String API_VERSION = "1.0";
    public static final String BUILD_VERSION = "2.0";

    @Override
    public Builder toBuilder() {
        return new Builder().fromApiMetadata(this);
    }

    public MyJenkinsApiMetaData() {
        this(new Builder());
    }

    protected MyJenkinsApiMetaData(Builder builder) {
        super(builder);
    }

    public static Properties defaultProperties() {
        return org.jclouds.rest.internal.BaseHttpApiMetadata.defaultProperties();
    }

    public static class Builder extends BaseHttpApiMetadata.Builder<MyJenkinsApi, Builder> {

        protected Builder() {
            super(MyJenkinsApi.class);
            id("jenkins").name("Jenkins API").identityName("Optional Username").credentialName("Optional Password")
                    .defaultIdentity("").defaultCredential("")
                    .documentation(URI.create("http://wiki.jenkins-ci.org/display/JENKINS/Remote+access+API"))
                    .version(API_VERSION).buildVersion(BUILD_VERSION).defaultEndpoint("http://127.0.0.1:8080")
                    .defaultProperties(MyJenkinsApiMetaData.defaultProperties())
                    .defaultModules(ImmutableSet.of(MyJenkinsHttpApiModule.class));
        }

        @Override
        public MyJenkinsApiMetaData build() {
            return new MyJenkinsApiMetaData(this);
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Builder fromApiMetadata(ApiMetadata in) {
            return this;
        }
    }
}
