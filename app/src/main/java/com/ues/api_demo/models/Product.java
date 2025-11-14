package com.ues.api_demo.models;

public class Product {
    private int code;
    private String name;
    private boolean status;

    // Constructor vacío necesario
    public Product() {}

    // Constructor para crear productos nuevos (sin ID)
    public Product(String name, boolean status) {
        this.name = name;
        this.status = status;
    }

    // Constructor completo (para editar)
    public Product(int code, String name, boolean status) {
        this.code = code;
        this.name = name;
        this.status = status;
    }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; } // Setter nuevo

    public String getName() { return name; }
    public void setName(String name) { this.name = name; } // Setter nuevo

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; } // Setter nuevo

    @Override
    public String toString() {
        return name + " (ID: " + code + ")";
    }
}