package nc.uap.portal.container.tags.el;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

class JSP21ExpressionEvaluatorProxy extends ExpressionEvaluatorProxy {

    public static Method jspApplicationContextGetter;

    public static Method expressionFactoryGetter;

    public static Method elContextGetter;

    public static Method valueExpressionGetter;

    public static Method evalMethod;

    private static Object jspFactory;

    static {
        try {
            jspFactory = Class.forName("javax.servlet.jsp.JspFactory")
                .getMethod("getDefaultFactory", new Class[0]).invoke(null);
            jspApplicationContextGetter = 
                jspFactory.getClass().getMethod("getJspApplicationContext",
                    new Class[] { ServletContext.class });
            expressionFactoryGetter = 
                Class.forName("javax.servlet.jsp.JspApplicationContext")
                    .getMethod("getExpressionFactory", new Class[0]);
            elContextGetter = 
                PageContext.class.getMethod("getELContext", new Class[0]);
            valueExpressionGetter = 
                Class.forName("javax.el.ExpressionFactory").getMethod(
                    "createValueExpression", new Class[] 
                    { Class.forName("javax.el.ELContext"), String.class, Class.class });
            evalMethod = Class.forName("javax.el.ValueExpression").getMethod(
                    "getValue", new Class[] { Class.forName("javax.el.ELContext") });
        } catch (Exception e) {
            throw new RuntimeException("Unable to find JSP2.1 methods.", e);
        }
    }

    public String evaluate(String value, PageContext pageContext)
            throws JspException {
        try {
            Object jspApplicationContext = jspApplicationContextGetter.invoke(
                    jspFactory,
                    new Object[] { pageContext.getServletContext() });

            Object expressionFactory = expressionFactoryGetter.invoke(
                    jspApplicationContext, new Object[] {});

            Object elContext = elContextGetter.invoke(pageContext);

            Object valueExpression = valueExpressionGetter.invoke(
                    expressionFactory, new Object[] { elContext, value,
                            Object.class });

            Object evaluated = evalMethod.invoke(valueExpression,
                    new Object[] { elContext });

            if (evaluated != null) {
                value = evaluated.toString();
            }
        } catch (IllegalAccessException e) {
            throw new JspException(e);
        } catch (InvocationTargetException e) {
            throw new JspException(e);
        }
        return value;
    }

}

