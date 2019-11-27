package com.xianglei.service;

import com.xianglei.domain.User;
import org.springframework.stereotype.Component;

/**
 * 描述：获取用户信息
 * 时间：[2019/11/27:11:38]
 * 作者：xianglei
 * params: * @param null
 */
@Component
public interface UserService {
    User getUser(User user);
}
