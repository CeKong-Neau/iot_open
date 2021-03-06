package com.flyjava.data.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.flyjava.data.dao.TbDataMapper;
import com.flyjava.data.dao.TbHostProductMapper;
import com.flyjava.data.dao.TbProductCatMapper;
import com.flyjava.data.dao.TbUserHostMapper;
import com.flyjava.data.pojo.DataTables;
import com.flyjava.data.pojo.EchartGranaryResult;
import com.flyjava.data.pojo.EchartSearchGranaryResult;
import com.flyjava.data.pojo.EchartTempResult;
import com.flyjava.data.pojo.Granary20;
import com.flyjava.data.pojo.GranaryTables;
import com.flyjava.data.pojo.HostListResult;
import com.flyjava.data.pojo.Location;
import com.flyjava.data.pojo.Product;
import com.flyjava.data.pojo.ProductListResult;
import com.flyjava.data.pojo.TbData;
import com.flyjava.data.pojo.TbHostProduct;
import com.flyjava.data.pojo.TbHostProductExample;
import com.flyjava.data.pojo.TbProductCat;
import com.flyjava.data.pojo.TbProductCatExample;
import com.flyjava.data.pojo.TbUserHost;
import com.flyjava.data.pojo.TbUserHostExample;
import com.flyjava.data.pojo.TbUserHostExample.Criteria;
import com.flyjava.data.pojo.Temp;
import com.flyjava.data.service.DataService;
import com.flyjava.redis.JedisClient;
import com.flyjava.utils.FlyjavaResult;
import com.flyjava.utils.JsonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class DataServiceImpl implements DataService {
	@Value("${ECHART_TEMP_TYPE}")
	private String ECHART_TEMP_TYPE;
	
	@Value("${ECHART_TEMP_STACK}")
	private String ECHART_TEMP_STACK;
	
	@Autowired
	private TbUserHostMapper tbUserHostMapper;
	
	@Autowired
	private TbHostProductMapper tbHostProductMapper;
	
	@Autowired
	private TbDataMapper tbDataMapper;
	
	@Autowired
	private TbProductCatMapper tbProductCatMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	

	/**
	 * ????????????ID,??????????????? ????????????
	 *  page ??????????????????  rows ???????????????
	 */
	@Override
	public HostListResult getHostListByUserId(Long userId, int page, int rows) throws Exception {
		if (userId==null){
			return null;
		}
		//??????????????????
		TbUserHostExample example=new TbUserHostExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		
		//??????????????????????????? ???????????????sql ????????????  ???????????????select??????????????????
		PageHelper.startPage(page, rows);
		//??????
		List<TbUserHost> hostList = tbUserHostMapper.selectByExample(example);
		//??????????????? pageInfo ?????????????????????
        PageInfo<TbUserHost>  pageInfo=new PageInfo<TbUserHost>(hostList);
        
        HostListResult result =new HostListResult();
        //??????  ???????????????
		long recordCount = pageInfo.getTotal();
		long pageCount = recordCount / rows;
		if (recordCount % rows > 0) {
			pageCount++;
		}
		// ???????????????
		result.setPageCount(pageCount);
		// ??????????????????
		result.setPageCount(recordCount);
		// ???????????????
		result.setHostList(hostList);
		
		
		return result;
	}

	/**
	 * ????????????ID,?????????????????????  ????????????
	 *  page ??????????????????  rows ???????????????
	 */
	@Override
	public ProductListResult getProductListByHostId(Long hostId, int page, int rows) {
		if (hostId==null){
			return null;
		}
		//????????????
		TbHostProductExample example=new TbHostProductExample();
		com.flyjava.data.pojo.TbHostProductExample.Criteria criteria = example.createCriteria();
		criteria.andHostIdEqualTo(hostId);
		//??????????????????????????? ???????????????sql ????????????  ???????????????select??????????????????
		PageHelper.startPage(page, rows);
		//??????
		List<Product> productList = tbHostProductMapper.selectProductByHostId(hostId);
		//??????????????? pageInfo ?????????????????????
        PageInfo<Product>  pageInfo=new PageInfo<Product>(productList);
        
        ProductListResult result=new ProductListResult();
        //??????  ???????????????
  		long recordCount = pageInfo.getTotal();
  		long pageCount = recordCount / rows;
  		if (recordCount % rows > 0) {
  			pageCount++;
  		}
  		// ???????????????
  		result.setPageCount(pageCount);
  		// ??????????????????
  		result.setPageCount(recordCount);
  		// ???????????????
  		result.setProductList(productList);
  		
		return result;
	}

	/**
	 * ????????????????????? redis
	 * ??????????????? ????????????????????? key  
	 * key???????????? key:??????id
	 * length ???????????????redis ?????????
	 */
	@Override
	public FlyjavaResult saveDataByRedis(TbData tbData,String key,Long length) throws Exception {
		//????????????????????????????????????
		if(tbData.getProductId()==null||tbData.getProductId()==0){
			return FlyjavaResult.build(400, "??????id????????????");
		}
		if(StringUtils.isBlank(tbData.getData())){
			return FlyjavaResult.build(400, "??????????????????");
		}
		
		//?????? -1????????????
		int insert =-1;
		//1 ??????key  length ?????????redis
		//????????????key ??????length ???0
		String keyOfProductId=key+":"+tbData.getProductId();
		long len=jedisClient.llen(keyOfProductId);
		if(length>len){
			//????????????????????? ???????????????
			jedisClient.rpush(keyOfProductId,JsonUtils.objectToJson(tbData));
		}else if(length==len){
			//??????????????????
			//????????????
			String jsonData = jedisClient.lpop(keyOfProductId);
			//?????????pojo
			TbData jsonToPojo = JsonUtils.jsonToPojo(jsonData, TbData.class);
			//????????????????????????
			insert = tbDataMapper.insert(jsonToPojo);
			//????????????
			jedisClient.rpush(keyOfProductId,JsonUtils.objectToJson(tbData));
		}
		return FlyjavaResult.ok(insert);
	}

	/**
	 * ????????????id ???redis ???????????????????????????
	 * key????????? key:??????Id
	 */
	@Override
	public EchartTempResult getEchartTempResultByProductId(Long productId, String key, Long length) throws Exception {
		//????????????id
		if(productId==null){
			return null;
		}
		//??????key:??????id ???redis????????????
		List<String> list = jedisClient.lrange(key+":"+productId, 0, -1);
		
		
		
		EchartTempResult result=new EchartTempResult();
		//x?????????categories
		List xData=new ArrayList();
		
		//y?????????
		List yDataAll=new ArrayList();
		
		//???????????????????????????   [1555,132,101,134,90,230,90] 
		List temp1List=new ArrayList();
		List temp2List=new ArrayList();
		List temp3List=new ArrayList();
		List temp4List=new ArrayList();
		List temp5List=new ArrayList();
		for (String string : list) {
			//??????????????????
			TbData tbData = JsonUtils.jsonToPojo(string, TbData.class);
			//????????????????????????Temp??????
			Temp temp=JsonUtils.jsonToPojo(tbData.getData(),Temp.class);
			
			//??????date?????? ???????????????????????????????????????
			Date created = tbData.getCreated();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm"); 
			String strDate=sdf.format(created);  
			
			//????????????????????????X??????
			xData.add(strDate);
			//?????????????????????????????????????????????
			temp1List.add(temp.getTemp1().toString());
			temp2List.add(temp.getTemp2().toString());
			temp3List.add(temp.getTemp3().toString());
			temp4List.add(temp.getTemp4().toString());
			temp5List.add(temp.getTemp5().toString());
		}
		//????????????y????????????
		Map temp1Data=new HashMap();
		Map temp2Data=new HashMap();
		Map temp3Data=new HashMap();
		Map temp4Data=new HashMap();
		Map temp5Data=new HashMap();
		
		//name??????
		temp1Data.put("name", "Temp1");
		temp2Data.put("name", "Temp2");
		temp3Data.put("name", "Temp3");
		temp4Data.put("name", "Temp4");
		temp5Data.put("name", "Temp5");
		
		//type ??????
		temp1Data.put("type", ECHART_TEMP_TYPE);
		temp2Data.put("type", ECHART_TEMP_TYPE);
		temp3Data.put("type", ECHART_TEMP_TYPE);
		temp4Data.put("type", ECHART_TEMP_TYPE);
		temp5Data.put("type", ECHART_TEMP_TYPE);
		
		//STACK ??????  ??????????????????????????????????????????????????????stack?????????????????????????????????????????????????????????????????????
		//???????????????????????????????????????????????????stack:?????? ???????????????????????????
		/*temp1Data.put("stack", ECHART_TEMP_STACK);
		temp2Data.put("stack", ECHART_TEMP_STACK);
		temp3Data.put("stack", ECHART_TEMP_STACK);
		temp4Data.put("stack", ECHART_TEMP_STACK);
		temp5Data.put("stack", ECHART_TEMP_STACK);*/
		
		//data ?????? ,??????????????? ??????temp ???10???????????????????????????
		temp1Data.put("data",temp1List );
		temp2Data.put("data",temp2List);
		temp3Data.put("data",temp3List);
		temp4Data.put("data",temp4List);
		temp5Data.put("data",temp5List);
		
		//???5???temp???????????? ??????y???????????? 
		yDataAll.add(temp1Data);
		yDataAll.add(temp2Data);
		yDataAll.add(temp3Data);
		yDataAll.add(temp4Data);
		yDataAll.add(temp5Data);
		
		//?????????????????????
		result.setCategories(xData);
		result.setData(yDataAll);
		return result;
	}

	/**
	 * ??????UserHost?????????tb_user_host
	 */
	@Override
	public FlyjavaResult saveUserHost(TbUserHost tbUserHost) throws Exception {
		int insert = tbUserHostMapper.insert(tbUserHost);
		if(insert==0){
			return FlyjavaResult.build(400, "??????tbUserHost??????");
		}
		return FlyjavaResult.ok(insert);
	}
	/**
	 * ??????HostName ????????????  ????????????400
	 */
	@Override
	public FlyjavaResult checkHostName(String hostName) throws Exception {
		TbUserHostExample example=new TbUserHostExample();
		Criteria criteria = example.createCriteria();
		criteria.andHostNameEqualTo(hostName);
		List<TbUserHost> list = tbUserHostMapper.selectByExample(example);
		
		//???????????? ??????400
		if(list!=null&&list.size()>0){
			return FlyjavaResult.build(400, "???????????????");
		}
		return FlyjavaResult.ok();
	}

	/**
	 * ????????????id ???????????????????????????
	 */
	@Override
	public FlyjavaResult getUserHostByUserId(Long userId) throws Exception{
		TbUserHostExample example=new TbUserHostExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<TbUserHost> list = tbUserHostMapper.selectByExample(example);
		return FlyjavaResult.ok(list);
	}
	
	/**
	 * ?????????????????????
	 */
	@Override
	public FlyjavaResult getProductCat() throws Exception {
		TbProductCatExample example=new TbProductCatExample();
		com.flyjava.data.pojo.TbProductCatExample.Criteria criteria = example.createCriteria();
		//???status ???1 ???????????????
		criteria.andStatusEqualTo((byte) 1);
		List<TbProductCat> list = tbProductCatMapper.selectByExample(example);
		return FlyjavaResult.ok(list);
	}
	/**
	 * ?????????????????????
	 */
	@Override
	public FlyjavaResult saveHostProduct(TbHostProduct tbHostProduct) throws Exception {
		int insert = tbHostProductMapper.insert(tbHostProduct);
		return FlyjavaResult.ok(insert);
	}

	/**
	 * ??????productId ????????????????????????
	 */
	@Override
	public FlyjavaResult checkProductId(Long productId) throws Exception {
		TbHostProductExample example=new TbHostProductExample();
		com.flyjava.data.pojo.TbHostProductExample.Criteria criteria = example.createCriteria();
		criteria.andProductIdEqualTo(productId);
		List<TbHostProduct> list = tbHostProductMapper.selectByExample(example);
		if(list!=null&&list.size()>0){
			return FlyjavaResult.build(400, "??????ID???????????????");
		}
		return FlyjavaResult.ok();
	}

	/**
	 * ????????????ID ???????????? ???????????? ????????????
	 */
	@Override
	public EchartTempResult getEchartTempResultBetweenStartAndEnd(Long productId, String start, String end)
			throws Exception {
		//????????????
		if(productId==null){
			return null;
		}
		if(StringUtils.isBlank(start)){
			return null;
		}
		if(StringUtils.isBlank(end)){
			return null;
		}
		//??????
	    List<TbData> DataList=tbDataMapper.selectDataByProductIdBetweenStartAndEnd(productId,start,end);
		
	    //?????????EchartTempResult
	    EchartTempResult result=new EchartTempResult();
  		//x?????????categories
  		List xData=new ArrayList();
  		
  		//y?????????
  		List yDataAll=new ArrayList();
  		
  		//???????????????????????????   [1555,132,101,134,90,230,90] 
  		List temp1List=new ArrayList();
  		List temp2List=new ArrayList();
  		List temp3List=new ArrayList();
  		List temp4List=new ArrayList();
  		List temp5List=new ArrayList();
	    
	    //?????????????????????
	    for (TbData tbData : DataList) {
	    	//????????????????????????Temp??????
			Temp temp=JsonUtils.jsonToPojo(tbData.getData(),Temp.class);
			//??????date?????? ???????????????????????????????????????
			Date created = tbData.getCreated();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm"); 
			String strDate=sdf.format(created);  
			
			//????????????????????????X??????
			xData.add(strDate);
			//?????????????????????????????????????????????
			temp1List.add(temp.getTemp1().toString());
			temp2List.add(temp.getTemp2().toString());
			temp3List.add(temp.getTemp3().toString());
			temp4List.add(temp.getTemp4().toString());
			temp5List.add(temp.getTemp5().toString());
		}
		//????????????y????????????
		Map temp1Data=new HashMap();
		Map temp2Data=new HashMap();
		Map temp3Data=new HashMap();
		Map temp4Data=new HashMap();
		Map temp5Data=new HashMap();
		
		//name??????
		temp1Data.put("name", "Temp1");
		temp2Data.put("name", "Temp2");
		temp3Data.put("name", "Temp3");
		temp4Data.put("name", "Temp4");
		temp5Data.put("name", "Temp5");
		
		//type ??????
		temp1Data.put("type", ECHART_TEMP_TYPE);
		temp2Data.put("type", ECHART_TEMP_TYPE);
		temp3Data.put("type", ECHART_TEMP_TYPE);
		temp4Data.put("type", ECHART_TEMP_TYPE);
		temp5Data.put("type", ECHART_TEMP_TYPE);
		
		//STACK ??????  ??????????????????????????????????????????????????????stack?????????????????????????????????????????????????????????????????????
		//???????????????????????????????????????????????????stack:?????? ???????????????????????????
		/*temp1Data.put("stack", ECHART_TEMP_STACK);
		temp2Data.put("stack", ECHART_TEMP_STACK);
		temp3Data.put("stack", ECHART_TEMP_STACK);
		temp4Data.put("stack", ECHART_TEMP_STACK);
		temp5Data.put("stack", ECHART_TEMP_STACK);*/
		
		//data ?????? ,??????????????? ??????temp ???10???????????????????????????
		temp1Data.put("data",temp1List );
		temp2Data.put("data",temp2List);
		temp3Data.put("data",temp3List);
		temp4Data.put("data",temp4List);
		temp5Data.put("data",temp5List);
		
		//???5???temp???????????? ??????y???????????? 
		yDataAll.add(temp1Data);
		yDataAll.add(temp2Data);
		yDataAll.add(temp3Data);
		yDataAll.add(temp4Data);
		yDataAll.add(temp5Data);
		
		//?????????????????????
		result.setCategories(xData);
		result.setData(yDataAll);
		return result;
	}

	/**
	 * ????????????id ??????location??????
	 *  select  * from tb_data where product_cat_id =5 
		group by product_id 
		having product_id in(select product_id from tb_host_product where host_id=1)
		order by created desc ;
		
		//
		 * select product_id from tb_host_product where host_id in 
			(select  host_id from tb_user_host where user_id =38 and `status`=1)
	 */
	@Override
	public List<Location> getLocationListByUserId(Long userId,String key) {
		if(userId==null){
			return null;
		}
		//1.????????????id ???????????????????????????????????????id
		List<TbHostProduct> tbHostProductList= tbHostProductMapper.getHostProductByUserId(userId);
		//2.for ?????? ????????????????????????id ???redis?????? location??????
		List<Location> locationList=new ArrayList<Location>();
		for (TbHostProduct tbHostProduct : tbHostProductList) {
			Long productId=tbHostProduct.getProductId();
			if(tbHostProduct==null||productId==null){
				break;
			}
			//???redis????????? ??????id?????? data??????
			Location location= getLocationByProductIdAndKey(productId,key);
			if(location!=null&&location.getLatitude()!=null){
				locationList.add(location);
			}
		}
		
		return locationList;
	}
	//????????????ID ???key ???Redis?????????location??????
	private Location getLocationByProductIdAndKey(Long productId, String key) {
		//0.??????????????????
		Location location=null;
		//1.????????????
		if(productId==null||productId==0){
			return null;
		}
		if(key==null||key.length()==0){
			return null;
		}
		//2.???redis????????? string
		List<String> list = jedisClient.lrange(key+":"+productId, 0, -1);
		//3.???????????????String????????????????????????0 ??????????????? tbdata  ??????
		if(list==null){
			return null;
		}
		for (String string : list) {
			if(string==null||string.length()==0){
				break;
			}
			TbData tbdata=JsonUtils.jsonToPojo(string,TbData.class);
			if(tbdata!=null&&tbdata.getData()!=null){
				location=JsonUtils.jsonToPojo(tbdata.getData(),Location.class);
			}
		}
		
		return location;
	}

	/**
	 * ??????userID ???key ???Redis ???tbData?????????
	 */
	@Override
	public List<TbData> getTbDataListByUserId(Long userId, String key) {
		if(userId==null){
			return null;
		}
		//1.????????????id ???????????????????????????????????????id
		List<TbHostProduct> tbHostProductList= tbHostProductMapper.getHostProductByUserId(userId);
		//2.for ?????? ????????????????????????id ???redis?????? location??????
		List<TbData> tbDataList=new ArrayList<TbData>();
		for (TbHostProduct tbHostProduct : tbHostProductList) {
			Long productId=tbHostProduct.getProductId();
			if(tbHostProduct==null||productId==null){
				break;
			}
			//3.???redis????????? ??????id?????? data??????
			List<String> strings=jedisClient.lrange(key+":"+productId, 0, -1);
			//4.??????
			for (String string : strings) {
				if(string==null||string.length()==0){
					break;
				}
				TbData tbdata=JsonUtils.jsonToPojo(string,TbData.class);
				tbDataList.add(tbdata);
			}
		}
		
		return tbDataList;
	}

	/**
	 * ?????????????????????
	 */
	@Override
	public EchartGranaryResult getEchartGranaryTemperatureByProductId(Long productId, String key,
			Long length) {
		//????????????id
		if(productId==null){
			return null;
		}
		//??????key:??????id ???redis????????????
		List<String> list = jedisClient.lrange(key+":"+productId, 0, -1);
		
		EchartGranaryResult result=new EchartGranaryResult();
		//x?????????categories
		List xData=new ArrayList();
		
		//y?????????
		List yDataAll=new ArrayList();
		
		//????????????20????????????   [1555,132,101,134,90,230,90] 
		List temp0List=new ArrayList();
		List temp1List=new ArrayList();
		List temp2List=new ArrayList();
		List temp3List=new ArrayList();
		List temp4List=new ArrayList();
		List temp5List=new ArrayList();
		List temp6List=new ArrayList();
		List temp7List=new ArrayList();
		List temp8List=new ArrayList();
		List temp9List=new ArrayList();
		List temp10List=new ArrayList();
		List temp11List=new ArrayList();
		List temp12List=new ArrayList();
		List temp13List=new ArrayList();
		List temp14List=new ArrayList();
		List temp15List=new ArrayList();
		List temp16List=new ArrayList();
		List temp17List=new ArrayList();
		List temp18List=new ArrayList();
		List temp19List=new ArrayList();
		for (String string : list) {
			//??????????????????
			TbData tbData = JsonUtils.jsonToPojo(string, TbData.class);
			//????????????????????????Granary??????
			Granary20 granary=JsonUtils.jsonToPojo(tbData.getData(),Granary20.class);
			
			if(granary==null){
				continue;
			}
			//??????date?????? ???????????????????????????????????????
			Date created = tbData.getCreated();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm"); 
			String strDate=sdf.format(created);  
			
			//????????????????????????X??????
			if(strDate!=null){
				xData.add(strDate);
			}
			//?????????????????????????????????????????????
			if(granary.getT0()!=null){
				temp0List.add(granary.getT0().toString());
			}
			if(granary.getT1()!=null){
				temp1List.add(granary.getT1().toString());
			}
			if(granary.getT2()!=null){
				temp2List.add(granary.getT2().toString());
			}
			if(granary.getT3()!=null){
				temp3List.add(granary.getT3().toString());
			}
			if(granary.getT4()!=null){
				temp4List.add(granary.getT4().toString());
			}
			if(granary.getT5()!=null){
				temp5List.add(granary.getT5().toString());
			}
			if(granary.getT6()!=null){
				temp6List.add(granary.getT6().toString());
			}
			if(granary.getT7()!=null){
				temp7List.add(granary.getT7().toString());
			}
			if(granary.getT8()!=null){
				temp8List.add(granary.getT8().toString());
			}
			if(granary.getT9()!=null){
				temp9List.add(granary.getT9().toString());
			}
			if(granary.getT10()!=null){
				temp10List.add(granary.getT10().toString());
			}
			if(granary.getT11()!=null){
				temp11List.add(granary.getT11().toString());
			}
			if(granary.getT12()!=null){
				temp12List.add(granary.getT12().toString());
			}
			if(granary.getT13()!=null){
				temp13List.add(granary.getT13().toString());
			}
			if(granary.getT14()!=null){
				temp14List.add(granary.getT14().toString());
			}
			if(granary.getT15()!=null){
				temp15List.add(granary.getT15().toString());
			}
			if(granary.getT16()!=null){
				temp16List.add(granary.getT16().toString());
			}
			if(granary.getT17()!=null){
				temp17List.add(granary.getT17().toString());
			}
			if(granary.getT18()!=null){
				temp18List.add(granary.getT18().toString());
			}
			if(granary.getT19()!=null){
				temp19List.add(granary.getT19().toString());
			}
		}
		//????????????y????????????
		Map temp0Data=new HashMap();
		Map temp1Data=new HashMap();
		Map temp2Data=new HashMap();
		Map temp3Data=new HashMap();
		Map temp4Data=new HashMap();
		Map temp5Data=new HashMap();
		Map temp6Data=new HashMap();
		Map temp7Data=new HashMap();
		Map temp8Data=new HashMap();
		Map temp9Data=new HashMap();
		Map temp10Data=new HashMap();
		Map temp11Data=new HashMap();
		Map temp12Data=new HashMap();
		Map temp13Data=new HashMap();
		Map temp14Data=new HashMap();
		Map temp15Data=new HashMap();
		Map temp16Data=new HashMap();
		Map temp17Data=new HashMap();
		Map temp18Data=new HashMap();
		Map temp19Data=new HashMap();
		
		//name??????
		temp0Data.put("name", "T0");
		temp1Data.put("name", "T1");
		temp2Data.put("name", "T2");
		temp3Data.put("name", "T3");
		temp4Data.put("name", "T4");
		temp5Data.put("name", "T5");
		temp6Data.put("name", "T6");
		temp7Data.put("name", "T7");
		temp8Data.put("name", "T8");
		temp9Data.put("name", "T9");
		temp10Data.put("name", "T10");
		temp11Data.put("name", "T11");
		temp12Data.put("name", "T12");
		temp13Data.put("name", "T13");
		temp14Data.put("name", "T14");
		temp15Data.put("name", "T15");
		temp16Data.put("name", "T16");
		temp17Data.put("name", "T17");
		temp18Data.put("name", "T18");
		temp19Data.put("name", "T19");
		
		//type ??????
		temp0Data.put("type", ECHART_TEMP_TYPE);
		temp1Data.put("type", ECHART_TEMP_TYPE);
		temp2Data.put("type", ECHART_TEMP_TYPE);
		temp3Data.put("type", ECHART_TEMP_TYPE);
		temp4Data.put("type", ECHART_TEMP_TYPE);
		temp5Data.put("type", ECHART_TEMP_TYPE);
		temp6Data.put("type", ECHART_TEMP_TYPE);
		temp7Data.put("type", ECHART_TEMP_TYPE);
		temp8Data.put("type", ECHART_TEMP_TYPE);
		temp9Data.put("type", ECHART_TEMP_TYPE);
		temp10Data.put("type", ECHART_TEMP_TYPE);
		temp11Data.put("type", ECHART_TEMP_TYPE);
		temp12Data.put("type", ECHART_TEMP_TYPE);
		temp13Data.put("type", ECHART_TEMP_TYPE);
		temp14Data.put("type", ECHART_TEMP_TYPE);
		temp15Data.put("type", ECHART_TEMP_TYPE);
		temp16Data.put("type", ECHART_TEMP_TYPE);
		temp17Data.put("type", ECHART_TEMP_TYPE);
		temp18Data.put("type", ECHART_TEMP_TYPE);
		temp19Data.put("type", ECHART_TEMP_TYPE);
		
		//STACK ??????  ??????????????????????????????????????????????????????stack?????????????????????????????????????????????????????????????????????
		//???????????????????????????????????????????????????stack:?????? ???????????????????????????
		/*temp1Data.put("stack", ECHART_TEMP_STACK);
		temp2Data.put("stack", ECHART_TEMP_STACK);
		temp3Data.put("stack", ECHART_TEMP_STACK);
		temp4Data.put("stack", ECHART_TEMP_STACK);
		temp5Data.put("stack", ECHART_TEMP_STACK);*/
		
		//data ?????? ,??????????????? ??????temp ???10???????????????????????????
		temp0Data.put("data",temp0List);
		temp1Data.put("data",temp1List );
		temp2Data.put("data",temp2List);
		temp3Data.put("data",temp3List);
		temp4Data.put("data",temp4List);
		temp5Data.put("data",temp5List);
		temp6Data.put("data",temp6List);
		temp7Data.put("data",temp7List);
		temp8Data.put("data",temp8List);
		temp9Data.put("data",temp9List);
		temp10Data.put("data",temp10List);
		temp11Data.put("data",temp11List );
		temp12Data.put("data",temp12List);
		temp13Data.put("data",temp13List);
		temp14Data.put("data",temp14List);
		temp15Data.put("data",temp15List);
		temp16Data.put("data",temp16List);
		temp17Data.put("data",temp17List);
		temp18Data.put("data",temp18List);
		temp19Data.put("data",temp19List);
		
		//???5???temp???????????? ??????y???????????? 
		yDataAll.add(temp0Data);
		yDataAll.add(temp1Data);
		yDataAll.add(temp2Data);
		yDataAll.add(temp3Data);
		yDataAll.add(temp4Data);
		yDataAll.add(temp5Data);
		yDataAll.add(temp6Data);
		yDataAll.add(temp7Data);
		yDataAll.add(temp8Data);
		yDataAll.add(temp9Data);
		yDataAll.add(temp10Data);
		yDataAll.add(temp11Data);
		yDataAll.add(temp12Data);
		yDataAll.add(temp13Data);
		yDataAll.add(temp14Data);
		yDataAll.add(temp15Data);
		yDataAll.add(temp16Data);
		yDataAll.add(temp17Data);
		yDataAll.add(temp18Data);
		yDataAll.add(temp19Data);
		
		//?????????????????????
		result.setCategories(xData);
		result.setData(yDataAll);
		return result;
	}

	/**
	 * ??????????????????
	 */
	@Override
	public EchartGranaryResult getEchartGranaryHumidityByProductId(Long productId, String key, Long length) {
		//????????????id
		if(productId==null){
			return null;
		}
		//??????key:??????id ???redis????????????
		List<String> list = jedisClient.lrange(key+":"+productId, 0, -1);
		
		EchartGranaryResult result=new EchartGranaryResult();
		//x?????????categories
		List xData=new ArrayList();
		
		//y?????????
		List yDataAll=new ArrayList();
		
		//????????????20????????????   [1555,132,101,134,90,230,90] 
		List humidity0List=new ArrayList();
		List humidity1List=new ArrayList();
		List humidity2List=new ArrayList();
		List humidity3List=new ArrayList();
		List humidity4List=new ArrayList();
		List humidity5List=new ArrayList();
		List humidity6List=new ArrayList();
		List humidity7List=new ArrayList();
		List humidity8List=new ArrayList();
		List humidity9List=new ArrayList();
		List humidity10List=new ArrayList();
		List humidity11List=new ArrayList();
		List humidity12List=new ArrayList();
		List humidity13List=new ArrayList();
		List humidity14List=new ArrayList();
		List humidity15List=new ArrayList();
		List humidity16List=new ArrayList();
		List humidity17List=new ArrayList();
		List humidity18List=new ArrayList();
		List humidity19List=new ArrayList();
		for (String string : list) {
			//??????????????????
			TbData tbData = JsonUtils.jsonToPojo(string, TbData.class);
			//????????????????????????Granary??????
			Granary20 granary=JsonUtils.jsonToPojo(tbData.getData(),Granary20.class);
			
			if(granary==null){
				continue;
			}
			
			//??????date?????? ???????????????????????????????????????
			Date created = tbData.getCreated();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm"); 
			String strDate=sdf.format(created);  
			
			//????????????????????????X??????
			if(strDate!=null){
				xData.add(strDate);
			}
			
			//????????????
			if(granary.getH0()!=null){
				humidity0List.add(granary.getH0().toString());
			}
			if(granary.getH1()!=null){
				humidity1List.add(granary.getH1().toString());
			}
			if(granary.getH2()!=null){
				humidity2List.add(granary.getH2().toString());
			}
			if(granary.getH3()!=null){
				humidity3List.add(granary.getH3().toString());
			}
			if(granary.getH4()!=null){
				humidity4List.add(granary.getH4().toString());
			}
			if(granary.getH5()!=null){
				humidity5List.add(granary.getH5().toString());
			}
			if(granary.getH6()!=null){
				humidity6List.add(granary.getH6().toString());
			}
			if(granary.getH7()!=null){
				humidity7List.add(granary.getH7().toString());
			}
			if(granary.getH8()!=null){
				humidity8List.add(granary.getH8().toString());
			}
			if(granary.getH9()!=null){
				humidity9List.add(granary.getH9().toString());
			}
			if(granary.getH10()!=null){
				humidity10List.add(granary.getH10().toString());
			}
			if(granary.getH11()!=null){
				humidity11List.add(granary.getH11().toString());
			}
			if(granary.getH12()!=null){
				humidity12List.add(granary.getH12().toString());
			}
			if(granary.getH13()!=null){
				humidity13List.add(granary.getH13().toString());
			}
			if(granary.getH14()!=null){
				humidity14List.add(granary.getH14().toString());
			}
			if(granary.getH15()!=null){
				humidity15List.add(granary.getH15().toString());
			}
			if(granary.getH16()!=null){
				humidity16List.add(granary.getH16().toString());
			}
			if(granary.getH17()!=null){
				humidity17List.add(granary.getH17().toString());
			}
			if(granary.getH18()!=null){
				humidity18List.add(granary.getH18().toString());
			}
			if(granary.getH19()!=null){
				humidity19List.add(granary.getH19().toString());
			}
		}
		//????????????y????????????
		Map humidity0Data=new HashMap();
		Map humidity1Data=new HashMap();
		Map humidity2Data=new HashMap();
		Map humidity3Data=new HashMap();
		Map humidity4Data=new HashMap();
		Map humidity5Data=new HashMap();
		Map humidity6Data=new HashMap();
		Map humidity7Data=new HashMap();
		Map humidity8Data=new HashMap();
		Map humidity9Data=new HashMap();
		Map humidity10Data=new HashMap();
		Map humidity11Data=new HashMap();
		Map humidity12Data=new HashMap();
		Map humidity13Data=new HashMap();
		Map humidity14Data=new HashMap();
		Map humidity15Data=new HashMap();
		Map humidity16Data=new HashMap();
		Map humidity17Data=new HashMap();
		Map humidity18Data=new HashMap();
		Map humidity19Data=new HashMap();
		
		//name??????
		humidity0Data.put("name", "H0");
		humidity1Data.put("name", "H1");
		humidity2Data.put("name", "H2");
		humidity3Data.put("name", "H3");
		humidity4Data.put("name", "H4");
		humidity5Data.put("name", "H5");
		humidity6Data.put("name", "H6");
		humidity7Data.put("name", "H7");
		humidity8Data.put("name", "H8");
		humidity9Data.put("name", "H9");
		humidity10Data.put("name", "H10");
		humidity11Data.put("name", "H11");
		humidity12Data.put("name", "H12");
		humidity13Data.put("name", "H13");
		humidity14Data.put("name", "H14");
		humidity15Data.put("name", "H15");
		humidity16Data.put("name", "H16");
		humidity17Data.put("name", "H17");
		humidity18Data.put("name", "H18");
		humidity19Data.put("name", "H19");
		
		//type ??????
		humidity0Data.put("type", ECHART_TEMP_TYPE);
		humidity1Data.put("type", ECHART_TEMP_TYPE);
		humidity2Data.put("type", ECHART_TEMP_TYPE);
		humidity3Data.put("type", ECHART_TEMP_TYPE);
		humidity4Data.put("type", ECHART_TEMP_TYPE);
		humidity5Data.put("type", ECHART_TEMP_TYPE);
		humidity6Data.put("type", ECHART_TEMP_TYPE);
		humidity7Data.put("type", ECHART_TEMP_TYPE);
		humidity8Data.put("type", ECHART_TEMP_TYPE);
		humidity9Data.put("type", ECHART_TEMP_TYPE);
		humidity10Data.put("type", ECHART_TEMP_TYPE);
		humidity11Data.put("type", ECHART_TEMP_TYPE);
		humidity12Data.put("type", ECHART_TEMP_TYPE);
		humidity13Data.put("type", ECHART_TEMP_TYPE);
		humidity14Data.put("type", ECHART_TEMP_TYPE);
		humidity15Data.put("type", ECHART_TEMP_TYPE);
		humidity16Data.put("type", ECHART_TEMP_TYPE);
		humidity17Data.put("type", ECHART_TEMP_TYPE);
		humidity18Data.put("type", ECHART_TEMP_TYPE);
		humidity19Data.put("type", ECHART_TEMP_TYPE);
		
		//STACK ??????  ??????????????????????????????????????????????????????stack?????????????????????????????????????????????????????????????????????
		//???????????????????????????????????????????????????stack:?????? ???????????????????????????
		/*temp1Data.put("stack", ECHART_TEMP_STACK);
		temp2Data.put("stack", ECHART_TEMP_STACK);
		temp3Data.put("stack", ECHART_TEMP_STACK);
		temp4Data.put("stack", ECHART_TEMP_STACK);
		temp5Data.put("stack", ECHART_TEMP_STACK);*/
		
		//data ?????? ,??????????????? ??????temp ???10???????????????????????????
		humidity0Data.put("data",humidity0List);
		humidity1Data.put("data",humidity1List );
		humidity2Data.put("data",humidity2List);
		humidity3Data.put("data",humidity3List);
		humidity4Data.put("data",humidity4List);
		humidity5Data.put("data",humidity5List);
		humidity6Data.put("data",humidity6List);
		humidity7Data.put("data",humidity7List);
		humidity8Data.put("data",humidity8List);
		humidity9Data.put("data",humidity9List);
		humidity10Data.put("data",humidity10List);
		humidity11Data.put("data",humidity11List );
		humidity12Data.put("data",humidity12List);
		humidity13Data.put("data",humidity13List);
		humidity14Data.put("data",humidity14List);
		humidity15Data.put("data",humidity15List);
		humidity16Data.put("data",humidity16List);
		humidity17Data.put("data",humidity17List);
		humidity18Data.put("data",humidity18List);
		humidity19Data.put("data",humidity19List);
		
		//???5???temp???????????? ??????y???????????? 
		yDataAll.add(humidity0Data);
		yDataAll.add(humidity1Data);
		yDataAll.add(humidity2Data);
		yDataAll.add(humidity3Data);
		yDataAll.add(humidity4Data);
		yDataAll.add(humidity5Data);
		yDataAll.add(humidity6Data);
		yDataAll.add(humidity7Data);
		yDataAll.add(humidity8Data);
		yDataAll.add(humidity9Data);
		yDataAll.add(humidity10Data);
		yDataAll.add(humidity11Data);
		yDataAll.add(humidity12Data);
		yDataAll.add(humidity13Data);
		yDataAll.add(humidity14Data);
		yDataAll.add(humidity15Data);
		yDataAll.add(humidity16Data);
		yDataAll.add(humidity17Data);
		yDataAll.add(humidity18Data);
		yDataAll.add(humidity19Data);
		
		//?????????????????????
		result.setCategories(xData);
		result.setData(yDataAll);
		return result;
	}

	/**
	 * ????????????????????? ????????????????????????  20?????????  ????????? 20?????????
	 */
	@Override
	public GranaryTables  getGranaryTablesByProductId(Long productId, String key, int length) {
		//????????????id
		if(productId==null){
			return null;
		}
		//??????key:??????id ???redis????????????
		List<String> list = jedisClient.lrange(key+":"+productId, 0, -1);//0 0 ?????????    0 1 ????????? ?????????  0 -1 ????????????
		
		//?????????  ??????
		DataTables granaryTemp=new DataTables();
		DataTables granaryHumidity=new DataTables();
		//?????????????????????
		GranaryTables granaryTables=new GranaryTables();
		List<DataTables>  granaryList=new ArrayList<DataTables>();
		if(list!=null&&list.size()>0){
			TbData tbData = JsonUtils.jsonToPojo(list.get(list.size()-1), TbData.class);
			//????????????????????????Granary??????
			Granary20 granary=JsonUtils.jsonToPojo(tbData.getData(),Granary20.class);
			
			//?????? ???????????????
			granaryTemp.setTitle("Temperature");
			granaryTemp.setT0(granary.getT0());
			granaryTemp.setT1(granary.getT1());
			granaryTemp.setT2(granary.getT2());
			granaryTemp.setT3(granary.getT3());
			granaryTemp.setT4(granary.getT4());
			granaryTemp.setT5(granary.getT5());
			granaryTemp.setT6(granary.getT6());
			granaryTemp.setT7(granary.getT7());
			granaryTemp.setT8(granary.getT8());
			granaryTemp.setT9(granary.getT9());
			granaryTemp.setT10(granary.getT10());
			granaryTemp.setT11(granary.getT11());
			granaryTemp.setT12(granary.getT12());
			granaryTemp.setT13(granary.getT13());
			granaryTemp.setT14(granary.getT14());
			granaryTemp.setT15(granary.getT15());
			granaryTemp.setT16(granary.getT16());
			granaryTemp.setT17(granary.getT17());
			granaryTemp.setT18(granary.getT18());
			granaryTemp.setT19(granary.getT19());
			//?????????????????????
			granaryHumidity.setTitle("Humidity");
			granaryHumidity.setT0(granary.getH0());
			granaryHumidity.setT1(granary.getH1());
			granaryHumidity.setT2(granary.getH2());
			granaryHumidity.setT3(granary.getH3());
			granaryHumidity.setT4(granary.getH4());
			granaryHumidity.setT5(granary.getH5());
			granaryHumidity.setT6(granary.getH6());
			granaryHumidity.setT7(granary.getH7());
			granaryHumidity.setT8(granary.getH8());
			granaryHumidity.setT9(granary.getH9());
			granaryHumidity.setT10(granary.getH10());
			granaryHumidity.setT11(granary.getH11());
			granaryHumidity.setT12(granary.getH12());
			granaryHumidity.setT13(granary.getH13());
			granaryHumidity.setT14(granary.getH14());
			granaryHumidity.setT15(granary.getH15());
			granaryHumidity.setT16(granary.getH16());
			granaryHumidity.setT17(granary.getH17());
			granaryHumidity.setT18(granary.getH18());
			granaryHumidity.setT19(granary.getH19());
		}
		
		//??????list 
		granaryList.add(granaryTemp);
		granaryList.add(granaryHumidity);
		
		//????????????
		granaryTables.setData(granaryList);
		return granaryTables;
	}

	/**
	 * ??????????????? ????????????  
	 */
	@Override
	public EchartSearchGranaryResult getEchartGranaryResultBetweenStartAndEnd(Long productId, String start,String end) {
		//????????????
		if(productId==null){
			return null;
		}
		if(StringUtils.isBlank(start)){
			return null;
		}
		if(StringUtils.isBlank(end)){
			return null;
		}
		//??????
	    List<TbData> DataList=tbDataMapper.selectDataByProductIdBetweenStartAndEnd(productId,start,end);
		
	    //?????????EchartTempResult
	    EchartSearchGranaryResult result=new EchartSearchGranaryResult();
  		//x?????????categories
  		List xData=new ArrayList();
  		
  		//y?????????
  		List yTemperature=new ArrayList();
  		List yhumidity=new ArrayList();
  		
  		//????????????20???????????????   [1555,132,101,134,90,230,90] 
  		List temp0List=new ArrayList();
		List temp1List=new ArrayList();
		List temp2List=new ArrayList();
		List temp3List=new ArrayList();
		List temp4List=new ArrayList();
		List temp5List=new ArrayList();
		List temp6List=new ArrayList();
		List temp7List=new ArrayList();
		List temp8List=new ArrayList();
		List temp9List=new ArrayList();
		List temp10List=new ArrayList();
		List temp11List=new ArrayList();
		List temp12List=new ArrayList();
		List temp13List=new ArrayList();
		List temp14List=new ArrayList();
		List temp15List=new ArrayList();
		List temp16List=new ArrayList();
		List temp17List=new ArrayList();
		List temp18List=new ArrayList();
		List temp19List=new ArrayList();
		//??????
  		List humidity0List=new ArrayList();
		List humidity1List=new ArrayList();
		List humidity2List=new ArrayList();
		List humidity3List=new ArrayList();
		List humidity4List=new ArrayList();
		List humidity5List=new ArrayList();
		List humidity6List=new ArrayList();
		List humidity7List=new ArrayList();
		List humidity8List=new ArrayList();
		List humidity9List=new ArrayList();
		List humidity10List=new ArrayList();
		List humidity11List=new ArrayList();
		List humidity12List=new ArrayList();
		List humidity13List=new ArrayList();
		List humidity14List=new ArrayList();
		List humidity15List=new ArrayList();
		List humidity16List=new ArrayList();
		List humidity17List=new ArrayList();
		List humidity18List=new ArrayList();
		List humidity19List=new ArrayList();
		
		
	    //?????????????????????
	    for (TbData tbData : DataList) {
	    	//????????????????????????Temp??????
			Granary20 granary=JsonUtils.jsonToPojo(tbData.getData(),Granary20.class);
			
			if(granary==null){
				continue;
			}
			//??????date?????? ???????????????????????????????????????
			Date created = tbData.getCreated();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm"); 
			String strDate=sdf.format(created);  
			
			//????????????????????????X??????
			if(strDate!=null){
				xData.add(strDate);
			}
			//????????????T
			if(granary.getT0()!=null){
				temp0List.add(granary.getT0().toString());
			}
			if(granary.getT1()!=null){
				temp1List.add(granary.getT1().toString());
			}
			if(granary.getT2()!=null){
				temp2List.add(granary.getT2().toString());
			}
			if(granary.getT3()!=null){
				temp3List.add(granary.getT3().toString());
			}
			if(granary.getT4()!=null){
				temp4List.add(granary.getT4().toString());
			}
			if(granary.getT5()!=null){
				temp5List.add(granary.getT5().toString());
			}
			if(granary.getT6()!=null){
				temp6List.add(granary.getT6().toString());
			}
			if(granary.getT7()!=null){
				temp7List.add(granary.getT7().toString());
			}
			if(granary.getT8()!=null){
				temp8List.add(granary.getT8().toString());
			}
			if(granary.getT9()!=null){
				temp9List.add(granary.getT9().toString());
			}
			if(granary.getT10()!=null){
				temp10List.add(granary.getT10().toString());
			}
			if(granary.getT11()!=null){
				temp11List.add(granary.getT11().toString());
			}
			if(granary.getT12()!=null){
				temp12List.add(granary.getT12().toString());
			}
			if(granary.getT13()!=null){
				temp13List.add(granary.getT13().toString());
			}
			if(granary.getT14()!=null){
				temp14List.add(granary.getT14().toString());
			}
			if(granary.getT15()!=null){
				temp15List.add(granary.getT15().toString());
			}
			if(granary.getT16()!=null){
				temp16List.add(granary.getT16().toString());
			}
			if(granary.getT17()!=null){
				temp17List.add(granary.getT17().toString());
			}
			if(granary.getT18()!=null){
				temp18List.add(granary.getT18().toString());
			}
			if(granary.getT19()!=null){
				temp19List.add(granary.getT19().toString());
			}
			
			//????????????
			if(granary.getH0()!=null){
				humidity0List.add(granary.getH0().toString());
			}
			if(granary.getH1()!=null){
				humidity1List.add(granary.getH1().toString());
			}
			if(granary.getH2()!=null){
				humidity2List.add(granary.getH2().toString());
			}
			if(granary.getH3()!=null){
				humidity3List.add(granary.getH3().toString());
			}
			if(granary.getH4()!=null){
				humidity4List.add(granary.getH4().toString());
			}
			if(granary.getH5()!=null){
				humidity5List.add(granary.getH5().toString());
			}
			if(granary.getH6()!=null){
				humidity6List.add(granary.getH6().toString());
			}
			if(granary.getH7()!=null){
				humidity7List.add(granary.getH7().toString());
			}
			if(granary.getH8()!=null){
				humidity8List.add(granary.getH8().toString());
			}
			if(granary.getH9()!=null){
				humidity9List.add(granary.getH9().toString());
			}
			if(granary.getH10()!=null){
				humidity10List.add(granary.getH10().toString());
			}
			if(granary.getH11()!=null){
				humidity11List.add(granary.getH11().toString());
			}
			if(granary.getH12()!=null){
				humidity12List.add(granary.getH12().toString());
			}
			if(granary.getH13()!=null){
				humidity13List.add(granary.getH13().toString());
			}
			if(granary.getH14()!=null){
				humidity14List.add(granary.getH14().toString());
			}
			if(granary.getH15()!=null){
				humidity15List.add(granary.getH15().toString());
			}
			if(granary.getH16()!=null){
				humidity16List.add(granary.getH16().toString());
			}
			if(granary.getH17()!=null){
				humidity17List.add(granary.getH17().toString());
			}
			if(granary.getH18()!=null){
				humidity18List.add(granary.getH18().toString());
			}
			if(granary.getH19()!=null){
				humidity19List.add(granary.getH19().toString());
			}
			
		}
	    //????????????T    y????????????
	    Map temp0Data=new HashMap();
		Map temp1Data=new HashMap();
		Map temp2Data=new HashMap();
		Map temp3Data=new HashMap();
		Map temp4Data=new HashMap();
		Map temp5Data=new HashMap();
		Map temp6Data=new HashMap();
		Map temp7Data=new HashMap();
		Map temp8Data=new HashMap();
		Map temp9Data=new HashMap();
		Map temp10Data=new HashMap();
		Map temp11Data=new HashMap();
		Map temp12Data=new HashMap();
		Map temp13Data=new HashMap();
		Map temp14Data=new HashMap();
		Map temp15Data=new HashMap();
		Map temp16Data=new HashMap();
		Map temp17Data=new HashMap();
		Map temp18Data=new HashMap();
		Map temp19Data=new HashMap();
		//????????????H   y????????????
  		Map humidity0Data=new HashMap();
  		Map humidity1Data=new HashMap();
  		Map humidity2Data=new HashMap();
  		Map humidity3Data=new HashMap();
  		Map humidity4Data=new HashMap();
  		Map humidity5Data=new HashMap();
  		Map humidity6Data=new HashMap();
  		Map humidity7Data=new HashMap();
  		Map humidity8Data=new HashMap();
  		Map humidity9Data=new HashMap();
  		Map humidity10Data=new HashMap();
  		Map humidity11Data=new HashMap();
  		Map humidity12Data=new HashMap();
  		Map humidity13Data=new HashMap();
  		Map humidity14Data=new HashMap();
  		Map humidity15Data=new HashMap();
  		Map humidity16Data=new HashMap();
  		Map humidity17Data=new HashMap();
  		Map humidity18Data=new HashMap();
  		Map humidity19Data=new HashMap();
		
  		//??????
  		//name??????
		temp0Data.put("name", "T0");
		temp1Data.put("name", "T1");
		temp2Data.put("name", "T2");
		temp3Data.put("name", "T3");
		temp4Data.put("name", "T4");
		temp5Data.put("name", "T5");
		temp6Data.put("name", "T6");
		temp7Data.put("name", "T7");
		temp8Data.put("name", "T8");
		temp9Data.put("name", "T9");
		temp10Data.put("name", "T10");
		temp11Data.put("name", "T11");
		temp12Data.put("name", "T12");
		temp13Data.put("name", "T13");
		temp14Data.put("name", "T14");
		temp15Data.put("name", "T15");
		temp16Data.put("name", "T16");
		temp17Data.put("name", "T17");
		temp18Data.put("name", "T18");
		temp19Data.put("name", "T19");
		//type ??????
		temp0Data.put("type", ECHART_TEMP_TYPE);
		temp1Data.put("type", ECHART_TEMP_TYPE);
		temp2Data.put("type", ECHART_TEMP_TYPE);
		temp3Data.put("type", ECHART_TEMP_TYPE);
		temp4Data.put("type", ECHART_TEMP_TYPE);
		temp5Data.put("type", ECHART_TEMP_TYPE);
		temp6Data.put("type", ECHART_TEMP_TYPE);
		temp7Data.put("type", ECHART_TEMP_TYPE);
		temp8Data.put("type", ECHART_TEMP_TYPE);
		temp9Data.put("type", ECHART_TEMP_TYPE);
		temp10Data.put("type", ECHART_TEMP_TYPE);
		temp11Data.put("type", ECHART_TEMP_TYPE);
		temp12Data.put("type", ECHART_TEMP_TYPE);
		temp13Data.put("type", ECHART_TEMP_TYPE);
		temp14Data.put("type", ECHART_TEMP_TYPE);
		temp15Data.put("type", ECHART_TEMP_TYPE);
		temp16Data.put("type", ECHART_TEMP_TYPE);
		temp17Data.put("type", ECHART_TEMP_TYPE);
		temp18Data.put("type", ECHART_TEMP_TYPE);
		temp19Data.put("type", ECHART_TEMP_TYPE);
		//data ?????? ,??????????????? ??????temp ???10???????????????????????????
		temp0Data.put("data",temp0List);
		temp1Data.put("data",temp1List );
		temp2Data.put("data",temp2List);
		temp3Data.put("data",temp3List);
		temp4Data.put("data",temp4List);
		temp5Data.put("data",temp5List);
		temp6Data.put("data",temp6List);
		temp7Data.put("data",temp7List);
		temp8Data.put("data",temp8List);
		temp9Data.put("data",temp9List);
		temp10Data.put("data",temp10List);
		temp11Data.put("data",temp11List );
		temp12Data.put("data",temp12List);
		temp13Data.put("data",temp13List);
		temp14Data.put("data",temp14List);
		temp15Data.put("data",temp15List);
		temp16Data.put("data",temp16List);
		temp17Data.put("data",temp17List);
		temp18Data.put("data",temp18List);
		temp19Data.put("data",temp19List);
		//???11???temp???????????? ??????y???????????? 
		yTemperature.add(temp0Data);
		yTemperature.add(temp1Data);
		yTemperature.add(temp2Data);
		yTemperature.add(temp3Data);
		yTemperature.add(temp4Data);
		yTemperature.add(temp5Data);
		yTemperature.add(temp6Data);
		yTemperature.add(temp7Data);
		yTemperature.add(temp8Data);
		yTemperature.add(temp9Data);
		yTemperature.add(temp10Data);
		yTemperature.add(temp11Data);
		yTemperature.add(temp12Data);
		yTemperature.add(temp13Data);
		yTemperature.add(temp14Data);
		yTemperature.add(temp15Data);
		yTemperature.add(temp16Data);
		yTemperature.add(temp17Data);
		yTemperature.add(temp18Data);
		yTemperature.add(temp19Data);
  		
  		
  		//??????
  		//name??????
		humidity0Data.put("name", "T0");
		humidity1Data.put("name", "T1");
		humidity2Data.put("name", "T2");
		humidity3Data.put("name", "T3");
		humidity4Data.put("name", "T4");
		humidity5Data.put("name", "T5");
		humidity6Data.put("name", "T6");
		humidity7Data.put("name", "T7");
		humidity8Data.put("name", "T8");
		humidity9Data.put("name", "T9");
		humidity10Data.put("name", "T10");
		humidity11Data.put("name", "T11");
		humidity12Data.put("name", "T12");
		humidity13Data.put("name", "T13");
		humidity14Data.put("name", "T14");
		humidity15Data.put("name", "T15");
		humidity16Data.put("name", "T16");
		humidity17Data.put("name", "T17");
		humidity18Data.put("name", "T18");
		humidity19Data.put("name", "T19");
		//type ??????
		humidity0Data.put("type", ECHART_TEMP_TYPE);
		humidity1Data.put("type", ECHART_TEMP_TYPE);
		humidity2Data.put("type", ECHART_TEMP_TYPE);
		humidity3Data.put("type", ECHART_TEMP_TYPE);
		humidity4Data.put("type", ECHART_TEMP_TYPE);
		humidity5Data.put("type", ECHART_TEMP_TYPE);
		humidity6Data.put("type", ECHART_TEMP_TYPE);
		humidity7Data.put("type", ECHART_TEMP_TYPE);
		humidity8Data.put("type", ECHART_TEMP_TYPE);
		humidity9Data.put("type", ECHART_TEMP_TYPE);
		humidity10Data.put("type", ECHART_TEMP_TYPE);
		humidity11Data.put("type", ECHART_TEMP_TYPE);
		humidity12Data.put("type", ECHART_TEMP_TYPE);
		humidity13Data.put("type", ECHART_TEMP_TYPE);
		humidity14Data.put("type", ECHART_TEMP_TYPE);
		humidity15Data.put("type", ECHART_TEMP_TYPE);
		humidity16Data.put("type", ECHART_TEMP_TYPE);
		humidity17Data.put("type", ECHART_TEMP_TYPE);
		humidity18Data.put("type", ECHART_TEMP_TYPE);
		humidity19Data.put("type", ECHART_TEMP_TYPE);
		//data ??????
		humidity0Data.put("data",humidity0List);
		humidity1Data.put("data",humidity1List );
		humidity2Data.put("data",humidity2List);
		humidity3Data.put("data",humidity3List);
		humidity4Data.put("data",humidity4List);
		humidity5Data.put("data",humidity5List);
		humidity6Data.put("data",humidity6List);
		humidity7Data.put("data",humidity7List);
		humidity8Data.put("data",humidity8List);
		humidity9Data.put("data",humidity9List);
		humidity10Data.put("data",humidity10List);
		humidity11Data.put("data",humidity11List );
		humidity12Data.put("data",humidity12List);
		humidity13Data.put("data",humidity13List);
		humidity14Data.put("data",humidity14List);
		humidity15Data.put("data",humidity15List);
		humidity16Data.put("data",humidity16List);
		humidity17Data.put("data",humidity17List);
		humidity18Data.put("data",humidity18List);
		humidity19Data.put("data",humidity19List);
		//???20??????????????? ??????y???????????? 
		yhumidity.add(humidity0Data);
		yhumidity.add(humidity1Data);
		yhumidity.add(humidity2Data);
		yhumidity.add(humidity3Data);
		yhumidity.add(humidity4Data);
		yhumidity.add(humidity5Data);
		yhumidity.add(humidity6Data);
		yhumidity.add(humidity7Data);
		yhumidity.add(humidity8Data);
		yhumidity.add(humidity9Data);
		yhumidity.add(humidity10Data);
		yhumidity.add(humidity11Data);
		yhumidity.add(humidity12Data);
		yhumidity.add(humidity13Data);
		yhumidity.add(humidity14Data);
		yhumidity.add(humidity15Data);
		yhumidity.add(humidity16Data);
		yhumidity.add(humidity17Data);
		yhumidity.add(humidity18Data);
		yhumidity.add(humidity19Data);
		
		//?????????????????????
		result.setCategories(xData);
		result.setTemperatureData(yTemperature);
		result.setHumidityData(yhumidity);
		return result;
	}

	
}
