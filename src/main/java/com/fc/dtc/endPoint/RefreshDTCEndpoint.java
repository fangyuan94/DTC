package com.fc.dtc.endPoint;


import com.fc.dtc.cache.DisctionaryTranslate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;

/**
 * @author 刷新字典缓存 需要spring 2.x系列
 */
@Endpoint(id = "dtc-refresh")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshDTCEndpoint{

    private DisctionaryTranslate disctionaryTranslate;

//    @WriteOperation
//    public void dtcRefreshWithDestination(@Selector String type) {
//        //修改某些类型字典
//        this.disctionaryTranslate.dtcRefresh(type);
//
//    }

    /**
     * 刷新所有字典
     */
    @WriteOperation
    public void dtcRefresh() {
        this.disctionaryTranslate.dtcRefresh();
        //返回结果
    }

}
