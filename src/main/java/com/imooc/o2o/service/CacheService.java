package com.imooc.o2o.service;


public interface CacheService {
	/**
	 * 依据key前缀匹配模式下删除的所有key-value 如传入：shopcategory，则以shopcategory打头的
	 * 所以key-value都会被清空
	 * 
	 */
	void removeFromCache(String keyPrefix);
}
