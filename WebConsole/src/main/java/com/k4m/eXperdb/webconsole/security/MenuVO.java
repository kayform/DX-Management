package com.k4m.eXperdb.webconsole.security;

import java.util.ArrayList;
import java.util.List;

public class MenuVO {

	private String id;
	private String name;
	private String itemOrder;
	private String subOrder;
	private String depth;
	private String parentId;
	private String url;
	private String menuType;
	private String menuColor;
	private List<MenuVO> subMenuList;

	public MenuVO() {
		id = "";
		name = "";
		itemOrder = "";
		subOrder = "";
		depth = "";
		parentId = "";
		url = "";
		menuType = "";
		menuColor = "";
		subMenuList = new ArrayList<MenuVO>();
	}

	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public List<MenuVO> getSubMenuList() {
		return subMenuList;
	}

	public void setSubMenuList(List<MenuVO> subMenuList) {
		this.subMenuList = subMenuList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getItemOrder() {
		return itemOrder;
	}

	public void setItemOrder(String itemOrder) {
		this.itemOrder = itemOrder;
	}

	public String getSubOrder() {
		return subOrder;
	}

	public void setSubOrder(String subOrder) {
		this.subOrder = subOrder;
	}

	public String getDepth() {
		return depth;
	}

	public void setDepth(String depth) {
		this.depth = depth;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMenuColor() {
		return menuColor;
	}

	public void setMenuColor(String menuColor) {
		this.menuColor = menuColor;
	}



}
