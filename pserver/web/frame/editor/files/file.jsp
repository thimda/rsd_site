<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
	<HEAD>
		<TITLE>
			附件上传对话框
		</TITLE>
		<META http-equiv=Content-Type content="text/html; charset=UTF-8">
		<SCRIPT language=JavaScript>
window.abc ="abc";
// 文件来源单选点击事件
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

// 文件上传完成时执行,带入上传文件名
function UploadSaved(sFileName, sFileKey, sFileType)
{	
	// 文件的原始名
	d_upfilename.value = sFileName;
	// 数据库中该记录的主键值
	d_upfilename.key = sFileKey;
	// 文件的type
	d_upfilename.filetype = sFileType;
	ReturnValue();
}

// 本窗口返回值
function ReturnValue()
{	 
	var sFromUrl = "附件: <a href='http://docserverip${ROOT_PATH}/file?cmd=getFile&fileid=" + encodeURIComponent(d_upfilename.key) + "&type=" + d_upfilename.filetype + "' target=_blank>" + d_upfilename.value + "</a>"; 
	
	// 带回链接串显示在编辑器中
	var re = new Array(2);
	re[0] = sFromUrl;
	re[1] = d_upfilename.key;
	window.returnValue = re;
	window.close();
}

// 点确定时执行
function ok(){
	if (d_checkfromurl.checked){
		// 取文件名
		GetFileName(d_fromurl.value, "/");
		// 返回值
		ReturnValue();
	}
	else{
		// 上传文件判断
		if(!d_file.CheckUploadForm()) 
			return false;
		
		// 显示正在上传文件
		divProcessing.style.display="";
		// 上传表单提交
		d_file.uploadForm.submit();
	}
}

/**
 * 仅隐藏对话框
 */
function onCancel()
{
	window.close();
}

// 使所有输入框无效
function DisableItems(){
	d_checkfromfile.disabled=true;
	d_checkfromurl.disabled=true;
	d_fromurl.disabled=true;
	Ok.disabled=true;
}

// 使所有输入框有效
function AbleItems(){
	d_checkfromfile.disabled=false;
	d_checkfromurl.disabled=false;
	d_fromurl.disabled=false;
	Ok.disabled=false;
}

// 取文件名到隐藏的输入框
function GetFileName(url, opt){
	d_filename.value = url.substr(url.lastIndexOf(opt) + 1);
}

</SCRIPT>

</HEAD>
	<BODY bgColor="#DCDCDC" onload="RadioClick('file');">
		<TABLE cellSpacing=0 cellPadding=0 align=center border=0>
			<TBODY>
				<TR>
					<TD>
						<FIELDSET>
							<LEGEND>
								文件来源
							</LEGEND>
							<TABLE cellSpacing=0 cellPadding=0 border=0>
								<TBODY>
									<TR>
										<td height=5></TD>
									</TR>
									<tr>
										<td>
											<INPUT id=d_checkfromfile onclick="RadioClick('file')" type=radio value=1 checked>
											来自本地:
										</td>
									</tr>
									<TR>
										<TD>
											<%
								          		String identifier = request.getParameter("identifier");
												String addr = request.getServerName() + "_" + request.getServerPort();
								          		String url = "upload.jsp?identifier=" + identifier + "&style=file&serverip=" + addr;
								          		
								          	%>
											<iframe style="background-color:menu" id=d_file frameborder=0 src="<%=url%>" width="100%" height="70" scrolling=no></iframe>
										</TD>
									</TR>
									<TR>
										<td height=5></TD>
									</TR>
									<tr>
										<td>
											<INPUT id=d_checkfromurl onclick="RadioClick('url')" type=radio value=1>
											来自网络:
										</TD>
										</td>
									</tr>
									<TR>
										<td>
											<INPUT id=d_fromurl style="WIDTH: 243px" size=30 value=http: //>
										</td>
									</TR>
									<TR>
										<td height=5></TD>
									</TR>
								</TBODY>
							</TABLE>
						</FIELDSET>
					</TD>
				</TR>
				<TR>
					<TD height=5></TD>
				</TR>
				<TR>
					<TD align=right>
						<INPUT id=Ok onclick="ok()" type=submit value="确定">
						&nbsp;&nbsp;
						<INPUT onclick="onCancel();" type=button value="取消">
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
									文件上传中,请等待...
								</FONT>
							</MARQUEE>
						</TD>
					</TR>
				</TBODY>
			</TABLE>
		</DIV>
		<INPUT id=d_filename type=hidden>
		<INPUT id=d_upfilename type=hidden>
	</BODY>
</HTML>
