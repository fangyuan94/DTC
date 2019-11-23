package com.fc.dtc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 
  * 项目名：dtc     类名：Translate.java
  * 创建人：fangyuan    创建时间：2019年3月18日
  * 描述:<p>标明需要翻译字段属性</p>
  * 修改描述：<p></p>
  * @version v1.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TranslateProperty {
	//指定使用
	Class<?>[] translate() default {};
	
	//判断为(true)解码还是编码(false) 解码:dmz-dmmc;编码:dmmc-dmz
	boolean encode() default true;

	//代码类别
	String type() default "";
	
//	//使用数据源名称
//	String dataBaseName();

}
