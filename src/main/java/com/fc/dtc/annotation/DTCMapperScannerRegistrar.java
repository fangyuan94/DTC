package com.fc.dtc.annotation;


import java.lang.reflect.Field;
import java.util.*;

import com.fc.dtc.bean.TranslateProperties;
import com.fc.dtc.constant.CacheConstant;
import com.fc.dtc.constant.TranslateConstant;
import com.fc.dtc.exception.TranslateException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.reflections.Reflections;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 
  * 项目名：dtc    类名：InsertMapperScannerRegistrar.java
  * 创建人：fangyuan    创建时间：2019年3月27日
  * 描述:<p>获取</p>
  * 修改描述：<p></p>
  * @version v1.0
 */

public class DTCMapperScannerRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware{

	String[] basePackages;

	private AutowireCapableBeanFactory autowireCapableBeanFactory;

	private DefaultListableBeanFactory defaultListableBeanFactory;
	
	private ApplicationContext applicationContext;

	private final Map<String, List<TranslateProperties>> translateProperties = new HashMap<String, List<TranslateProperties>>();


	private void  initStart(){

		for (String tpn : basePackages) {

			Reflections reflections = new Reflections(tpn);

			Set<Class<?>> classesList = reflections
					.getTypesAnnotatedWith(Translate.class);

			for (Class<?> classes : classesList) {

				//只包含 TranslateProperty
				List<Field> fields =  FieldUtils.getFieldsListWithAnnotation(classes, TranslateProperty.class);

				if (null != fields && fields.size() != 0) {

					List<TranslateProperties> tpvs = new ArrayList<TranslateProperties>();

					String className = classes.getName();

					for (Field field : fields) {

						//获取字段返回类型
						Class<?> typeClass = field.getType();

						// 得到该类下面的TranslateProperty注解
						TranslateProperty translateProperty = field
								.getAnnotation(TranslateProperty.class);
						// 指定
						Class<?>[] ts = translateProperty.translate();

						TranslateProperties translateProperties = new TranslateProperties();

						translateProperties.setFiledName(field
								.getName());
						// 不需要指定类型
						if (ts.length == 0) {
////							String dataBaseName = translateProperty.dataBaseName();
//
//							if(dataBaseName.isEmpty()){
//								//默认取第一条
//								throw new RuntimeException("翻译表数据源dataBaseName无法为空");
//							}

							String type = translateProperty.type();

							boolean flag = translateProperty.encode();

							if (flag) {
								translateProperties
										.setCacheKey(CacheConstant.DMZ_DMMC);
							} else {
								translateProperties
										.setCacheKey(CacheConstant.DMMC_DMZ);
							}

							translateProperties.setTemplate(type + CacheConstant.SEPARATOR);
							tpvs.add(translateProperties);

						} else {

							Class<?> tp = ts[0];
							if(classes.equals(tp)){
								throw new TranslateException(
										"TranslateProperty中translate属性中包含Translate注解类不能为本身");
							}

							translateProperties.setClasses(tp);

							if (!tp.isAnnotationPresent(Translate.class)) {
								throw new TranslateException(
										"TranslateProperty中translate属性中必须传包含Translate注解类");
							}

							Integer type ;
							// 为列表类型
							if (List.class.equals(typeClass) || Set.class.equals(typeClass)) {

								type= TranslateConstant.LIST_TYPE;
							} else if (Map.class.equals(typeClass)) {
								// map类型
								type= TranslateConstant.MAP_TYPE;
							} else {
								// 自定义JavaBean 类型
								type= TranslateConstant.OBJECT_TYPE;
							}
							translateProperties.setType(type);

							tpvs.add(translateProperties);
						}
					}
					translateProperties.put(className, tpvs);
				}
			}
		}

		this.instertSpringBean("translateProperties",this.translateProperties);
	}
	


	@Override
	public void registerBeanDefinitions(
			AnnotationMetadata importingClassMetadata,
			BeanDefinitionRegistry registry) {

		AnnotationAttributes annoAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(DTCMapperScanner.class.getName()));
		this.basePackages = annoAttrs.getStringArray("basePackages");
		initStart();
	}

	
	/**
	 * 
	 * 创建时间：2019年3月26日 描述:
	 * <p>
	 * 对象注入到bean中
	 * </p>
	 * 修改描述：
	 * <p>
	 * </p>
	 * 
	 * @param name
	 * @param obj
	 */
	private void instertSpringBean(String name, Object obj) {

		defaultListableBeanFactory.registerSingleton(name, obj);
		autowireCapableBeanFactory.autowireBean(obj);
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		//初始化参数
		this.applicationContext = (ApplicationContext) resourceLoader;
		this.autowireCapableBeanFactory =  applicationContext.getAutowireCapableBeanFactory();
		ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
		this.defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();;
	}

}
