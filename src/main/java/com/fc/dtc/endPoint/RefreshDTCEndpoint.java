package com.fc.dtc.endPoint;


import com.fc.dtc.cache.DictionaryTranslate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

/**
 * @author 刷新字典缓存 需要spring 2.x系列
 */
@Endpoint(id = "dtc-refresh")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class RefreshDTCEndpoint{

    private DictionaryTranslate disctionaryTranslate;

    /**
     * 刷新所有字典
     */
    @WriteOperation
    public void dtcRefresh() {
        this.disctionaryTranslate.dtcRefresh();
        //返回结果
    }

}
