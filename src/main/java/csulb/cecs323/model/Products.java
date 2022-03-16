package csulb.cecs323.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
/*
 * Licensed under the Academic Free License (AFL 3.0).
 *     http://opensource.org/licenses/AFL-3.0
 *
 *  This code is distributed to CSULB students in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, other than educational.
 *
 *  2021 David Brown <david.brown@csulb.edu>
 *
 */

@Entity
@NamedNativeQuery(
        name="ReturnProduct",
        query = "SELECT * " +
                "FROM   PRODUCTS " +
                "WHERE  prod_name = ? ",
        resultClass = Products.class
)

@NamedNativeQuery(
        name="ReturnProductList",
        query = "SELECT * " +
                "FROM   PRODUCTS " +
                "ORDER BY prod_name ", //delete where clause since we want ALL
        resultClass = Products.class
)

/** Something that we stock, that the customer can order. */
public class Products {
    @Id
    @Column(nullable = false, length = 30)
    /** The Product Universal Product Code */
    private String UPC;

    @Column(nullable = false, length = 128)
    /** Descriptive name for the product */
    private String prod_name;

    @Column(nullable = false, length = 40)
    /** The name of the manufacturer. */
    private String mfgr;

    @Column(nullable = false, length = 20)
    /** The manufacturer's model number for this product. */
    private String model;

    @Column(nullable = false)
    /** Price in US $ */
    private double unit_list_price;

    @Column(nullable = false)
    /** The quantity of this item that we have on hand. */
    private int units_in_stock;

    /** Overloaded constructor that creates a Product object.
     *  @param UPC              The universal product code used to identify a product.
     *  @param prod_name        The name of the product.
     *  @param mfgr             The manufacturer of the product.
     *  @param model            The model of the product.
     *  @param unit_list_price  The listed price for this product.
     *  @param units_in_stock   The amount of units available for this particular product.
     */
    public Products(String UPC, String prod_name, String mfgr, String model, double unit_list_price, int units_in_stock) {
        this.UPC = UPC;
        this.prod_name = prod_name;
        this.mfgr = mfgr;
        this.model = model;
        this.unit_list_price = unit_list_price;
        this.units_in_stock = units_in_stock;
    }

    /** Default constructor that creates a Product. */
    public Products() {}

    /** Retrieves the UPC for the given product
     * @return UPC  The universal product code of the product.
     */
    public String getUPC() {
        return UPC;
    }

    /** Sets the UPC for the given product.
     * @param UPC  The universal product code of the product.
     */
    public void setUPC(String UPC) {
        this.UPC = UPC;
    }

    /** Retrieves the product name of the product.
     * @return prod_name  The name of the product.
     */
    public String getProd_name() {
        return prod_name;
    }

    /** Sets the product name for a Product.
     * @param prod_name  The name of the product.
     */
    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    /** Retrieves the manufacturer of the product.
     * @return mfgr The manufacturer of the product.
     */
    public String getMfgr() {
        return mfgr;
    }

    /** Sets the name of the manufacturer for a Product.
     * @param mfgr  The name of the product manufacturer.
     */
    public void setMfgr(String mfgr) {
        this.mfgr = mfgr;
    }

    /** Retrieves the specific model of the product.
     * @return model The product model.
     */
    public String getModel() {
        return model;
    }

    /** Sets the model of a particular Product.
     * @param model  The model of the given product.
     */
    public void setModel(String model) {
        this.model = model;
    }

    /** Retrieves the current list price of the product.
     * @return unit_list_price The list price of an individual product.
     */
    public double getUnit_list_price() {
        return unit_list_price;
    }

    /** Sets the unit list price of a particular Product.
     * @param unit_list_price  The list price of an individual product.
     */
    public void setUnit_list_price(double unit_list_price) {
        this.unit_list_price = unit_list_price;
    }

    /** Retrieves the amount of units currently available for a product.
     * @return units_in_stock The number of product units available.
     */
    public int getUnits_in_stock() {
        return units_in_stock;
    }

    /** Sets the number of units in stock for a particular Product.
     * @param units_in_stock  The amount of units available for a product.
     */
    public void setUnits_in_stock(int units_in_stock) {
        this.units_in_stock = units_in_stock;
    }

    /** The string representation of Products
     * @return string representation of Products
     */
    @Override
    public String toString () {
        return "Product- UPC: " + this.UPC + ", Name: " + this.prod_name + ", Price: " + this.unit_list_price
                + " QTY on hand: " + this.units_in_stock;
    }
}
