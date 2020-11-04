package com.unicom.admin.bo;

import lombok.Data;

@Data
public class WebLogErrorInfo {
    private String ip;
    private String method;
    private String url;

}
