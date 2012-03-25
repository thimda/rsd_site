package nc.uap.portal.container.tags.el;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;


class ApacheTaglibStandardExpressionEvaluatorProxy extends ExpressionEvaluatorProxy {

    private static Method evaluateMethod;

    static {
        try {
            Class<?> expressionEvaluatorManagerClass = Class.forName("org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager");
            evaluateMethod = expressionEvaluatorManagerClass.getMethod("evaluate", new Class[] { String.class, String.class,  Class.class, PageContext.class});
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to find ExpressionEvaluatorManager.  Make sure standard.jar is in your classpath");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to fine method 'evaluate' on ExpressionEvaluatorManager");
        }
    }

    public String evaluate(String value, PageContext pageContext) throws JspException {
        try {
            return (String)evaluateMethod.invoke(null, new Object[] { "attributeName", value, Object.class,  pageContext});
        } catch (IllegalAccessException e) {
            throw new JspException(e);
        } catch (InvocationTargetException e) {
            throw new JspException(e);
        }
    }
}
