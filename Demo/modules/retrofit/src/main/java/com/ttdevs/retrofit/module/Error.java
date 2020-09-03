/*
 * Created by ttdevs at 16-7-14 上午9:51.
 * E-mail:ttdevs@gmail.com
 * https://github.com/ttdevs
 * Copyright (c) 2016 ttdevs
 */

package com.ttdevs.retrofit.module;

import java.util.List;

public class Error {

    /**
     * message : Validation Failed
     * errors : [{"resource":"Issue","field":"title","code":"missing_field"}]
     */

    private String message;
    /**
     * resource : Issue
     * field : title
     * code : missing_field
     */

    private List<ErrorsBean> errors;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ErrorsBean> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorsBean> errors) {
        this.errors = errors;
    }

    public static class ErrorsBean {
        private String resource;
        private String field;
        private String code;

        public String getResource() {
            return resource;
        }

        public void setResource(String resource) {
            this.resource = resource;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
