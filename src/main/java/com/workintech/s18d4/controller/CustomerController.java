package com.workintech.s18d4.controller;

import com.workintech.s18d4.dto.AccountResponse;
import com.workintech.s18d4.dto.CustomerResponse;
import com.workintech.s18d4.dto.CustomerResponseWithAccounts;
import com.workintech.s18d4.entity.Customer;
import com.workintech.s18d4.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private CustomerService customerService;

    @Autowired

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public Customer save(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.save(customer);
        return savedCustomer;
    }

    @GetMapping
    public List<CustomerResponse> findAll() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public CustomerResponseWithAccounts findById(@PathVariable long id) {
        Customer customer = customerService.find(id);
        List<AccountResponse> accountResponses = new ArrayList<>();
        customer.getAccounts().forEach(account -> {
            accountResponses.add(new AccountResponse(account.getId(), account.getAccountName(),
                    account.getMoneyAmount()));
        });
        return new CustomerResponseWithAccounts(customer.getId(), customer.getEmail(), customer.getSalary()
                , accountResponses);
    }

    @PutMapping("/{id}")
    public CustomerResponse update(@PathVariable long id, @RequestBody Customer customer){
        Customer foundCustomer = customerService.find(id);
        if(foundCustomer != null){
            customerService.save(customer);
        }else{
            throw new RuntimeException("Id is not exist: " + id);
        }
        return new CustomerResponse(customer.getId(), customer.getEmail(), customer.getSalary());

    }

    @DeleteMapping("/{id}")
    public CustomerResponse remove(@PathVariable long id){
        return customerService.delete(id);
    }
}
