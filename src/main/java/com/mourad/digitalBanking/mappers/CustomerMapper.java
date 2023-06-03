package com.mourad.digitalBanking.mappers;

import com.mourad.digitalBanking.dtos.CurrentBankAccountDTO;
import com.mourad.digitalBanking.dtos.CustomerDTO;
import com.mourad.digitalBanking.entities.BankAccount;
import com.mourad.digitalBanking.entities.Customer;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {
    public Customer fromCustomerDTO(CustomerDTO customerDTO){
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    public CustomerDTO fromCustomer(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }
}
