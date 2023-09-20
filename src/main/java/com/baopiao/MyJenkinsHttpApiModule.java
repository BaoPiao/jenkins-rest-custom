package com.baopiao;

import com.cdancy.jenkins.rest.handlers.JenkinsErrorHandler;
import com.cdancy.jenkins.rest.shaded.org.jclouds.http.HttpErrorHandler;
import com.cdancy.jenkins.rest.shaded.org.jclouds.http.annotation.ClientError;
import com.cdancy.jenkins.rest.shaded.org.jclouds.http.annotation.Redirection;
import com.cdancy.jenkins.rest.shaded.org.jclouds.http.annotation.ServerError;
import com.cdancy.jenkins.rest.shaded.org.jclouds.rest.ConfiguresHttpApi;
import com.cdancy.jenkins.rest.shaded.org.jclouds.rest.config.HttpApiModule;

@ConfiguresHttpApi
public class MyJenkinsHttpApiModule extends HttpApiModule<MyJenkinsApi> {

    @Override
    protected void bindErrorHandlers() {
        bind(HttpErrorHandler.class).annotatedWith(Redirection.class).to(JenkinsErrorHandler.class);
        bind(HttpErrorHandler.class).annotatedWith(ClientError.class).to(JenkinsErrorHandler.class);
        bind(HttpErrorHandler.class).annotatedWith(ServerError.class).to(JenkinsErrorHandler.class);
    }
}