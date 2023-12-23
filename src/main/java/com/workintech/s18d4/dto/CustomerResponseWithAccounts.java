package com.workintech.s18d4.dto;

import java.util.List;

public record CustomerResponseWithAccounts(long id, String email, double salary, List<AccountResponse> accountResponseList) {
}
