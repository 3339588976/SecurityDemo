package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: SecurityDemo
 * @Package: com.example.config
 * @ClassName: CustomExpireSessionStrategy
 * @Author: shengshuli
 * @Description: 自定义旧用户被踢出的处理类-旧用户登录失败的逻辑
 * @Date: 2019/10/23 11:54
 * @Version: 1.0
 */
public class CustomExpireSessionStrategy implements SessionInformationExpiredStrategy {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent sessionInformationExpiredEvent) throws IOException {
        Map<String,Object> map = new HashMap<>(16);
        map.put("code",0);
        map.put("msg","已经被另一台机器登录，您被迫下线" + sessionInformationExpiredEvent.getSessionInformation().getLastRequest());
        //map->json
        String json = objectMapper.writeValueAsString(map);
        //设置文本类型
        sessionInformationExpiredEvent.getResponse().setContentType("application/json;charset=UTF-8");
        //打印输出
        sessionInformationExpiredEvent.getResponse().getWriter().write(json);

    }
}
