package com.dazuizui.business.service.onlineJudge.impl;

import com.alibaba.fastjson2.JSONArray;
import com.dazuizui.basicapi.entry.AcContestQuestion;
import com.dazuizui.basicapi.entry.StatusCode;
import com.dazuizui.basicapi.entry.StatusCodeMessage;
import com.dazuizui.basicapi.entry.vo.ResponseVo;
import com.dazuizui.business.domain.bo.ElementOfQueryLogBo;
import com.dazuizui.business.domain.bo.QueryContestSubmissionLogBo;
import com.dazuizui.business.domain.bo.QueryCountByContestIdAndQuestionIdBo;
import com.dazuizui.business.domain.vo.QueryContestSubmissionLogVo;
import com.dazuizui.business.domain.vo.QueryLogByContestIdAndQuestionIdVo;
import com.dazuizui.business.domain.vo.QueryLogByElementVo;
import com.dazuizui.business.mapper.AcContestQuestionMapper;
import com.dazuizui.business.service.onlineJudge.AcContestQuestionSerivce;
import com.dazuizui.business.service.onlineJudge.SubmmitionCodeInContestSerivce;
import com.dazuizui.business.util.ThreadLocalUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;

/**
 * 比赛题目提交业务层操作
 */
@Service
public class AcContestQuestionSerivceImpl implements AcContestQuestionSerivce {
    @Autowired
    private AcContestQuestionMapper acContestQuestionMapper;
    @Autowired
    private SubmmitionCodeInContestSerivce submmitionCodeInContestSerivce;

    /**
     * @author Bryan Yang(Dazui) 06/08/2022 22:00:00 PM
     *
     * 通过比赛id还有问题id查询提交日志
     * query log by contestId and questionId
     *
     * @param elementOfQueryLogBo 查询元素实体
     * @return
     */
    @Override
    public String queryLogByContestIdAndQuestionId(ElementOfQueryLogBo elementOfQueryLogBo){
        List<AcContestQuestion> acContestQuestions = acContestQuestionMapper.queryLogByContestIdAndQuestionId(elementOfQueryLogBo);
        //Long count = acContestQuestionMapper.queryCountByContestIdAndQuestionId(elementOfQueryLogBo);
        QueryLogByContestIdAndQuestionIdVo queryLogByContestIdAndQuestionIdVo = new QueryLogByContestIdAndQuestionIdVo();
        //queryLogByContestIdAndQuestionIdVo.setCount(count);
        queryLogByContestIdAndQuestionIdVo.setAcContestQuestions(acContestQuestions);

        return JSONArray.toJSONString(new ResponseVo<>(StatusCodeMessage.OK,queryLogByContestIdAndQuestionIdVo, StatusCode.OK));
    }

    /**
     * 通过QuestionId和ContestId查询提交数据
     * @param elementOfQueryLogBo
     * @return
     */
    @Override
    public String queryCountByContestIdAndQuestionId(ElementOfQueryLogBo elementOfQueryLogBo){
        Long count = acContestQuestionMapper.queryCountByContestIdAndQuestionId(elementOfQueryLogBo.getContestId(),elementOfQueryLogBo.getQuestionId());
        List<AcContestQuestion> acContestQuestions = acContestQuestionMapper.queryLogByElement(elementOfQueryLogBo);
        QueryLogByElementVo queryLogByElementVo = new QueryLogByElementVo();
        queryLogByElementVo.setAcContestQuestions(acContestQuestions);
        queryLogByElementVo.setCount(count);

        return JSONArray.toJSONString(new ResponseVo<>(StatusCodeMessage.OK,queryLogByElementVo, StatusCode.OK));
    }

    /**
     * @author Bryan Yang(Dazui) 07/08/2022 22:00:00 PM
     *
     * 查看提交日志
     * query commition log
     *
     * 业务层
     *    通过比赛id和用户名
     *
     * Business
     *    query log by contestId and username
     *
     * @param elementOfQueryLogBo 查询元素实体
     * @return String
     */
    @Override
    public String   queryLogByElement(ElementOfQueryLogBo elementOfQueryLogBo) {
        List<AcContestQuestion> acContestQuestions = acContestQuestionMapper.queryLogByElement(elementOfQueryLogBo);
        Long count =  acContestQuestionMapper.queryCountByElement(elementOfQueryLogBo);
        QueryLogByElementVo queryLogByElementVo = new QueryLogByElementVo();
        queryLogByElementVo.setAcContestQuestions(acContestQuestions);
        queryLogByElementVo.setCount(count);

        return JSONArray.toJSONString(new ResponseVo<>(StatusCodeMessage.OK,queryLogByElementVo, StatusCode.OK));
    }

    /**
     * 删除此用户的提交记录通过Id
     * @param id
     * @return
     */
    @Override
    public String deleteLogById(@Param("id")Long id){
        //删除数据
        Long aLong = acContestQuestionMapper.deleteLogById(id);
        //删除现有提交数量
        if (aLong == 0){
            return JSONArray.toJSONString(new ResponseVo<>(StatusCodeMessage.Error,null, StatusCode.Error));
        }
        return JSONArray.toJSONString(new ResponseVo<>(StatusCodeMessage.OK,null, StatusCode.OK));
    }

    /**
     * @author Bryan Yang(Dazui) 07/08/2022 22:00:00 PM
     *
     * 查看本体提交日志
     * query commition log
     *
     * 业务层
     *    通过比赛id查询提交日志
     *
     * Business
     *    Query submission logs through competition ID
     *
     * @param queryContestSubmissionLogBo 查询元素实体
     * @return String
     */
    public String queryContestSubmissionLog(QueryContestSubmissionLogBo queryContestSubmissionLogBo){
        //查询分页数据
        List<AcContestQuestion> acContestQuestions = acContestQuestionMapper.queryContestSubmissionLog(queryContestSubmissionLogBo);
        //查询个数
        //todo 赶工期，暂不优化，等主要功能开发完毕回来优化
        Long count = acContestQuestionMapper.queryCount(queryContestSubmissionLogBo.getContestId());
        //封装
        QueryContestSubmissionLogVo queryContestSubmissionLogVo = new QueryContestSubmissionLogVo();
        queryContestSubmissionLogVo.setCount(count);
        queryContestSubmissionLogVo.setAcContestQuestions(acContestQuestions);

        return JSONArray.toJSONString(new ResponseVo<>(StatusCodeMessage.OK,queryContestSubmissionLogVo, StatusCode.OK));
    }

    /**
     *
     * 移除当前比赛提交数据
     * @param id
     * @return
     */
    @Override
    public String removeAllContestSubmissionLogbyContestId(Long id) {
        //删除提交日志
        Long aLong = acContestQuestionMapper.removeAllContestSubmissionLogbyContestId(id);
        //删除代码存储
        submmitionCodeInContestSerivce.deleteByContestId(id);

        return JSONArray.toJSONString(new ResponseVo<>(StatusCodeMessage.OK,null, StatusCode.OK));
    }

    /**
     * 提交答案
     * @param acContestQuestion
     * @param status    代码判决状态
     */
    @Override
    public Long submitAnswer(AcContestQuestion acContestQuestion,String status) {
        //在比赛信息标记通过
        AcContestQuestion acContestQuestionInDB = acContestQuestionMapper.checkSubmissions(acContestQuestion);
        acContestQuestion.setCreateByName((String) ThreadLocalUtil.mapThreadLocalOfJWT.get().get("userinfo").get("name"));
        String idstring = (String) ThreadLocalUtil.mapThreadLocalOfJWT.get().get("userinfo").get("id");
        acContestQuestion.setCreateById(Long.valueOf(idstring));
        acContestQuestion.setCreateTime(new Date());

        /**
         * 如果没有提交记录就创建提交记录
         * 如果有提交记录，就当前比赛的题目的提交次数++
         * 第一次提交就ac的情况
         */
        if (acContestQuestionInDB == null){

            if (status.equals("Accepted")){
                acContestQuestion.setFirstAc(new Date());
                acContestQuestion.setNumberOfAttempts(0);
                acContestQuestionMapper.acTheQuestionInDB(acContestQuestion,1);
            }else{
                acContestQuestion.setNumberOfAttempts(1);
                acContestQuestionMapper.acTheQuestionInDB(acContestQuestion,0);
            }
            return acContestQuestion.getId();
        }else{
            /**
             * acContestQuestionInDB.getStatus() == 1  代表已经AC
             * acContestQuestionInDB.getStatus() == 0  还未AC
             * 此if条件是罚时
             * 如果已经AC但是没有通过，或者没有ac也没有通过就增加错误次数
             */
            if (acContestQuestionInDB.getStatus() == 1 && !status.equals("Accepted")){
                acContestQuestion.setFirstAc(acContestQuestionInDB.getFirstAc() == null ? null : acContestQuestionInDB.getFirstAc());
                acContestQuestion.setNumberOfAttempts(acContestQuestionInDB.getNumberOfAttempts()+1);
                acContestQuestionMapper.recordSubmissions(acContestQuestion,1);
            }
            //
            else if (acContestQuestionInDB.getStatus() == 0 && !status.equals("Accepted")) {
                acContestQuestion.setFirstAc(acContestQuestionInDB.getFirstAc() == null ? null : acContestQuestionInDB.getFirstAc());
                acContestQuestion.setNumberOfAttempts(acContestQuestionInDB.getNumberOfAttempts()+1);
                acContestQuestionMapper.recordSubmissions(acContestQuestion,0);
            }
            /**
             * 如果是首次通过
             */
            else if (status.equals("Accepted") && acContestQuestionInDB.getStatus() == 0){
                acContestQuestion.setFirstAc(new Date());
                acContestQuestion.setNumberOfAttempts(acContestQuestionInDB.getNumberOfAttempts());
                acContestQuestionMapper.recordSubmissions(acContestQuestion,1);
            }
            return acContestQuestionInDB.getId();
        }
    }
}
