package com.backbase.billpay.fiserv.payees;

import static com.backbase.billpay.fiserv.utils.FiservUtils.fromFiservDate;

import com.backbase.billpay.fiserv.payees.model.PayeeSummary;
import com.backbase.billpay.fiserv.payees.model.PaymentServices;
import com.backbase.billpay.fiserv.payees.model.PaymentServices.PaymentServiceType;
import com.backbase.billpay.integration.rest.spec.v2.billpay.payees.PaymentService;
import com.backbase.rest.spec.common.types.Currency;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PaymentServicesMapper {

    public static final String CURRENCY = "USD";

    /**
     * Convert a PayeeSummary to a list of PaymentServices.
     * @param source PayeeSummary to convert
     * @return Converted payment services
     */
    public List<PaymentService> toPaymentServices(PayeeSummary source) {
        ArrayList<PaymentService> paymentServices = new ArrayList<>();
        PaymentService regularPaymentService = new PaymentService()
                        .withCutoffTime(source.getCutoffTime())
                        .withEarliestPaymentDate(fromFiservDate(source.getEarliestPaymentDate()))
                        .withNextPaymentDate(fromFiservDate(source.getNextPaymentDate()))
                        .withDeliveryDays(source.getLeadDays())
                        .withPaymentServiceType("REGULAR_PAYMENT");
        paymentServices.add(regularPaymentService);
        if (source.getPaymentServices() != null) {
            for (PaymentServices service : source.getPaymentServices()) {
                PaymentService paymentService = new PaymentService()
                                .withCutoffTime(service.getCutOffTime())
                                .withEarliestPaymentDate(fromFiservDate(service.getEarliestDate()))
                                .withNextPaymentDate(fromFiservDate(service.getNextDate()))
                                .withDeliveryDays(source.getLeadDays())
                                .withPaymentFee(service.getFee())
                                .withFee(new Currency().withAmount(service.getFee()).withCurrencyCode(CURRENCY));
                if (service.getPaymentService() == PaymentServiceType.OVERNIGHT_CHECK) {
                    paymentService.setPaymentServiceType("OVERNIGHT_CHECK");
                } else if (service.getPaymentService() == PaymentServiceType.EXPEDITED_PAYMENT) {
                    paymentService.setPaymentServiceType("EXPEDITED_PAYMENT");
                }
                paymentServices.add(paymentService);
            }
        }
        return paymentServices;
    }

    /**
     * Convert a PayeeSummary to a list of PaymentServices.
     * @param source PayeeSummary to convert
     * @return Converted payment services
     */
    public List<com.backbase.billpay.integration.rest.spec.v2.billpay.payeessummary.PaymentService> 
                    toSummaryPaymentServices(PayeeSummary source) {
        List<com.backbase.billpay.integration.rest.spec.v2.billpay.payeessummary.PaymentService> services =
                        new ArrayList<>();
        toPaymentServices(source).stream().forEach(service -> services.add(convertToSummaryPaymentService(service)));
        return services;
    }

    private static com.backbase.billpay.integration.rest.spec.v2.billpay.payeessummary.PaymentService 
                    convertToSummaryPaymentService(PaymentService service) {
        return new com.backbase.billpay.integration.rest.spec.v2.billpay.payeessummary.PaymentService()
                        .withCutoffTime(service.getCutoffTime())
                        .withDeliveryDays(service.getDeliveryDays())
                        .withEarliestPaymentDate(service.getEarliestPaymentDate())
                        .withFee(service.getFee())
                        .withNextPaymentDate(service.getNextPaymentDate())
                        .withPaymentFee(service.getPaymentFee())
                        .withPaymentServiceType(service.getPaymentServiceType());
    }

}
