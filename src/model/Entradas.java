package model;

import javafx.beans.property.*;

import java.sql.Date;


public class Entradas {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty productoId = new SimpleIntegerProperty();
    private final IntegerProperty cantidad = new SimpleIntegerProperty();
    private final ObjectProperty<Date> fechaIngreso = new SimpleObjectProperty<>();
    private final StringProperty proveedor = new SimpleStringProperty();
    private final IntegerProperty usuarioId = new SimpleIntegerProperty();
    private final StringProperty numeroFactura = new SimpleStringProperty();
    private final StringProperty observaciones = new SimpleStringProperty();
    
    //Constructor sin parámetros
    public Entradas() {
    	
    }

    // Constructor con todos los parámetros
    public Entradas(int id, int productoId, int cantidad, Date fechaIngreso, String proveedor, int usuarioId, String numeroFactura, double costoUnitario,
            double costoTotal, String observaciones) {
        this.id.set(id);
        this.productoId.set(productoId);
        this.cantidad.set(cantidad);
        this.fechaIngreso.set(fechaIngreso);
        this.proveedor.set(proveedor);
        this.usuarioId.set(usuarioId);
        this.numeroFactura.set(numeroFactura);
        this.observaciones.set(observaciones);
    }

 // Constructor alternativo que recibe propiedades directamente
    public Entradas(int id,
                    SimpleIntegerProperty productoId,
                    SimpleIntegerProperty cantidad,
                    Date fechaIngreso,
                    String proveedor,
                    int usuarioId,
                    String numeroFactura,
                    String observaciones) {
        this.id.set(id);
        this.productoId.bind(productoId); // o .set(productoId.get()) si no quieres binding
        this.cantidad.bind(cantidad);
        this.fechaIngreso.set(fechaIngreso);
        this.proveedor.set(proveedor);
        this.usuarioId.set(usuarioId);
        this.numeroFactura.set(numeroFactura);
        this.observaciones.set(observaciones);
    }
    
    
    // Métodos getter y setter
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getProductoId() {
        return productoId.get();
    }

    public void setProductoId(int productoId) {
        this.productoId.set(productoId);
    }

    public IntegerProperty productoIdProperty() {
        return productoId;
    }

    public int getCantidad() {
        return cantidad.get();
    }

    public void setCantidad(int cantidad) {
        this.cantidad.set(cantidad);
    }

    public IntegerProperty cantidadProperty() {
        return cantidad;
    }

    public Date getFechaIngreso() {
        return fechaIngreso.get();
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso.set(fechaIngreso);
    }

    public ObjectProperty<Date> fechaIngresoProperty() {
        return fechaIngreso;
    }

    public String getProveedor() {
        return proveedor.get();
    }

    public void setProveedor(String proveedor) {
        this.proveedor.set(proveedor);
    }

    public StringProperty proveedorProperty() {
        return proveedor;
    }
    
    public int getUsuarioId() {
        return usuarioId.get();
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId.set(usuarioId);
    }

    public IntegerProperty usuarioIdProperty() {
        return usuarioId;
    }

    public String getNumeroFactura() {
        return numeroFactura.get();
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura.set(numeroFactura);
    }

    public StringProperty numeroFacturaProperty() {
        return numeroFactura;
    }

    public String getObservaciones() {
        return observaciones.get();
    }

    public void setObservaciones(String observaciones) {
        this.observaciones.set(observaciones);
    }

    public StringProperty observacionesProperty() {
        return observaciones;
    }

	public void setUsuarioId(String usuario) {
		// TODO Auto-generated method stub
		
	}
}
