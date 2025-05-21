package model;

import java.util.Date;
import javafx.beans.property.*;

public class Salidas {

    private IntegerProperty id;
    private IntegerProperty productoId;
    private StringProperty codigoProducto; // no parte de BD
    private IntegerProperty cantidad;
    private ObjectProperty<Date> fechaSalida;
    private StringProperty departamento;
    private IntegerProperty usuarioId;
    private String numeroFactura;
    private StringProperty motivo;

    public Salidas() {
        this.id = new SimpleIntegerProperty();
        this.productoId = new SimpleIntegerProperty();
        this.codigoProducto = new SimpleStringProperty();
        this.cantidad = new SimpleIntegerProperty();
        this.fechaSalida = new SimpleObjectProperty<>();
        this.departamento = new SimpleStringProperty();
        this.usuarioId = new SimpleIntegerProperty();
        this.numeroFactura = new String();
        this.motivo = new SimpleStringProperty();
    }

    public Salidas(int id, int productoId, int cantidad, Date fechaSalida, String departamento, int usuarioId,
                   String numeroFactura, double costoUnitario, double costoTotal, String motivo) {
        this();
        this.id.set(id);
        this.productoId.set(productoId);
        this.cantidad.set(cantidad);
        this.fechaSalida.set(fechaSalida);
        this.departamento.set(departamento);
        this.usuarioId.set(usuarioId);
        this.numeroFactura =numeroFactura;
        this.motivo.set(motivo);
    }

    // Getters y setters normales (usando get() y set())
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

    public String getCodigoProducto() {
        return codigoProducto.get();
    }
    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto.set(codigoProducto);
    }
    public StringProperty codigoProductoProperty() {
        return codigoProducto;
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

    public Date getFechaSalida() {
        return fechaSalida.get();
    }
    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida.set(fechaSalida);
    }
    public ObjectProperty<Date> fechaSalidaProperty() {
        return fechaSalida;
    }

    public String getDepartamento() {
        return departamento.get();
    }
    public void setDepartamento(String departamento) {
        this.departamento.set(departamento);
    }
    public StringProperty departamentoProperty() {
        return departamento;
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
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getMotivo() {
        return motivo.get();
    }
    public void setMotivo(String motivo) {
        this.motivo.set(motivo);
    }
    public StringProperty motivoProperty() {
        return motivo;
    }
    

    @Override
    public String toString() {
        return "Salidas{" +
                "id=" + getId() +
                ", productoId=" + getProductoId() +
                ", codigoProducto='" + getCodigoProducto() + '\'' +
                ", cantidad=" + getCantidad() +
                ", fechaSalida=" + getFechaSalida() +
                ", departamento='" + getDepartamento() + '\'' +
                ", usuarioId=" + getUsuarioId() +
                ", numeroFactura='" + getNumeroFactura() + '\'' +
                ", motivo='" + getMotivo() + '\'' +
                '}';
    }

}



