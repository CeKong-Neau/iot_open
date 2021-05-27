package com.flyjava.data.service;

import java.util.List;

import com.flyjava.data.pojo.DataTables;
import com.flyjava.data.pojo.EchartGranaryResult;
import com.flyjava.data.pojo.EchartSearchGranaryResult;
import com.flyjava.data.pojo.EchartTempResult;
import com.flyjava.data.pojo.GranaryTables;
import com.flyjava.data.pojo.HostListResult;
import com.flyjava.data.pojo.Location;
import com.flyjava.data.pojo.ProductListResult;
import com.flyjava.data.pojo.TbData;
import com.flyjava.data.pojo.TbHostProduct;
import com.flyjava.data.pojo.TbUserHost;
import com.flyjava.utils.FlyjavaResult;




public interface DataService {
	//根据用户ID,查询主机表 支持分页
	public HostListResult getHostListByUserId(Long userId,int page,int rows) throws Exception;
	
	//根据主机ID,查询产品,返回产品表 支持分页
	public ProductListResult getProductListByHostId(Long hostId, int page, int rows)throws Exception;
	
	//保存实时数据到 redis  需要key  格式为： key:产品id
	//不同的数据 保存需要不同的 key 和list长度
	public FlyjavaResult saveDataByRedis(TbData tbData,String key,Long length) throws Exception;
    
	//从redis 读取温度数据，并转换成 echarts 格式
	//key  格式为： key:产品id
	public EchartTempResult getEchartTempResultByProductId(Long productId, String key,
			Long length)throws Exception;
	
    //保存userHost 对象
	public FlyjavaResult saveUserHost(TbUserHost tbUserHost)throws Exception;

	//检查HostName 是否重复  重复返回400 
	public FlyjavaResult checkHostName(String hostName)throws Exception;

	//根据用户id 获取用户的所有主机
	public FlyjavaResult getUserHostByUserId(Long userId)throws Exception;

	//展示传感器种类
	public FlyjavaResult getProductCat()throws Exception;

	//绑定产品和主机
	public FlyjavaResult saveHostProduct(TbHostProduct tbHostProduct)throws Exception;

	//检查productId是否被占用
	public FlyjavaResult checkProductId(Long productId)throws Exception;
	
	//根据设备ID 起始时间 终止时间 查询数据
	public EchartTempResult getEchartTempResultBetweenStartAndEnd(Long productId, String start,
			String end)throws Exception;

	//根据用户id 获取location集合 key 为保存在Redis中location的key
	public List<Location> getLocationListByUserId(Long userId,String key);
	
	//根据用户id 获取TbData集合   key 为保存在Redis中数据的key
	public List<TbData> getTbDataListByUserId(Long userId, String key);

	//从redis 读取粮仓温度Temperature数据，并转换成 echarts 格式
	//key  格式为： key:产品id
	public EchartGranaryResult getEchartGranaryTemperatureByProductId(Long productId, String key,
			Long length);

	//从redis 读取粮仓湿度Humidity数据，并转换成 echarts 格式
	//key  格式为： key:产品id
	public EchartGranaryResult getEchartGranaryHumidityByProductId(Long productId, String key,
			Long length);

	//从redis 读取粮仓温湿度数据，并转换成 datatables 格式  表格显示
	public GranaryTables getGranaryTablesByProductId(Long productId, String key, int length);

	//粮仓温湿度查询   根据设备ID 起始时间 终止时间 查询数据
	public EchartSearchGranaryResult getEchartGranaryResultBetweenStartAndEnd(Long productId, String start, String end);
}
