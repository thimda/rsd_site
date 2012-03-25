<%@ page contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
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
<%@ page import="org.apache.lucene.analysis.Analyzer,
                 org.apache.lucene.util.AttributeSource,
                 org.apache.lucene.util.Attribute,
                 org.apache.lucene.analysis.TokenStream,
                 org.apache.lucene.index.Payload,
                 org.apache.lucene.analysis.CharReader,
                 org.apache.lucene.analysis.CharStream,
                 org.apache.lucene.analysis.tokenattributes.*,
                 org.apache.lucene.util.AttributeReflector,                 
                 org.apache.solr.analysis.CharFilterFactory,
                 org.apache.solr.analysis.TokenFilterFactory,
                 org.apache.solr.analysis.TokenizerChain,
                 org.apache.solr.analysis.TokenizerFactory,
                 org.apache.solr.schema.FieldType,
                 org.apache.solr.schema.SchemaField,
                 org.apache.solr.common.util.XML,
                 javax.servlet.jsp.JspWriter,
                 java.util.*,
                 java.io.IOException,
                 org.apache.solr.util.AnalysisJspUtil
                "%>
<%@ page import="java.io.Reader"%>
<%@ page import="java.io.StringReader"%>
<%@ page import="java.util.*"%>
<%@ page import="java.math.BigInteger" %>

<%-- $Id: analysis.jsp 1095519 2011-04-20 21:30:47Z uschindler $ --%>
<%-- $Source: /cvs/main/searching/org.apache.solrolarServer/resources/admin/analysis.jsp,v $ --%>
<%-- $Name:  $ --%>

<%@include file="header.jsp" %>

<%
  // is name a field name or a type name?
  String nt = request.getParameter("nt");
  if (nt==null || nt.length()==0) nt="name"; // assume field name
  nt = nt.toLowerCase(Locale.ENGLISH).trim();
  String name = request.getParameter("name");
  if (name==null || name.length()==0) name="";
  String val = request.getParameter("val");
  if (val==null || val.length()==0) val="";
  String qval = request.getParameter("qval");
  if (qval==null || qval.length()==0) qval="";
  String verboseS = request.getParameter("verbose");
  boolean verbose = verboseS!=null && verboseS.equalsIgnoreCase("on");
  String qverboseS = request.getParameter("qverbose");
  boolean qverbose = qverboseS!=null && qverboseS.equalsIgnoreCase("on");
  String highlightS = request.getParameter("highlight");
  boolean highlight = highlightS!=null && highlightS.equalsIgnoreCase("on");
%>

<br clear="all">

<h2>Field Analysis</h2>

<form method="POST" action="analysis.jsp" accept-charset="UTF-8">
<table>
<tr>
  <td>
  <strong>Field
          <select name="nt">
    <option <%= nt.equals("name") ? "selected=\"selected\"" : "" %> >name</option>
    <option <%= nt.equals("type") ? "selected=\"selected\"" : "" %>>type</option>
          </select></strong>
  </td>
  <td>
  <input class="std" name="name" type="text" value="<% XML.escapeCharData(name, out); %>">
  </td>
</tr>
<tr>
  <td>
  <strong>Field value (Index)</strong>
  <br/>
  verbose output
  <input name="verbose" type="checkbox"
     <%= verbose ? "checked=\"true\"" : "" %> >
    <br/>
  highlight matches
  <input name="highlight" type="checkbox"
     <%= highlight ? "checked=\"true\"" : "" %> >
  </td>
  <td>
  <textarea class="std" rows="8" cols="70" name="val"><% XML.escapeCharData(val,out); %></textarea>
  </td>
</tr>
<tr>
  <td>
  <strong>Field value (Query)</strong>
  <br/>
  verbose output
  <input name="qverbose" type="checkbox"
     <%= qverbose ? "checked=\"true\"" : "" %> >
  </td>
  <td>
  <textarea class="std" rows="1" cols="70" name="qval"><% XML.escapeCharData(qval,out); %></textarea>
  </td>
</tr>
<tr>

  <td>
  </td>

  <td>
  <input class="stdbutton" type="submit" value="analyze">
  </td>

</tr>
</table>
</form>


<%
  SchemaField field=null;
  if (name!="") {
    if (nt.equals("name")) {
      try {
        field = schema.getField(name);
      } catch (Exception e) {
        out.print("<strong>Unknown Field: ");
        XML.escapeCharData(name, out);
        out.println("</strong>");
      }
    } else {
       FieldType t = (FieldType)schema.getFieldTypes().get(name);
       if (null == t) {
        out.print("<strong>Unknown Field Type: ");
        XML.escapeCharData(name, out);
        out.println("</strong>");
       } else {
         field = new SchemaField("fakefieldoftype:"+name, t);
       }
    }
  }

  if (field!=null) {
    //HashSet<String> matches = null;
    ArrayList matches = null;
    if (qval!="" && highlight) {
      Reader reader = new StringReader(qval);
      Analyzer analyzer =  field.getType().getQueryAnalyzer();
      TokenStream tstream = analyzer.reusableTokenStream(field.getName(),reader);
      CharTermAttribute termAtt = (CharTermAttribute)tstream.addAttribute(CharTermAttribute.class);
      tstream.reset();
      //matches = new HashSet<String>();
      matches = new ArrayList();
      while (tstream.incrementToken()) {
        matches.add(termAtt.toString());
      }
    }

    if (val!="") {
      out.println("<h3>Index Analyzer</h3>");
      AnalysisJspUtil.doAnalyzer(out, field, val, false, verbose, matches);
    }
    if (qval!="") {
      out.println("<h3>Query Analyzer</h3>");
      AnalysisJspUtil.doAnalyzer(out, field, qval, true, qverbose, null);
    }
  }

%>


</body>
</html>


