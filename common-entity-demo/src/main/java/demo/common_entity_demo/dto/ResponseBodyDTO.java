package demo.common_entity_demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import demo.common_entity_demo.enumurations.CommonStatus;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseBodyDTO {
    private String action;
    private String code = CommonStatus.ERROR_EXCEPTION_OCCURRED.getCode();
    private String message = CommonStatus.ERROR_EXCEPTION_OCCURRED.getDescTh();
    private String stateCode;
    private Object data = null;

    public ResponseBodyDTO(String action, String code, String message, String stateCode, Object data) {
        this.action = action;
        this.code = code;
        this.message = message;
        this.stateCode = stateCode;
        this.data = data;
    }

    public ResponseBodyDTO(String message, String code, Object data) {
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public ResponseBodyDTO(Object data, CommonStatus status) {
        this.code = status.getCode();
        this.message = status.getDescTh();
        this.data = data;
    }
    public ResponseBodyDTO(CommonStatus status) {
        this.code = status.getCode();
        this.message = status.getDescTh();
    }

    public String getAction() {
        return action;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getStateCode() {
        return stateCode;
    }

    public Object getData() {
        return data;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
