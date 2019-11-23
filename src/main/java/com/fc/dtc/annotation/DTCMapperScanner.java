package com.fc.dtc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Documented;

import org.springframework.context.annotation.Import;


/**
 * 项目名：dtc     类名：InsertMapperScanner.java
 * 创建人：fangyuan    创建时间：2019年3月18日
  * 描述:<p>初始化扫描类</p>
  * 修改描述：<p></p>
  * @version v1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DTCMapperScannerRegistrar.class)
public @interface DTCMapperScanner {
	
	/**
	 * 
	  * 版权归属：上海天源迪科
	  * 项目名：crjgj-common     类名：InsertMapper.java
	  * 创建人：fangyuan    创建时间：2019年3月27日
	  * 描述:<p>扫描包名</p>
	  * 修改描述：<p></p>  
	  * @return
	 */
	String[] basePackages();
	

}
