package com.dazuizui.business.mapper;

import com.dazuizui.basicapi.entry.AcContestQuestion;
import com.dazuizui.business.domain.bo.ElementOfQueryLogBo;
import com.dazuizui.business.domain.bo.QueryContestSubmissionLogBo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * AC比赛题目实体类
 */
@Mapper
public interface AcContestQuestionMapper {
    /**
     * 通过比赛id和问题id查询指定日志
     * @param elementOfQueryLogBo
     * @return
     */
    public List<AcContestQuestion> queryLogByContestIdAndQuestionId(ElementOfQueryLogBo elementOfQueryLogBo);
    /**
     * 通过比赛id和问题id查询指定日志数量
     * @param elementOfQueryLogBo
     * @return
     */
    public Long queryCountByElement(ElementOfQueryLogBo elementOfQueryLogBo);

    /**
     * 获取当前比赛指定比赛的通过记录
     * @param contestId   比赛id
     * @param questionId  问题id
     */
    public Long queryCountByContestIdAndQuestionId(@Param("contestId")Long contestId,@Param("questionId") Long questionId);

    /**
     * 修改提交记录通过id
     * @param acContestQuestion
     * @return
     */
    public Long updateAcContestQuestionById(AcContestQuestion acContestQuestion);

    /**
     * 通过元素查询
     * @param elementOfQueryLogBo
     * @return
     */
    public List<AcContestQuestion> queryLogByElement(ElementOfQueryLogBo elementOfQueryLogBo);

    /**
     * 删除此用户的提交记录通过Id
     * @param id
     * @return
     */
    public Long deleteLogById(@Param("id")Long id);

    /**
     * todo 因为赶工期，暂不做优化，等主要功能忙完在回来优化
     * @param id
     * @return
     */
    @Select("select count(id) from ac_contest_question where contest_id = #{id}")
    public Long queryCount(@Param("id")Long id);

    /**
     * 移除本次比赛所有提交记录
     * @param id
     * @return
     */
    public Long removeAllContestSubmissionLogbyContestId(@Param("id")Long id);

    /**
     * 查看本题提交日志
     * @return
     */
    public List<AcContestQuestion> queryContestSubmissionLog(QueryContestSubmissionLogBo queryContestSubmissionLogBo);

    /**
     * 通过比赛题目
     * @param acContestQuestion
     * @return
     */
    public Long acTheQuestionInDB(@Param("acContestQuestion") AcContestQuestion acContestQuestion,@Param("status") int status);

    /**
     * 增加提交量
     * @param acContestQuestion
     * @return
     */
    public Long recordSubmissions(@Param("acContestQuestion") AcContestQuestion acContestQuestion,@Param("status") int status);


    /**
     * 检查提交是否存在
     * @param acContestQuestion
     * @return
     */
    public AcContestQuestion checkSubmissions(AcContestQuestion acContestQuestion);

    /**
     * 通过id查询指定提交信息
     * @param id
     * @return
     */
    public AcContestQuestion findOneById(@Param("id")Long id);

    /**
     * 删除此题通关记录通过quesiton Id list
     */
    public Long deleteAcContestQuestionByQuestionIdList(@Param("list")List list);



}
