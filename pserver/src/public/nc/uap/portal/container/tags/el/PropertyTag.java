package nc.uap.portal.container.tags.el;

import javax.servlet.jsp.JspException;


public class PropertyTag extends nc.uap.portal.container.tags.PropertyTag {

	private static final long serialVersionUID = 286L;

	public String getValue() throws JspException {
        return ExpressionEvaluatorProxy.getProxy().evaluate(super.getValue(), pageContext);
    }

}
