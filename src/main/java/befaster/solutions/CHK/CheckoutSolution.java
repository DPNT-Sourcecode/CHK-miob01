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
}

public class CheckoutSolution {

    private final HashMap<String,Integer> inventoryItems = new HashMap<>();
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
        updateBoughtProducts();
        for (Map.Entry<String,Integer> entry : inventoryItems.entrySet()){
            if (entry.getValue() > 0){
                sumToPay += getSumForProduct(entry.getKey(),entry.getValue());
            }
        }
        inventoryItems.clear();
        return sumToPay;
    }

    private boolean allEntriesValid(String skus){
        boolean allValid = skus.chars().allMatch( c -> {
            String ch = (char)c+"";
            if (storeItems.containsKey(ch)){
                int productQuantity = inventoryItems.getOrDefault(ch,0)+1;
                inventoryItems.put(ch,productQuantity);
                return true;
            } else{
                return false;
            }
        });
        if (!allValid){
            inventoryItems.clear();
            return false;
        }
        return true;
    }

    private void updateBoughtProducts(){
        for (Map.Entry<String,Integer> entry : inventoryItems.entrySet()) {
            int productQuantity = inventoryItems.get(entry.getKey());
            inventoryItems.put(entry.getKey(),productQuantity);
            Item item = storeItems.get(entry.getKey());
            if (item.hasOffersForOtherProducts().isPresent()){
                SpecialOfferPair specialOfferPair = item.hasOffersForOtherProducts().get();
                if (productQuantity >= specialOfferPair.getQuantity()){
                    String offerProductKey = (String)specialOfferPair.getPrice();
                    inventoryItems.put(offerProductKey,inventoryItems.getOrDefault(offerProductKey,0)-productQuantity/specialOfferPair.getQuantity());
                }
            }
        }
    }

    private int getSumForProduct(String product, int quantity){
        int sumToPay = 0;
        Item item = storeItems.get(product);
        int fullQuantity = quantity;
        for (SpecialOfferPair specialOfferPair : item.getOffers()){
            if (! (specialOfferPair.getPrice() instanceof String) && fullQuantity >= specialOfferPair.getQuantity()){
                sumToPay += (fullQuantity/specialOfferPair.getQuantity()) * (Integer)specialOfferPair.getPrice();
                fullQuantity = fullQuantity % specialOfferPair.getQuantity();
            }
        }
        sumToPay += fullQuantity * item.getPrice();
        return sumToPay;
    }

    public static void main(String args[]){
        CheckoutSolution checkout = new CheckoutSolution();
        System.out.println(checkout.checkout("AAAA"));
    }
}
