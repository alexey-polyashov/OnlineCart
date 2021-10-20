package ru.polyan.onlinecart.utils;

public enum OrderStatus {
    PLACED("Принят (ожидает оплаты)"),
    PAID("Оплачен"),
    PROCESSED("В обработке"),
    DELIVERED("Доставлен"),
    CANCELLED("Отменен");

    private String view;

    OrderStatus(String view) {
        this.view = view;
    }

    public String getView() {
        return view;
    }
}
