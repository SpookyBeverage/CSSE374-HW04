import org.json.simple.JSONObject;

public abstract class Item {
    String id;
    String name;
    String description;
    Double price;
    String imageURL;
    public Item(String id, String name, String description, Double price, String imageURL){
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageURL = imageURL;
    }
    public JSONObject toJson(){
        JSONObject result = new JSONObject();
        result.put("id",id);
        result.put("name",name);
        result.put("desc",description);
        result.put("price",price);
        result.put("imageurl",imageURL);
        return result;
    }

}
