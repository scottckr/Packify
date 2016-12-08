package com.scottcrocker.packify.helper;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;

import com.scottcrocker.packify.MainActivity;
import com.scottcrocker.packify.model.Order;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.scottcrocker.packify.MainActivity.db;

/**
 * Created by niklasbolwede on 2016-12-07.
 */

public class OrderHandlerHelper {

    List<Order> allOrders = new ArrayList<>();
    List<Order> undeliveredOrders = new ArrayList<>();
    List<Order> deliveredOrders = new ArrayList<>();
    static int amountOfOrdersToDisplay;
    int amountOfOrdersToAdd;
    int amountOfOrdersToRemove;
    static int seekBarValue;
    RandomHelper rnd = new RandomHelper();





    //kvar att göra:
    //Updatera seekbar i oncreate men inte delivered?

    public static int getSeekBarValue() {
        return seekBarValue;
    }

    public static void setSeekBarValue(int seekBarValue) {
        OrderHandlerHelper.seekBarValue = seekBarValue;
    }


    public void updateOrdersDisplayed(){
        //rensa listan
        allOrders = db.getAllOrders();
        undeliveredOrders.clear();
        for(int i = 0; i < allOrders.size(); i++){
            if(!allOrders.get(i).getIsDelivered()){
                undeliveredOrders.add(allOrders.get(i));

            }else{
                deliveredOrders.add(allOrders.get(i));
            }
        }
        //här kan vi ta bort från delivered.
        //Tar bort lev från currentOrders
        for(int i = 0; i < Order.getCurrentListedOrders().size(); i++){
            Iterator<Order> iterator = undeliveredOrders.iterator();
            while (iterator.hasNext()){
                if(iterator.next().getOrderNo() == Order.getCurrentListedOrders().get(i).getOrderNo()){
                    iterator.remove();
                }
            }
        }


        //hämta seekbar value
        if(getSeekBarValue() < 1){
            setSeekBarValue(30);

        }

        //kolla vad som är störst. getCurrentListedOrders eller seekbar

        int orderAmountToShow;
        if (getSeekBarValue() < Order.getCurrentListedOrders().size()){
            orderAmountToShow = getSeekBarValue();
        }else{
            int amountToAdd = getSeekBarValue()-Order.getCurrentListedOrders().size();
            if(undeliveredOrders.size() < amountToAdd){
                 orderAmountToShow = Order.getCurrentListedOrders().size()+undeliveredOrders.size();
            } else{
                 orderAmountToShow = Order.getCurrentListedOrders().size()+amountToAdd;
            }
        }

        //kolla om det behöver tas bort.
        if (orderAmountToShow < Order.getCurrentListedOrders().size()){
            //ta bort ordrar
            while (Order.getCurrentListedOrders().size() > orderAmountToShow){
                Order.getCurrentListedOrders().remove(Order.getCurrentListedOrders().size() -1);
            }
            //kolla om den är mindre och lägg till
        }else if (orderAmountToShow > Order.getCurrentListedOrders().size()){
            //lägg till ordrar.
            int rndOrder = 0;

            while(Order.getCurrentListedOrders().size() < orderAmountToShow){
                rndOrder = rnd.randomNrGenerator(undeliveredOrders.size());
                Order.getCurrentListedOrders().add(undeliveredOrders.get(rndOrder));
                undeliveredOrders.remove(rndOrder);
            }

        }
    }

    public void updateOrdersDisplayedAndDelivered(){
        //Om listan är tom vid start eller inte har några ordrar
        boolean emptyOrderList = false;
        if(Order.getCurrentListedOrders().size() < 1){
            emptyOrderList = true;
        }

        allOrders = db.getAllOrders();
        undeliveredOrders.clear();
        for(int i = 0; i < allOrders.size(); i++){
            if(!allOrders.get(i).getIsDelivered()){
                undeliveredOrders.add(allOrders.get(i));
                if(emptyOrderList){
                    //om tom så lägger den till alla olev. första uppstarten.
                    Order.getCurrentListedOrders().add(allOrders.get(i));
                }

            }else{
                deliveredOrders.add(allOrders.get(i));
            }
        }
        //här kan vi ta bort från delivered.
        //Tar bort lev från currentOrders
        for(int i = 0; i < deliveredOrders.size(); i++){
            Iterator<Order> iterator = Order.getCurrentListedOrders().iterator();
            while (iterator.hasNext()){
                if(iterator.next().getOrderNo() == deliveredOrders.get(i).getOrderNo()){
                    iterator.remove();
                }
            }
        }


        //kollar om den inte är satt.
        if(getSeekBarValue() < 1){
            setSeekBarValue(30);

        }
        int orderAmountToShow;
        if (getSeekBarValue() < undeliveredOrders.size()){
            orderAmountToShow = getSeekBarValue();
        }else{
            orderAmountToShow = undeliveredOrders.size();
        }

        //clean undelivered:
        for(int i = 0; i < Order.getCurrentListedOrders().size(); i++){
            Iterator<Order> iterator = undeliveredOrders.iterator();
            while (iterator.hasNext()){
                if(iterator.next().getOrderNo() == Order.getCurrentListedOrders().get(i).getOrderNo()){
                    iterator.remove();
                }
            }
        }


        if (orderAmountToShow < Order.getCurrentListedOrders().size()){
            //ta bort ordrar
            while (Order.getCurrentListedOrders().size() > orderAmountToShow){
                Order.getCurrentListedOrders().remove(Order.getCurrentListedOrders().size() -1);
            }
        }else if (orderAmountToShow > Order.getCurrentListedOrders().size()){
            //lägg till ordrar.
            int rndOrder = 0;
            while(Order.getCurrentListedOrders().size() < orderAmountToShow){
                rndOrder = rnd.randomNrGenerator(undeliveredOrders.size());
                Order.getCurrentListedOrders().add(undeliveredOrders.get(rndOrder));
                undeliveredOrders.remove(rndOrder);
            }

        }








    }















/*
    public void updateOrders(int amount){
        filterOrders();
        removeDeliveredOrders();
        orderAmountToShow(amount);
        refreshOrders();
    }

    public void filterOrders() {
        allOrders = MainActivity.db.getAllOrders();
        Order.getCurrentListedOrders().clear();
        for (int i = 0; i < allOrders.size(); i++) {
            if (!allOrders.get(i).getIsDelivered()) {
                undeliveredOrders.add(allOrders.get(i));
            }
        }
    }


    public void removeDeliveredOrders() {
        List<Order> tempOrders = new ArrayList<>();
        for (int i = 0; i < Order.getCurrentListedOrders().size(); i++) {
            if (!Order.getCurrentListedOrders().get(i).getIsDelivered()) {
                tempOrders.add(Order.getCurrentListedOrders().get(i));
            }
        }
        Order.getCurrentListedOrders().clear();
        for (int i = 0; i < tempOrders.size(); i++) {
            Order.getCurrentListedOrders().add(tempOrders.get(i));
        }

    }

    public int orderAmountToShow(int amount) {
        if (amount > undeliveredOrders.size()) {
            amountOfOrdersToDisplay = undeliveredOrders.size();
        } else {
            amountOfOrdersToDisplay = amount;
        }
        return amountOfOrdersToDisplay;

    }

    public void refreshOrders() {
        amountOfOrdersToAdd = 0;
        amountOfOrdersToRemove = 0;

        if (amountOfOrdersToDisplay > Order.getCurrentListedOrders().size()) {
            amountOfOrdersToAdd = amountOfOrdersToDisplay - Order.getCurrentListedOrders().size();
            addRandomOrders();//utöka med random
        } else if (amountOfOrdersToDisplay < Order.getCurrentListedOrders().size()) {
            amountOfOrdersToRemove = Order.getCurrentListedOrders().size() - amountOfOrdersToDisplay;
            removeOrders();//ta bort överflödiga orders.
        } else {
            //no change.
        }
    }

    public void addRandomOrders() {
        RandomHelper rnd = new RandomHelper();
        int rndOrder;
        boolean orderDuplicateCheck = false;
        for(int i = 0; i < amountOfOrdersToAdd; i++){//kör antalet gånger som behövs
            rndOrder = rnd.randomNrGenerator(undeliveredOrders.size());//hittar en ny random order
            for(int y = 0; y < Order.getCurrentListedOrders().size(); y++){//kollar igenom alla Orders och ser om någon redan finns.
                if(undeliveredOrders.get(rndOrder).getOrderNo() == Order.getCurrentListedOrders().get(y).getOrderNo()){
                    orderDuplicateCheck = true;
                }
            }
            if (orderDuplicateCheck){
                i--;
                orderDuplicateCheck = false;
            }else{
                if (Order.getCurrentListedOrders().size() < amountOfOrdersToDisplay){
                    Order.getCurrentListedOrders().add(undeliveredOrders.get(rndOrder));
                }
            }
        }


    }

    public void removeOrders() {
        int amountToSave = Order.getCurrentListedOrders().size() - amountOfOrdersToRemove;
        List<Order> tempOrders = new ArrayList<>();

        for (int i = 0; i < amountToSave; i++) {
            tempOrders.add(Order.getCurrentListedOrders().get(i));
        }
        Order.getCurrentListedOrders().clear();
        for (int i = 0; i < tempOrders.size(); i++) {
            Order.getCurrentListedOrders().add(tempOrders.get(i));
        }
    }



*/



}
