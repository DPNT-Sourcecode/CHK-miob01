package befaster.solutions.CHK;

import java.util.*;

class SpecialOfferPair{
    private final Integer quantity;
    private final Object price;

    public SpecialOfferPair(Integer quantity, Object price) {
        this.quantity = quantity;
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Object getPrice() {
        return price;
    }

    public Integer getSpecialOffer(Integer price){
        return  price;
    }

    public String getSpecialOffer(String price){
        return  price;
    }

}

class Item{
    private int price;
    private String name;
    private PriorityQueue<SpecialOfferPair> offers;

    public Item(String name, int price, PriorityQueue<SpecialOfferPair> offers) {
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

    public PriorityQueue<SpecialOfferPair> getOffers() {
        return offers;
    }

    public void setOffers(PriorityQueue<SpecialOfferPair> offers) {
        this.offers = offers;
    }

    public Optional<SpecialOfferPair> hasOffersForOtherProducts(){
        for (SpecialOfferPair offer : offers){
            if (offer.getPrice() instanceof String){
                return Optional.of(offer);
            }
        }
        return Optional.empty();
    }

    public int getPriceForQuantity(int quantityToBuy){
        int sumToPay = 0;
        int fullQuantity = quantityToBuy;
        for (SpecialOfferPair specialOfferPair : getOffers()){
            if (fullQuantity >= specialOfferPair.getQuantity()){
                sumToPay += (fullQuantity/specialOfferPair.getQuantity()) * (Integer)specialOfferPair.getPrice();
                fullQuantity = fullQuantity % specialOfferPair.getQuantity();
            }
        }
        sumToPay += fullQuantity * getPrice();
        return sumToPay;
    }
}

public class CheckoutSolution {

    private final HashMap<String,Integer> boughtProducts = new HashMap<>();
    private Map<String,Item> storeItems;
    private void initStore(){
        PriorityQueue<SpecialOfferPair> offersForA = new PriorityQueue<>(Comparator.comparing(SpecialOfferPair::getQuantity).reversed());
        offersForA.add(new SpecialOfferPair(3, 130));
        offersForA.add(new SpecialOfferPair(5, 200));
        PriorityQueue<SpecialOfferPair> offersForB = new PriorityQueue<>(Comparator.comparing(SpecialOfferPair::getQuantity).reversed()) {
        };
        offersForB.add(new SpecialOfferPair(2, 45));
        PriorityQueue<SpecialOfferPair> offersForE = new PriorityQueue<>(Comparator.comparing(SpecialOfferPair::getQuantity).reversed()) {
        };
        offersForE.add(new SpecialOfferPair(2,"B"));

        storeItems = Map.of("A", new Item("A",50, offersForA),
                "B", new Item("B",30, offersForB),
                "C", new Item("C",20, new PriorityQueue<>()),
                "D", new Item("D",15,new PriorityQueue<>()),
                "E", new Item("E",40, offersForE));
    }

    public Integer checkout(String skus) {
        initStore();
        int sumToPay = 0;
        if (!allEntriesValid(skus)) {
            return -1;
        }
        for (Map.Entry<String,Integer> entry : boughtProducts.entrySet()){
            sumToPay += getSumForProduct(entry.getKey(),entry.getValue());
        }
        boughtProducts.clear();
        return sumToPay;
    }

    private boolean allEntriesValid(String skus){
        boolean allValid = skus.chars().allMatch( c -> {
            String ch = (char)c+"";
            if (storeItems.containsKey(ch)){
                int productQuantity = boughtProducts.getOrDefault(ch,0)+1;
                boughtProducts.put(ch,productQuantity);
                return true;
            } else{
                return false;
            }
        });
        if (!allValid){
            boughtProducts.clear();
            return false;
        }
        return true;
    }

    private void updateBoughtProducts(){
        for (Map.Entry<String,Integer> entry : boughtProducts.entrySet()){
            Item item = storeItems.get(entry.getKey());
            if (item.hasOffersForOtherProducts().isPresent()){
                SpecialOfferPair specialOfferPair = item.hasOffersForOtherProducts().get();
                int productQuantity = entry.getValue();
                if (productQuantity >= specialOfferPair.getQuantity()){
                    String offerProductKey = (String)specialOfferPair.getPrice();
                    Item offerItem = storeItems.get(offerProductKey);
                    boughtProducts.put(offerProductKey, boughtProducts.getOrDefault(offerProductKey,0)+1);
                    boughtProducts.put(ch,productQuantity - specialOfferPair.getQuantity());
                }
            }
        }

    }

    private int getSumForProduct(String product, int quantity){
        Item item = storeItems.get(product);
        return item.getPriceForQuantity(quantity);
    }
}
