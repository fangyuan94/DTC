package com.fc.dtc.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class DisctionaryBean implements Serializable {

    private static final long serialVersionUID = 1480820276212818247L;

    //代码类型
    private String type;

    //代码名称
    private String name;

    //代码值
    private String code;

    //排序
    private String itemOrder;
}
