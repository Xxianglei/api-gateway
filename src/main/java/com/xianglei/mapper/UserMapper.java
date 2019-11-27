package com.xianglei.mapper;

import com.xianglei.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT \n" +
            "*\n" +
            "FROM\n" +
            "`BS_USER` \n" +
            "WHERE FLOW_ID = #{flowId}\n"
    )
    User getUserFromNomal(User user);

    @Select("SELECT \n" +
            "*\n" +
            "FROM\n" +
            "`BS_ADMIN` \n" +
            "WHERE FLOW_ID = #{flowId}\n"
    )
    User getUserFromSuper(User user);
}
