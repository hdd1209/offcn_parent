package com.hyd.project.mapper;

import com.hyd.project.pojo.TProjectInitiator;
import com.hyd.project.pojo.TProjectInitiatorExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TProjectInitiatorMapper {
    long countByExample(TProjectInitiatorExample example);

    int deleteByExample(TProjectInitiatorExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TProjectInitiator record);

    int insertSelective(TProjectInitiator record);

    List<TProjectInitiator> selectByExample(TProjectInitiatorExample example);

    TProjectInitiator selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TProjectInitiator record, @Param("example") TProjectInitiatorExample example);

    int updateByExample(@Param("record") TProjectInitiator record, @Param("example") TProjectInitiatorExample example);

    int updateByPrimaryKeySelective(TProjectInitiator record);

    int updateByPrimaryKey(TProjectInitiator record);
}