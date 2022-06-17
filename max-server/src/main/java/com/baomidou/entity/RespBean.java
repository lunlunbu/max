package com.baomidou.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {
    private long code;
    private String message;
    private Object obj;

    /*
    成功返回结果
     */
    public static RespBean success(long code,String message,Object obj){
        return new RespBean(code,message,obj);
    }

    /*
    失败返回结果
     */
    public static RespBean error(long code,String message,Object obj){
        return new RespBean(code,message,obj);
    }

}
