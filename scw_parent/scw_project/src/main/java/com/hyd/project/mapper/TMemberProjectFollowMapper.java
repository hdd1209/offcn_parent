package com.hyd.project.mapper;

import com.hyd.project.pojo.TMemberProjectFollow;
import com.hyd.project.pojo.TMemberProjectFollowExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TMemberProjectFollowMapper {
    long countByExample(TMemberProjectFollowExample example);

    int deleteByExample(TMemberProjectFollowExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TMemberProjectFollow record);

    int insertSelective(TMemberProjectFollow record);

    List<TMemberProjectFollow> selectByExample(TMemberProjectFollowExample example);

    TMemberProjectFollow selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TMemberProjectFollow record, @Param("example") TMemberProjectFollowExample example);

    int updateByExample(@Param("record") TMemberProjectFollow record, @Param("example") TMemberProjectFollowExample example);

    int updateByPrimaryKeySelective(TMemberProjectFollow record);

    int updateByPrimaryKey(TMemberProjectFollow record);
}