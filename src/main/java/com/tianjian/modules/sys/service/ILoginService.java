package com.tianjian.modules.sys.service;

import com.tianjian.modules.sys.entity.User;
import com.tianjian.common.utils.ReJson;

public interface ILoginService {

    ReJson login(User user);
}
