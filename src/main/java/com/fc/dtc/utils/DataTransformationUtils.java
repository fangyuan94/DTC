package com.fc.dtc.utils;

import java.util.List;
import java.util.Map;

import com.fc.dtc.bean.TranslateProperties;
import com.fc.dtc.cache.DisctionaryTranslate;
import com.fc.dtc.constant.CacheConstant;
import com.fc.dtc.constant.TranslateConstant;
import lombok.AllArgsConstructor;
import org.springframework.transaction.NoTransactionException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 
  * 项目名：dtc     类名：TranslateUtils.java
  * 创建人：fangyuan    创建时间：2019年4月2日
  * 描述:<p>代码翻译工具类</p>
  * 修改描述：<p></p>
  * @version v1.0
 */
@AllArgsConstructor
public class DataTransformationUtils {


	private Map<String, List<TranslateProperties>> translateProperties;


	private DisctionaryTranslate dsctionaryTranslate;

    /**
     * 转译代码
     * @param data
     * @param classes
     * @return
     */
	public JSONObject transformationData(Object data, Class classes) {

	    String className = classes.getName();

		if (!translateProperties.containsKey(className)) {
			throw new NoTransactionException("无代码装换bean异常");
		}

		// 处理单实体类
		String txt = JSONObject.toJSONString(data,
				SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullStringAsEmpty,
				SerializerFeature.WriteNullNumberAsZero);
		JSONObject j = JSONObject.parseObject(txt);

		// 获取类字段上的注解值
		List<TranslateProperties> tpvs = translateProperties
				.get(className);
		//先用递归 后替换
		j = this.dataTranslate(tpvs,j);

		return j;
	}

    /**
     *
     * @param data 需要转译类
     * @param baseClasses 需要转译类class
     * @param returnClasses 返回类class
     * @param <T>
     * @return
     */
    public <T> T transformationData(Object data, Class baseClasses,Class<T> returnClasses) {

        return JSONObject.toJavaObject(this.transformationData(data,baseClasses),returnClasses);
    }


    /**
     *
     * 描述:<p>目前递归数据翻译</p>
     */
    private JSONObject dataTranslate(List<TranslateProperties> tps,JSONObject j){

        for (TranslateProperties translatePropertiesVo : tps) {

            String filedName = translatePropertiesVo.getFiledName();

            Integer type = translatePropertiesVo.getType();

            if(TranslateConstant.DEFAULT_TYPE == type){

                String z = j.getString(filedName);

                if(z ==null){
                    continue;
                }
                String key;

                String cacheKey = translatePropertiesVo.getCacheKey();

                if (CacheConstant.DMZ_DMMC.equals(cacheKey)) {
                    key = filedName + "_mc";
                } else {
                    key = filedName.replace("_mc", "");
                }

                if (null != z && !z.isEmpty()) {

                    String template = translatePropertiesVo.getTemplate();

                    template = template + z;

                    String value = dsctionaryTranslate.get(cacheKey,template);

                    j.put(key, value);
                } else {
                    j.put(key, "");
                }

            }else if(TranslateConstant.LIST_TYPE == type){

                //type list 处理
                Class<?> classes = translatePropertiesVo.getClasses();

                List<TranslateProperties> ls = translateProperties.get(classes.getName());

                JSONArray arry =  j.getJSONArray(filedName);

                if(arry == null ){
                    continue;
                }

                for (int i = 0; i < arry.size(); i++) {

                    JSONObject obj = arry.getJSONObject(i);

                    arry.set(i, dataTranslate(ls, obj));

                }
                j.put(filedName, arry);

            }else if(TranslateConstant.MAP_TYPE == type){
                //map 处理
                Class<?> classes = translatePropertiesVo.getClasses();

                List<TranslateProperties> ls = translateProperties.get(classes.getName());

                JSONObject obj =  j.getJSONObject(filedName);

                if(obj == null || obj.isEmpty()){
                    break;
                }

                for (String key : obj.keySet()) {

                    JSONObject value =obj.getJSONObject(key);

                    obj.put(key, dataTranslate(ls, value));
                }
                j.put(filedName, obj);

            }else if(TranslateConstant.OBJECT_TYPE == type){
                //type 处理
                Class<?> classes = translatePropertiesVo.getClasses();

                List<TranslateProperties> ls = translateProperties.get(classes.getName());

                JSONObject obj =  j.getJSONObject(filedName);

                if(obj == null || obj.isEmpty()){
                    break;
                }
                j.put(filedName, dataTranslate(ls, obj));
            }
        }

        return j;
    }


}
