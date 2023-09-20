package com.baopiao;

import com.cdancy.jenkins.rest.JenkinsAuthentication;
import com.cdancy.jenkins.rest.auth.AuthenticationType;
import com.cdancy.jenkins.rest.domain.crumb.Crumb;
import com.cdancy.jenkins.rest.shaded.com.google.common.net.HttpHeaders;
import com.cdancy.jenkins.rest.shaded.com.google.inject.Inject;
import com.cdancy.jenkins.rest.shaded.com.google.inject.Singleton;
import com.cdancy.jenkins.rest.shaded.org.jclouds.http.HttpException;
import com.cdancy.jenkins.rest.shaded.org.jclouds.http.HttpRequest;
import com.cdancy.jenkins.rest.shaded.org.jclouds.http.HttpRequestFilter;
import com.cdancy.jenkins.rest.shaded.org.jclouds.rest.ResourceNotFoundException;

import java.util.Optional;

@Singleton
public class MyJenkinsAuthenticationFilter implements HttpRequestFilter {
    private final JenkinsAuthentication creds;
    private final MyJenkinsApi jenkinsApi;

    // key = Crumb, value = true if exception is ResourceNotFoundException false otherwise
    private volatile Pair<Crumb, Boolean> crumbPair = null;
    private static final String CRUMB_HEADER = "Jenkins-Crumb";

    private static final String RNFSimpleName = ResourceNotFoundException.class.getSimpleName();


    @Inject
    MyJenkinsAuthenticationFilter(final JenkinsAuthentication creds, final MyJenkinsApi jenkinsApi) {
        this.creds = creds;
        this.jenkinsApi = jenkinsApi;
    }

    private Pair<Crumb, Boolean> getCrumb() {
        Pair<Crumb, Boolean> crumbValueInit = this.crumbPair;
        if (crumbValueInit == null) {
            synchronized (this) {
                crumbValueInit = this.crumbPair;
                if (crumbValueInit == null) {
                    final Crumb crumb = jenkinsApi.crumbIssuerApi().crumb();
                    final Boolean isRNFE = crumb.errors().isEmpty() || crumb.errors().get(0).exceptionName().endsWith(RNFSimpleName);
                    this.crumbPair = crumbValueInit = new Pair<>(crumb, isRNFE);
                }
            }
        }
        return crumbValueInit;
    }


    @Override
    public HttpRequest filter(HttpRequest request) throws HttpException {
        HttpRequest.Builder<?> builder = request.toBuilder();

        // Password and API Token are both Basic authentication (there is no Bearer authentication in Jenkins)
        if (creds.authType() == AuthenticationType.Bearer || creds.authType() == AuthenticationType.Basic) {
            final String authHeader = creds.authType() + " " + creds.authValue();
            builder.addHeader(HttpHeaders.AUTHORIZATION, authHeader);
        }

        // Anon and Password need the crumb and the cookie when POSTing
        if (request.getMethod().equals("POST") &&
                (creds.authType() == AuthenticationType.Basic || creds.authType() == AuthenticationType.Anonymous)
        ) {
            final Pair<Crumb, Boolean> localCrumb = getCrumb();
            if (localCrumb.getKey().value() != null) {
                builder.addHeader(CRUMB_HEADER, localCrumb.getKey().value());
                Optional.ofNullable(localCrumb.getKey().sessionIdCookie())
                        .ifPresent(sessionId -> builder.addHeader(HttpHeaders.COOKIE, sessionId));
            } else {
                if (!localCrumb.getValue()) {
                    throw new RuntimeException("Unexpected exception being thrown: error=" + localCrumb.getKey().errors().get(0));
                }
            }
        }
        return builder.build();
    }

    // simple impl/copy of javafx.util.Pair
    private static class Pair<A, B> {
        private final A a;
        private final B b;

        public Pair(final A a, final B b) {
            this.a = a;
            this.b = b;
        }

        public A getKey() {
            return a;
        }

        public B getValue() {
            return b;
        }
    }
}
