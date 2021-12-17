import org.json.simple.JSONObject;

import java.util.ArrayList;

public class main {
    public static void main(String[] args){
        ArrayList<StockItem> testing = new ArrayList<>();
        CartItem test = new CartItem("1","item1","desc",100.0,"url",10);
        CartItem test2 = new CartItem("2","item2","desc",10.0,"url",1);
        Cart cart = new Cart("21");
        cart.addItem(test);
        cart.addItem(test2);
        cart.addDiscount(.5);
        JSONObject jason = cart.toJson();
        System.out.println(jason);


    }
}
