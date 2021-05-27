package com.flyjava.data.dao;

import com.flyjava.data.pojo.Location;
import com.flyjava.data.pojo.TbData;
import com.flyjava.data.pojo.TbDataExample;
import com.flyjava.data.pojo.TbHostProduct;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbDataMapper {
    int countByExample(TbDataExample example);

    int deleteByExample(TbDataExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TbData record);

    int insertSelective(TbData record);

    List<TbData> selectByExample(TbDataExample example);

    TbData selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TbData record, @Param("example") TbDataExample example);

    int updateByExample(@Param("record") TbData record, @Param("example") TbDataExample example);

    int updateByPrimaryKeySelective(TbData record);

    int updateByPrimaryKey(TbData record);

    //根据产品ID 起始时间和终止时间查询
	List<TbData> selectDataByProductIdBetweenStartAndEnd(@Param("productId")Long productId, @Param("start")String start, @Param("end")String end);

	
}