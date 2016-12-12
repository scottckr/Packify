package com.scottcrocker.packify.helper;

import com.scottcrocker.packify.model.Order;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.scottcrocker.packify.MainActivity.db;

/**
 * ActiveOrderHelper updates the displayed list of orders in ActiveOrders.
 */
public class ActiveOrdersHelper {

    private List<Order> undeliveredOrders = new ArrayList<>();
    private List<Order> deliveredOrders = new ArrayList<>();
    private static int seekBarValue;
    private RandomHelper rnd = new RandomHelper();

    private static int getSeekBarValue() {
        return seekBarValue;
    }

    public static void setSeekBarValue(int seekBarValue) {
        ActiveOrdersHelper.seekBarValue = seekBarValue;
    }

    /**
     * Uses the ordernumber and removes that order from the currentListedOrders list.
     *
     * @param deletedOrderNo -  is the ordernumber of the deleted order.
     */
    public void updateCurrentOrdersOnDelete(int deletedOrderNo) {
        filterOrders();

        Iterator<Order> iterator = Order.getCurrentListedOrders().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getOrderNo() == deletedOrderNo) {
                iterator.remove();
            }
        }
    }

    /**
     * Checks if the amount of orders currently displayed is needed to be changed to meet
     * the seekBar value chosen in SettingsActivity and updates the list to the right amount of orders
     */
    public void updateOrdersDisplayed() {
        filterOrders();

        for (int i = 0; i < Order.getCurrentListedOrders().size(); i++) {
            Iterator<Order> iterator = undeliveredOrders.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getOrderNo() == Order.getCurrentListedOrders().get(i).getOrderNo()) {
                    iterator.remove();
                }
            }
        }

        if (getSeekBarValue() < 1) {
            setSeekBarValue(30);
        }

        int orderAmountToShow;
        if (getSeekBarValue() < Order.getCurrentListedOrders().size()) {
            orderAmountToShow = getSeekBarValue();
        } else {
            int amountToAdd = getSeekBarValue() - Order.getCurrentListedOrders().size();
            if (undeliveredOrders.size() < amountToAdd) {
                orderAmountToShow = Order.getCurrentListedOrders().size() + undeliveredOrders.size();
            } else {
                orderAmountToShow = Order.getCurrentListedOrders().size() + amountToAdd;
            }
        }
        editCurrentListedOrders(orderAmountToShow);
    }

    /**
     * Checks if the amount of orders currently displayed is needed to be changed to meet
     * the seekBar value and if there are any delivered orders needed to be sorted out of the list.
     */
    public void updateOrdersDisplayedAndDelivered() {
        filterOrders();

        for (int i = 0; i < deliveredOrders.size(); i++) {
            Iterator<Order> iterator = Order.getCurrentListedOrders().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getOrderNo() == deliveredOrders.get(i).getOrderNo()) {
                    iterator.remove();
                }
            }
        }

        if (getSeekBarValue() < 1) {
            setSeekBarValue(30);
        }

        int orderAmountToShow;
        if (getSeekBarValue() < undeliveredOrders.size()) {
            orderAmountToShow = getSeekBarValue();
        } else {
            orderAmountToShow = undeliveredOrders.size();
        }

        for (int i = 0; i < Order.getCurrentListedOrders().size(); i++) {
            Iterator<Order> iterator = undeliveredOrders.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getOrderNo() == Order.getCurrentListedOrders().get(i).getOrderNo()) {
                    iterator.remove();
                }
            }
        }
        editCurrentListedOrders(orderAmountToShow);
    }

    /**
     * Filter all orders into delivered and undelivered lists
     */
    private void filterOrders() {
        List<Order> allOrders = db.getAllOrders();
        undeliveredOrders.clear();
        for (int i = 0; i < allOrders.size(); i++) {
            if (!allOrders.get(i).getIsDelivered()) {
                undeliveredOrders.add(allOrders.get(i));
            } else {
                deliveredOrders.add(allOrders.get(i));
            }
        }
    }

    /**
     * Check if more or less orders are needed to meet the orderAmountToShow value.
     * If less orders are needed it removes the last one till currentListedOrders amount is right.
     * If more orders are needed it adds a random order from undeliveredOrders till currentListedOrders amount is right.
     *
     * @param orderAmountToShow - is the right amount of orders to be displayed
     */
    private void editCurrentListedOrders(int orderAmountToShow) {
        if (orderAmountToShow < Order.getCurrentListedOrders().size()) {
            while (Order.getCurrentListedOrders().size() > orderAmountToShow) {
                Order.getCurrentListedOrders().remove(Order.getCurrentListedOrders().size() - 1);
            }
        } else if (orderAmountToShow > Order.getCurrentListedOrders().size()) {
            int rndOrder;

            while (Order.getCurrentListedOrders().size() < orderAmountToShow) {
                rndOrder = rnd.randomNrGenerator(undeliveredOrders.size());
                Order.getCurrentListedOrders().add(undeliveredOrders.get(rndOrder));
                undeliveredOrders.remove(rndOrder);
            }
        }
    }
}