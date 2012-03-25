<#--
导航宏
@param pages 用户有权限的page集合
-->
<#macro menu pages currPage>
<style>

.head {
	height:28px;
	background:url(/portal/images/topbg1.gif);
	padding:0;
	margin:0;
	font-size:12px;
	border:0;
}
/**导航**/
.head .nav {
	margin:0 0 0 5px;
	padding:0;
}
.head .nav ul {
	margin:0px;
	list-style:none;
	padding:0;
}
.head .nav ul li {
	float:left;
	padding:7px 10px 5px 10px;
}
.head .nav ul li a:link, .head .nav ul li a:visited {
	color:#1d6397;
	text-decoration:none;
}

/**选中状态**/
.head .nav ul .active {
	background:url(/portal/images/topbg.gif);
}
.head .nav ul .active  a:hover { text-decoration:none;}
.head .nav ul .active a:link, .head .nav ul .active  a:visited {
	font-weight:bold;
	color:#FFF;
}
/**分隔符**/
.head .nav ul .navSplit {
	width:1px;
	height:12px;
	background:#a0d6e8;
	padding:0;
	margin-top:7px;
}
.none {
 	display:none;
 }
/**系统工具**/
.head .ctrl dl {
	float:right;
	margin:0;
	padding:0;
    margin-right:2px;
}
.head .ctrl dl dt {
	float:left;
	padding:7px 10px 6px 10px;
}
/**分隔符**/
.head .ctrl dl .navSplit {
	width:1px;
	height:12px;
	background:#c5c5c5;
	padding:0;
	margin-top:7px;
}
.head .ctrl dl dt a:link, .head .ctrl dl dt a:visited {
	color:#687883;
	text-decoration:none;
}
.head .nav ul li a:hover { border-bottom:#1d6397; border-bottom-width:1px; border-bottom-style:solid;  }
.head .ctrl dl dt a:hover {
	color:#687883;
	text-decoration:underline;
}
.head .ctrl dl .help {
	height:12px;
	padding:0;
	margin-top:7px;
}
.head .ctrl dl .help img {
	border:0;
}
.search {
	border:none;
	margin:0 auto;
	height:56px;
	width:100%;
	border:0;
	border-top: #5cb4d4 1px solid;
}
.search .logo{
	width:316px; height:56px; float:left; background:url(${href("/images/topbj-left.gif")}) repeat-x; overflow:hidden; display:inline; margin:0; padding:0;_margin: 0 -3px 0 0 ;
}
.search .logo a{
	_display:block;_width:191px;_height:56px;_FILTER: progid:DXImageTransform.Microsoft.AlphaImageLoader(src=${href("/images/logo.png")});
}
.search .logo a img{
	border:0;
	_visibility:hidden;
}
.search .searchbox {
	margin: 0 0 0 316px
	
	padding:0; 
	height:56px;
	background:url(/portal/images/topbj-middle.gif) repeat-x;
}
.search .searchbox dl {
	margin:0;
	padding:0;
	float:right;
	width:420px; background:url(/portal/images/topbj-right.gif) no-repeat;
}
.search .searchbox .welcome {
	height:56px;
	float:left;
	width:120px;
}
.search .searchbox .searchform {
	height:56px;
	float:right;
	display:inline;
	background:url(/portal/images/topbj-right.gif) right no-repeat;
	width:300px;
}
.search .searchbox .searchform .form {
	font-size:12px;
	padding:0;
	height:44px;
	width:270px;
	background:url(/portal/images/search-center.gif);
	float:left;
	margin:0px 12px auto 18px;
	_display:inline;
}
.search .searchbox .searchform .form .leftTL {
	width:4px;
	height:44px;
	background:url(/portal/images/search-left.gif);
	float:left
}
.search .searchbox .searchform .form .rightTL {
	width:4px;
	height:44px;
	background:url(/portal/images/search-right.gif);
	float:right;
	margin:0;
}
.search .searchbox .welcome span {font-size:12px;	height:24px;	margin-top:16px;	float:right;}

.search .searchbox .searchform .form .centerTL{ margin-left:0 4px;}
.search .searchbox .searchform .form .centerTL form{float:left;	margin-top:3px;}
.search .searchbox .searchform .form .centerTL form input{ width:110px;  margin-left:4px; margin-right:4px; border:0; height:20px; }
.search .searchbox .searchform .form .centerTL form select{ width:86px; border:none; height:21px; border:0; }
.search .searchbox .searchform .form .centerTL form .searchBtn{width:48px; height:24px;background:url(/portal/images/search_btn.png);}
.search .searchbox .searchform .form .centerTL form .searchBtn:hover{background:url(/portal/images/search_btn_over.png);}
.search .searchbox .searchform .form .centerTL form .searchBtn:active{background:url(/portal/images/search_btn_click.png);}

</style>

<div   tp="nagigation">
<div class="head">
  <div class="nav">
    <ul>
	<#include "pagemenu.ftl">
    </ul>
  </div>
 <div class="ctrl">
    <dl>
      <dt><a onclick="window.open ('${env().web}/pt/manager/setting', 'newwindow', 'height=250, width=420, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');">设置</a></dt>
       	<dt class="navSplit"></dt>
        <dt><a onclick="window.open('${env().web}/core/uimeta.um?pageId=watchlog', 'newwindow', 'height=480, width=800, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no')" >下载日志</a></dt>
      <dt class="navSplit"></dt>
       <dt><a href="${env().web}/pt/home/logout">注销</a></dt>
      <!--
      <dt class="help"><img src="${href("/images/help.gif")}" /></dt>
     
			<dt><a href="javascript:void(0)">帮助</a></dt>
      -->
    </dl>
  </div>
</div>
<div class="search">
  <div class="logo"><a href="javascript:void(0)"><img src = "${href("/images/logo.png")}"/></a></div>
  <div class="searchbox">
  	<#if userinfo.usertype = 0>
	    <dl>
	      <dt class="welcome"><span>你好,${userinfo.username}</span></dt>
	      <dt class="searchform">
	        <div class="form">
	          <div class="leftTL"></div>
	          <div class="rightTL"></div>
	          <div class="centerTL">
		        <form action="pt/search/doSearch" method="post" target="_blank"> 
			          <table cellpadding="0" cellspacing="0" border="0">
			          <tr>
			          <td><input type="text" name="queryString" /></td>
			          <td>
			          	<select name="queryPosition">
		          	 	   <option value="-1">全部</option>
		          	 	   <option value="0">标题</option>
		          	 	   <option value="1">内容</option>
		           		 </select>
	           		   </td>
			          <td><input type="submit" class="searchBtn" value="" /></td>
			          </tr>
			          </table>
	          	 </form>
	          </div>
	        </div>
	      </dt>
	    </dl>
    </#if>
  </div>
</div>
</div>

</#macro>
