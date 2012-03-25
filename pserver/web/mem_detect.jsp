<%@ page import="java.util.Map"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="nc.uap.lfw.core.cache.ILfwCache"%>
<%@ page import="nc.uap.lfw.core.cache.LfwCacheMonitor"%>
<%@ page import="nc.uap.lfw.core.cache.LfwCacheManager"%>
<body>
	<table style="width:100%" border="1">
		<thead>
			<tr>
				<td colspan="2" align="center" style="font-weight:bold;color:red">Session Scope</td>
			</tr>
			<tr>
				<td>Object Key</td><td>Object Type</td>
			</tr>
		</thead>
		<tbody>
			<%
				Enumeration em = request.getSession().getAttributeNames();
				while(em.hasMoreElements()){
					String key = (String)em.nextElement();
					Object value = request.getSession().getAttribute(key);
				
			%>
			<tr>
				<td><%= key %></td><td><%= value.getClass().toString() %></td>
			</tr>
			<%
				}
			%>
		</tbody>
	</table>
	<br>
	<br>
	<br>
	<table style="width:100%" border="1">
		<thead>
			<tr>
				<td colspan="2" align="center" style="font-weight:bold;color:red">Session Cache Scope</td>
			</tr>
			<tr>
				<td>Object Key</td><td>Object Type</td>
			</tr>
		</thead>
		<tbody>
			<%
				ILfwCache cache = LfwCacheManager.getSessionCache();
				Iterator it = cache.getKeys().iterator();
				while(it.hasNext()){
					Object obj = it.next();
					Object value = cache.get(obj);
			%>
			<tr>
				<td><%= obj %></td><td><%= value.getClass().toString() %></td>
			</tr>
			<%
					if(value instanceof Map){
						Iterator mit = ((Map)value).keySet().iterator();
						while(mit.hasNext()){
							Object mkey = mit.next();
							Object mvalue = ((Map)value).get(mkey);
			%>
			<tr>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%= mkey %></td><td><%= mvalue.getClass().toString() %></td>
			</tr>
			<%
						}
					}
				}
			%>
		</tbody>
	</table>

	<br>
	<br>
	<br>
	<table style="width:100%" border="1">
		<thead>
			<tr>
				<td colspan="2" align="center" style="font-weight:bold;color:red">Session File Cache Scope</td>
			</tr>
			<tr>
				<td>Object Key</td><td>Object Type</td>
			</tr>
		</thead>
		<tbody>
			<%
				ILfwCache sfcache = LfwCacheManager.getSessionFileCache();
				Iterator sfit = sfcache.getKeys().iterator();
				while(sfit.hasNext()){
					Object obj = sfit.next();
					Object value = sfcache.get(obj);
			%>
			<tr>
				<td><%= obj %></td><td><%= value.getClass().toString() %></td>
			</tr>
			<%
					if(value instanceof Map){
						Iterator mit = ((Map)value).keySet().iterator();
						while(mit.hasNext()){
							Object mkey = mit.next();
							Object mvalue = ((Map)value).get(mkey);
			%>
			<tr>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%= mkey %></td><td><%= mvalue.getClass().toString() %></td>
			</tr>
			<%
						}
					}
				}
			%>
		</tbody>
	</table>
		
	<%
		Map cmap = LfwCacheMonitor.getExitFileCacheMap();
		Iterator cit = cmap.keySet().iterator();
		while(cit.hasNext()){
			Object ckey = cit.next();
			ILfwCache cvalue = (ILfwCache)cmap.get(ckey);
	%>
	<br>
	<br>
	<br>
	<table style="width:100%" border="1">
		<thead>
			<tr>
				<td colspan="2" align="center" style="font-weight:bold;color:red">Public File Cache "<%= ckey%>"</td>
			</tr>
			<tr>
				<td>Object Key</td><td>Object Type</td>
			</tr>
		</thead>
		<tbody>
			<%
				Iterator fit = cvalue.getKeys().iterator();
				while(fit.hasNext()){
					Object obj = fit.next();
					Object value = cvalue.get(obj);
			%>
			<tr>
				<td><%= obj %></td><td><%= value.getClass().toString() %></td>
			</tr>
			<%
					if(value instanceof Map){
						Iterator mit = ((Map)value).keySet().iterator();
						while(mit.hasNext()){
							Object mkey = mit.next();
							Object mvalue = ((Map)value).get(mkey);
			%>
			<tr>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%= mkey %></td><td><%= mvalue.getClass().toString() %></td>
			</tr>
			<%
						}
					}
				}
			%>
		</tbody>
	</table>
	<%
		}
	%>
	

		
	
	<%
		Iterator ecit = LfwCacheMonitor.getExistCacheKeys().iterator();
		while(ecit.hasNext()){
			Object ckey1 = ecit.next();
			Map cvalueMap = (Map)LfwCacheMonitor.getExistCacheMapByRegion((String)ckey1);
			Iterator ccIt = cvalueMap.keySet().iterator();
			while(ccIt.hasNext()){
				Object ckey = ccIt.next();
				ILfwCache cvalue = (ILfwCache)cvalueMap.get(ckey);
	%>
	<br>
	<br>
	<br>
	<table style="width:100%" border="1">
		<thead>
			<tr>
				<td colspan="2" align="center" style="font-weight:bold;color:red">Public Memory Cache "<%= ckey%>"</td>
			</tr>
			<tr>
				<td>Object Key</td><td>Object Type</td>
			</tr>
		</thead>
		<tbody>
			<%
				Iterator fit = cvalue.getKeys().iterator();
				while(fit.hasNext()){
					Object obj = fit.next();
					Object value = cvalue.get(obj);
			%>
			<tr>
				<td><%= obj %></td><td><%= value.getClass().toString() %></td>
			</tr>
			<%
					if(value instanceof Map){
						Iterator mit = ((Map)value).keySet().iterator();
						while(mit.hasNext()){
							Object mkey = mit.next();
							Object mvalue = ((Map)value).get(mkey);
			%>
			<tr>
				<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%= mkey %></td><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%= mvalue.getClass().toString() %></td>
			</tr>
			<%		
						
						}
					}
				}
			}
			%>
		</tbody>
	</table>
	<%
		}
	%>
</body>