package mini_core_banking_system.demo.dto.customer;

import lombok.Data;

@Data
public class RequestAddCustomerDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

}
