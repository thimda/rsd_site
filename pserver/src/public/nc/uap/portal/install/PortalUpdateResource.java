package nc.uap.portal.install;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import nc.newinstall.update.IUpdateResource;
import nc.uap.lfw.core.log.LfwLogger;
import nc.vo.jcom.xml.XMLUtil;

/**
 * 安装后，合并part文件
 * 
 * @author dingrf
 */
public class PortalUpdateResource implements IUpdateResource {

	/* web.xml 中元素类型  */
	private static String LISTENER = "listener";
	
	private static String FILTER = "filter";
	
	private static String FILTER_MAPPING = "filter-mapping";
	
	private static String SERVLET = "servlet";
	
	private static String SERVLET_MAPPING = "servlet-mapping";
	
	@Override
	public void doAfterInstall(String ncHome, String newVersion,
			String oldVersion) {
		if(ncHome == null){
			return;
		}
		String portalPath = ncHome + "/hotwebs/portal";
		mergeWebXml(portalPath);
	}

	@Override
	public void doBeforeInstall(String ncHome, String newVersion,
			String oldVersion) {
	}
	
	/**
	 * 合并part到web.xml
	 * 
	 * @param portalPath
	 */
	private void mergeWebXml(String portalPath){
		String webPath = portalPath + "/WEB-INF";
		File webFile = new File(webPath + "/web.xml");
		/* 如果不存在web.xml 不处理合并 */
		if (!webFile.exists()){
			LfwLogger.info(webPath + "目录下,未找到web.xml");
			return;
		}
		File webDir= new File(webPath);
		for (File partFile : webDir.listFiles()){
			if (partFile.isFile() && partFile.getName().endsWith(".portal.part")){
				LfwLogger.info("开始合并：" + partFile.getName());
				String moduleName =  partFile.getName().replace(".portal.part", "");
				/* web.xml Buffer*/
		 		StringBuffer sb = new StringBuffer();
		 		String tempString = null;
		 		
		 		/* 找到part部分，进行删除 */
		 		Boolean needDelete = false;
		 		
		 		Boolean havInsertListener = false;
		 		
		 		Boolean havInsertFilter = false;
		 		
		 		Boolean havInsertFilterMapping = false;
		 		
		 		Boolean havInsertServlet = false;
		 		
		 		Boolean havInsertServletMapping = false;
		 		
		 		String paramName = "";

		 		BufferedReader reader = null;
				try {
					Document doc = null;
					try{
						doc = XMLUtil.getDocument(partFile);
					}catch(Exception e){
						LfwLogger.error("文件: " + partFile.getPath() + "为空或解析错误", e);
					}	
		 			reader = new BufferedReader(new FileReader(webFile));
					while ((tempString = reader.readLine()) != null){
						if (tempString.indexOf("<param-name>modules</param-name>") != -1){
							paramName = "modules";
						}
						if (paramName.equals("modules")  && tempString.indexOf("<param-value>") != -1){
							String modules = tempString.replace("<param-value>", "").replace("</param-value>", "");
							if (("," + modules.trim()+",").indexOf("," + moduleName + ",") == -1){
								modules =  modules + ","+moduleName;
								sb.append("<param-value>"+ modules.trim()+ "</param-value>\n");
			 					continue;
							}
						}
						if (tempString.indexOf("</context-param>")!=-1){
							paramName = "";
						}
						/*删除原有part*/
						if (tempString.indexOf("<!--" + LISTENER + "_begin:"+ moduleName +"-->")!=-1){
							needDelete = true;
						}
						else if (tempString.indexOf("<!--" + LISTENER + "_end:"+ moduleName +"-->")!=-1){
							needDelete = false;
							continue;
						}
						else if (tempString.indexOf("<!--" + FILTER + "_begin:"+ moduleName +"-->")!=-1){
							needDelete = true;
						}
						else if (tempString.indexOf("<!--" + FILTER + "_end:"+ moduleName +"-->")!=-1){
							needDelete = false;
							continue;
						}
						else if (tempString.indexOf("<!--" + FILTER_MAPPING + "_begin:"+ moduleName +"-->")!=-1){
							needDelete = true;
						}
						else if (tempString.indexOf("<!--" + FILTER_MAPPING + "_end:"+ moduleName +"-->")!=-1){
							needDelete = false;
							continue;
						}
						else if (tempString.indexOf("<!--" + SERVLET + "_begin:"+ moduleName +"-->")!=-1){
							needDelete = true;
						}
						else if (tempString.indexOf("<!--" + SERVLET + "_end:"+ moduleName +"-->")!=-1){
							needDelete = false;
							continue;
						}
						else if (tempString.indexOf("<!--" + SERVLET_MAPPING + "_begin:"+ moduleName +"-->")!=-1){
							needDelete = true;
						}
						else if (tempString.indexOf("<!--" + SERVLET_MAPPING + "_end:"+ moduleName +"-->")!=-1){
							needDelete = false;
							continue;
						}
						
						/* 增加新的part信息 */
						if (tempString.indexOf("<filter>")!=-1 || tempString.indexOf("<!--" + FILTER + "_begin:")!=-1 ){
							if (!havInsertListener){
								addPartContent(sb,doc,moduleName,LISTENER);
								havInsertListener=true;
							}
						}
						else if (tempString.indexOf("<filter-mapping>")!=-1 || tempString.indexOf("<!--" + FILTER_MAPPING + "_begin:")!=-1 ){
							if (!havInsertListener){
								addPartContent(sb,doc,moduleName,LISTENER);
								havInsertListener=true;
							}
							if (!havInsertFilter){
								addPartContent(sb,doc,moduleName,FILTER);
								havInsertFilter=true;
							}
						}
						else if (tempString.indexOf("<servlet>")!=-1 || tempString.indexOf("<!--" + SERVLET + "_begin:")!=-1 ){
							if (!havInsertListener){
								addPartContent(sb,doc,moduleName,LISTENER);
								havInsertListener=true;
							}
							if (!havInsertFilter){
								addPartContent(sb,doc,moduleName,FILTER);
								havInsertFilter=true;
							}
							if (!havInsertFilterMapping){
								addPartContent(sb,doc,moduleName,FILTER_MAPPING);
								havInsertFilterMapping=true;
							}
						}
						else if (tempString.indexOf("<servlet-mapping>")!=-1 || tempString.indexOf("<!--" + SERVLET_MAPPING + "_begin:")!=-1 ){
							if (!havInsertListener){
								addPartContent(sb,doc,moduleName,LISTENER);
								havInsertListener=true;
							}
							if (!havInsertFilter){
								addPartContent(sb,doc,moduleName,FILTER);
								havInsertFilter=true;
							}
							if (!havInsertFilterMapping){
								addPartContent(sb,doc,moduleName,FILTER_MAPPING);
								havInsertFilterMapping=true;
							}
							if (!havInsertServlet){
								addPartContent(sb,doc,moduleName,SERVLET);
								havInsertServlet=true;
							}
						}
						else if (tempString.indexOf("<session-config>")!=-1 || tempString.indexOf("<jsp-config>")!=-1 
								|| tempString.indexOf("<welcome-file-list>")!=-1 || tempString.indexOf("</web-app>")!=-1){
							if (!havInsertListener){
								addPartContent(sb,doc,moduleName,LISTENER);
								havInsertListener=true;
							}
							if (!havInsertFilter){
								addPartContent(sb,doc,moduleName,FILTER);
								havInsertFilter=true;
							}
							if (!havInsertFilterMapping){
								addPartContent(sb,doc,moduleName,FILTER_MAPPING);
								havInsertFilterMapping=true;
							}
							if (!havInsertServlet){
								addPartContent(sb,doc,moduleName,SERVLET);
								havInsertServlet=true;
							}
							if (!havInsertServletMapping){
								addPartContent(sb,doc,moduleName,SERVLET_MAPPING);
								havInsertServletMapping=true;
							}
						}
						
						if(!needDelete){
							sb.append(tempString + "\n");
						}
					}
					saveFile(webPath + "/web.xml", sb.toString().getBytes());
		 		}
		 		catch(Exception e){
		 			LfwLogger.error(e.getMessage(), e);
		 		}finally{
		 			try {
						if (reader != null)
							reader.close();
					} catch (IOException e) {
						LfwLogger.error(e.getMessage(), e);
					}
		 		}
				
			}
		}
	}

	/**
	 * 往web.xml中增加内容
	 * 
	 * @param webBuffer
	 * @param doc
	 * @param moduleName
	 * @param type
	 */
	private void addPartContent(StringBuffer webBuffer, Document doc, String moduleName, String type) {
		if (doc == null) return;
		NodeList nodeList = doc.getElementsByTagName(type);
		if (nodeList.getLength()<1)
			return;
		webBuffer.append("<!--"+ type +"_begin:"+ moduleName +"-->\n");
		for (int i=0; i<nodeList.getLength(); i++){
			Node node = nodeList.item(i);
 			Writer wr = new StringWriter();
 			XMLUtil.printDOMTree(wr, node, 0, "UTF-8");
			webBuffer.append(wr.toString() + "\n");
		}	
		webBuffer.append("<!--"+ type +"_end:"+ moduleName +"-->\n");
	}

	
	/**
	 * 保存文件
	 * @param fileName
	 * @param content
	 * @throws Exception
	 */
	private void saveFile(String fileName, byte[] content) throws IOException{
		FileOutputStream out = null;
		File file = new File(fileName);
		try{
			if (!file.exists()){
				File dir = new File(file.getParent());
				if(!dir.exists())
					dir.mkdirs();
				file.createNewFile();
			}
			out = new FileOutputStream(file);
			out.write(content);
		}catch(IOException e){
			throw new IOException(e);
		}finally{
			try {
				if(out != null)
					out.close();
			}catch (IOException e) {
				LfwLogger.error(e.getMessage(), e);
			}
		}
	}
}
