package com.fc.dtc.cache;

import com.fc.dtc.bean.DictionaryBean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * 执行从jdbc
 */
public interface DictionaryJDBCActuator {

    /**
     *
     * @param jdbcTemplate
     */
    public List<DictionaryBean> execute(JdbcTemplate jdbcTemplate);

}
