package com.moneytransfer.model;

import java.math.BigDecimal;

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
public class UserTransaction {

	private String currency;
	private BigDecimal amount;
	private Long fromAccountId;
	private Long toAccountId;

}