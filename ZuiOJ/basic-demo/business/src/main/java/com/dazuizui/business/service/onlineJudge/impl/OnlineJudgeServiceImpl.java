package com.dazuizui.business.service.onlineJudge.impl;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson2.JSONArray;
import com.dazuizui.basicapi.entry.*;
import com.dazuizui.basicapi.entry.bo.ProgramBo;
import com.dazuizui.business.domain.CodeInContest;
import com.dazuizui.business.domain.RedisKey;
import com.dazuizui.business.mapper.ContestMapper;
import com.dazuizui.business.mapper.LanguageCommandMapper;
import com.dazuizui.business.mapper.ProblemLimitMapper;
import com.dazuizui.business.mapper.QuestionCaseMapper;
import com.dazuizui.business.messageQueue.cofnig.MessageSource;
import com.dazuizui.business.service.onlineJudge.AcContestQuestionSerivce;
import com.dazuizui.business.service.onlineJudge.OnlineJudgeService;
import com.dazuizui.business.service.onlineJudge.ProblemLimitService;
import com.dazuizui.business.util.HttpUtil;
import com.dazuizui.business.util.RedisUtil;
import com.dazuizui.business.util.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class OnlineJudgeServiceImpl implements OnlineJudgeService {
    @Autowired
    private LanguageCommandMapper languageCommandMapper;
    @Autowired
    private ProblemLimitMapper problemLimitMapper;
    @Autowired
    private QuestionCaseMapper questionCaseMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private AcContestQuestionSerivce acContestQuestionSerivce;
    @Autowired
    private ProblemLimitService problemLimitService;
    //消息队列
    @Autowired
    private MessageSource source;
    /**
     * 判决代码
     * 不要动 危险
     *                              -- 杨易达 Bryan yang
     * @param programBo
     * @return
     */
    @Override
    @Transactional
    public String judgeTheProgram(ProgramBo programBo){
        //封装代码记录
        CodeInContest codeInContest = new CodeInContest();
        //获取操作人id
        String JWTStringID = (String) ThreadLocalUtil.mapThreadLocalOfJWT.get().get("userinfo").get("id");
        Long id = Long.valueOf(JWTStringID);

        /**
         * 初始化代码运行
         */
        HashMap<Integer, List<String>> map = InitializerData.langSystem.get(programBo.getLanguageId());
        programBo.setEnv(Arrays.asList("PATH=/usr/bin:/bin"));
        programBo.setParseCodeArgs(map.get(new Integer(0)));
        programBo.setCopyOutCached(map.get(new Integer(2)));
        programBo.setRunCommandArgs(map.get(new Integer(1)));

        /**
         * 初始化题目限制
         */
        ProblemLimit problemLimit = problemLimitService.getProblemLimitById(programBo.getTopicId());
        programBo.setProblemLimit(problemLimit);

        /**
         * 获取案例he todo 改成mongodb
         */
        List<QuestionCase> questionCases = questionCaseMapper.queryTheQuestionCasesByQuestionId(programBo.getTopicId());
        /*
        淘汰方案
        List<QuestionCase> questionCases = redisUtil.getListInRedis(RedisKey.ZuiOJQuestionCase +programBo.getTopicId());
        System.out.println(questionCases.size());
        if (questionCases.size() == 0){
            questionCases = questionCaseMapper.queryTheQuestionCasesByQuestionId(programBo.getTopicId());

            redisUtil.putListInRedis(RedisKey.ZuiOJQuestionCase +programBo.getTopicId(),60*60*24*15,questionCases);
            //System.out.println("in db");
        }
        */

        //封装请求
        JSONObject request = new JSONObject();

        for (QuestionCase questionCase : questionCases) {
            programBo.setInput(questionCase.getInputs());
            //发起请求
            request = HttpUtil.request(programBo);

            if (!request.get("status").equals("Accepted")) {
               break;
            }

            //判断答案是否正确
            JSONObject jsonObject1 = new JSONObject(request.get("files"));
            String stdout = jsonObject1.get("stdout").toString() ;
            stdout = stdout.replace("\n","\\n").trim();
            questionCase.setAnswer( questionCase.getAnswer().replace("\n","\\n").trim());

            /**
             * 查看stdout最后两位是否为\n 如果是\n则忽略
             *      此处我想不到更好的业务解决方案了，如果后续有人有更好的解决方案请联系我通过email
             *      y51288033@outlook.com
             *      y51288033@gmail.com
             *   出现的问题是 如果我们正确答案是Hello World
             *   但是我们某些语言比如使用println， 打印出来的结果是Hello World\n 我们也想让他通过
             *   所以采用此处优化
             */
            //System.out.println(stdout.substring(stdout.length() - 2).equals("\\n"));
            if (stdout.length() >= 2 && stdout.substring(stdout.length()-2).equals("\\n")){
                stdout = stdout.substring(0,stdout.length()-2);
            }

            System.out.println(questionCase.getAnswer()+" and "+stdout);
            if (!stdout.trim().equals(questionCase.getAnswer())) {
                request.set("status","Answer error");
                break;
            }
        }


        //如果通过并且是比赛类型的题目
        if (programBo.getQuestionType() != 1 ) {
            //查看是否为比赛题目
            AcContestQuestion acContestQuestion = new AcContestQuestion();
            acContestQuestion.setContestId(programBo.getContestId());
            acContestQuestion.setUserId(id);
            acContestQuestion.setQuestionId(programBo.getTopicId());

            //查看当前啊比赛是否结束
            if (checkIfTheCurrentGameIsOver(acContestQuestion.getContestId())){
                //在比赛中标记记
                Long acContestQuestionId = acContestQuestionSerivce.submitAnswer(acContestQuestion, (String) request.get("status"));
                //封装竞赛id
                codeInContest.setContestId(programBo.getContestId());

                //获取状态
                String status = (String) request.get("status");
                //封装判决状态
                if (status.equals("File Error")){
                    codeInContest.setStatus(1);
                }else if (status.equals("Nonzero Exit Status")){
                    codeInContest.setStatus(2);
                }else if (status.equals("Answer error")){
                    codeInContest.setStatus(3);
                }else if (status.equals("Accepted")){
                    codeInContest.setStatus(0);
                }else if (status.equals("Time Limit Exceeded")){
                    codeInContest.setStatus(5);
                }else{
                    codeInContest.setStatus(6);
                }
                //绑定删除状态
                codeInContest.setDelFlag(0);
                //绑定通过几率id
                codeInContest.setAcContestQuestionId(acContestQuestionId);
                //封装判决代码
                codeInContest.setCode(programBo.getCode());
                //封装题目id
                codeInContest.setQuestionId(programBo.getTopicId());
                //封装通过者和创建人和创建时间
                codeInContest.setCreateBy(id);
                codeInContest.setUserId(id);
                codeInContest.setCreateTime(new Date());
                //放入消息队列
                source.addContestSubmittionCodeOutput().send(MessageBuilder.withPayload(codeInContest).build());
            }
        }


        return JSONArray.toJSONString(request);
    }

    @Autowired
    private ContestMapper contestMapper;
    /**
     * 检查比赛是否结束
     *
     *   没结束返回true，结束返回false
     * @return
     */
    public boolean checkIfTheCurrentGameIsOver(Long contestId){
        //获取比赛数据
//       因redis Key混乱 后续优化
//        Contest contest = (Contest) redisUtil.getStringInRedis(RedisKey.ZuiOJContestInfo + contestId);
//        if (contest == null){
//            contest = contestMapper.getEventById(contestId);
//            if (contest == null){
//                return false;
//            }else{
//                redisUtil.setStringInRedis(RedisKey.ZuiOJContestInfo + contestId,RedisKey.OutTime,contest);
//            }
//        }
        Contest  contest = contestMapper.getEventById(contestId);;

        //查看是否结束
        Date endTime = contest.getEndTime();
        Date cur = new Date();
       // System.out.println(endTime);
        //System.out.println(cur);
        //System.out.println(endTime.after(cur));
        //int b = endTime.compareTo(cur);
        return endTime.after(cur);
    }

}
