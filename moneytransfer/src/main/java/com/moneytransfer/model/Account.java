package com.moneytransfer.model;

import java.math.BigDecimal;
import java.util.Currency;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@lombok.Builder
public class Account {
    private Long accountId;
    private Long userId;
    private BigDecimal balance;
    private Currency currency;
}
