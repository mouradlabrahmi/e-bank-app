package com.mourad.digitalBanking.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mourad.digitalBanking.entities.BankAccount;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class CustomerDTO {
        private Long id;
        private String name;
        private String email;
}
