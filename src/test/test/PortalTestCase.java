package test;

import nc.bs.framework.test.AbstractTestCase;
import nc.uap.lfw.core.LfwRuntimeEnvironment;

/**
 * Portal测试超类
 * 设置了Portal数据源
 * @author licza
 *
 */
public class PortalTestCase extends AbstractTestCase{

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		LfwRuntimeEnvironment.setDatasource("design");
	}
	
}
