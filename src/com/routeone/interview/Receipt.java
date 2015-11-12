package com.routeone.interview;
import java.util.List;
/**
 * Created by bullprog3 on 11/3/15.
 */
public interface Receipt {
    /**
     * @return Currency formatted total ($X,XXX.XX) of all items
     */
    public String getFormattedTotal();
    /**
     * @return List of all items in descending order by amount
     */
    public List<String> getOrderedItems();
}
