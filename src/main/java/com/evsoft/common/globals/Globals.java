package com.evsoft.common.globals;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Globals {

    /**
     * 是否开启二次验证
     * */
    @Value("${td.parmas.eagle2fa:true}")
    public boolean eagle2fa;

    @Value("${file.upload.key:tcshipin-1257933730}")
    public String uploadKey;

}
