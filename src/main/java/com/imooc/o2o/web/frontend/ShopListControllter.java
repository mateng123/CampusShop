package com.imooc.o2o.web.frontend;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.service.ShopCategoryService;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.util.HttpServletRequestUtil;

@Controller
@RequestMapping("/frontend")
public class ShopListControllter {

	@Autowired
	private AreaService areaService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private ShopService shopService;
	
	@RequestMapping(value="/listshoppageinfo",method = RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> listShopsPageInfo(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		//试着从前端获取parentId
		long parentId = HttpServletRequestUtil.getLong(request, "parentId");
		List<ShopCategory> shopCategoryList = null;
		if(parentId != -1) {
			//如果parentId存在,则取出该一级ShopCategory下的二级SHopCategory列表
			try {
				ShopCategory shopCategoryCondition = new ShopCategory();
				ShopCategory parent = new ShopCategory();
				parent.setShopCategoryId(parentId);
				shopCategoryCondition.setParent(parent);
				shopCategoryList = shopCategoryService.getShopCategoryList(shopCategoryCondition);
			}catch (Exception e) {
				modelMap.put("success",false);
				modelMap.put("errMsg",e.getMessage());
			}
		}else {
			try {
				//如果parentId不存在，则取出所有一级ShopCategory(用户在首页选择的是全部列表)
				shopCategoryList = shopCategoryService.getShopCategoryList(null);
			}catch (Exception e) {
				modelMap.put("success",false);
				modelMap.put("errMsg",e.getMessage());
			}
		}
		modelMap.put("shopCategoryList",shopCategoryList);
		List<Area> areaList = null;
		try {
			//获取区域列表信息
			areaList = areaService.getAreaList();
			modelMap.put("areaList",areaList);
			modelMap.put("success",true);
			return modelMap;
		}catch (Exception e) {
			modelMap.put("success",false);
			modelMap.put("errMsg",e.getMessage());
		}
		return modelMap;
	}
	
	
	@RequestMapping(value="/listshops",method = RequestMethod.GET)
	@ResponseBody
	private Map<String,Object> listShops(HttpServletRequest request){
		Map<String,Object> modelMap = new HashMap<String,Object>();
		//获取页码
		int paegIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
		//获取一页需要显示的数据条数
		int pageSize = HttpServletRequestUtil.getInt(request,"pageSize");
		//非空判断
		if((paegIndex>-1)&&(pageSize>-1)) {
			//试着获取一级类别Id
			long parentId = HttpServletRequestUtil.getLong(request, "parentId");
			//试着获取特定二级类别Id
			long shopCategoryId = HttpServletRequestUtil.getLong(request,"shopCategoryId");
			//试着获取区域Id
			int areaId = HttpServletRequestUtil.getInt(request,"areaId");
			//试着获取模糊查询的名字
			String shopName = HttpServletRequestUtil.getString(request, "shopName");
			//获取组合之后的查询条件
			Shop shopCondition = compactShopCoondition4Serach(parentId,shopCategoryId,areaId,shopName);
			//根据查询条件和分页信息获取店铺列表，并返回总数
			ShopExecution se = shopService.getShopList(shopCondition, paegIndex, pageSize);
			modelMap.put("shopList",se.getShopList());
			modelMap.put("count",se.getCount());
			modelMap.put("success",true);
		}else {
			
			modelMap.put("success",false);
			modelMap.put("errMsg","empty pagesize or pageIndex");
		}
		return modelMap;
	}


	private Shop compactShopCoondition4Serach(long parentId, long shopCategoryId, int areaId, String shopName) {
		Shop shopCondition = new Shop();
		if(parentId != -1L) {
			//查询某一个一级ShopCategory下面的所有二级ShopCategory里面的店铺列表
			ShopCategory childCategory = new ShopCategory();
			ShopCategory parentCategory = new ShopCategory();
			parentCategory.setShopCategoryId(parentId);
			childCategory.setParent(parentCategory);
			shopCondition.setShopCategory(childCategory);
		}
		if(shopCategoryId != -1L) {
			//查询某个二级ShopCategory下面的店铺列表
			ShopCategory shopCategory = new ShopCategory();
			shopCategory.setShopCategoryId(shopCategoryId);
			shopCondition.setShopCategory(shopCategory);
		}
		if(areaId != -1L) {
			//查询位于某个区域Id下的区域列表
			Area area = new Area();
			area.setAreaId(areaId);
			shopCondition.setArea(area);
		}
		
		if(shopName != null) {
			shopCondition.setShopName(shopName);
		}
		shopCondition.setEnableStatus(1);
		return shopCondition;
	}

}
