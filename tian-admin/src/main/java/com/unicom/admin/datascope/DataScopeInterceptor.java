package com.unicom.admin.datascope;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

/**
 *
 * @Description mybatis拦截器，处理sql拼接进行数据权限控制
 * @param null
 * @return
 * @date 2020/8/15
 * @author ctf
 **/

@Intercepts({
  @Signature(
      type = StatementHandler.class,
      method = "prepare",
      args = {Connection.class, Integer.class})
})
public class DataScopeInterceptor implements Interceptor {

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    StatementHandler statementHandler =
        (StatementHandler) PluginUtils.realTarget(invocation.getTarget());
    MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
    MappedStatement mappedStatement =
        (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");

    if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
      return invocation.proceed();
    }

    BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
    String originalSql = boundSql.getSql();
    Object parameterObject = boundSql.getParameterObject();
    Class<?> clazz = parameterObject.getClass();
    Field[] fields = clazz.getDeclaredFields();
    DataScope dataScope = null;
    for (Field f : fields) {
      Class<?> c = f.getType();
      Class<?> d = DataScope.class;
      if (c == d) {
        f.setAccessible(true);
        dataScope = (DataScope) f.get(parameterObject);
      }
    }

    /* Class<?> clazz =
        Class.forName(
            mappedStatement.getId().substring(0, mappedStatement.getId().lastIndexOf(".")));
    String methodName =
        mappedStatement.getId().substring(mappedStatement.getId().lastIndexOf(".") + 1);
    for (Method method : clazz.getMethods()) {
      if (method.isAnnotationPresent(DataScopeAnnotation.class)
          && methodName.equals(method.getName())) {
        DataScopeAnnotation dataScopeAnnotation = method.getAnnotation(DataScopeAnnotation.class);
        String alias = dataScopeAnnotation.Alias();
      }
    }*/
    // 查找参数中包含DataScope类型的参数
    if (dataScope == null) {
      return invocation.proceed();
    } else {
      List<Integer> deptIds = dataScope.getDeptIds();
      if (deptIds == null || deptIds.size() == 0) {
        return invocation.proceed();
      }
      String scopeName = dataScope.getScopeName();
      String join =
          deptIds.stream()
              .map(x -> x + "")
              .collect(Collectors.toList())
              .stream()
              .collect(Collectors.joining(","));
      originalSql =
          "select * from ("
              + originalSql
              + ") temp_data_scope where temp_data_scope."
              + scopeName
              + " in ("
              + join
              + ")";
      metaStatementHandler.setValue("delegate.boundSql.sql", originalSql);
      return invocation.proceed();
    }
  }

  @Override
  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {}
}
