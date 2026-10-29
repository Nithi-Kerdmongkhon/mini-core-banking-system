package demo.common_entity_demo.dto.dtoRepo;


import lombok.Data;
@Data
public class CustomerRepoDTO {
    private String email;

    public CustomerRepoDTO(String email) {
        this.email = email;
    }
}
