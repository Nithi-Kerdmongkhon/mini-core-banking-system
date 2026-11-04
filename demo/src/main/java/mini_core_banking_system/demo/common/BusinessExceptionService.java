package mini_core_banking_system.demo.common;

import demo.common_entity_demo.enumurations.CommonStatus;
import lombok.Getter;

@Getter
public class BusinessExceptionService extends RuntimeException{

    private final CommonStatus status;
    public BusinessExceptionService(CommonStatus status){
        super(status.getDescEn());
        this.status = status;
    }
}
