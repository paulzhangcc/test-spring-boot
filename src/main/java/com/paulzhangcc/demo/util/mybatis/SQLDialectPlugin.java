package com.paulzhangcc.demo.util.mybatis;

/**
 * Created by paul on 2017/11/16.
 */

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static com.paulzhangcc.demo.constant.ConfigConsts.OFFSET;
import static com.paulzhangcc.demo.constant.ConfigConsts.PAGESIZE;

@Component
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class,Integer.class }) })
public class SQLDialectPlugin implements Interceptor {
    private static final Logger logger = LoggerFactory.getLogger(SQLDialectPlugin.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        RoutingStatementHandler target = (RoutingStatementHandler) invocation
                .getTarget();
        StatementHandler delegate = (StatementHandler) FieldUtils.readField(
                target, "delegate", true);
        BoundSql boundSql = delegate.getBoundSql();
        String sql = boundSql.getSql().trim();
        String dialectSql = SQLDialect.parse(sql);
        ParameterHandler parameterHandler = target.getParameterHandler();
        Object parameterObj = parameterHandler.getParameterObject();

        if (!sql.equalsIgnoreCase(dialectSql)) {
            if (parameterObj != null && parameterObj.toString().indexOf(OFFSET) != -1) {
                Map obj = (Map) parameterObj;
                Set<String> s = obj.keySet();// 获取KEY集合
                for (String str : s) {
                    int offset = (int) obj.get(OFFSET);
                    int pagesize = (int) obj.get(PAGESIZE);
                    int newpagesize = 0;
                    if (offset != 0) {
                        newpagesize = (offset / pagesize + 1) * pagesize;
                        obj.put(PAGESIZE, newpagesize);
                    }
                }
            }
            FieldUtils.writeField(boundSql, "sql", dialectSql, true);
        }
        if (logger.isDebugEnabled()){
            logger.debug("执行sql的内容如下:\n{}\n请求的参数:{}\n",dialectSql,parameterObj);

        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

}
