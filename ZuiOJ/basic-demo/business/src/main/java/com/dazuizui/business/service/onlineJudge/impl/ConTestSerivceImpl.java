package com.dazuizui.business.service.onlineJudge.impl;

import com.alibaba.fastjson2.JSONArray;
import com.dazuizui.basicapi.entry.Contest;
import com.dazuizui.basicapi.entry.vo.ResponseVo;
import com.dazuizui.business.mapper.ConTestMapper;
import com.dazuizui.business.service.onlineJudge.ConTestSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author yida yang
 * 竞赛模块接口实现类
 */
@Service
public class ConTestSerivceImpl implements ConTestSerivce {

    @Autowired
    private ConTestMapper conTestMapper;

    /**
     * 创建比赛
     * @param conTest
     * @return
     */
    @Override
    @Transactional
    public String postContest(Contest conTest) {
        System.out.println(conTest);
        long l = conTestMapper.insertConTest(conTest);
        if (l == 0){
            //todo error
        }
        l = conTestMapper.insertConTestDetailed(conTest);
        if (l == 0){
            //todo error
        }

        return JSONArray.toJSONString(new ResponseVo<>("创建比赛成功",null,"0x1001"));
    }
}
