package com.dazuizui.business.mapper;

import com.dazuizui.business.domain.Invigilator;
import com.dazuizui.business.domain.bo.FindByUserIdAndStatusAndDelFlagBo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface InvigilatorMapper {
    /**
     * 增加面试官
     * @param invigilator
     * @return
     */
    public Long addInvigilator(Invigilator invigilator);

    /**
     * 通过userId查询数据
     * @return
     */
    public Invigilator findByUserIdAndStatusAndDelFlag(FindByUserIdAndStatusAndDelFlagBo findByUserIdAndStatusAndDelFlagBo);
}
