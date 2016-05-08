package com.erjiao.surveypark.service.impl;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.erjiao.surveypark.dao.BaseDao;
import com.erjiao.surveypark.model.security.Right;
import com.erjiao.surveypark.service.RightService;
import com.erjiao.surveypark.util.DataUtil;
import com.erjiao.surveypark.util.StringUtil;
import com.erjiao.surveypark.util.ValidateUtil;

@Service("rightService")
public class RightServiceImpl extends BaseServiceImpl<Right> implements
		RightService {
	
	@Resource(name="rightDao")
	public void setDao(BaseDao<Right> dao) {
		super.setDao(dao);
	}

	@Override
	public void saveOrUpdateRight(Right r) {
		//插入操作
		int pos = 0;
		long code = 1L;
		if (r.getId() == null) {
//			String hql = "from Right r order by r.rightPos desc, r.rightCode desc";
//			List<Right> rights = this.findEntityByHQL(hql);
//			if (!ValidateUtil.isValid(rights)) {
//				pos = 0;
//				code = 1L;
//			} else {
//				//得到最上面的right
//				Right top = rights.get(0);
//				int topPos = top.getRightPos();
//				long topCode = top.getRightCode();
//				//判断权限码是否达到最大值
//				if (topCode >= (1L << 60)) {
//					pos = topPos + 1;
//					code = 1L;
//				} else {
//					pos = topPos;
//					code = topCode << 1;
//				}
//			}
			String hql = "select max(r.rightPos), max(r.rightCode) from Right r where " +
					"r.rightPos = (select max(rr.rightPos) from Right rr)";
			Object[] arr = (Object[]) this.uniqueResult(hql);
			Integer topPos = (Integer) arr[0];
			Long topCode = (Long) arr[1];
			//没有权限
			if (topPos == null) {
				pos = 0;
				code = 1L;
			} else {
				//权限码是否到达最大值
				if (topCode >= (1L << 60)) {
					pos  = topPos + 1;
					code = 1L;
				} else {
					pos = topPos;
					code = topCode << 1;
				}
			}
			r.setRightCode(code);
			r.setRightPos(pos);
		}
		this.saveOrUpdate(r);
	}

	@Override
	public void appendRightByURL(String url) {
		String hql = "select count(*) from Right r where r.rightUrl = ?";
		Long count = (Long) this.uniqueResult(hql, url);
		if (count == 0) {
			Right r = new Right();
			r.setRightUrl(url);
			this.saveOrUpdateRight(r);
		}
	}

	@Override
	public void batchUpdateRigths(List<Right> allRights) {
		String hql = "update Right r set r.rightName = ?, r.common = ? where r.id = ?";
		if (ValidateUtil.isValid(allRights)) {
			for (Right r : allRights) {
				this.batchEntityByHQL(hql, r.getRightName(), r.isCommon(), r.getId());
			}
		}
	}

	@Override
	public List<Right> findRightsInRange(Integer[] ids) {
		if (ValidateUtil.isValid(ids)) {
			String hql = "from Right r where r.id in (" + StringUtil.arr2str(ids) + ")";
			return this.findEntityByHQL(hql);
		}
		return null;
	}

	@Override
	public List<Right> findRightsNotInRange(Set<Right> rights) {
		if (!ValidateUtil.isValid(rights)) {
			return this.findAllEntities();
		} else {
			String hql = "from Right r where r.id not in (" + DataUtil.extractEntityIds(rights) + ")";
			return this.findEntityByHQL(hql);
		}
		
	}

	@Override
	public int getMaxRightPos() {
		String hql = "select max(r.rightPos) from Right r";
		Integer pos = (Integer) this.uniqueResult(hql);
		return null == pos ? 0 : pos;
	}
	
}
