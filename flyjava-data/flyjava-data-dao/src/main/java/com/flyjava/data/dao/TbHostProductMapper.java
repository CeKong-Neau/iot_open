package com.flyjava.data.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.flyjava.data.pojo.Product;
import com.flyjava.data.pojo.TbHostProduct;
import com.flyjava.data.pojo.TbHostProductExample;

public interface TbHostProductMapper {
    int countByExample(TbHostProductExample example);

    int deleteByExample(TbHostProductExample example);

    int deleteByPrimaryKey(Long productId);

    int insert(TbHostProduct record);

    int insertSelective(TbHostProduct record);

    List<TbHostProduct> selectByExample(TbHostProductExample example);

    TbHostProduct selectByPrimaryKey(Long productId);

    int updateByExampleSelective(@Param("record") TbHostProduct record, @Param("example") TbHostProductExample example);

    int updateByExample(@Param("record") TbHostProduct record, @Param("example") TbHostProductExample example);

    int updateByPrimaryKeySelective(TbHostProduct record);

    int updateByPrimaryKey(TbHostProduct record);
    
    //根据主机ID,查询产品,返回pojo 有产品列表名称 需要联合产品类别表查询
    List<Product> selectProductByHostId(Long hostId);

    //根据用户id 把用户所有的productId  productId在TbHostProduct对象中
	List<TbHostProduct> getHostProductByUserId(Long userId);
}