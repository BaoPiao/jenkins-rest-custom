# jenkins-rest-custom

本项目是基于[jenkins-rest](https://github.com/cdancy/jenkins-rest)修改而来，主要增加了git凭据相关操作等等

## demo

```java 
MyJenkinsClient client = MyJenkinsClient.builder()
.endPoint("http://127.0.0.1:8080") // Optional. Defaults to http://127.0.0.1:8080
.credentials("admin:password") // Optional.
.build();

SystemInfo systemInfo = client.api().systemApi().systemInfo();
assertTrue(systemInfo.jenkinsVersion().equals("1.642.4"));
// 增加git全局凭据
RequestStatus requestStatus=client.api().systemApi().addCredentials("code","root","1234555");

```
