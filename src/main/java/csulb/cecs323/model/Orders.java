package csulb.cecs323.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
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
@IdClass(Orders_pk.class)
/**
A request by a Customer for a collection of one or more
Products.  The Order includes a quantity of each Product
within the order.
 */
public class Orders {
    @Id
    @ManyToOne
    /* I could easily have left the @JoinColumn annotation out
     */
    @JoinColumn(name="customer_id",
    referencedColumnName = "customer_id")
    /** The individual placing the order */
    private Customers customer;
    @Id
    @Column(nullable=false)
    /** When they placed it.  This allows us to distinguish
    one order from another by the same customer.
     */
    private LocalDateTime order_date;
    /** make this just a string for now.  Perhaps recast Customer to "Person" and make soldby
    * a relationship from Person instead of just a String.  Or a lookup table is fine too.
     */
    @Column(nullable=false, length=128)
    /** The name of the sales person who sold the goods. */
    private String sold_by;

    public Orders () {}

    /**
     * Orders give information on who purchased at what date from what company or reseller
     *
     * @param customer      A customer is a person who would like to consume products from our store
     * @param order_date    Order Date is when the Product has been ordered
     * @param sold_by       What company sold an item through our storefront
     */
    public Orders (Customers customer, LocalDateTime order_date,
                   String sold_by) {
        this.customer = customer;
        this.order_date = order_date;
        this.sold_by = sold_by;
    }

    /**
     * returns the customer for any function requiring it
     * @return
     */
    public Customers getCustomer() {
        return customer;
    }

    /**
     * grabs value customer for Orders
     * @param customer       A customer is a person who would like to consume products from our store
     */
    public void setCustomer(Customers customer) {
        this.customer = customer;
    }

    /**
     * grabs value customer for Orders
     * @return
     */
    public LocalDateTime getOrder_date() {
        return order_date;
    }

    /**
     * grabs the date ordered for Orders file
     * @param order_date Order Date is when the Product has been ordered
     */
    public void setOrder_date(LocalDateTime order_date) {
        this.order_date = order_date;
    }

    /**
     * grabs value of who sold the time to customer and gives it to any function that requires it.
     * @return
     */
    public String getSold_by() {
        return sold_by;
    }

    /**
     * grabs the date ordered for Orders file
     * @param sold_by       What company sold an item through our storefront
     */
    public void setSold_by(String sold_by) {
        this.sold_by = sold_by;
    }

    @Override
    public String toString () {
        return "Order: Placed by: " + this.getCustomer() + ", On: " + this.getOrder_date() +
                ", Sold by: " + this.getSold_by();
    }

    @Override
    public boolean equals (Object o) {
        Orders order = (Orders) o;
        return (this.getCustomer().equals(order.getCustomer()) &&
                this.getOrder_date() == order.getOrder_date());
    }

    @Override
    public int hashCode () {
        return Objects.hash(this.getOrder_date(), this.getCustomer());
    }
}
