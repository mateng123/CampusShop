package com.imooc.o2o.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.dao.ProductImgDao;
import com.imooc.o2o.dto.ImageHolder;
import com.imooc.o2o.dto.ProductExecution;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductImg;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.enums.ProductStateEnum;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.exceptions.ProductOperationException;
import com.imooc.o2o.service.ProductService;
import com.imooc.o2o.util.ImageUtil;
import com.imooc.o2o.util.PageCalculator;
import com.imooc.o2o.util.PathUtil;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDao productDao;
	@Autowired
	private ProductImgDao productImgDao;
	
	@Override
	@Transactional
	// 1.处理缩略图，获取缩略图相对路径并赋值给product
	// 2.往tb_productId批量处理商品详情图
	// 3.结合productId批量处理商品详情图
	// 4.将商品详情图列表批量插入tb_product_img中
	public ProductExecution addProduct(Product product, ImageHolder thumbnail, List<ImageHolder> productImgHolderList)
			throws ProductOperationException {
		//空值判断
		if(product != null && product.getShop()!=null &&product.getShop().getShopId()!=null) {
			//给商品设置上默认属性
			product.setCreateTime(new Date());
			product.setLastEditTime(new Date());
			//默认为上架的状态
			product.setEnableStatus(1);
			//若商品缩略图不为空则添加
			if(thumbnail != null) {
				addThumbnail(product,thumbnail);
			}
			try {
				//创建商品信息
				int effectedNum = productDao.insertProduct(product);
				if(effectedNum <= 0) {
					throw new ProductOperationException("创建商品失败");
				}
			}catch (ProductOperationException e) {
				throw new ProductOperationException("创建商品失败"+e.toString());
			}
			//若商品详情图不为空则添加
			if(productImgHolderList != null && productImgHolderList.size()>0 ) {
				addProductImgList(product,productImgHolderList);
			}
			return new ProductExecution(ProductStateEnum.SUCCESS,product);
		}else {
			//传参为空则返回空值信息
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}
	
	/**
	 * 批量添加图片
	 * @param product
	 * @param productImgHolderList
	 */
	private void addProductImgList(Product product, List<ImageHolder> productImgHolderList)
			throws ProductOperationException {
		// 获取图片存储路径，这里直接存放到相应店铺的文件夹底下
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		List<ProductImg> productImgList = new ArrayList<>();
		// 遍历图片一次处理，添加进productImg实体类中
		for (ImageHolder productImgHolder : productImgHolderList) {
			String imgAddr = ImageUtil.generateNormalImg(productImgHolder, dest);
			ProductImg productImg = new ProductImg();
			productImg.setImgAddr(imgAddr);
			productImg.setProductId(product.getProductId());
			productImg.setCreateTime(new Date());
			productImgList.add(productImg);
		}
		// 如果确实是有图片需要添加的，就执行批量添加操作
		if (productImgList.size() > 0) {
			try {
				int effectedNum = productImgDao.batchInsertProductImg(productImgList);
				if (effectedNum <= 0) {
					throw new ProductOperationException("创建商品详情图片失败");
				}
			} catch (Exception e) {
				throw new ProductOperationException("创建商品详情图片失败" + e.toString());
			}
		}
	}


	/**
	 * 添加缩略图
	 * @param product
	 * @param thumbnail
	 */
	private void addThumbnail(Product product, ImageHolder thumbnail) {
		String dest = PathUtil.getShopImagePath(product.getShop().getShopId());
		String thumnailAddr = ImageUtil.generateThumbnail(thumbnail, dest);
		product.setImgAddr(thumnailAddr);
		
	}

	@Override
	@Transactional
	//1.若缩略图参数有值，则处理缩略图
		//若原先存在缩略图则先删除再添加新图，之后获取缩略图相对路径并赋值给product
		//2.若商品详情图列表参数有值，对商品列表进行同样操作
		//3.将tb_product_img下面的该商品原先的商品详情图记录全部清除
		//4.更新tb_product以及tb_product_img的信息
	public ProductExecution modifyProduct(Product product, ImageHolder thumbnail,List<ImageHolder> productImgHolderList) throws ProductOperationException {
		//空值判断
		if(product != null && product.getShop()!=null &&product.getShop().getShopId()!=null) {
			product.setLastEditTime(new Date());
			//若商品缩略图不为空且原有缩略图不为空则删除原有缩略图并添加
			if(thumbnail != null) {
				//先获取一遍原有信息，因为原来的信息里有原图片地址
				Product tempProduct = productDao.queryProductById(product.getProductId());
				if(tempProduct.getImgAddr() != null) {
					ImageUtil.deleteFileOrPath(tempProduct.getImgAddr());
				}
				addThumbnail(product,thumbnail);
			}
			//如果有新存入的商品详情图，则将原来的删除，并添加新的图片
			if(productImgHolderList != null && productImgHolderList.size()>0) {
				deleteProductImgList(product.getProductId());
				addProductImgList(product, productImgHolderList);
			}
			try {
				//更新商品信息
				int effectedNum = productDao.updateProduct(product);
				if(effectedNum <= 0) {
					throw new ProductOperationException("更新商品信息失败");
				}
				return new ProductExecution(ProductStateEnum.SUCCESS,product);
			}catch (Exception e) {
				throw new ProductOperationException("修改商品失败"+e.toString());
			}
		}else {
			//传参为空则返回空值信息
			return new ProductExecution(ProductStateEnum.EMPTY);
		}
	}

	@Override
	public Product getByProductId(long productId) {
		
		return productDao.queryProductById(productId);
	}
	
	private void deleteProductImgList(long productId) {
		//根据productId获取原来图片
		List<ProductImg> productImgList = productImgDao.queryProductImgList(productId);
		//删掉原来的图片
		for (ProductImg productImg : productImgList) {
			ImageUtil.deleteFileOrPath(productImg.getImgAddr());
		}
		//删除数据库里原有图片信息
		productImgDao.deleteProductImgByProductId(productId);
	}

	@Override
	public int getProductCount(Product productCondition) {
		return productDao.queryProductCount(productCondition);
	}

	@Override
	public ProductExecution getProductList(Product productCondition, int rowId, int pageSize) {
		int rowIndex = PageCalculator.calculateRowIndex(rowId, pageSize);
		List<Product> productList =productDao.queryProductList(productCondition, rowIndex, pageSize);
		int count = productDao.queryProductCount(productCondition);
		ProductExecution pe = new ProductExecution();
		pe.setProductList(productList);
		pe.setCount(count);
		return pe;
	}
}
