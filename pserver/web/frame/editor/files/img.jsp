<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
	<META http-equiv=Content-Type content="text/html; charset=UTF-8">
	<LINK href="pop.css" type=text/css rel=stylesheet>
	<STYLE type=text/css>
BODY {
	FONT: 9pt "宋体", Verdana, Arial, Helvetica, sans-serif
}
A {
	FONT: 9pt "宋体", Verdana, Arial, Helvetica, sans-serif
}
TABLE {
	FONT: 9pt "宋体", Verdana, Arial, Helvetica, sans-serif
}
DIV {
	FONT: 9pt "宋体", Verdana, Arial, Helvetica, sans-serif
}
SPAN {
	FONT: 9pt "宋体", Verdana, Arial, Helvetica, sans-serif
}
TD {
	FONT: 9pt "宋体", Verdana, Arial, Helvetica, sans-serif
}
TH {
	FONT: 9pt "宋体", Verdana, Arial, Helvetica, sans-serif
}
INPUT {
	FONT: 9pt "宋体", Verdana, Arial, Helvetica, sans-serif
}
SELECT {
	FONT: 9pt "宋体", Verdana, Arial, Helvetica, sans-serif
}
BODY {
	PADDING-RIGHT: 5px; PADDING-LEFT: 5px; PADDING-BOTTOM: 5px; PADDING-TOP: 5px
}
</STYLE>

<SCRIPT language=JavaScript>
var sAction = "INSERT";
var sTitle = "插入";
var oControl;
var oSeletion;
var sRangeType;

//初始化变量
var sFromUrl = "http://";
var sAlt = "";
var sBorder = "0";
var sBorderColor = "#000000";
var sFilter = "";
var sAlign = "";
var sWidth = "";
var sHeight = "";
var sVSpace = "";
var sHSpace = "";
var what = ""
var sCheckFlag = "file";

if (sRangeType == "Control") 
{
	if (oSelection.item(0).tagName == "IMG"){
		sAction = "MODI";
		sTitle = "修改";
		sCheckFlag = "url";
		oControl = oSelection.item(0);
		sFromUrl = oControl.src;
		sAlt = oControl.alt;
		sBorder = oControl.border;
		sBorderColor = oControl.style.borderColor;
		sFilter = oControl.style.filter;
		sAlign = oControl.align;
		sWidth = oControl.width;
		sHeight = oControl.height;
		sVSpace = oControl.vspace;
		sHSpace = oControl.hspace;
	}
}
document.write("<title>插入图片</title>");

// 初始值
function InitDocument()
{
	d_fromurl.value = sFromUrl;
	d_border.value = sBorder;
	d_bordercolor.value = sBorderColor;
	d_width.value = sWidth;
	d_height.value = sHeight;
	d_vspace.value = sVSpace;
	d_hspace.value = sHSpace;
}

//文件来源单选点击事件
function RadioClick(what)
{
	if (what=="url")
	{
		d_checkfromfile.checked=false;
		d_fromurl.disabled=false;
		d_checkfromurl.checked=true;
		d_file.uploadForm.uploadfile.disabled=true;
		//上传按钮的禁用
		d_file.uploadForm.ok.disabled = true;
	}
	else
	{
		d_checkfromurl.checked=false;
		d_file.uploadForm.uploadfile.disabled=false;
		d_checkfromfile.checked=true;
		d_fromurl.disabled=true;
		//上传按钮的激活
		d_file.uploadForm.ok.disabled = false;
	}
}

// 初始化数组
function makearray(n) 
{
	this.length = n;
	for(var i = 1; i <= n; i++)
	this[i] = 0;
	return this;
}

// 取文件名到隐藏的输入框
function GetFileName(url, opt)
{
	d_filename.value = url.substr(url.lastIndexOf(opt) + 1);
}

// 文件上传完成时执行,带入上传文件名
function UploadSaved(sFileName, sFileKey, sFileType)
{
	UploadSaved.value = sFileName;
	UploadSaved.key = sFileKey;
	// 文件的type
	UploadSaved.filetype = sFileType;
	ReturnValue();
}

// 本窗口返回值
function ReturnValue()
{
	//来自网络
	sFromUrl = d_fromurl.value;
	//来自本地
	if(sFromUrl.length <= 7)
	{ 
		sFromUrl = "${ROOT_PATH}/file?cmd=getFile&fileid=" + UploadSaved.key + "&type=" + UploadSaved.filetype;
	}	
	sBorder = d_border.value;
	sBorderColor = d_bordercolor.value;
	sFilter = d_filter.value;
	sAlign = d_align.value;
	sWidth = d_width.value;
	sHeight = d_height.value;
	sVSpace = d_vspace.value;
	sHSpace = d_hspace.value;

	if (sAction == "MODI") 
	{
		oControl.src = sFromUrl;
		oControl.alt = sAlt;
		oControl.border = sBorder;
		oControl.style.borderColor = sBorderColor;
		oControl.style.filter = sFilter;
		oControl.align = sAlign;
		oControl.width = sWidth;
		oControl.height = sHeight;
		oControl.vspace = sVSpace;
		oControl.hspace = sHSpace;
	}
	else
	{
		var sHTML = '';
		if (sFilter != "")
		{
			sHTML = sHTML + 'filter:' + sFilter + ';';
		}
		if (sBorderColor != "")
		{
			sHTML = sHTML + 'border-color:' + sBorderColor + ';';
		}
		if (sHTML != "")
		{
			sHTML = ' style="' + sHTML + '"';
		}
		sHTML = '<img src="' + sFromUrl + '"' + sHTML;
		
		if (sBorder != "")
		{
			sHTML = sHTML + ' border="' + sBorder + '"';
		}
		if (sAlt != "")
		{
			sHTML=sHTML+' alt="'+sAlt+'"';
		}
		if (sAlign!=""){
			sHTML=sHTML+' align="'+sAlign+'"';
		}
		if (sWidth!=""){
			sHTML=sHTML+' width="'+sWidth+'"';
		}
		if (sHeight!=""){
			sHTML=sHTML+' height="'+sHeight+'"';
		}
		if (sVSpace!=""){
			sHTML=sHTML+' vspace="'+sVSpace+'"';
		}
		if (sHSpace!=""){
			sHTML=sHTML+' hspace="'+sHSpace+'"';
		}
		sHTML=sHTML+'>';
	}
	re = new makearray(2);
	re[1] = sHTML;
	re[2] = UploadSaved.value;
	window.returnValue = re;
	window.close();
}

// 点确定时执行
function ok()
{
	// 数字型输入的有效性
	d_border.value = ToInt(d_border.value);
	d_width.value = ToInt(d_width.value);
	d_height.value = ToInt(d_height.value);
	d_vspace.value = ToInt(d_vspace.value);
	d_hspace.value = ToInt(d_hspace.value);
	
	// 显示正在上传文件
	divProcessing.style.display="";
	
	// 返回值
	ReturnValue();
}

// 使所有输入框无效
function DisableItems()
{
	d_checkfromfile.disabled=true;
	d_checkfromurl.disabled=true;
	d_fromurl.disabled=true;
	d_border.disabled=true;
	d_bordercolor.disabled=true;
	d_filter.disabled=true;
	d_align.disabled=true;
	d_width.disabled=true;
	d_height.disabled=true;
	d_vspace.disabled=true;
	d_hspace.disabled=true;
	Ok.disabled=true;
}

// 使所有输入框有效
function AbleItems()
{
	d_checkfromfile.disabled=false;
	d_checkfromurl.disabled=false;
	d_fromurl.disabled=false;
	d_border.disabled=false;
	d_bordercolor.disabled=false;
	d_filter.disabled=false;
	d_align.disabled=false;
	d_width.disabled=false;
	d_height.disabled=false;
	d_vspace.disabled=false;
	d_hspace.disabled=false;
	Ok.disabled=false;
}

// 转为数字型，并无前导0，不能转则返回""
function ToInt(str)
{
	str=BaseTrim(str);
	if (str!="")
	{
		var sTemp = parseFloat(str);
		if (isNaN(sTemp))
		{
			str="";
		}
		else
		{
			str=sTemp;
		}
	}
	return str;
}

// 去空格，left,right,all可选
function BaseTrim(str)
{
	  lIdx=0;rIdx=str.length;
	  if (BaseTrim.arguments.length==2)
	    act=BaseTrim.arguments[1].toLowerCase()
	  else
	    act="all"
      for(var i=0;i<str.length;i++){
	  	thelStr=str.substring(lIdx,lIdx+1)
		therStr=str.substring(rIdx,rIdx-1)
        if ((act=="all" || act=="left") && thelStr==" "){
			lIdx++
        }
        if ((act=="all" || act=="right") && therStr==" "){
			rIdx--
        }
      }
	  str=str.slice(lIdx,rIdx)
      return str
}

function oblog_foreColor()
{
	var oblog_bIsIE5=document.all;
	if (oblog_bIsIE5)
	{
		var arr = showModalDialog("/lfw/frame/editor/files/selcolor.html", "", "dialogWidth:18.5em; dialogHeight:17.5em; status:0; help:0");
		if (arr != null) d_bordercolor.value=arr;
	}
}

// 只允许输入数字
function IsDigit()
{
  return ((event.keyCode >= 48) && (event.keyCode <= 57));
}
</SCRIPT>

<META content="MSHTML 6.00.3790.2666" name=GENERATOR>
</HEAD>

<BODY bgColor="#DCDCDC" onload=InitDocument()>
<TABLE cellSpacing=0 cellPadding=0 align=center border=0>
  <TBODY>
	  <TR>
	    <TD>
	      <FIELDSET>
		      <LEGEND>
		      	图片来源
		      </LEGEND>
	      <TABLE cellSpacing=0 cellPadding=0 border=0>
	        <TBODY>
		        <TR>
		          <TD colSpan=9 height=5></TD>
		        </TR>
		        <TR>
		          <TD width=7></TD>
		          <TD align=right width=80>
		          	<INPUT id=d_checkfromurl onclick="RadioClick('url')" type=radio value=1>
		          	来自网络:
		          </TD>
		          <TD width=5></TD>
		          <TD colSpan=5>
		          	  <INPUT id=d_fromurl style="WIDTH: 243px" size=30>
		          </TD>
		          <TD width=7></TD>
		        </TR>
		        <TR>
		          <TD width=7></TD>
		          <TD align=right width=80>
		          	<INPUT id=d_checkfromfile onclick="RadioClick('file')" type=radio value=1 checked>
		          	来自本地:
		          </TD>
		          <TD width=5></TD>
		          <TD colSpan=5>
		          	<%
		          		String identifier = request.getParameter("identifier");
		          		String url = null;
		          		if(identifier.startsWith("topic_"))
		          			url = "upload.jsp?identifier=" + identifier + "&style=img&source=forum";
		          		else	
		          			url = "upload.jsp?identifier=" + identifier + "&style=img&source=news";
		          	%>
		          	  <iframe style="background-color:#DCDCDC" id=d_file frameborder=0 src="<%=url%>" width="100%" height="70" scrolling=no></iframe>
		          <TD width=7></TD>
		        </TR>
		        <TR>
		          <TD colSpan=9 height=5>
		          </TD>
		        </TR>
	        </TBODY>
	       </TABLE>
	       </FIELDSET> 
	    </TD>
	  </TR>
	  
	  <TR>
	    <TD height=5>
	    </TD>
	  </TR>
	  
	  <TR>
	    <TD>
	      <FIELDSET><LEGEND>显示效果</LEGEND>
	      <TABLE cellSpacing=0 cellPadding=0 border=0>
	        <TBODY>
	        <TR>
	          <TD colSpan=9 height=5></TD>
	        </TR>
	        <TR>
	          <TD colSpan=9 height=5></TD>
	        </TR>
	        <TR>
	          <TD width=7></TD>
	          <TD noWrap>边框粗细:</TD>
	          <TD width=5></TD>
	          <TD>
	          	<INPUT onkeypress=event.returnValue=IsDigit(); id=d_border size=10>
	          </TD>
	          <TD width=40></TD>
	          <TD noWrap>边框颜色:</TD>
	          <TD width=5></TD>
	          <TD>
	            <TABLE cellSpacing=0 cellPadding=0 border=0>
	              <TBODY>
		              <TR>
		                <TD>
		                	<INPUT id=d_bordercolor size=7>
		                </TD>
		                <TD language=javascript onclick=oblog_foreColor();>
		                	<IMG src="/lfw/frame/editor/files/emot_files/color.gif">
		                </TD>
		              </TR>
	              </TBODY>
	            </TABLE>
	          </TD>
	          <TD width=7></TD></TR>
	        <TR>
	          <TD colSpan=9 height=5></TD></TR>
	        <TR>
	          <TD width=7></TD>
	          <TD>特殊效果:</TD>
	          <TD width=5></TD>
	          <TD><SELECT id=d_filter style="WIDTH: 72px" size=1> <OPTION 
	              value="" selected>无</OPTION> <OPTION 
	              value=Alpha(Opacity=50)>半透明</OPTION> <OPTION 
	              value="Alpha(Opacity=0, FinishOpacity=100, Style=1, StartX=0, StartY=0, FinishX=100, FinishY=140)">线型透明</OPTION> 
	              <OPTION 
	              value="Alpha(Opacity=10, FinishOpacity=100, Style=2, StartX=30, StartY=30, FinishX=200, FinishY=200)">放射透明</OPTION> 
	              <OPTION 
	              value=blur(add=1,direction=14,strength=15)>模糊效果</OPTION><OPTION 
	              value=blur(add=true,direction=45,strength=30)>风动模糊</OPTION> 
	              <OPTION 
	              value="Wave(Add=0, Freq=60, LightStrength=1, Phase=0, Strength=3)">正弦波纹</OPTION> 
	              <OPTION value=gray>黑白照片</OPTION><OPTION 
	              value=Chroma(Color=#FFFFFF)>白色透明</OPTION> <OPTION 
	              value="DropShadow(Color=#999999, OffX=7, OffY=4, Positive=1)">投射阴影</OPTION> 
	              <OPTION value="Shadow(Color=#999999, Direction=45)">阴影</OPTION> 
	              <OPTION value="Glow(Color=#ff9900, Strength=5)">发光</OPTION> 
	              <OPTION value=flipv>垂直翻转</OPTION> <OPTION 
	              value=fliph>左右翻转</OPTION> <OPTION value=grays>降低彩色</OPTION> 
	              <OPTION value=xray>X光照片</OPTION> <OPTION 
	            value=invert>底片</OPTION></SELECT> </TD>
	          <TD width=40></TD>
	          <TD>对齐方式:</TD>
	          <TD width=5></TD>
	          <TD><SELECT id=d_align style="WIDTH: 72px" size=1> <OPTION 
	              value="" selected>默认</OPTION> <OPTION value=left>居左</OPTION> 
	              <OPTION value=right>居右</OPTION> <OPTION value=top>顶部</OPTION> 
	              <OPTION value=middle>中部</OPTION> <OPTION value=bottom>底部</OPTION> 
	              <OPTION value=absmiddle>绝对居中</OPTION> <OPTION 
	              value=absbottom>绝对底部</OPTION> <OPTION value=baseline>基线</OPTION> 
	              <OPTION value=texttop>文本顶部</OPTION></SELECT> </TD>
	          <TD width=7></TD></TR>
	        <TR>
	          <TD colSpan=9 height=5></TD></TR>
	        <TR>
	          <TD width=7></TD>
	          <TD>图片宽度:</TD>
	          <TD width=5></TD>
	          <TD><INPUT onkeypress=event.returnValue=IsDigit(); id=d_width 
	            maxLength=4 size=10></TD>
	          <TD width=40></TD>
	          <TD>图片高度:</TD>
	          <TD width=5></TD>
	          <TD><INPUT onkeypress=event.returnValue=IsDigit(); id=d_height 
	            maxLength=4 size=10></TD>
	          <TD width=7></TD></TR>
	        <TR>
	          <TD colSpan=9 height=5></TD></TR>
	        <TR>
	          <TD width=7></TD>
	          <TD>上下间距:</TD>
	          <TD width=5></TD>
	          <TD><INPUT onkeypress=event.returnValue=IsDigit(); id=d_vspace 
	            maxLength=2 size=10></TD>
	          <TD width=40></TD>
	          <TD>左右间距:</TD>
	          <TD width=5></TD>
	          <TD><INPUT onkeypress=event.returnValue=IsDigit(); id=d_hspace 
	            maxLength=2 size=10></TD>
	          <TD width=7></TD></TR>
	        <TR>
	          <TD colSpan=9 height=5></TD></TR></TBODY></TABLE></FIELDSET> </TD></TR>
	  <TR>
	    <TD height=5></TD>
	  </TR>
	  <TR>
	    <TD align=right>
	    	<INPUT id=d_upfilename type=hidden> 
		    <INPUT id=Ok onclick="ok();" type=submit value=确定> &nbsp;&nbsp; 
		    <INPUT onclick="window.close();" type=button value=取消>
	  	</TD>
	  </TR>
	  </TBODY>
	  </TABLE>
  	  <DIV id=divProcessing style="DISPLAY: none; LEFT: 70px; WIDTH: 200px; POSITION: absolute; TOP: 30px; HEIGHT: 30px">
			<TABLE height="100%" cellSpacing=1 cellPadding=0 width="100%" bgColor=#000000 border=0>
				<TBODY>
					<TR>
						<TD bgColor=#3a6ea5>
							<MARQUEE scrollAmount=5 behavior=alternate align="middle">
								<FONT color=#ffffff>
									...文件上传中...请等待...
								</FONT>
							</MARQUEE>
						</TD>
					</TR>
				</TBODY>
			</TABLE>
	 </DIV>
	 <INPUT id=d_upfilename type=hidden>
	 <INPUT id=d_upfilename type=hidden>
  	</BODY></HTML>
