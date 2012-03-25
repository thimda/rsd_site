package nc.uap.portal.layoutdesign.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import nc.uap.lfw.core.ContextResourceUtil;
import nc.uap.lfw.core.exception.LfwRuntimeException;
import nc.uap.lfw.core.log.LfwLogger;
import nc.uap.lfw.servletplus.annotation.Action;
import nc.uap.lfw.servletplus.annotation.Servlet;
import nc.uap.lfw.servletplus.core.impl.BaseAction;
 
/**
 * 2011-4-13 ÏÂÎç04:22:35  limingf
 */

@Servlet(path="/swf")
public class FlashPlayerAction extends BaseAction {
	
	/**
	 * ÏÂÔØflashplayer
	 * @throws IOException
	 */
	@Action
	public void down() throws IOException {
		response.setContentType("application/x-msdownload");
        response.setHeader("Content-Disposition","attachment; filename=InstallPlugin.exe");
		BufferedOutputStream bos = null;   
	    BufferedInputStream  bis = null;  
	    String pluginUrl = "";
	    String browser = request.getParameter("browser");
	    if("IE".equals(browser))
	    	pluginUrl = "/html/bin-release/iexplorePlugin.exe";
	    else pluginUrl = "/html/bin-release/firefoxPlugin.exe";
	    String filename = ContextResourceUtil.getCurrentAppPath()+pluginUrl;
	    
	    try {   
	            bis = new BufferedInputStream(new FileInputStream(filename));               
	            bos = new BufferedOutputStream(response.getOutputStream());   	               
	            byte[] buff = new byte[2048];   
	            int bytesRead;   	  
	            while(-1 != (bytesRead = bis.read(buff, 0, buff.length))) {   
	                bos.write(buff,0,bytesRead);   
	                bos.flush();
	            }   
	        } catch(final IOException e) {   
	            LfwLogger.error(e.getMessage(),e);
	            throw new LfwRuntimeException(e);
	        } catch(Exception e) {   
	        	LfwLogger.error(e.getMessage(),e);
	        	 throw new LfwRuntimeException(e);
	        }finally {   
	            if (bis != null)   
	                bis.close();   
	            if (bos != null)   
	            {   
	                bos.flush();   
	                bos.close();   
	                bos=null;   
	            }   
	        }   
	        response.flushBuffer();   
	}

}
