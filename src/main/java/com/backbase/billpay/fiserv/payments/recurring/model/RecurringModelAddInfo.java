package com.backbase.billpay.fiserv.payments.recurring.model;

import java.math.BigDecimal;
import com.backbase.billpay.fiserv.payees.model.BldrDate;

public class RecurringModelAddInfo {
    
    private ModelFrequency frequency;
    private Boolean isIndefinite;
    private String memo;
    private BldrDate nextPaymentDate;
    private Integer numberOfPayments;
    private BigDecimal recurringPaymentAmount;
    
    public enum ModelFrequency {
        WEEKLY("Weekly"),
        EVERY_2_WEEKS("Every2Weeks"),
        EVERY_4_WEEKS("Every4Weeks"),
        TWICE_A_MONTH("TwiceAMonth"),
        MONTHLY("Monthly"),
        EVERY_2_MONTHS("Every2Months"),
        EVERY_3_MONTHS("Every3Months"),
        EVERY_4_MONTHS("Every4Months"),
        EVERY_6_MONTHS("Every6Months"),
        ANNUALLY("Annually");
        
        private String name;
        
        private ModelFrequency(String name) {
            this.name = name;
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }

}
