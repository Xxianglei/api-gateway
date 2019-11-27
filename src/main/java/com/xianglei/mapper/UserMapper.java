package com.xianglei.mapper;


import com.xianglei.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT \n" +
            "*\n" +
            "FROM\n" +
            "`BS_USER` \n" +
            "WHERE FLOW_ID = #{flowId}\n"
    )
    @Results({
            @Result(column = "CREATE_DATE",property = "createDate"),
            @Result(column = "FLOW_ID",property = "flowId")
    })
    User getUserFromNomal(User user);

}
