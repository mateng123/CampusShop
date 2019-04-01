package com.imooc.o2o.dao;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.ProductCategory;
//按照方法名顺序执行 testA   testB  testC ....
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductCategoryDaoTest extends BaseTest {

	@Autowired
	private ProductCategoryDao productCategoryDao;
	
	@Test
	@Ignore
	public void testQueryByShopId() throws Exception {
		long shopId = 1;
		List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(shopId);
		System.out.println("该店铺自定义类别数为："+productCategoryList.size());
	}
}
