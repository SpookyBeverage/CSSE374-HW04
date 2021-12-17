import org.json.simple.JSONObject;

public class StockItem extends Item {
    int stock;
    public StockItem(String id, String name, String description, Double price, String imageURL, int stock) {
        super(id, name, description, price, imageURL);
        this.stock = stock;
    }
    public CartItem toCartItem(int quantity){
        return new CartItem(id,name,description,price,imageURL,quantity);
    }
    public JSONObject toJson(){
        JSONObject result = super.toJson();
        result.put("stock",stock);
        return result;
    }
}
