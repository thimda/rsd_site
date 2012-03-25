<%@ page contentType="text/html; charset=utf-8" language="java" import="java.sql.*" errorPage="" %>
<html>
<head>
<title>修改密码</title>
<style>
*{ padding:0px; margin:0px;  }
</style>

</head>
<body>
<form action="/portal/pt/setting/passwd" method="get" target="sbtFrame">
    <table width="280" border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td height="30" colspan="2"><div align="center"><strong>更改密码</strong></div></td>
      </tr>
      <tr>
        <td width="84">原始密码</td>
        <td width="168"><input type="password" name="c_passwd" id="c_passwd" /></td>
       </tr>
      <tr>
        <td>新密码</td>
        <td><input type="password" name="n_passwd" id="n_passwd" /></td>
      </tr>
      <tr>
        <td>确认</td>
        <td><input type="password" name="n_passwd1" id="n_passwd1" /></td>
       </tr>
      <tr>
        <td height="30" colspan="2" align="center"><label>
          <input type="submit" name="button" id="button" value="修改" />
          <input type="reset" name="button2" id="button2" value="取消" />
        </label></td>
       </tr>
    </table>
</form>
<iframe name="sbtFrame" id="sbtFrame" width="0" height="0" scrolling="no" marginheight="0" marginwidth="0" frameborder="0"></iframe>
</body>
</html>

