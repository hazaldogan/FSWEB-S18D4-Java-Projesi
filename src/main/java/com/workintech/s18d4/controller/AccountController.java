package com.workintech.s18d4.controller;

import com.workintech.s18d4.dto.AccountResponse;
import com.workintech.s18d4.dto.CustomerResponseWithAccounts;
import com.workintech.s18d4.entity.Account;
import com.workintech.s18d4.entity.Customer;
import com.workintech.s18d4.service.AccountService;
import com.workintech.s18d4.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;
    private final CustomerService customerService;

    @Autowired

    public AccountController(AccountService accountService, CustomerService customerService) {
        this.accountService = accountService;
        this.customerService = customerService;
    }

    @GetMapping
    public List<Account> findAll(){
        return accountService.findAll();
    }

    @GetMapping("/{id}")
    public Account findById(@PathVariable long id){
        return accountService.find(id);
    }

    @PostMapping("/{customerId}")
    public AccountResponse save(@RequestBody Account account, @PathVariable long customerId){
        Customer customer = customerService.find(customerId);
        if(customer != null){
            customer.getAccounts().add(account);
            account.setCustomer(customer);
            accountService.save(account);
        }else{
            throw new RuntimeException("No customer found!");
        }
        return new AccountResponse(account.getId(),account.getAccountName(), account.getMoneyAmount());
    }

    @PutMapping("/{customerId}")
    public AccountResponse update(@RequestBody Account account, @PathVariable long customerId){
        Customer customer = customerService.find(customerId);
        Account foundAccount = null;
        for(Account account1 : customer.getAccounts()){
            if(account.getId() == account1.getId()){
                foundAccount = account1;
            }
        }
        if(foundAccount == null){
            throw new RuntimeException("Account not found for this customer: " + customerId);
        }
        int indexOfFound = customer.getAccounts().indexOf(foundAccount);
        customer.getAccounts().set(indexOfFound,account);
        account.setCustomer(customer);
        accountService.save(account);
        return new AccountResponse(account.getId(),account.getAccountName(), account.getMoneyAmount());
    }

    @DeleteMapping("/{id}")
    public AccountResponse remove(@PathVariable long id){
        Account account = accountService.find(id);
        if(account!= null){
            accountService.delete(id);
            return new AccountResponse(account.getId(),account.getAccountName(), account.getMoneyAmount());
        }else{
            throw new RuntimeException("No account found!");
        }
    }
}
