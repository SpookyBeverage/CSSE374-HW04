import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Test;

import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;

public class tests {
    @Test
    public void CartTests(){
        Cart cart = new Cart("1");
        CartItem item = new CartItem("1","Pizza","12-Inch Pizza",12.0,"url",1);
        cart.addItem(item);
        //item should be in cart.items
        assertEquals(true,cart.items.contains(item));
        //the price should be 12.0
        assertEquals(12.0,cart.getPrice());
        //getItemQuantity("1") should return 1
        assertEquals(1,cart.getItemQuantity("1"));

        //Test for setQuantity
        cart.setItemQuantity("1",12);
        //cart.getItemQuantity("1") should now return 12
        assertEquals(12,cart.getItemQuantity("1"));
        //getPrice() should now return 12*12
        assertEquals(12.0*12.0,cart.getPrice());

        //Test for add Discount
        cart.addDiscount(0.5);
        //0.5 should be in cart.discounts
        assertEquals(true,cart.discounts.contains(0.5));
        //getPrice() should return 6.0 now
        assertEquals(12*6.0,cart.getPrice());

        int quantity = cart.getItemQuantity("2");
        //expected value of quantity is -1
        assertEquals(-1,quantity);
    }
    @Test
    public void DiscountHandlerTests(){
        Cart cart = new Cart("1");
        CartItem item = new CartItem("1","Pizza","12-Inch Pizza",12.0,"url",1);
        CartItem item2 = new CartItem("2","Cookies","12 cookies",3.0,"url",1);
        cart.addItem(item);
        cart.addItem(item2);
        DiscountHandler handler = new DiscountHandler();
        handler.discountInfo.put("discount1",0.3);
        handler.expiredDiscount.add("discount0");
        int result = handler.addDiscount(cart,"user1","discount1");
        //result should be 0;
        //cart should now have a discount of 0.3 in the cart.discounts, the cart.price should be 10.5, and handler.userDiscountTry
        //should not have "user1" in it
        assertEquals(true,cart.discounts.contains(0.3));
        assertEquals(0,result);
        assertEquals(10.5,cart.getPrice());
        assertEquals(false,handler.userDiscountTry.containsKey("user1"));

        result = handler.addDiscount(cart,"user1","discount2");
        //result should now be 1, the price should still be 10.5 and handler.userDiscountTry should now have "user1", 1 in it
        assertEquals(1,result);
        assertEquals(10.5,cart.getPrice());
        assertEquals(1,(int)handler.userDiscountTry.get("user1"));

        result = handler.addDiscount(cart,"user1","discount0");
        //result should now be 2, price should be unchanged, and handler.userDiscountTry should now have "user1", 2 in it
        assertEquals(2,result);
        assertEquals(10.5,cart.getPrice());
        assertEquals(2,(int)handler.userDiscountTry.get("user1"));

        handler.userDiscountTry.put("user1",5);
        result = handler.addDiscount(cart,"user1","discount1");
        //result should now be 5 price and tries should remain unchanged
        assertEquals(3,result);
        assertEquals(10.5,cart.getPrice());
        assertEquals(5,(int)handler.userDiscountTry.get("user1"));

        //Reseting
        cart = new Cart("1");
        item = new CartItem("1","Pizza","12-Inch Pizza",12.0,"url",1);
        item2 = new CartItem("2","Cookies","12 cookies",3.0,"url",1);
        cart.addItem(item);
        cart.addItem(item2);
        handler.userDiscountTry = new HashMap<>();
        result = handler.addDiscount(cart,null,"discount1");
        //result should be 0;
        //cart should now have a discount of 0.3 in the cart.discounts, the cart.price should be 10.5, and handler.userDiscountTry
        //should be empty
        assertEquals(true,cart.discounts.contains(0.3));
        assertEquals(0,result);
        assertEquals(10.5,cart.getPrice());
        assertEquals(0,handler.userDiscountTry.size());

        result = handler.addDiscount(cart,null,"discount2");
        //result should now be 1, price should be unchanged, and handler.userDiscountTry should still be empty
        assertEquals(1,result);
        assertEquals(10.5,cart.getPrice());
        assertEquals(0,handler.userDiscountTry.size());

        result = handler.addDiscount(cart,null,"discount0");
        //result should now be 2, price should be unchanged, and handler.userDiscountTry should still be empty
        assertEquals(2,result);
        assertEquals(10.5,cart.getPrice());
        assertEquals(0,handler.userDiscountTry.size());
    }
    @Test
    public void ItemHandlerTests(){
        Cart cart = new Cart("1");
        ItemHandler handler = new ItemHandler();
        StockItem item = new StockItem("1","Pizza","12-Inch Pizza",12.0,"url",10);
        StockItem item2 = new StockItem("2","Cookies","12 cookies",3.0,"url",0);
        handler.items.put(item.id,item);
        handler.items.put(item2.id,item2);

        int result = handler.addItem(cart,"1",1);
        //cart.items should now have item in it with 1 item ie item.toCartItem(1) and item1 should now have 9 stock
        //result should also be 0
        assertEquals(0,result);
        assertEquals(1,cart.getItemQuantity("1"));
        assertEquals(9,handler.items.get("1").stock);

        result = handler.addItem(cart,"3",1);
        //result should be 1, cart.items should only have 1 CartItem in it
        assertEquals(1,result);
        assertEquals(1,cart.items.size());

        result = handler.addItem(cart,"2",10);
        //result should be 2, cart.items should only have 1 CartItem in it, item2 stock should be unchanged
        assertEquals(2,result);
        assertEquals(1,cart.items.size());
        assertEquals(0,handler.items.get("2").stock);

        result = handler.modifyItem(cart,"1",5);
        //result should be 0, cart.getItemQuantity("1") should now be 5, item.stock should be 5
        assertEquals(0,result);
        assertEquals(5,cart.getItemQuantity("1"));
        assertEquals(5,handler.items.get("1").stock);

        result = handler.modifyItem(cart,"1",4);
        //result should be 0, cart.getItemQuantity("1") should now be 4, item.stock should be 6
        assertEquals(0,result);
        assertEquals(4,cart.getItemQuantity("1"));
        assertEquals(6,handler.items.get("1").stock);

        result = handler.modifyItem(cart,"1",11);
        //result should be 3, cart.getItemQuantity("1") should remain 4, item.stock should remain 6
        assertEquals(2,result);
        assertEquals(4,cart.getItemQuantity("1"));
        assertEquals(6,handler.items.get("1").stock);

        result = handler.modifyItem(cart, "1", 0);
        //result should be 0, cart.getItemQuantity("1") should be -1 cart.items.size() should be 0 and item.stock should be 10
        assertEquals(0,result);
        assertEquals(0,cart.items.size());
        assertEquals(-1,cart.getItemQuantity("1"));
        assertEquals(10,handler.items.get("1").stock);

        result = handler.modifyItem(cart, "3", 100);
        //result should be 1 and nothing should be changed
        assertEquals(1,result);
        assertEquals(0,cart.items.size());
        assertEquals(-1,cart.getItemQuantity("1"));
        assertEquals(10,handler.items.get("1").stock);
    }
    @Test
    public void ViewHandlerTests(){
        Cart cart = new Cart("1");
        CartItem item = new CartItem("1","Pizza","12-Inch Pizza",12.0,"url",1);
        CartItem item2 = new CartItem("2","Cookies","12 cookies",3.0,"url",1);
        cart.addItem(item);
        cart.addItem(item2);
        ViewHandler handler = new ViewHandler();

        double result = handler.calculateTaxPrice(cart.getPrice(),"IN");
        assertEquals((cart.getPrice()+15)*1.02,result);
        result = handler.calculateTaxPrice(cart.getPrice(),"IL");
        assertEquals((cart.getPrice()+20),result);
        result = handler.calculateTaxPrice(cart.getPrice(),"CA");
        assertEquals((cart.getPrice()+0)*1.5,result);
        result = handler.calculateTaxPrice(cart.getPrice(),"NY");
        assertEquals((cart.getPrice()-10)*1.00,result);
        result = handler.calculateTaxPrice(cart.getPrice(),null);
        assertEquals(-1.0,result);

        JSONObject jason = handler.view(cart,"IN");
        assertEquals("1",jason.get("id"));
        assertEquals(15.0,jason.get("price"));
        assertEquals(15.0,jason.get("subtotal"));
        assertEquals((cart.getPrice()+15)*1.02,jason.get("priceWithTax"));
        JSONArray list = (JSONArray) jason.get("items");
        for(Object object : list){
            JSONObject temp = (JSONObject) object;
            boolean itemresult = false;
            for(CartItem i : cart.items){
                if(i.toJson().equals(temp)) itemresult = true;
            }
            assertEquals(true,itemresult);
        }

        jason = handler.view(cart,null);
        assertEquals("1",jason.get("id"));
        assertEquals(15.0,jason.get("price"));
        assertEquals(15.0,jason.get("subtotal"));
        assertEquals("Tax couldn't be calculated",jason.get("priceWithTax"));
        list = (JSONArray) jason.get("items");
        for(Object object : list){
            JSONObject temp = (JSONObject) object;
            boolean itemresult = false;
            for(CartItem i : cart.items){
                if(i.toJson().equals(temp)) itemresult = true;
            }
            assertEquals(true,itemresult);
        }

    }
    @Test
    public void APITest(){
        StockItem item1 = new StockItem("1","Pizza","12-Inch Pizza",5.0,"url",10);
        StockItem item2 = new StockItem("2","Cookies","12 cookies",1.0,"url",10);
        CartAPI api = new CartAPI();
        api.addItemToStock(item1);
        api.addItemToStock(item2);
        api.addDiscount("discount1",0.5);
        api.addExpiredDiscount("discount0");
        api.createNewCart("1");
        api.createNewCart("2");

        JSONObject jason = api.handleViewCart("0",null);
        //state should be false, message should be No cart found with given id
        assertEquals("Fail",jason.get("state"));
        assertEquals("No cart found with given id",jason.get("message"));

        jason = api.handleAddItem("1","1",5);
        //state should be success and the message should be Item Added to cart
        assertEquals("Success",jason.get("state"));
        assertEquals("Item Added to cart",jason.get("message"));
        jason = api.handleViewCart("1",null);
        //price should be 25.0, subtotal should be 25.0 tax should be Tax couldn't be calculated and items should have item1 in it wih a quantity of 5
        assertEquals(25.0,jason.get("subtotal"));
        assertEquals(25.0,jason.get("price"));
        assertEquals("Tax couldn't be calculated",jason.get("priceWithTax"));
        JSONArray list = (JSONArray) jason.get("items");
        assertEquals(1,list.size());
        boolean itemresult = false;
        for(Object object : list){
            JSONObject temp = (JSONObject) object;
            if(item1.toCartItem(5).toJson().equals(temp)) itemresult = true;
        }
        assertEquals(true,itemresult);

        jason = api.handleAddItem("1","0",100);
        //result should be state: failure and message: No such item
        assertEquals("Fail",jason.get("state"));
        assertEquals("No such item",jason.get("message"));
        jason = api.handleViewCart("1",null);
        assertEquals(25.0,jason.get("subtotal"));
        assertEquals(25.0,jason.get("price"));
        list = (JSONArray) jason.get("items");
        assertEquals(1,list.size());

        jason = api.handleAddItem("1","1",100);
        //result should be state: failure and message: Item out of stock
        assertEquals("Fail",jason.get("state"));
        assertEquals("Item out of stock",jason.get("message"));
        jason = api.handleViewCart("1",null);
        assertEquals(25.0,jason.get("subtotal"));
        assertEquals(25.0,jason.get("price"));
        list = (JSONArray) jason.get("items");
        assertEquals(1,list.size());

        jason = api.handleModifyQuantity("1","1",100);
        //result should be state: faulure and message Not enough stock to change quantity with no other changes
        assertEquals("Fail",jason.get("state"));
        assertEquals("Not enough stock to change quantity",jason.get("message"));
        jason = api.handleViewCart("1",null);
        assertEquals(25.0,jason.get("subtotal"));
        assertEquals(25.0,jason.get("price"));
        list = (JSONArray) jason.get("items");
        assertEquals(1,list.size());

        jason = api.handleModifyQuantity("1","2",100);
        //result should be state: faulure and message Item is not in cart with no other changes
        assertEquals("Fail",jason.get("state"));
        assertEquals("Item is not in cart",jason.get("message"));
        jason = api.handleViewCart("1",null);
        assertEquals(25.0,jason.get("subtotal"));
        assertEquals(25.0,jason.get("price"));
        list = (JSONArray) jason.get("items");
        assertEquals(1,list.size());

        jason = api.handleModifyQuantity("1","1",8);
        //result should be state: success and message: Item quantity modified
        assertEquals("Success",jason.get("state"));
        assertEquals("Item quantity modified",jason.get("message"));
        jason = api.handleViewCart("1",null);
        //price and subtotal should be 40.0
        assertEquals(40.0,jason.get("subtotal"));
        assertEquals(40.0,jason.get("price"));

        jason = api.handleDiscount("user1","1","discount1");
        //result should be state: success and message: Discount Applied
        assertEquals("Success",jason.get("state"));
        assertEquals("Discount Applied",jason.get("message"));
        jason = api.handleViewCart("1","NJ");
        //subtotal should be 40, price should be 20 and priceWithTax should be 20
        assertEquals(40.0,jason.get("subtotal"));
        assertEquals(20.0,jason.get("price"));
        assertEquals(20.0,jason.get("priceWithTax"));

        jason = api.handleDiscount("user1","1","discount0");
        //result should be state: fail and message: Expired Discount
        assertEquals("Fail",jason.get("state"));
        assertEquals("Expired Discount",jason.get("message"));
        jason = api.handleViewCart("1","NJ");
        //subtotal should be 40, price should be 20 and priceWithTax should be 20
        assertEquals(40.0,jason.get("subtotal"));
        assertEquals(20.0,jason.get("price"));
        assertEquals(20.0,jason.get("priceWithTax"));

        jason = api.handleDiscount("user1","1","discount2");
        //result should be state: fail and message: Invalid discount code
        assertEquals("Fail",jason.get("state"));
        assertEquals("Invalid discount code",jason.get("message"));
        jason = api.handleViewCart("1","NJ");
        //subtotal should be 40, price should be 20 and priceWithTax should be 20
        assertEquals(40.0,jason.get("subtotal"));
        assertEquals(20.0,jason.get("price"));
        assertEquals(20.0,jason.get("priceWithTax"));

        api.handleDiscount("user1","1","discount2");
        api.handleDiscount("user1","1","discount2");
        api.handleDiscount("user1","1","discount2");
        jason = api.handleDiscount("user1","1","discount1");
        //result should be fail with the message Too many tries
        assertEquals("Fail",jason.get("state"));
        assertEquals("Too many tries",jason.get("message"));
        //subtotal should be 40, price should be 20 and priceWithTax should be 20
        jason = api.handleViewCart("1","NJ");
        assertEquals(40.0,jason.get("subtotal"));
        assertEquals(20.0,jason.get("price"));
        assertEquals(20.0,jason.get("priceWithTax"));
    }
}
