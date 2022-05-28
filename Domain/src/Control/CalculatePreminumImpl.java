package Control;

import Customer.Customer;
import java.util.Calendar;

public class CalculatePreminumImpl implements CalculatePremium{



    public CalculatePreminumImpl() {
    }

    @Override
    public double calculatePremium(Customer customer, int premiumRate) {

        int birth = Integer.parseInt((customer.getSSN().split("")[0]+customer.getSSN().split("")[1]));

        if (birth<20){
            birth = birth + 2000;
        } else {
            birth = birth + 1900;
        }


        double age = Calendar.getInstance().get(Calendar.YEAR) - birth;

        if (customer.isSex()){
            //남자일 경우
            return Math.round((1/age * 90) * premiumRate);

        }

        return Math.round((1/age * 80) * premiumRate);
    }
}
