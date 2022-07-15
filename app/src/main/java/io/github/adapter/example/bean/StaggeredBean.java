package io.github.adapter.example.bean;

public class StaggeredBean {

    private String title;
    private String describe;
    private String price;
    private String discount;

    public StaggeredBean(String title, String describe, String price, String discount) {
        this.title = title;
        this.describe = describe;
        this.price = price;
        this.discount = discount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
