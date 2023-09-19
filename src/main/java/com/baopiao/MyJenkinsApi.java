package com.baopiao;

import com.cdancy.jenkins.rest.JenkinsApi;
import com.cdancy.jenkins.rest.shaded.org.jclouds.rest.annotations.Delegate;

import java.io.Closeable;

public interface MyJenkinsApi extends Closeable, JenkinsApi {

    @Delegate
   MySystemApi systemApi();

    @Delegate
    MyJobsApi jobsApi();

    @Delegate
    MyPluginManagerApi pluginManagerApi();

    @Delegate
    MyQueueApi queueApi();

    @Delegate
    MyStatisticsApi statisticsApi();

}
