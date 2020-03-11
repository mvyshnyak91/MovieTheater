package ua.vyshnyak.aspects;

public class EventCounterInfo {
    private int getCounter;
    private int priceRequestCounter;
    private int bookTicketCounter;

    public int getGetCounter() {
        return getCounter;
    }

    public int getPriceRequestCounter() {
        return priceRequestCounter;
    }

    public int getBookTicketCounter() {
        return bookTicketCounter;
    }

    public void incrementGetCounter() {
        getCounter++;
    }

    public void incrementPriceRequestCounter() {
        priceRequestCounter++;
    }

    public void incrementBookTicketCounter() {
        bookTicketCounter++;
    }
}
