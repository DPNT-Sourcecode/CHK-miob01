package befaster.solutions.CHK;

import java.util.HashMap;
import java.util.Map;

class Item{
    int price;
    String name;
    int specialOfferQuantity;
    int specialOfferPrice;

    public Item(String name, int price, int specialOfferQuantity, int specialOfferPrice) {
        this.price = price;
        this.name = name;
        this.specialOfferQuantity = specialOfferQuantity;
        this.specialOfferPrice = specialOfferPrice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSpecialOfferQuantity() {
        return specialOfferQuantity;
    }

    public void setSpecialOfferQuantity(int specialOfferQuantity) {
        this.specialOfferQuantity = specialOfferQuantity;
    }

    public int getSpecialOfferPrice() {
        return specialOfferPrice;
    }

    public void setSpecialOfferPrice(int specialOfferPrice) {
        this.specialOfferPrice = specialOfferPrice;
    }
}

public class CheckoutSolution {

    private final HashMap<String,Integer> inventoryItems = new HashMap<>();
    private final Map<String,Item> storeItems = Map.of("A", new Item("A",50,3,130),
        "B", new Item("B",30,2,45),
        "C", new Item("C",20,0,0),
        "D", new Item("D",15,0,0));


    public Integer checkout(String skus) {
        int sumToPay = 0;
        if (null == skus || skus.trim().isEmpty()){
            return -1;
        }
        String[] itemNames = skus.split("[,\\s]+");
        for (String itemName: itemNames){
            inventoryItems.put(itemName,inventoryItems.getOrDefault(itemName,0)+1);
        }
        for (Map.Entry<String,Integer> entry : inventoryItems.entrySet()){
            if (!storeItems.containsKey(entry.getKey())){
                return -1;
            }
            Item item = storeItems.get(entry.getKey());
            if (item.specialOfferQuantity == 0){
                sumToPay += (entry.getValue()/item.specialOfferQuantity) * item.specialOfferPrice + item.price * entry.getValue() % item.specialOfferQuantity;
            } else{
                sumToPay += entry.getValue() * item.price;
            }
        }
        return sumToPay;
    }
}

