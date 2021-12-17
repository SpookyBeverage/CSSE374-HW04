import java.util.ArrayList;
import java.util.HashMap;

public class ItemHandler {
    HashMap<String,StockItem> items;
    public ItemHandler(){
        items = new HashMap<>();
    }
    //Outputs, 0 -successful, 1 -no such item, 2 -item out of stock
    public int addItem(Cart cart, String itemId, int quantity){
        StockItem toAdd=items.get(itemId);
        if(toAdd == null) return 1;
        if(toAdd.stock < quantity) return 2;
        toAdd.stock-=quantity;
        cart.addItem(toAdd.toCartItem(quantity));
        items.put(itemId,toAdd);
        //TODO sync cart contents and items to json
        return 0;
    }
    //Outputs, 0 -successful, 1 -item not in cart, 2 -Not enough stock
    public int modifyItem(Cart cart, String itemId, int newQuantity){
        int currentQuantity = cart.getItemQuantity(itemId);
        int currentStock = 0;
        int diff = newQuantity - currentQuantity;
        StockItem item = items.get(itemId);
        if(currentQuantity == -1) return 1;
        if(diff > item.stock) return 2;
        cart.setItemQuantity(itemId,newQuantity);
        item.stock-=diff;
        items.put(itemId,item);
        //TODO sync cart contents and items to json
        return 0;
    }
}
