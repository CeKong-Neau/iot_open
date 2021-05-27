package com.flyjava.data.dao;

import com.flyjava.data.pojo.TbUserHost;
import com.flyjava.data.pojo.TbUserHostExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbUserHostMapper {
    int countByExample(TbUserHostExample example);

    int deleteByExample(TbUserHostExample example);

    int deleteByPrimaryKey(Long hostId);

    int insert(TbUserHost record);

    int insertSelective(TbUserHost record);

    List<TbUserHost> selectByExample(TbUserHostExample example);

    TbUserHost selectByPrimaryKey(Long hostId);

    int updateByExampleSelective(@Param("record") TbUserHost record, @Param("example") TbUserHostExample example);

    int updateByExample(@Param("record") TbUserHost record, @Param("example") TbUserHostExample example);

    int updateByPrimaryKeySelective(TbUserHost record);

    int updateByPrimaryKey(TbUserHost record);
}