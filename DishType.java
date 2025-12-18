package model;

public enum DishType {
    СУП(2000, "Суп", 150),
    САЛАТ(3000, "Салат", 200),
    ОСНОВНОЕ_БЛЮДО(5000, "Основное блюдо", 350),
    ДЕСЕРТ(1500, "Десерт", 100),
    НАПИТОК(1000, "Напиток", 80);

    private final int cookingTime;
    private final String name;
    private final int price;

    DishType(int cookingTime, String name, int price) {
        this.cookingTime = cookingTime;
        this.name = name;
        this.price = price;
    }

    public int getCookingTime() { return cookingTime; }
    public String getName() { return name; }
    public int getPrice() { return price; }

    @Override
    public String toString() { return name; }

    public static DishType getRandomDish() {
        DishType[] dishes = values();
        return dishes[(int)(Math.random() * dishes.length)];
    }
}
