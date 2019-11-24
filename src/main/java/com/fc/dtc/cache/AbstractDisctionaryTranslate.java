package com.fc.dtc.cache;

import com.fc.dtc.bean.DisctionaryBean;
import com.fc.dtc.constant.CacheConstant;
import com.fc.dtc.exception.TranslateException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Serializable;
import java.util.*;

/**
 * @author fangyuan
 */
abstract class AbstractDisctionaryTranslate  implements DisctionaryTranslate {


    protected DisctionaryJDBCActuator disctionaryJDBCActuator;


    protected JdbcTemplate jdbcTemplate;

    // (dmlb_dmzm,dmmc)
    protected Map<String, String> cacheData = new HashMap<String, String>(200);

    // //(dmlb_dmmc,dmz)
    protected Map<String, String> cacheData1 = new HashMap<String, String>(200);

    // (dmlb,(dmz,dmmc))
    protected Map<String, TreeSet<DisctionaryBean>> cacheData3 = new HashMap<String, TreeSet<DisctionaryBean>>(200);

    //构建比较器
    protected Comparator sortComparator = new SortComparator() ;

    @Override
    public String get(String cacheKey,String filedKey) {
       throw new TranslateException("需实现get方法");
    }

    @Override
    public TreeSet<DisctionaryBean> getDictionaryByType(String type) {
        throw new TranslateException("需实现getDictionaryByType方法");
    }

    @Override
    public void wirteToCache() {
        throw new TranslateException("需实现init方法");
    }

    static class SortComparator implements Comparator<DisctionaryBean>,Serializable{

        @Override
        public int compare(DisctionaryBean d1, DisctionaryBean d2) {

            String order1 = d1.getItemOrder();

            String order2 = d2.getItemOrder();

            if(order1!=null && order2!=null){
                return order1.compareTo(order2);
            }else {
                return d1.getCode().compareTo(d2.getCode());
            }
        }
    }


    public final void init(){

        //将数据写入缓存中
        List<DisctionaryBean> dbs = this.disctionaryJDBCActuator.execute(this.jdbcTemplate);

        dbs.forEach(disctionaryBean->{
            String type = disctionaryBean.getType();
            String code = disctionaryBean.getCode();
            String name = disctionaryBean.getName();
            //写入数据入库
            cacheData.put(type + CacheConstant.SEPARATOR + code, name);
            cacheData1.put(type + CacheConstant.SEPARATOR + name, code);

            TreeSet<DisctionaryBean> ts;

            if (!cacheData3.containsKey(type)) {
                //构建有序list
                ts = new TreeSet(sortComparator);
            } else {
                ts = cacheData3.get(type);
            }
            ts.add(disctionaryBean);
            cacheData3.put(type, ts);
        });

        wirteToCache();

        //保存成功后移除数据
        cacheData.clear();
        cacheData1.clear();
        cacheData3.clear();
    }
}
