import org.json.simple.JSONObject;

public class ViewHandler {

    public JSONObject view(Cart cart, String state){
        double taxPrice = calculateTaxPrice(cart.getPrice(),state); //TODO make a random tax thing with state
        return cart.viewCart(taxPrice);
    }
    public double calculateTaxPrice(double total, String state){
        if(state == null) return -1;
        double result = 0;
        switch (state){
            case "IN":
                result = (total + 15) * 1.02;
                break;
            case "IL":
                result = total + 20;
                break;
            case "CA":
                result = total*1.5;
                break;
            case "NY":
                result = total - 10;
                break;
            default:
                result = total;
        }
        return result;
    }
}
