package mini_core_banking_system.demo.implement.customer;

import demo.common_entity_demo.dto.ResponseBodyDTO;
import demo.common_entity_demo.entity.Customer;
import demo.common_entity_demo.enumurations.CommonStatus;
import demo.common_entity_demo.repo.CustomerRepository;
import lombok.RequiredArgsConstructor;
import mini_core_banking_system.demo.common.CommonService;
import mini_core_banking_system.demo.dto.customer.ResponseViewCustomerDTO;
import mini_core_banking_system.demo.interfaces.customer.ViewCustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ViewCustomerServiceImpl extends CommonService implements ViewCustomerService {
    private static final Logger logger = LoggerFactory.getLogger(ViewCustomerService.class);
    private final CustomerRepository customerRepository;

    @Override
    public ResponseBodyDTO handle(Long id) {

        if (id == null || id <= 0) {
            return new ResponseBodyDTO(CommonStatus.INVALID_REQUEST);
        }

        Optional<Customer> customer = customerRepository.findById(id);

        if (customer.isEmpty()) {
            logger.warn("View customer: not found, id={}", id);
            return new ResponseBodyDTO(CommonStatus.NOT_FOUND_CUSTOMER);
        }

        Customer detail = customer.get();
        logger.info("Customer Detail : di= {}",id);

        ResponseViewCustomerDTO dto = new ResponseViewCustomerDTO();
        dto.setName(detail.getFirstName() + " " + detail.getLastName());
        dto.setEmail(detail.getEmail());
        dto.setPhone(detail.getPhone());


        return new ResponseBodyDTO(dto, CommonStatus.SUCCESS);
    }
}
