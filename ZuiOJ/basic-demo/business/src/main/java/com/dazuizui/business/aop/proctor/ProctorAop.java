package com.dazuizui.business.aop.proctor;

import com.dazuizui.business.domain.bo.AddProctorBo;
import com.dazuizui.business.domain.bo.AdminGetProctorsByPaginBo;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
public interface ProctorAop {
    /**
     * 添加一个面试官Aop 前置切面，主要负责了鉴别是否为管理员
     * @param joinpoint
     * @return
     */
    public void addProctor(JoinPoint joinpoint) throws Exception;

    /**
     * 监考人员获取未来进行时候和现在进行时的差事
     * @param joinpoint
     * @throws Exception
     */
    public void proctorGetFutureEventsInfo(JoinPoint joinpoint) throws Exception;


    /**
     * 监考人员获取未来进行时候和现在进行时的差事
     * @param joinpoint
     * @throws Exception
     */
    public void proctorGetLastEventsInfo(JoinPoint joinpoint) throws Exception;

    /**
     * 监考人员分页获取参赛人员
     * @param joinpoint
     * @return
     * @throws Exception
     */
    public void paglingQueryContestantsInThisContest(JoinPoint joinpoint) throws Exception;

    /**
     * 筛选查询比赛提交保存的代码 主要做了监考身份的鉴权
     * @param joinpoint
     * @return
     */
    public String proctorFilterQueryMatchSaveCode(JoinPoint joinpoint) throws Exception;

    /**
     * 通过id获取问题
     * @param joinpoint
     * @return
     * @throws Exception
     */
    public String getQuestionById(JoinPoint joinpoint) throws Exception;

    /**
     * 管理员分页获取监考人员数据
     * @param
     * @return
     */
    public String adminGetProctorsByPagin(JoinPoint joinpoint) throws Exception;

    /**
     * 管理员删除监考人员通过监考人员Id
     * @param
     * @return
     */
    public String adminDeleteProctorByIdOfProctor(JoinPoint joinpoint) throws Exception;

    /**
     * 监考人员通过id查询代码详细信息
     *  检查监考人员是否为该比赛的监考人员
     */
    public String findOneCodeDetailedById(JoinPoint joinpoint) throws Exception;
}
