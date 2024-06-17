package com.dbt.platform.question.service;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dbt.framework.pagination.PageOrderInfo;
import com.dbt.framework.util.ReadExcel;
import com.dbt.platform.question.bean.VpsQuestionnaireOrder;
import com.dbt.platform.question.dao.IVcodeQuestionOrderDao;

/**
 * @description 
 */
@Service
public class QuestionOrderService{

	@Autowired
	private IVcodeQuestionOrderDao questionOrderDao;
	private final static Log log = LogFactory.getLog(QuestionOrderService.class);


	public List<VpsQuestionnaireOrder> queryQuestionOrderList(VpsQuestionnaireOrder queryBean, PageOrderInfo pageInfo) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("queryBean", queryBean);
		map.put("pageInfo", pageInfo);
		return questionOrderDao.queryForLst(map);
	}

	public int countQuestionOrderList(VpsQuestionnaireOrder queryBean) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("queryBean", queryBean);
		return questionOrderDao.queryForCount(map);
	}
	
	
	/**
     * excel解析
     * @param batchFile
     * @return
     * @throws IOException
     */
    public List<String> checkFile(MultipartFile batchFile) throws IOException{
        String importfileFileName = batchFile.getOriginalFilename();
        InputStream input = batchFile.getInputStream();
        String content="";
        if(input.available()>0){
            List<String> list=new ArrayList<String>();
            List<List<Object>> readExcel = ReadExcel.readExcel(input, importfileFileName.substring(importfileFileName.lastIndexOf(".") + 1));
            for (int i = 0; i < readExcel.size(); i++) {
                Object cell = readExcel.get(i).get(0);
                if(cell!=null){
                    content=cell.toString();
                }
                list.add(content);
            }
            return list;
        }
        return null;
    }

	public void createBatch(List<String> list) {
		questionOrderDao.createBatch(list);	
	}
	
}
