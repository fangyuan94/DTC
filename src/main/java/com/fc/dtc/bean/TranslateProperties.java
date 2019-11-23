package com.fc.dtc.bean;

import java.io.Serializable;

import com.fc.dtc.constant.TranslateConstant;
import lombok.Getter;
import lombok.Setter;

/**
 * 
  * 项目名：dtc     类名：TranslatePropertiesVo.java
  * 创建人：fangyuan    创建时间：2019年3月18日
  * 描述:<p>需要翻译代码描述类</p>
  * 修改描述：<p></p>
  * @version v1.0
 */
@Getter
@Setter
public class TranslateProperties implements Serializable {

	private static final long serialVersionUID = 1454940276202717247L;
	
	//类型为 0-基本数据类型 1-list 2-map 3-需要代码转换的object
	private Integer type = TranslateConstant.DEFAULT_TYPE;
	
	private Class<?> classes;
	
	//缓存key
	private String cacheKey;
	
	//字段名称
	private String filedName;
	
	//替换模板
	private String template;



}
