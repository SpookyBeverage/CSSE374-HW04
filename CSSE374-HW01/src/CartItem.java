import org.json.simple.JSONObject;

public class CartItem extends Item {
    int quantity;

    public CartItem(String id, String name, String description, Double price, String imageURL, int quantity) {
        super(id, name, description, price, imageURL);
        this.quantity = quantity;
    }
    public JSONObject toJson(){
        JSONObject result = super.toJson();
        result.put("quantity",quantity);
        return result;
    }
}
