package demo.common_entity_demo.repo;

import demo.common_entity_demo.dto.dtoRepo.CustomerRepoDTO;
import demo.common_entity_demo.entity.Account;
import demo.common_entity_demo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Long>{
    @Query("select c.email from Customer c where c.email = :email")
    List<CustomerRepoDTO> findByEmailCustomer(@Param("email") String email);

    boolean existsByEmail(String email);

    Optional<Customer> findById(Customer customerId);
}
