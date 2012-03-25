package nc.uap.portal.service.itf;

import nc.uap.portal.vo.PtModuleVO;

/**
 * Portal模块增删改服务
 * 
 * @author licza
 * 
 */
public interface IPtPortalModuleService {
	/**
	 * 插入一个PtModuleVO对象
	 * 
	 * @param vo
	 * @return
	 */
	public String add(PtModuleVO vo);

	/**
	 * 插入一批PtModuleVO对象
	 * 
	 * @param vo
	 */
	public void add(PtModuleVO[] vos);

	/**
	 * 更新一批PtModuleVO
	 * 
	 * @param vos
	 */
	public void update(PtModuleVO[] vos);

	/**
	 * 更新一个Module（包括集团）
	 * 
	 * @param vos
	 */
	public void updateAll(PtModuleVO vo);
}
