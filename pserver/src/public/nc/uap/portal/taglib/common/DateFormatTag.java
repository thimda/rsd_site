package nc.uap.portal.taglib.common;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 日期格式化工具。针对UFDate，UFDateTime进行格式化
 * 
 * @author dengjt 2006-5-25
 */
public class DateFormatTag extends TagSupport {

	private static final long serialVersionUID = 4352408353524788994L;

	private String pattern = null;

	private String date;

	// 输入类型 0: UFDateTime 1:UFDate
	private int type = 0;

	public int doStartTag() throws JspException {

		try {
			// 格式化日期
			SimpleDateFormat sdf = null;
			Date formatDate;
			String formatedStr = null;
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			if(date == null || date.equals(""))
			{
				formatedStr = "";
			}
			else if (type == 0)
			{
				sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				formatDate = sdf.parse(date);
				formatedStr = df.format(formatDate);
			}
			else if (type == 1)
			{
				sdf = new SimpleDateFormat("yyyy-MM-dd");
				formatDate = sdf.parse(date);
				formatedStr = df.format(formatDate);
			}
			else
			{
				formatedStr = date;
			}
			

			pageContext.getOut().write(formatedStr);

		} 
		catch (Exception e) {
			throw new JspException(e);
		} 
		return EVAL_BODY_INCLUDE;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
