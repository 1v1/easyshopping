package it.polito.easyshopping.app;

/**
 * Created by jessica on 06/05/14.
 */
public class Product {
    private String name;
    private String description;
    private String price;
    private String productID;
    private String width;
    private String depth;
    private String set;
    private String imagePath;
    private String mapPath;

    public Product(String name, String description, String price, String productID, String width,
                   String depth, String set, String imagePath, String mapPath) {
        super();
        this.name = name;
        this.description = description;
        this.price = price;
        this.productID = productID;
        this.width = width;
        this.depth = depth;
        this.set = set;
        this.imagePath = imagePath;
        this.mapPath = mapPath;
    }

    public Product() {
    }

    public float getScreenWidth(float realRoomWidth, float screenRoomWidth) {
        return (Float.parseFloat(this.width) * screenRoomWidth)/realRoomWidth;
    }

    public float getScreenHight(float realRoomHeight, float screenRoomHeight) {
        return (Float.parseFloat(this.depth) * screenRoomHeight)/realRoomHeight;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getProductID() {
        return productID;
    }

    public String getWidth() {
        return width;
    }

    public String getDepth() {
        return depth;
    }

    public String getSet() {
        return set;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getMapPath() {
        return mapPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public void setSet(String set) {
        this.set = set;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setMapPath(String mapPath) {
        this.mapPath = mapPath;
    }
}
