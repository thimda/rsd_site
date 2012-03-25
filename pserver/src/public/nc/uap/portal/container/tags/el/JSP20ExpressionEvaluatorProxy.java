package nc.uap.portal.container.tags.el;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

class JSP20ExpressionEvaluatorProxy extends ExpressionEvaluatorProxy {

    public static Method expressionEvaluatorGetter;

    public static Method variableResolverGetter;

    public static Method evalMethod;

    static {
        try {
            expressionEvaluatorGetter = PageContext.class.getMethod("getExpressionEvaluator", new Class[0]);

            variableResolverGetter = PageContext.class.getMethod("getVariableResolver", new Class[0]);

            evalMethod = expressionEvaluatorGetter.getReturnType().getMethod("evaluate",
                new Class[]{String.class, Class.class, variableResolverGetter.getReturnType(), Class.forName("javax.servlet.jsp.el.FunctionMapper")});

        } catch (Exception e) {
            throw new RuntimeException("Unable to located JSP20 Methods.", e);
        }
    }

    public String evaluate(String value, PageContext pageContext) throws JspException {
        try {
            Object evaluator = expressionEvaluatorGetter.invoke(pageContext, new Object[0]);
            Object resolver = variableResolverGetter.invoke(pageContext, new Object[0]);

            Object evaluated = evalMethod.invoke(evaluator, new Object[]{value, Object.class, resolver, null});

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
    
    /*
        ExpressionEvaluator eval = pageContext.getExpressionEvaluator();

        try {
            Object evaluated = eval.evaluate(
                    value,
                    Object.class,
                    pageContext.getVariableResolver(),
                    null
            );

            if(evaluated != null) {
                value = evaluated.toString();
            }

        }
        catch(ELException el) {
            throw new JspException(el);
        }

        return value;
    }
    */
}
