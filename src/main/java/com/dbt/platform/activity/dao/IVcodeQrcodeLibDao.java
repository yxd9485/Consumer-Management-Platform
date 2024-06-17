package com.dbt.platform.activity.dao;

import com.dbt.framework.base.dao.IBaseDao;
import com.dbt.platform.activity.bean.VpsVcodeQrcodeLib;
import com.dbt.platform.autocode.dto.VpsQrcodeLibNameSaveDTO;

import java.util.List;
import java.util.Map;

/**
 * 码库
 * @author:Jiquanwei<br>
 * @date:2016-4-22 下午01:40:05<br>
 * @version:1.0.0<br>
 * 
 */
public interface IVcodeQrcodeLibDao extends IBaseDao<VpsVcodeQrcodeLib>{
	
	/**
	 * 根据二维码内容和表名查询码库表对象
	 * @param map
	 * @return
	 */
	public VpsVcodeQrcodeLib queryVcodeQrcodeLibByQrcodeContent(Map<String,Object> map);
	/**
	 * 更新二维码的扫码状态
	 */
	public void modifyQrcodeLibUseStatus(Map<String,Object> map);
	
	/**
	 * <pre>
	 * the queryQrcodeLibByVcodeKeyAndUserKey method for
	 * 根据用户和码号查询扫码记录
	 * </pre>
	 *
	 * @param libName(库表),vcodeKey(码号),userKey(用户主键)
	 * @return
	 * @author zhangtao 2016年5月9日
	 */
	public VpsVcodeQrcodeLib queryQrcodeLibByVcodeKeyAndUserKey(Map<String,Object> map);
	/**
	 * 更新码库表中对应活动key
	 * @param map</br> 
	 * @return void </br>
	 */
	public void updateVcodeQrcode(Map<String, Object> map);
	
	/**
	 * 该表是否存在
	 * @param </br> 
	 * @return String </br>
	 */
	public String isExistsTableName(String libName);
	/**
	 * 批量新增二维码
	 * @param </br> 
	 * @return void </br>
	 */
	public void batchInsertQrcode(Map<String, Object> paramsMap);

	/**
	 * 批量新增二维码 replace
	 * 主键不同，唯一键相同，替换；
	 * 主键相同，唯一键不同，替换；
	 * 主键和唯一键均相同，替换；
	 * 主键和唯一键均不同，新增
	 * @return void
	 */
	public void batchReplaceInsertQrcode(Map<String, Object> paramsMap);
	/**
	 * 批量新增二维码
	 * @param </br> 
	 * @return void </br>
	 */
	public void batchInsertQrcodeAutocodeNo(Map<String, Object> paramsMap);
	
	/**
	 * 新增二维码
	 * @param </br> 
	 * @return void </br>
	 */
	public void insertQrcode(Map<String, Object> paramsMap);
	
	/**
	 * 查询成功导入的二维码数量
	 * @param map
	 */
	public int querySuccessImportQrcodeCount(Map<String, Object> map);
	
	/**
	 * 创建码库表
	 * @param map
	 */
	public void createQrcodeTable(Map<String, Object> map);

	/**
	 * 清空码库表
	 * @param map
	 */
	public void truncateQrcodeTable(Map<String, Object> map);
	/**
	 * 二维码入库
	 * @param map
	 */
	public void writeQrcodeToData(Map<String, Object> map);
	
	/**
	 * 查询码源数量
	 * @param map：libNameList
	 * @return
	 */
	public int findQrcodeCountByLibNames(Map<String, Object> map);
	
	/**
	 * 删除码库表记录
	 * @param map
	 */
	public void deleteByLibNameList(Map<String, Object> map);
	
	/**
	 * 重命名码库表
	 * @param map
	 */
	public void renameTable(Map<String, Object> map);
	
	/**
	 * 查询表是否存在
	 * @param map:libName
	 * @return
	 */
	public int findTableIsExist(Map<String, Object> map);
	
	/**
	 * 批量更新二维码使用完成状态
	 * @param map
	 */
	public void batchUpdateQrcodeIsFinish(Map<String, Object> map);
	
	/**
	 * 查找已存在的码源
	 * 
	 * @param map
	 * @return
	 */
	public List<String> queryQrcode(Map<String, Object> map);
	/**
	 * 查找码库中所有码源
	 * 
	 * @param map
	 * @return
	 */
	public List<String> queryAllQrcode(Map<String, Object> map);
	
	/**
	 * 查找已存在的批次序号
	 * 
	 * @param map
	 * @return
	 */
	public List<VpsVcodeQrcodeLib> queryQrcodeByBatchAutocodeNo(Map<String, Object> map);
	
	/**
	 * 查找批次序号分批次码量
	 * 
	 * @param map
	 * @return
	 */
	public List<VpsVcodeQrcodeLib> queryQrcodeByBatchAutocodeNoGroup(Map<String, Object> map);
	/**
	 * 根据码库表名称，条件为已扫过，时间倒叙，取第一条记录
	 * @param map
	 * @return
	 */
	public VpsVcodeQrcodeLib queryVcodeQrcodeLibByIsFinishAndTimeDescAndFirst(Map<String,Object> map);
	/**
	 * 二维码补录新增
	 * @param vpsQrcodeLibNameSaveDTO
	 * @return int
	 */
	public int insertVpsQrcodeLib(VpsQrcodeLibNameSaveDTO vpsQrcodeLibNameSaveDTO);

    VpsVcodeQrcodeLib queryQrcodeLibByCol5(Map<String, Object> map);
    
    /**
     * 更新批次序号
     * 
     * @param libName 码库表名
     */
    public void updateBatchAutocodeNo(String libName);

	/**
	 * 更新外码
	 * @param vpsVcodeQrcodeLib
	 */
	public void updateCode(VpsVcodeQrcodeLib vpsVcodeQrcodeLib);
}
