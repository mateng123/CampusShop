package com.imooc.o2o.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.imooc.o2o.entity.Product;

public interface ProductDao {

	/**
	 * 查询商品列表并分页，可输入条件有：商品名（模糊），商品状态，店铺Id，商品类别
	 * @param productCondition
	 * @param rowId
	 * @param pageSize
	 * @return
	 */
	List<Product> queryProductList(@Param("productCondition") Product productCondition,@Param("rowIndex") int rowId,
			@Param("paegSize") int pageSize);
	
	/**
	 * 查询对应的商品总数
	 * @param productCondition
	 * @return
	 */
	int queryProductCount(@Param("productCondition") Product productCondition);
	/**
	 * 插入商品
	 * @param product
	 * @return
	 */
	int insertProduct(Product product);
	
	/**
	 * 商品编辑
	 * @param product
	 * @return
	 */
	int updateProduct(Product product);
	
	/**
	 * 通过productId查询唯一的商品信息
	 * @param productId
	 * @return
	 */
	Product queryProductById(long productId);
	
	/**
	 * 删除指定商品下的所有详情图
	 */
	int delectProductImgByProductId(long productId);
	
	/**
	 * 删除商品类别之前，将商品类别ID置为空
	 * @param productCategoryId
	 * @return
	 */
	int updateProductCategoryToNull(long productCategoryId);
}
