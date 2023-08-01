package com.shopme.admin.currence;

import static org.mockito.ArgumentMatchers.anyList;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.currency.CurrencyRepository;
import com.shopme.common.entity.Currency;

@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
@Rollback(false)
public class CurrencyRepositoryTest {

	@Autowired
	private CurrencyRepository currencyRepository;
	@Test
	public void testCreateCurrency() {
//		Currency currencyBritishPound = new Currency("British Pound", "£", "GBP");
//		Currency currencyJapaneseYen = new Currency("Japanese Yen", "¥", "JPY");
//		Currency currency = new Currency("Euro", "€", "EUR");
//		Currency currencyRussianRuble = new Currency("Russian Ruble", "₽", "RUB");
//		Currency currencyouthKoreanWon = new Currency("South Korean Won", "₩", "KRW");
//		Currency currencyChineseYuan = new Currency("Chinese Yuan", "¥", "CNY");
//		Currency currencyBrazilianReal = new Currency("Brazilian Real", "R$", "BRL");
//		Currency currencyAustralianDollar = new Currency("Australian Dollar", "$", "AUD");
//		Currency currencyCanadianDollar = new Currency("Canadian Dollar", "$", "CAD");
//		Currency currencyVietNam = new Currency("VietNam đồng", "đ", "VND");
//		Currency currencyIndianRupee = new Currency("Indian Rupee", "₹", "INR");
//		
//		currencyRepository.saveAll(List.of(currencyBritishPound, currencyJapaneseYen, currencyRussianRuble, currencyouthKoreanWon, currencyChineseYuan, currencyBrazilianReal, currencyAustralianDollar, currencyCanadianDollar, currencyVietNam, currencyIndianRupee,currency));
//		
		List<Currency> currencies = currencyRepository.findAll();
		for (Currency currency : currencies) {
			System.err.println(currency);
			
		}
		
//	Currency saveCurrency=	currencyRepository.save(currency);
//	System.err.println(saveCurrency);
	}
}
