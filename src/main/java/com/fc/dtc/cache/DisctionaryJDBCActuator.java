package com.fc.dtc.cache;

import com.fc.dtc.bean.DisctionaryBean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * 执行从jdbc
 */
public interface DisctionaryJDBCActuator {

    /**
     *
     * @param jdbcTemplate
     */
    public List<DisctionaryBean> execute(JdbcTemplate jdbcTemplate);

}
