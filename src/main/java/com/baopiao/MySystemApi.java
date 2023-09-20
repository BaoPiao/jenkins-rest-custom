package com.baopiao;

import com.cdancy.jenkins.rest.domain.common.RequestStatus;
import com.cdancy.jenkins.rest.fallbacks.JenkinsFallbacks;
import com.cdancy.jenkins.rest.features.SystemApi;
import com.cdancy.jenkins.rest.parsers.RequestStatusParser;
import com.cdancy.jenkins.rest.shaded.javax.inject.Named;
import com.cdancy.jenkins.rest.shaded.javax.ws.rs.*;
import com.cdancy.jenkins.rest.shaded.javax.ws.rs.core.MediaType;
import com.cdancy.jenkins.rest.shaded.org.jclouds.rest.annotations.*;


@RequestFilters({MyJenkinsAuthenticationFilter.class})
@Consumes(MediaType.APPLICATION_JSON)
@Path("/")
public interface MySystemApi extends SystemApi {
    @Named("system:add-credentials")
    @Path("credentials/store/system/domain/_/createCredentials")
    @Fallback(JenkinsFallbacks.RequestStatusOnError.class)
    @ResponseParser(RequestStatusParser.class)
    @Produces({"application/x-www-form-urlencoded"})
    @Consumes({"text/html"})
    @Payload("_.scope=GLOBAL&_.username={credentialUsername}" +
            "&_.password={credentialPassword}" +
            "&_.id={credentialsId}" +
            "&_.description=&stapler-class=com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl" +
            "&$class=com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl" +
            "&json={\"\": \"0\", \"credentials\": {\"scope\": \"GLOBAL\"," +
            " \"username\": \"{credentialUsername}\", " +
            "\"usernameSecret\": false, " +
            "\"password\": \"{credentialPassword}\", " +
            "\"$redact\": \"password\"," +
            " \"id\": \"{credentialsId}\", \"description\": \"\"," +
            " \"stapler-class\": \"com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl\"," +
            " \"$class\": \"com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl\"}, \"Submit\": \"\"}")
    @POST
    RequestStatus addCredentials(@PayloadParam("credentialsId") String credentialsId,
                                 @PayloadParam("credentialUsername") String credentialUsername,
                                 @PayloadParam("credentialPassword") String credentialPassword);

    @Named("system:delete-credentials")
    @Path("/credentials/store/system/domain/_/credential/{id}/doDelete")
    @Fallback(JenkinsFallbacks.RequestStatusOnError.class)
    @ResponseParser(RequestStatusParser.class)
    @Produces({"application/x-www-form-urlencoded"})
    @Consumes({"text/html"})
    @POST
    RequestStatus deleteCredentials(@PathParam("id") String id);
}
