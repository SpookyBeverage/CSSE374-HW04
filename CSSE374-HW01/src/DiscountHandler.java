import java.util.ArrayList;
import java.util.HashMap;

public class DiscountHandler {
    HashMap<String, Integer> userDiscountTry;
    HashMap<String, Double> discountInfo;
    ArrayList<String> expiredDiscount;
    public DiscountHandler(){
        userDiscountTry = new HashMap<>();
        discountInfo = new HashMap<>();
        expiredDiscount = new ArrayList<>();
    }
    //Returns 0  if successful, 1 if invalid code 2 if expired code, 3 if too many tries
    public int addDiscount(Cart cart, String uid, String discountCode){
        Double discount = discountInfo.get(discountCode);
        Integer tries = userDiscountTry.get(uid);
        if(tries != null && tries >= 5) return 3;
        if(discount == null){
            if(uid != null){
                userDiscountTry.put(uid,tries == null ? 1 : tries + 1);
            }
            if(expiredDiscount.contains(discountCode)) return 2;
            return 1;
        }
        cart.addDiscount(discount);
        return 0;
    }
}
