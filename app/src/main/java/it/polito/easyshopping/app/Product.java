package it.polito.easyshopping.app;

/**
 * Created by jessica on 06/05/14.
 */
public class Product {
    private String name;
    private String description;
    private String price;
    private String width;
    private String depth;
    private String imagePath;
    private String mapPath;

    public Product(String name, String description, String price, String width, String depth, String imagePath, String mapPath) {
        super();
        this.name = name;
        this.description = description;
        this.price = price;
        this.width = width;
        this.depth = depth;
        this.imagePath = imagePath;
        this.mapPath = mapPath;
    }

    public Product() {
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

    public String getWidth() {
        return width;
    }

    public String getDepth() {
        return depth;
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

    public void setWidth(String width) {
        this.width = width;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setMapPath(String mapPath) {
        this.mapPath = mapPath;
    }
}
