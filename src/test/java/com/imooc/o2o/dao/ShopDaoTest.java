package com.imooc.o2o.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.imooc.o2o.BaseTest;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;

public class ShopDaoTest extends BaseTest {
	@Autowired
	private ShopDao shopDao;
	@Test
	@Ignore
	public void testAInsertShop() throws Exception {
		
	}
	
	@Test
	public void testBQueryShopList() throws Exception {
		Shop shop = new Shop();
		List<Shop> shopList = shopDao.queryShopList(shop, 0, 2);
		assertEquals(2, shopList.size());
		int count = shopDao.queryShopCount(shop);
		assertEquals(3, count);
		shop.setShopName("花");
		shopList = shopDao.queryShopList(shop, 0, 3);
		assertEquals(2, shopList.size());
		count = shopDao.queryShopCount(shop);
		assertEquals(2, count);
		shop.setShopId(1L);
		shopList = shopDao.queryShopList(shop, 0, 3);
		assertEquals(1, shopList.size());
		count = shopDao.queryShopCount(shop);
		assertEquals(1, count);

	}
	
}
