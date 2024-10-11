package az.edu.turing.bankingservice.utils;

import az.edu.turing.bankingservice.exceptions.NotFoundException;
import az.edu.turing.bankingservice.model.enums.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static az.edu.turing.bankingservice.model.enums.Error.ERR_09;

@Service
@RequiredArgsConstructor
public class CurrencyRateFetcher {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final int CACHE_DURATION_MINUTES = 1;
    private static ExchangeRateRepository exchangeRateRepository;

    @Autowired
    public void setExchangeRateRepository(ExchangeRateRepository repository) {
        CurrencyRateFetcher.exchangeRateRepository = repository;
    }

    private static String getCurrencyRatesUrl() {
        LocalDate currentDate = LocalDate.now();
        return "https://cbar.az/currencies/" + currentDate.format(formatter) + ".xml";
    }

    public static void fetchExchangeRates() throws Exception {
        if (isCacheValid()) {
            return;
        }

        String currencyRatesUrl = getCurrencyRatesUrl();
        URL url = new URL(currencyRatesUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RuntimeException("Failed to fetch data: HTTP error code : " + connection.getResponseCode());
        }

        InputStream xmlInput = connection.getInputStream();
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlInput);
        doc.getDocumentElement().normalize();

        NodeList nodeList = doc.getElementsByTagName("Valute");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String code = element.getAttribute("Code");
                BigDecimal value = new BigDecimal(element.getElementsByTagName("Value").item(0).getTextContent());

                ExchangeRate exchangeRate = exchangeRateRepository
                        .findByCurrencyCode(code)
                        .orElse(ExchangeRate.builder().currencyCode(code).rate(value).lastUpdated(LocalDateTime.now()).build());

                exchangeRate.setRate(value);
                exchangeRate.setLastUpdated(LocalDateTime.now());

                exchangeRateRepository.save(exchangeRate);
            }
        }

        connection.disconnect();
    }

    private static boolean isCacheValid() {
        Optional<ExchangeRate> latestRate = exchangeRateRepository.findFirstByOrderByLastUpdatedDesc();
        return latestRate
                .map(rate -> rate.getLastUpdated().plusMinutes(CACHE_DURATION_MINUTES).isAfter(LocalDateTime.now()))
                .orElse(false);
    }

    public static BigDecimal getRate(Currency currency) {
        if (currency == Currency.AZN) {
            return BigDecimal.ONE;
        }

        return exchangeRateRepository.findByCurrencyCode(currency.name())
                .map(ExchangeRate::getRate)
                .orElseThrow(() -> new NotFoundException(ERR_09.getErrorCode(), ERR_09.getErrorDescription() + currency.name()));
    }
}
