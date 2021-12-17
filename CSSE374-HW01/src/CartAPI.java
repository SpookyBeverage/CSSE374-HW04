import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.HashMap;

public class CartAPI {
    ItemHandler itemHandler;
    DiscountHandler discountHandler;
    ViewHandler viewHandler;
    HashMap<String, Cart> carts;
    public CartAPI(){
        carts = new HashMap<>();
        itemHandler = new ItemHandler();
        discountHandler = new DiscountHandler();
        viewHandler = new ViewHandler();
    }

    public JSONObject handleViewCart(String cartId, String state){
        Cart cart = carts.get(cartId);
        if(cart == null)return generateReturn(false,"No cart found with given id");
        return viewHandler.view(cart,state);
    }
    public JSONObject handleAddItem(String cartId, String itemId, int quantity){
        Cart cart = carts.get(cartId);
        if(cart == null)return generateReturn(false,"No cart found with given id");
        if(quantity < 0) return generateReturn(false, "Invalid quantity");
        int result = itemHandler.addItem(cart,itemId,quantity);
        switch (result){
            case 0:
                return generateReturn(true,"Item Added to cart");
            case 1:
                return generateReturn(false,"No such item");
            case 2:
                return generateReturn(false,"Item out of stock");
            default:
                return generateReturn(false,"Error");
        }
    }
    public JSONObject handleDiscount(String uid, String cartId, String discountCode){
        Cart cart = carts.get(cartId);
        if(cart == null)return generateReturn(false,"No cart found with given id");
        int returnCode = discountHandler.addDiscount(cart,uid,discountCode);
        switch(returnCode){
            case 0:
                return generateReturn(true,"Discount Applied");
            case 1:
                return generateReturn(false,"Invalid discount code");
            case 2:
                return generateReturn(false,"Expired Discount");
            default:
                return generateReturn(false,"Too many tries");
        }
    }
    public JSONObject handleModifyQuantity(String cartId, String itemId, int newQuantity){
        Cart cart = carts.get(cartId);
        if(cart == null)return generateReturn(false,"No cart found with given id");
        if(newQuantity < 0) return generateReturn(false, "Invalid quantity");
        int result = itemHandler.modifyItem(cart,itemId,newQuantity);
        switch(result){
            case 0:
                return generateReturn(true,"Item quantity modified");
            case 1:
                return generateReturn(false,"Item is not in cart");
            case 2:
                return generateReturn(false,"Not enough stock to change quantity");
            default:
                return generateReturn(false,"Error");
        }
    }

    private JSONObject generateReturn(boolean state, String message){
        JSONObject result = new JSONObject();
        if(state){
            result.put("state","Success");
        }
        else result.put("state","Fail");
        result.put("message",message);
        return result;
    }
    public void addItemToStock(StockItem item){ //FOR TESTING PURPOSES ONLY
        itemHandler.items.put(item.id,item);
    }
    public void createNewCart(String id){ //FOR TESTING PURPOSES ONLY
        carts.put(id,new Cart(id));
    }
    public void addDiscount(String id, Double discount){//FOR TESTING PURPOSES ONLY
        discountHandler.discountInfo.put(id,discount);
    }
    public void addExpiredDiscount(String id){//FOR TESTING PURPOSES ONLY
        discountHandler.expiredDiscount.add(id);
    }
}
