package com.baopiao;

import com.cdancy.jenkins.rest.domain.common.RequestStatus;
import com.cdancy.jenkins.rest.fallbacks.JenkinsFallbacks;
import com.cdancy.jenkins.rest.features.JobsApi;
import com.cdancy.jenkins.rest.parsers.OptionalFolderPathParser;
import com.cdancy.jenkins.rest.parsers.RequestStatusParser;
import com.cdancy.jenkins.rest.shaded.javax.inject.Named;
import com.cdancy.jenkins.rest.shaded.javax.ws.rs.*;
import com.cdancy.jenkins.rest.shaded.javax.ws.rs.core.MediaType;
import com.cdancy.jenkins.rest.shaded.org.jclouds.Fallbacks;
import com.cdancy.jenkins.rest.shaded.org.jclouds.rest.annotations.*;
import com.cdancy.jenkins.rest.shaded.org.jclouds.javax.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

@RequestFilters(MyJenkinsAuthenticationFilter.class)
@Path("/")
public interface MyJobsApi extends JobsApi {

    static String CONFIG_XML = "<project>\n" +
            "<actions/>\n" +
            "<description/>\n" +
            "<keepDependencies>false</keepDependencies>\n" +
            "<properties>\n" +
            "<hudson.model.ParametersDefinitionProperty>\n" +
            "<parameterDefinitions>\n" +
            "<hudson.model.StringParameterDefinition>\n" +
            "<name>env</name>\n" +
            "<defaultValue>dev</defaultValue>\n" +
            "<trim>false</trim>\n" +
            "</hudson.model.StringParameterDefinition>\n" +
            "<hudson.model.StringParameterDefinition>\n" +
            "<name>uid</name>\n" +
            "<defaultValue>1</defaultValue>\n" +
            "<trim>false</trim>\n" +
            "</hudson.model.StringParameterDefinition>\n" +
            "</parameterDefinitions>\n" +
            "</hudson.model.ParametersDefinitionProperty>\n" +
            "<jenkins.model.BuildDiscarderProperty>\n" +
            "<strategy class=\"hudson.tasks.LogRotator\">\n" +
            "<daysToKeep>7</daysToKeep>\n" +
            "<numToKeep>10</numToKeep>\n" +
            "<artifactDaysToKeep>-1</artifactDaysToKeep>\n" +
            "<artifactNumToKeep>-1</artifactNumToKeep>\n" +
            "</strategy>\n" +
            "</jenkins.model.BuildDiscarderProperty>\n" +
            "</properties>\n" +
            "<scm class=\"hudson.plugins.git.GitSCM\" plugin=\"git@5.1.0\">\n" +
            "<configVersion>2</configVersion>\n" +
            "<userRemoteConfigs>\n" +
            "<hudson.plugins.git.UserRemoteConfig>\n" +
            "<url>{gitUrl}</url>\n" +
            "<credentialsId>{credentialsId}</credentialsId>\n" +
            "</hudson.plugins.git.UserRemoteConfig>\n" +
            "</userRemoteConfigs>\n" +
            "<branches>\n" +
            "<hudson.plugins.git.BranchSpec>\n" +
            "<name>*/${env}</name>\n" +
            "</hudson.plugins.git.BranchSpec>\n" +
            "</branches>\n" +
            "<doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>\n" +
            "<submoduleCfg class=\"empty-list\"/>\n" +
            "<extensions/>\n" +
            "</scm>\n" +
            "<canRoam>true</canRoam>\n" +
            "<disabled>false</disabled>\n" +
            "<blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>\n" +
            "<blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>\n" +
            "<triggers/>\n" +
            "<concurrentBuild>false</concurrentBuild>\n" +
            "<builders>\n" +
            "<hudson.tasks.Maven>\n" +
            "<targets>{taskMavenCommand}</targets>\n" +
            "<mavenName>maven1</mavenName>\n" +
            "<usePrivateRepository>false</usePrivateRepository>\n" +
            "<settings class=\"jenkins.mvn.DefaultSettingsProvider\"/>\n" +
            "<globalSettings class=\"jenkins.mvn.DefaultGlobalSettingsProvider\"/>\n" +
            "<injectBuildVariables>false</injectBuildVariables>\n" +
            "</hudson.tasks.Maven>\n" +
            "<hudson.tasks.Shell>\n" +
            "<command>mv ${WORKSPACE}/{jarPath} ${WORKSPACE}/{jarPath}.${uid}</command>\n" +
            "<configuredLocalRules/>\n" +
            "</hudson.tasks.Shell>\n" +
            "</builders>\n" +
            "<publishers/>\n" +
            "<buildWrappers/>\n" +
            "</project>";

    @Named("jobs:testGitUrl")
    @Path("{optionalFolderPath}job/test/descriptorByName/hudson.plugins.git.UserRemoteConfig/checkUrl")
    @Fallback(JenkinsFallbacks.RequestStatusOnError.class)
    @Produces(MediaType.APPLICATION_FORM_URLENCODED)
    @Consumes(MediaType.TEXT_HTML)
    @Payload("value={url}&credentialsId={credentialsId}")
    @ResponseParser(RequestStatusParser.class)
    @POST
    RequestStatus testGitUrl(@Nullable @PathParam("optionalFolderPath")
                             @ParamParser(OptionalFolderPathParser.class) String optionalFolderPath,
                              @PayloadParam("url") String url,
                              @PayloadParam("credentialsId") String credentialsId);


    @Named("my jobs:create")
    @Path("{optionalFolderPath}createItem")
    @Fallback(JenkinsFallbacks.RequestStatusOnError.class)
    @ResponseParser(RequestStatusParser.class)
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.WILDCARD)
    @Payload(CONFIG_XML)
    @POST
    RequestStatus myCreate(@Nullable @PathParam("optionalFolderPath") @ParamParser(OptionalFolderPathParser.class) String optionalFolderPath,
                           @QueryParam("name") String jobName,
                           @PayloadParam(value = "credentialsId") String credentialsId,
                           @PayloadParam(value = "gitUrl") String gitUrl,
                           @PayloadParam(value = "taskMavenCommand") String taskMavenCommand,
                           @PayloadParam(value = "jarPath") String jarPath);

    @Named("jobs:update-config")
    @Path("{optionalFolderPath}job/{name}/config.xml")
    @Fallback(Fallbacks.FalseOnNotFoundOr404.class)
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.TEXT_HTML)
    @Payload(CONFIG_XML)
    @POST
    boolean myUpdateConfig(@Nullable @PathParam("optionalFolderPath") @ParamParser(OptionalFolderPathParser.class) String optionalFolderPath,
                           @PathParam("name") String jobName,
                           @PayloadParam(value = "credentialsId") String credentialsId,
                           @PayloadParam(value = "gitUrl") String gitUrl,
                           @PayloadParam(value = "taskMavenCommand") String taskMavenCommand,
                           @PayloadParam(value = "jarPath") String jarPath);

}
