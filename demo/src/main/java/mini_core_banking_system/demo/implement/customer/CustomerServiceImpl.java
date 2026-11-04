package mini_core_banking_system.demo.implement.customer;

import demo.common_entity_demo.dto.ResponseBodyDTO;
import demo.common_entity_demo.entity.Customer;
import demo.common_entity_demo.enumurations.CommonStatus;
import demo.common_entity_demo.enumurations.CommonStatusAccount;
import demo.common_entity_demo.repo.CustomerRepository;
import mini_core_banking_system.demo.common.CommonService;
import mini_core_banking_system.demo.dto.customer.RequestAddCustomerDTO;
import mini_core_banking_system.demo.interfaces.customer.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CustomerServiceImpl extends CommonService implements CustomerService  {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public ResponseBodyDTO handle(RequestAddCustomerDTO json){

        logger.info("Request : {} " , json );

        if(isNullOrEmpty(json.getFirstName()) || isNullOrEmpty(json.getLastName())){
            return new ResponseBodyDTO(CommonStatus.NOT_FOUND_FNAME_AND_LNAME);
        }
        if (!isValidEmail(json.getEmail())){
            return new ResponseBodyDTO(CommonStatus.INVALID_EMAIL_FORMAT);
        }
        UUID uuid = UUID.randomUUID();
        String uuidRandom = uuid.toString();

        boolean existing = customerRepository.existsByEmail(json.getEmail());
        if (existing){
            return new ResponseBodyDTO(CommonStatus.EMAIL_IS_ALREADY);
        }

        Customer customer = new Customer();
        customer.setFirstName(json.getFirstName().trim());
        customer.setLastName(json.getLastName().trim());
        customer.setEmail(json.getEmail());
        customer.setCustomerNo(uuidRandom);
        customer.setStatus(CommonStatusAccount.ACTIVE);
        customer.setPhone(json.getPhone());
        customer.setCreatedAt(LocalDateTime.now());

        customerRepository.save(customer);


        return new ResponseBodyDTO(CommonStatus.SUCCESS);
    }
}
