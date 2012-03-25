<%@ page contentType="text/xml; charset=utf-8" pageEncoding="UTF-8" language="java" %>
<%--
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
--%>
<%@ page import="org.apache.solr.core.SolrCore,
                 java.lang.management.ManagementFactory,
                 java.lang.management.ThreadMXBean,
                 java.lang.management.ThreadInfo,
                 java.io.IOException,
                 org.apache.solr.common.util.XML,
                 org.apache.solr.util.AnalysisJspUtil"%>
<%@include file="_info.jsp" %>


<?xml-stylesheet type="text/xsl" href="threaddump.xsl"?>
<%!
  static ThreadMXBean tmbean = ManagementFactory.getThreadMXBean();
%>
<solr>
  <core><%= collectionName %></core>
  <system>
  <jvm>
    <version><%=System.getProperty("java.vm.version")%></version>
    <name><%=System.getProperty("java.vm.name")%></name>
  </jvm>
  <threadCount>
    <current><%=tmbean.getThreadCount()%></current>
    <peak><%=tmbean.getPeakThreadCount()%></peak>
    <daemon><%=tmbean.getDaemonThreadCount()%></daemon>
  </threadCount>
<%
  long[] tids;
  ThreadInfo[] tinfos;
  tids = tmbean.findMonitorDeadlockedThreads();
  if (tids != null) {
      out.println("  <deadlocks>");
      tinfos = tmbean.getThreadInfo(tids, Integer.MAX_VALUE);
      for(int i=0;i<tinfos.length;i++){
          AnalysisJspUtil.printThreadInfo(tinfos[i], out);
      }
      out.println("  </deadlocks>");
  }
%>
<%
  tids = tmbean.getAllThreadIds();
  tinfos = tmbean.getThreadInfo(tids, Integer.MAX_VALUE);
  out.println("  <threadDump>");
  if(null != tinfos)
  {
	  for (int i=0;i< tinfos.length;i++) {
	     AnalysisJspUtil.printThreadInfo(tinfos[i], out);
	  }
  }
  out.println("  </threadDump>");
%>
  </system>
</solr>

<%!
  
%>
