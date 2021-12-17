import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class Cart {
    ArrayList<Double> discounts;
    ArrayList<CartItem> items;
    String id;
    private double subtotal;
    private  double priceWithTax;
    public Cart(String id){
        this.id = id;
        subtotal = 0;
        priceWithTax=0;
        items = new ArrayList<>();
        discounts = new ArrayList<>();
    }

    public void addItem(CartItem item){
        items.add(item);
    }
    public void addDiscount(double discount){
        discounts.add(discount);
    }
    public JSONObject viewCart(double tax){
        priceWithTax = tax;
        return this.toJson();
    }
    public int getItemQuantity(String itemId){
        for(CartItem item : items){
            if(item.id.equals(itemId)) return item.quantity;
        }
        return -1;
    }
    public void setItemQuantity(String itemId, int newQuantity){
        for(int i = 0; i < items.size();i++){
            CartItem item = items.get(i);
            if(item.id.equals(itemId)){
                if(newQuantity == 0) items.remove(item);
                else{
                    item.quantity = newQuantity;
                    items.set(i,item);
                }
            }
        }
    }
    public double getPrice(){
        double result = 0;
        for(CartItem item : items) result += (item.price*item.quantity);
        subtotal = result;
        for(double discount : discounts) result *= (1-discount);
        return result;
    }
    public JSONObject toJson(){
        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        for(CartItem i : items) {
            array.add(i.toJson());
        }
        result.put("id",id);
        result.put("price", getPrice());
        result.put("subtotal", subtotal);
        result.put("priceWithTax", priceWithTax == -1 ? "Tax couldn't be calculated" : priceWithTax);
        result.put("items",array);
        return  result;
    }

}
