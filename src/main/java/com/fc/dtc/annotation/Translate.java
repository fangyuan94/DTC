package com.fc.dtc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 
  * 项目名：dtc     类名：Translate.java
  * 创建人：fangyuan    创建时间：2019年3月18日
  * 描述:<p>标明此类为代码翻译类</p>
  * 修改描述：<p></p>
  * @version v1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Translate {
	
}
