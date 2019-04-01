package com.imooc.o2o.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.exceptions.ProductOperationException;
import com.imooc.o2o.exceptions.ShopOperationException;

public interface ProductService {
	
	/**
	 * 添加商品信息以及图片处理
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution addProduct(Product product,ImageHolder thumbnail,List<ImageHolder> productImgList) throws ProductOperationException;
	
	/**
	 * 修改产品信息
	 * @param shop
	 * @param thumbnail
	 * @return
	 * @throws ProductOperationException
	 */
	ProductExecution modifyProduct(Product shop,ImageHolder thumbnail,List<ImageHolder> productImgHolderList) throws ProductOperationException;
	
	/**
	 * 通过productId查询product
	 * @param ProductId
	 * @return
	 */
	Product getByProductId(long productId);
	
	/**
	 * 通过商品条件查询满足该条件的商品数量
	 * @param productCondition
	 * @return
	 */
	int getProductCount(Product productCondition);
	
	/**
	 * 根据shopCondition分页返回相应商品列表
	 * @param productCondition
	 * @param rowId
	 * @param pageSize
	 * @return
	 */
	ProductExecution getProductList( Product productCondition, int rowId, int pageSize);
}
