package com.imooc.o2o.entity;

import java.util.Date;

/**
 * 区域实体类
 * 
 * @author mateng
 *
 */
public class Area {

	/**
	 * id
	 */
	private Integer areaId;

	/**
	 * 区域名称
	 */
	private String areaName;

	/**
	 * 优先级，这里使用Integer是因为不想要默认值0
	 */
	private Integer priority;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	public Integer getAreaId() {
		return areaId;
	}

	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	
}
