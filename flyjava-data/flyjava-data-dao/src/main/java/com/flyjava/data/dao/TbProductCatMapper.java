package com.flyjava.data.dao;

import com.flyjava.data.pojo.TbProductCat;
import com.flyjava.data.pojo.TbProductCatExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbProductCatMapper {
    int countByExample(TbProductCatExample example);

    int deleteByExample(TbProductCatExample example);

    int deleteByPrimaryKey(Integer productCatId);

    int insert(TbProductCat record);

    int insertSelective(TbProductCat record);

    List<TbProductCat> selectByExample(TbProductCatExample example);

    TbProductCat selectByPrimaryKey(Integer productCatId);

    int updateByExampleSelective(@Param("record") TbProductCat record, @Param("example") TbProductCatExample example);

    int updateByExample(@Param("record") TbProductCat record, @Param("example") TbProductCatExample example);

    int updateByPrimaryKeySelective(TbProductCat record);

    int updateByPrimaryKey(TbProductCat record);
}