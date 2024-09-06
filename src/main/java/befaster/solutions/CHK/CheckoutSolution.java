package befaster.solutions.CHK;

import java.util.*;

class SpecialOfferPair<K,V>{
    private final K quantity;
    private final V price;

    public SpecialOfferPair(K quantity, V price) {
        this.quantity = quantity;
        this.price = price;
    }

    public K getQuantity() {
        return quantity;
    }

    public V getPrice() {
        return price;
    }
}

class Item<K,V>{
    int price;
    String name;


    PriorityQueue<SpecialOfferPair<K,V>> offers;

    public Item(String name, int price, PriorityQueue<SpecialOfferPair<K,V>> offers) {
        this.price = price;
        this.name = name;
        this.offers = offers;
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

    public PriorityQueue<SpecialOfferPair<K, V>> getOffers() {
        return offers;
    }

    public void setOffers(PriorityQueue<SpecialOfferPair<K, V>> offers) {
        this.offers = offers;
    }
}

public class CheckoutSolution {

    private final HashMap<String,Integer> inventoryItems = new HashMap<>();
    private PriorityQueue<SpecialOfferPair<Integer,Integer>> offersForA;
    private PriorityQueue<SpecialOfferPair<Integer,Integer>> offersForB;
    private PriorityQueue<SpecialOfferPair<Integer,String>> offersForE;
    private Map<String,Item> storeItems;
    private void initStore(){
        offersForA = new PriorityQueue<>(Comparator.comparing(SpecialOfferPair<Integer,Integer>::getQuantity).reversed());
        offersForA.add(new SpecialOfferPair<Integer,Integer>(3,130));
        offersForA.add(new SpecialOfferPair<Integer,Integer>(5,200));
        offersForB = new PriorityQueue<>(Comparator.comparing(SpecialOfferPair<Integer,Integer>::getQuantity).reversed()){};
        offersForB.add(new SpecialOfferPair<Integer,Integer>(2,45));
        offersForE = new PriorityQueue<>(Comparator.comparing(SpecialOfferPair<Integer,String>::getQuantity).reversed()){};
        offersForE.add(new SpecialOfferPair<>(2,"B"));

        storeItems = Map.of("A", new Item("A",50,offersForA),
                "B", new Item("B",30,offersForB),
                "C", new Item("C",20,new PriorityQueue<>()),
                "D", new Item("D",15,new PriorityQueue<>()),
                "E", new Item("E",40, offersForE));
    }

    public Integer checkout(String skus) {
        int sumToPay = 0;
        boolean allValid = skus.chars().allMatch( c -> {
            String ch = (char)c+"";
            if (storeItems.containsKey(ch)){
                inventoryItems.put(ch,inventoryItems.getOrDefault(ch,0)+1);
                return true;
            } else{
                return false;
            }
        });
        if (!allValid){
            inventoryItems.clear();
            return -1;
        }
        for (Map.Entry<String,Integer> entry : inventoryItems.entrySet()){
            Item item = storeItems.get(entry.getKey());
            if (item.specialOfferQuantity != 0  ){
                sumToPay += (entry.getValue()/item.specialOfferQuantity) * item.specialOfferPrice + item.price * (entry.getValue() % item.specialOfferQuantity);
            } else{
                sumToPay += entry.getValue() * item.price;
            }
        }
        inventoryItems.clear();
        return sumToPay;
    }

    private int getSumForProduct(String product){
        Item item = storeItems.get(product);
        for (SpecialOfferPair specialOfferPair : item.getOffers()){

        }
        if (item.specialOfferQuantity != 0  ){
            sumToPay += (entry.getValue()/item.specialOfferQuantity) * item.specialOfferPrice + item.price * (entry.getValue() % item.specialOfferQuantity);
        } else{
            sumToPay += entry.getValue() * item.price;
        }
    }
}

